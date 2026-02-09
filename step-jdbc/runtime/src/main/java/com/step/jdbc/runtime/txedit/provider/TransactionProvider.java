package com.step.jdbc.runtime.txedit.provider;


import com.step.jdbc.runtime.api.TransactionAPI;
import com.step.jdbc.runtime.txedit.config.TransactionConfig;

import jakarta.inject.Singleton;
import jakarta.transaction.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TransactionProvider implements TransactionAPI {

    /**
     * 用于在服务器端保存事务状态的Map
     */
    private final Map<String, Transaction> trxMap = new HashMap<>();

    private final TransactionManager transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();


    @Override
    public void begin() {
        try {
            transactionManager.begin();
            transactionManager.setTransactionTimeout(TransactionConfig.TIME_OUT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction suspend() {
        try {
            return transactionManager.suspend();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resume(Transaction transaction) {
        try {
            transactionManager.resume(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        try {
            transactionManager.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            transactionManager.rollback();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
