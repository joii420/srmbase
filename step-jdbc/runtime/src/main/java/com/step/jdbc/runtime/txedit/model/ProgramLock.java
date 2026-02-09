package com.step.jdbc.runtime.txedit.model;

import com.step.jdbc.runtime.txedit.model.enums.EditType;

/**
 * 程序锁对象
 * @author joii
 */
public class ProgramLock {
    /**
     * 程序代码
     */
    private String programCode;
    /**
     * 数据主键
     */
    private String dataKey;
    /**
     * 事务编辑类型
     */
    private Integer editType;
    /**
     * 数据源key
     */
    private String jdbcKey;
    /**
     * 账号
     */
    private String account;
    /**
     * 锁定ip
     */
    private String ip;
    /**
     * 用户工号
     */
    private String userCode;
    /**
     * 事务状态key
     */
    private String transactionKey;
    /**
     *  用户行为锁 确保行为原子性的key
     */
    private String executeKey;
    /**
     * 服务器信息
     * (从vert的getAddress上切入)
     */
    private String serviceInfo;
    /**
     * 锁定时间戳
     */
    private Long lockTime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getExecuteKey() {
        return executeKey;
    }

    public void setExecuteKey(String executeKey) {
        this.executeKey = executeKey;
    }

    public ProgramLock() {
        this.editType = EditType.JDBC.getEditType();
    }

    public Integer getEditType() {
        return editType;
    }

    public void setEditType(Integer editType) {
        this.editType = editType;
    }

    public String getJdbcKey() {
        return jdbcKey;
    }

    public void setJdbcKey(String jdbcKey) {
        this.jdbcKey = jdbcKey;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }
}
