package com.step.jdbc.runtime.txedit.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2023/1/31  16:04
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TxInfo {

    private Boolean isCommit = true;

    private String transactionKey;

    private String serverName;

    private TransactionSql transactionSql;

    private String lockSql;

    public String getLockSql() {
        return lockSql;
    }

    public void setLockSql(String lockSql) {
        this.lockSql = lockSql;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public TransactionSql getTransactionSql() {
        return transactionSql;
    }

    public void setTransactionSql(TransactionSql transactionSql) {
        this.transactionSql = transactionSql;
    }

    public Boolean getIsCommit() {
        return isCommit;
    }

    public void setIsCommit(Boolean commit) {
        isCommit = commit;
    }

    public TxInfo(String transactionKey, String serverName, String lockSql, TransactionSql transactionSql) {
        this.transactionKey = transactionKey;
        this.serverName = serverName;
        this.lockSql = lockSql;
        this.transactionSql = transactionSql;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public TxInfo(boolean isCommit, String transactionKey, String serverName) {
        this.isCommit = isCommit;
        this.transactionKey = transactionKey;
        this.serverName = serverName;
    }

    public TxInfo(boolean isCommit, String transactionKey, String serverName,TransactionSql transactionSql) {
        this.isCommit = isCommit;
        this.transactionKey = transactionKey;
        this.serverName = serverName;
        this.transactionSql = transactionSql;
    }

    public TxInfo(JsonObject jsonObject) {
        TxInfoConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TxInfoConverter.toJson(this, json);
        return json;
    }
}
