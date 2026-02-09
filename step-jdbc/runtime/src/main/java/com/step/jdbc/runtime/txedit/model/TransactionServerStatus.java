package com.step.jdbc.runtime.txedit.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2023/2/1  20:28
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class TransactionServerStatus {

    private String transactionServerName;
    /**
     * 服务是否在线
     */
    private boolean active;
    /**
     * 超过限制数量
     */
    private boolean busy;
    /**
     * 设置当前计数
     */
    private long currentCount;

    private long maxCount;

    public TransactionServerStatus(String transactionServerName) {
        this.transactionServerName = transactionServerName;
        this.active = true;
        this.busy = false;
        this.currentCount = 0;
        this.maxCount = 10;
    }

    public TransactionServerStatus(){

    }
    public TransactionServerStatus(JsonObject jsonObject) {
    	TransactionServerStatusConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	TransactionServerStatusConverter.toJson(this, json);
    	return json;
    }

    public long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(long currentCount) {
        this.currentCount = currentCount;
    }

    public String getTransactionServerName() {
        return transactionServerName;
    }

    public void setTransactionServerName(String transactionServerName) {
        this.transactionServerName = transactionServerName;
    }

    public long getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
}
