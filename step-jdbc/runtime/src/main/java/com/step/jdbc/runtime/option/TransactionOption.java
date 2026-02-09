package com.step.jdbc.runtime.option;

/**
 * @author : Sun
 * @date : 2023/1/17  15:45
 */
public class TransactionOption {
    private static final int DEFAULT_MAX_EXECUTE_SECONDS = 30;
    private static final int DEFAULT_RETRY_COUNT = 3;
    private static final long DEFAULT_WAIT_TIME = 500;
    /**重试次数*/
    private Integer retryCont;
    /**重试等待时间*/
    private Long waitTime;
    /**用户行为锁锁定时间 默认为30秒*/
    private int executeTime = DEFAULT_MAX_EXECUTE_SECONDS;

    public Integer getRetryCont() {
        return retryCont;
    }

    public void setRetryCont(Integer retryCont) {
        this.retryCont = retryCont;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public TransactionOption() {
        this.retryCont = DEFAULT_RETRY_COUNT;
        this.waitTime = DEFAULT_WAIT_TIME;
    }

    public TransactionOption(Integer retryCont) {
        this.retryCont = retryCont;
        this.waitTime = DEFAULT_WAIT_TIME;
    }

    public TransactionOption(Long waitTime) {
        this.retryCont = DEFAULT_RETRY_COUNT;
        this.waitTime = waitTime;
    }

    public TransactionOption(Integer retryCont, Long waitTime) {
        this.retryCont = retryCont;
        this.waitTime = waitTime;
    }


}
