package com.step.jdbc.runtime.txedit.provider;

import com.step.api.runtime.core.CacheAPI;
import com.step.api.runtime.core.IResult;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.api.TransactionEditAPI;
import com.step.jdbc.runtime.option.*;
import com.step.jdbc.runtime.txedit.model.*;
import com.step.jdbc.runtime.txedit.model.enums.EditType;
import com.step.jdbc.runtime.txedit.config.TransactionConfig;
import com.step.jdbc.runtime.txedit.exception.OperationErrorCode;
import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.logger.LOGGER;
import com.step.tool.utils.JsonUtil;
import com.step.tool.utils.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author : Sun
 * @date : 2023/1/5  14:54
 */
@Singleton
public class TransactionEdit implements TransactionEditAPI {
    public static final Logger log = LoggerFactory.getLogger(TransactionEdit.class);

    @Inject
    CacheAPI cacheAPI;
    @Inject
    ServiceDiscoverProvider serviceDiscoverProvider;
    @Inject
    JdbcEditorProxy jdbcEditorProxy;

    @Override
    public TxR beginEdit(String lockIp, String account, String programCode, String dataKey, String lockSql, JdbcParam jdbcParam, EditType editType) {
        if (StringUtil.isEmpty(lockIp, account, programCode, dataKey)) {
            throw new BaseException(OperationErrorCode.MISS_PARAM.getMessage()).record();
        }
        if (StringUtil.isEmpty(lockSql)) {
            throw new BaseException(OperationErrorCode.SQL_LOCK_IS_NULL.getMessage()).record();
        }
        String tranKey = getTranKey(account, programCode);
        //封装缓存程序锁参数
        ProgramLock programLock = new ProgramLock();
        programLock.setAccount(account);
        programLock.setDataKey(dataKey);
        programLock.setIp(lockIp);
        programLock.setEditType(editType.getEditType());
        programLock.setLockTime(System.currentTimeMillis());
        programLock.setTransactionKey(tranKey);
        programLock.setExecuteKey(getExecuteKey(tranKey));
        programLock.setProgramCode(programCode);
        //服务器地址 TX_EDIT_1
        programLock = getProgramLock(lockIp, account, programCode, dataKey, programLock);
        //获取用户执行sql的行为锁保证原子性
        if (!getExecuteLock(programLock, new TransactionOption())) {
            throw new BaseException(OperationErrorCode.BUSY.getMessage()).record();
        }
        LockParam lockParam = new LockParam();
        try {
            //封装事务服务器需要的事务参数
            lockParam.setTranKey(programLock.getTransactionKey());
            lockParam.setServerName(programLock.getServiceInfo());
            lockParam.setLockTime(System.currentTimeMillis());
            lockParam.setProgramCode(programCode);
            lockParam.setDataKey(dataKey);
            //校验参数中必须包含 for update nowait
//            String sqlFormat = ParamCheckProvider.checkSqlLock(lockSqls);
            lockParam.setHandleLockSql(lockSql);
            //微服务获取jdbc参数
            lockParam.setJdbcParam(jdbcParam);
            jdbcEditorProxy.beginEdit(lockParam);
        } catch (BaseException e) {
            LOGGER.error(e, "开启事务失败");
            throw e;
        } catch (Exception e) {
            LOGGER.error(e, "开启事务失败");
            throw new BaseException("开启事务失败").record();
        } finally {
            cacheAPI.delete(programLock.getExecuteKey());
        }
        return new TxR(lockParam.getTranKey());
    }

    /**
     * 用户获取程序锁
     *
     * @param lockIp      锁定ip
     * @param account     用户账号
     * @param programCode 程序号
     * @param dataKey     数据主键
     * @param programLock 数据锁信息
     * @return 锁
     */
    @NotNull
    private ProgramLock getProgramLock(String lockIp, String account, String programCode, String dataKey, ProgramLock programLock) {
        //获取程序数据锁
        String programKey = TransactionConfig.getProgramKey(programCode, dataKey);
        //获取程序锁编辑权限
        if (!cacheAPI.lock(programKey, JsonUtil.format(programLock))) {
            //如果获取锁失败.则获取缓存信息.查看是否是当前用户被闪退导致强制退出编辑状态
            programLock = checkUserTransactionValid(lockIp, account, programCode, dataKey, OperationErrorCode.DATA_LOCKED);
        } else {
            try {
                //获取服务地址
                //设置编辑请求服务器参数
                programLock.setServiceInfo(serviceDiscoverProvider.getServer());
                cacheAPI.replaceJson(programKey, programLock);
            } catch (BaseException e) {
                cacheAPI.delete(programKey);
                throw e;
            }
        }
        return programLock;
    }

    @Override
    public TxR updateData(String lockIp, String account, String programCode, String oldDataKey, String dataKey, String lockSql, TransactionSql transactionSql, TransactionOption options) {
        if (StringUtil.isEmpty(lockIp, account, programCode, dataKey, lockSql, transactionSql)) {
            throw new BaseException(OperationErrorCode.MISS_PARAM.getMessage()).record();
        }
        ProgramLock lock;
        if (StringUtil.isEmpty(oldDataKey)) {
            lock = checkUserTransactionValid(lockIp, account, programCode, dataKey, OperationErrorCode.DATA_LOCKED);
        } else {
            //如果主表主键修改,则需要进行替换缓存中的原程序锁
            ProgramLock oldLock = checkUserTransactionValid(lockIp, account, programCode, oldDataKey, OperationErrorCode.DATA_LOCKED);
            String oldProgramKey = TransactionConfig.getProgramKey(programCode, oldDataKey);

            //设置新的数据主键
            oldLock.setDataKey(dataKey);
            //1. 先尝试创建新锁(使用原来的锁的内容)
            lock = getProgramLock(lockIp, account, programCode, dataKey, oldLock);
            String programKey = TransactionConfig.getProgramKey(programCode, dataKey);
            if (!programKey.equals(oldProgramKey)) {
                //2.再在释放原来的锁
                cacheAPI.delete(oldProgramKey);
            }
        }
        //获取用户执行sql的行为锁保证原子性
        if (!getExecuteLock(lock, options)) {
            throw new BaseException(OperationErrorCode.BUSY.getMessage()).record();
        }
        try {
            jdbcEditorProxy.updateData(new TxInfo(lock.getTransactionKey(), lock.getServiceInfo(), lockSql, transactionSql));
        } catch (BaseException e) {
            LOGGER.error(e, "事务更新失败");
            throw e;
        } catch (Exception e) {
            LOGGER.error(e, "事务更新失败");
            throw new BaseException(e);
        } finally {
            cacheAPI.delete(lock.getExecuteKey());
        }
        return new TxR(lock.getTransactionKey());
    }

