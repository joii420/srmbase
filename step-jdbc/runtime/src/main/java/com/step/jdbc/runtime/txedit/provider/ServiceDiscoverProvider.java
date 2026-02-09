package com.step.jdbc.runtime.txedit.provider;

import com.step.api.runtime.common.MicroRegister;
import com.step.api.runtime.core.CacheAPI;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.txedit.model.TransactionServerStatus;
import com.step.jdbc.runtime.txedit.service.TxAsyncService;
import com.step.logger.LOGGER;
import com.step.tool.utils.StringUtil;
import com.step.web.runtime.register.VertxRegister;
import io.vertx.mutiny.core.Vertx;
import io.vertx.serviceproxy.ServiceProxyBuilder;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;


/**
 * @author : Sun
 * @date : 2023/1/31  10:32
 */
@Singleton
public class ServiceDiscoverProvider {
    /**
     * 事务编辑的服务类
     */
    private final Class<TxAsyncService> CLZ = TxAsyncService.class;
    private final String TRAN_PREFIX = "TRAN_SERVE:";
    /**
     * 服务器最大同时编辑数量
     */
    private static final int MAX_COUNT = 2;
    /**
     * 通过缓存进行对服务器中正在执行的事务计数
     */
    @Inject
    CacheAPI cacheAPI;

    @Inject
    VertxRegister vertxRegister;

    public ServiceDiscoverProvider() {

    }

    /**
     * 获取服务的名称
     *
     * @param serveName 获取服务名称: AUTO.TX_EDIT_1.TXASYNC
     */
    private String getServiceName(String serveName) {
        return TRAN_PREFIX + serveName;
    }


    public TxAsyncService getInstance(String serverName, Vertx vertx) {
        return new ServiceProxyBuilder(vertx.getDelegate()).setAddress(getTransactionServer(serverName)).build(CLZ);
    }

    /**
     * 根据规则获取一个空闲的事务服务器
     * 并且给计数+1
     */
    public String getServer() {
        return vertxRegister.MICRO_GROUP + "." + vertxRegister.REGISTRY_SERVE;
//        List<TransactionServerStatus> serverStatusList =
//                MicroRegister.transactionServerList(vertxRegister.MICRO_GROUP).stream()
//                        .map(this::getStatusRegisterName)
//                        .filter(cacheAPI::exist)
//                        .map(s -> cacheAPI.getJson(s, TransactionServerStatus.class))
//                        .filter(TransactionServerStatus::isActive)
//                        .collect(Collectors.toList());
//        if (CollectionUtil.isEmpty(serverStatusList)) {
//            throw new BaseException("无法找到有效的事务服务器,请联系管理员");
//        }
//        //获取当前编辑数量最小的服务器
//        List<TransactionServerStatus> collect = serverStatusList.stream()
////                .filter(s -> !s.isBusy())
//                .peek(s -> s.setCurrentCount(getServerCurrentInUseCount(s.getTransactionServerName()))).collect(Collectors.toList());
//        LOGGER.info(JSON.toJSONString(collect));
//        TransactionServerStatus transactionServer = collect.stream()
//                .min(Comparator.comparingLong(TransactionServerStatus::getCurrentCount))
//                .orElseThrow(() -> new BaseException("编辑服务器繁忙,请稍后再试..."));
//        //给获取的服务器计数+1
//        incrAddress(transactionServer);
//        return transactionServer.getTransactionServerName();
    }

    /**
     * 设置服务器繁忙
     *
     * @param serverName 服务器名称 : TX_EDIT_1
     */
    public void setTransactionStatus(String serverName, boolean isBusy) {
        String statusRegisterName = getStatusRegisterName(serverName);
        TransactionServerStatus json = cacheAPI.getJson(statusRegisterName, TransactionServerStatus.class);
        if (json.isBusy() != isBusy) {
            json.setBusy(isBusy);
            cacheAPI.replaceJson(statusRegisterName, json);
        }
    }

    /**
     * 获取用户上次编辑的事务服务器信息
     * 如果上次编辑的服务信息为null 则根据规则获取一个空闲的事务服务器
     */
    private String getTransactionServer(String server) {
        if (StringUtil.isEmpty(server)) {
            throw new BaseException("无效的服务名 : " + server).record();
        }
        return server.endsWith(CLZ.getSimpleName()) ? server : server + "." + CLZ.getSimpleName();
    }

    /**
     * 获取当前服务器数量
     *
     * @param serverName serverName
     * @return
     */
    private long getServerCurrentInUseCount(String serverName) {
        return cacheAPI.getNumber(getServiceName(serverName));
    }

    /**
     * 开启编辑递增
     *
     * @param transactionServer 服务器状态
     */
    public void incrAddress(TransactionServerStatus transactionServer) {
        //不存在则初始化
        String serverName = transactionServer.getTransactionServerName();
        if (cacheAPI.increment(getServiceName(serverName)) >= transactionServer.getMaxCount()) {
            setTransactionStatus(serverName, true);
            cacheAPI.decrement(getServiceName(serverName));
        }
    }

    /**
     * 退出编辑递减
     *
     * @param serverName serverName
     */
    public void decrAddress(String serverName) {
//        TransactionServerStatus status = cacheAPI.getJson(getStatusRegisterName(serverName), TransactionServerStatus.class);
//        //不存在则初始化0
//        if (cacheAPI.decrement(getServiceName(serverName), 1, -1) < status.getMaxCount()) {
//            setTransactionStatus(serverName, false);
//        }
    }


    /**
     * 获取注册名称
     *
     * @param serverName 服务名称
     */
    public String getStatusRegisterName(String serverName) {
        return serverName + "_STATUS";
    }

    /**
     * 获取计数器
     *
     * @param serverStatus 服务名称
     */
    public boolean transactionServerDeployInit(TransactionServerStatus serverStatus) {
        List<String> tranServers = MicroRegister.transactionServerList(vertxRegister.MICRO_GROUP);
        String transactionServerName = serverStatus.getTransactionServerName();
        if (!tranServers.contains(transactionServerName)) {
            throw new BaseException("未注册的事务服务器" + transactionServerName).record();
        }
        if (MicroRegister.TX_EDIT_1.getServerName().equals(transactionServerName)) {
            //部署服务器1之前,清除所有服务器参数,即其他事务服务器集群需要重新部署
            tranServers.forEach(serverName -> {
                boolean removeStatus = cacheAPI.delete(getStatusRegisterName(serverName));
                boolean removeAtomicLong = cacheAPI.removeAtomicLong(getServiceName(serverName));
                if (removeStatus) {
                    LOGGER.info("移除事务服务器: %s状态 成功", serverName);
                }
                if (removeAtomicLong) {
                    LOGGER.info("移除事务服务器: %s计数器 成功", serverName);
                }
            });
        }
        return initTransactionServerStatus(serverStatus);
    }

    /**
     * 初始化事务服务器的状态 用于负载均衡
     *
     * @param status 服务器名称
     */
    private boolean initTransactionServerStatus(TransactionServerStatus status) {
        cacheAPI.setJson(getStatusRegisterName(status.getTransactionServerName()), status);
        cacheAPI.removeAtomicLong(getServiceName(status.getTransactionServerName()));
        long increment = cacheAPI.increment(getServiceName(status.getTransactionServerName()), -1, 1, true);
        return increment == 0;
    }
}
