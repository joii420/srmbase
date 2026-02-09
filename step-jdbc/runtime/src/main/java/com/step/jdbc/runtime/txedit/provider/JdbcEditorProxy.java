package com.step.jdbc.runtime.txedit.provider;

import com.step.api.runtime.exception.base.BaseException;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.jdbc.runtime.txedit.model.TxInfo;
import com.step.jdbc.runtime.txedit.model.TxR;
import com.step.jdbc.runtime.txedit.provider.test.service.TxService;
import com.step.tool.utils.StringUtil;
import io.vertx.mutiny.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class JdbcEditorProxy {
    public static final Logger log = LoggerFactory.getLogger(JdbcEditorProxy.class);
    @Inject
    Vertx vertx;
    @Inject
    ServiceDiscoverProvider serviceDiscoverProvider;
    @Inject
    TxService txService;

    public JdbcEditorProxy() {
    }

    public void beginEdit(LockParam lockParam) {
        //获取服务地址
        if (StringUtil.isEmpty(lockParam.getServerName())) {
            throw new BaseException("无效的服务名称:" + lockParam.getServerName()).record().record();
        }
        //todo 修改为调用本地事务
        checkTrx(txService.beginEdit(lockParam));
    }

    /**
     * @param txInfo txInfo
     */
    public void updateData(TxInfo txInfo) {
        //todo 修改为调用本地事务
        checkTrx(txService.updateData(txInfo));
        //地址获取
//        TxAsyncService service = serviceDiscoverProvider.getInstance(txInfo.getServerName(), vertx);
//        checkTrx(MicroProvider.awaitResult((Handler<AsyncResult<TxR>> x) -> service.updateData(txInfo, x), TxR::new));
    }

    /**
     * @param txInfo txInfo
     */
    public void updateDataNonCommit(TxInfo txInfo) {
        //todo 修改为调用本地事务
        checkTrx(txService.updateDataNonCommit(txInfo));
        //地址获取
//        TxAsyncService service = serviceDiscoverProvider.getInstance(txInfo.getServerName(), vertx);
//        checkTrx(MicroProvider.awaitResult((Handler<AsyncResult<TxR>> x) -> service.updateData(txInfo, x), TxR::new));
    }

    public void commitTransaction(TxInfo txInfo) {
        //todo 修改为调用本地事务
        checkTrx(txService.exitEdit(txInfo));
//        TxAsyncService service = serviceDiscoverProvider.getInstance(txInfo.getServerName(), vertx);
//        checkTrx(MicroProvider.awaitResult((Handler<AsyncResult<TxR>> x) -> service.exitEdit(txInfo, x), TxR::new));
    }

    /**
     * 校验事务服务器返回结果
     *
     * @param txR 事务服务器的调用结果信息
     */
    private void checkTrx(TxR txR) {
        if (txR == null) {
            throw new BaseException("TXR IS NULL").record();
        }
        if (!txR.isSuccess()) {
//            ResInfo resInfo = new ResInfo(txR.getMsg());
//            resInfo.setCode(txR.getCode());
            throw new BaseException(txR.getErrorInfo().getMessage()).record();
        }
    }

//    /**
//     * 微服务获取jdbc参数信息
//     *
//     * @param jdbcKey jdbc参数key
//     */
//    public JdbcParam getJdbcParam(String jdbcKey) {
//        CompletableFuture<JdbcParam> blockingWaitResult = MicroProvider.createBlockingWaitResult(JdbcParam.class);
//        System.out.println("线程节点测试getJdbcParam -> " + Thread.currentThread().getName());
//        EXECUTOR.execute(() -> JDBC_SERVICE.getJdbcParam(jdbcKey, ar -> {
//            System.out.println("线程节点测试execute -> " + Thread.currentThread().getName());
//            if (ar.succeeded()) {
//                blockingWaitResult.complete(ar.result());
//            } else {
//                LOGGER.error("获取JDBC参数失败", ar.cause());
//                blockingWaitResult.completeExceptionally(ar.cause());
//            }
//        }));
//        return MicroProvider.waitResult(blockingWaitResult, 10);
//    }

}
