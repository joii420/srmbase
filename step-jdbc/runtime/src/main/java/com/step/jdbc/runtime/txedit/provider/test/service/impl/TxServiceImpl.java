package com.step.jdbc.runtime.txedit.provider.test.service.impl;


import com.step.jdbc.runtime.txedit.exception.OperationErrorCode;
import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.jdbc.runtime.txedit.model.TxInfo;
import com.step.jdbc.runtime.txedit.model.TxR;
import com.step.jdbc.runtime.txedit.provider.test.service.TxService;
import com.step.jdbc.runtime.txedit.provider.test.txedit.factory.JdbcFactory;
import com.step.jdbc.runtime.txedit.provider.test.txedit.provider.EditorAPI;
import com.step.logger.LOGGER;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.JsonUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class TxServiceImpl implements TxService {
    public static final Logger log = LoggerFactory.getLogger(TxServiceImpl.class);
    @Inject
    EditorAPI editorAPI;
    @Inject
    JdbcFactory factory;

    @Override
    public TxR beginEdit(LockParam lockParam) {
        return editorAPI.beginEdit(lockParam);
    }

    @Override
    public TxR updateData(TxInfo txInfo) {
        LockParam lockParam = factory.getLockParam(txInfo.getTransactionKey());
        TxR checkResult = checkLockParam(txInfo, lockParam);
        if (checkResult != null) {
            return checkResult;
        }
        return editorAPI.updateData(txInfo.getLockSql(), lockParam, txInfo.getTransactionSql());
    }

    @Override
    public TxR updateDataNonCommit(TxInfo txInfo) {
        LockParam lockParam = factory.getLockParam(txInfo.getTransactionKey());
        TxR checkResult = checkLockParam(txInfo, lockParam);
        if (checkResult != null) {
            return checkResult;
        }
        return editorAPI.updateDataNonCommit(lockParam, txInfo.getTransactionSql());
    }

    @Nullable
    private TxR checkLockParam(TxInfo txInfo, LockParam lockParam) {
        if (lockParam == null) {
            LOGGER.error(log, "找不到的事务ID :[ %s ] ", JsonUtil.format(txInfo));
            //当前系统中获取不到事务参数
            return new TxR(OperationErrorCode.INVALID_TRANSACTION);
        }
        if (txInfo.getTransactionSql() == null || CollectionUtil.isEmpty(txInfo.getTransactionSql().getExecuteSqlList())) {
            //当前系统中获取不到事务参数
            LOGGER.error(log, "更新sql为空 :[ %s ] ", JsonUtil.format(txInfo));
            return new TxR(OperationErrorCode.ERROR_EXECUTE_SQL);
        }
        return null;
    }

    @Override
    public TxR exitEdit(TxInfo txInfo) {
        LOGGER.info(log, "conn-ID [ %s ] 开始提交事务 !!!", txInfo.getTransactionKey());
        //移除本地缓存的事务锁信息
        LockParam lockParam = factory.removeLockParam(txInfo.getTransactionKey());
        if (lockParam == null) {
            LOGGER.error(log, "找不到的事务ID :[ %s ] ", txInfo);
            //当前系统中获取不到事务参数
            return new TxR(OperationErrorCode.INVALID_TRANSACTION);
        }
        TxR txR = editorAPI.commitTransaction(txInfo.getIsCommit(), lockParam);
        LOGGER.info(log, "conn-ID [ %s ] 提交事务成功 !!!", txInfo.getTransactionKey());
        return txR;
    }
}
