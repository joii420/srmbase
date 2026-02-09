package com.step.jdbc.runtime.api;

import jakarta.transaction.Transaction;

public interface TransactionAPI {


    /**
     * 开启事务
     */
    void begin();

    /**
     * 暂停事务
     */
    Transaction suspend();

    /**
     * 恢复事务
     */
    void resume(Transaction transaction);

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();

}