    /**
     * 用户开启数据编辑需要获取行为锁
     *
     * @param programLock programLock
     * @return true 为获取操作权限
     */
    private boolean getExecuteLock(ProgramLock programLock, TransactionOption option) {
        String executeKey = getExecuteKey(programLock.getTransactionKey());
        return cacheAPI.lockExpired(executeKey, programLock.getTransactionKey(), option.getExecuteTime());
    }

    /**
     * 校验用户的程序锁信息 : 程序锁是否为当前用户开启
     *
     * @param account     account
     * @param programCode 程序号
     * @param dataKey     数据主键
     * @return 用户的缓存锁
     */
    private ProgramLock checkUserTransactionValid(String lockIp, String account, String programCode, String dataKey, IResult result) {
        String programKey = TransactionConfig.getProgramKey(programCode, dataKey);
        ProgramLock programLock = cacheAPI.getJson(programKey, ProgramLock.class);
        //校验账号和锁定时的ip是否为同一个用户
        if (programLock == null || !(account.equals(programLock.getAccount()) && lockIp.equals(programLock.getIp()))) {
            //非当前用户,抛出数据被锁定异常
            throw new BaseException(result.getMessage()).record();
        }
        return programLock;
    }

    @Override
    public TxR exitEdit(String lockIp, String account, String programCode, String dataKey, boolean isCommit, TransactionOption option) {
        if (StringUtil.isEmpty(lockIp, account, programCode, dataKey)) {
            throw new BaseException(OperationErrorCode.MISS_PARAM.getMessage()).record();
        }
        String programKey = TransactionConfig.getProgramKey(programCode, dataKey);
        ProgramLock programLock = cacheAPI.getJson(programKey, ProgramLock.class);
        if (programLock == null) {
            LOGGER.warn("IP: %s ,Account: %s ,ProgramCode: %s ,DataKey: %s,退出事务时,找不到程序锁对象", lockIp, account, programCode, dataKey);
            return new TxR("TransactionKey Not Found");
        }
        //校验账号和锁定时的ip是否为同一个用户
        if (!(account.equals(programLock.getAccount()) && lockIp.equals(programLock.getIp()))) {
            //非当前用户,抛出数据被锁定异常
            throw new BaseException(OperationErrorCode.ERROR_EXIT.getMessage()).record();
        }
        String transactionKey = programLock.getTransactionKey();
        //获取用户行为锁保证原子性
        if (!getExecuteLock(programLock, option)) {
            throw new BaseException(OperationErrorCode.BUSY.getMessage()).record();
        }
        try {
            jdbcEditorProxy.commitTransaction(new TxInfo(isCommit, transactionKey, programLock.getServiceInfo()));
            //清除程序锁
            cacheAPI.delete(TransactionConfig.getProgramKey(programCode, dataKey));
            serviceDiscoverProvider.decrAddress(programLock.getServiceInfo());
        } catch (BaseException e) {
            LOGGER.error(e, "事务提交失败");
            throw e;
        } catch (Exception e) {
            LOGGER.error(e, "事务提交失败");
            throw new BaseException(e);
        } finally {
            cacheAPI.delete(programLock.getExecuteKey());
        }
        return new TxR(transactionKey);
    }

    @Override
    public TxR updateDataNonCommit(String lockIp, String account, String programCode, String dataKey, TransactionSql transactionSql) {
        if (StringUtil.isEmpty(lockIp, account, programCode, dataKey)) {
            throw new BaseException(OperationErrorCode.MISS_PARAM.getMessage()).record();
        }
        String programKey = TransactionConfig.getProgramKey(programCode, dataKey);
        ProgramLock programLock = cacheAPI.getJson(programKey, ProgramLock.class);
        if (programLock == null) {
            LOGGER.warn("IP: %s ,Account: %s ,ProgramCode: %s ,DataKey: %s,使用事务时,找不到程序锁对象", lockIp, account, programCode, dataKey);
            throw new BaseException(OperationErrorCode.INVALID_TRANSACTION.getMessage()).record();
        }
        //校验账号和锁定时的ip是否为同一个用户
        if (!(account.equals(programLock.getAccount()) && lockIp.equals(programLock.getIp()))) {
            LOGGER.error("无效的编辑用户, 事务锁用户: [ %s ] , IP: [ %s ],使用者用户: [ %s ] , IP: [ %s ]", programLock.getAccount(), programLock.getIp(), account, lockIp);
            //非当前用户,抛出数据被锁定异常
            throw new BaseException(OperationErrorCode.INVALID_EDIT.getMessage()).record();
        }
        String transactionKey = programLock.getTransactionKey();
        jdbcEditorProxy.updateDataNonCommit(new TxInfo(false, transactionKey, programLock.getServiceInfo(), transactionSql));
        return new TxR(transactionKey);
    }
}
