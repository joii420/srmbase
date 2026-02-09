package com.step.jdbc.runtime.txedit.provider.test.txedit.provider;


import com.step.jdbc.runtime.txedit.model.TransactionSql;
import com.step.jdbc.runtime.txedit.model.LockParam;
import com.step.jdbc.runtime.txedit.model.TxR;

/**
 * @author : Sun
 * @date : 2023/1/5  13:41
 */
public interface EditorAPI {
    /**
     * 开启编辑
     *
     * @param lockParam jdbcKey   使用的jdbc数据源
     *                  transactionKey     用户当前的事务密钥
     *                  sqlLocks      数据锁定sql
     */
    TxR beginEdit(LockParam lockParam);

    /**
     * 用户更新数据的操作,要将操作调取带有事务状态的服务器处理
     */
    TxR updateData(String lockSql, LockParam lockParam, TransactionSql transactionSql);

    TxR updateDataNonCommit(LockParam lockParam, TransactionSql transactionSql);


    /**
     * 解锁之前的事务
     *
     * @param lock lock
     */
    void unlock(boolean isCommit,LockParam lock);

    /**
     * 提交之前的事务
     *
     * @param isCommit  true 为 Commit  false 为 Rollback
     * @param lockParam 事务锁定的参数信息
     */
    TxR commitTransaction(boolean isCommit, LockParam lockParam);

}
