package com.step.jdbc.runtime.txedit.model;

import com.step.jdbc.runtime.param.JdbcParam;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2023/1/5  13:23
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class LockParam {
    /**
     * 程序代码
     */
    private String programCode;
    /**
     * 数据主键
     */
    private String dataKey;
    /**
     * 事务锁key
     */
    private String tranKey;
    /**
     * 数据源key
     */
    private String jdbcDataSourceKey;
    /**
     * 锁表sql
     */
    private String lockSql;
    /**
     * 组装好的锁表sql;
     */
    private String handleLockSql;
    /***
     * 用户的数据库连接信息
     */
    private UserTx userTx;
    /**
     * jdbc连接信息
     */
    private JdbcParam jdbcParam;
    /**
     * 事务服务器信息
     */
    private String serverName;
    /**
     * 开始锁定的时间戳
     */
    private Long lockTime;

    public JdbcParam getJdbcParam() {
        return jdbcParam;
    }

    public void setJdbcParam(JdbcParam jdbcParam) {
        this.jdbcParam = jdbcParam;
    }
    public LockParam(){

    }
    public LockParam(JsonObject jsonObject) {
    	LockParamConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	LockParamConverter.toJson(this, json);
    	return json;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public UserTx getUserTx() {
        return userTx;
    }

    public void setUserTx(UserTx userTx) {
        this.userTx = userTx;
    }

    public String getJdbcDataSourceKey() {
        return jdbcDataSourceKey;
    }

    public void setJdbcDataSourceKey(String jdbcDataSourceKey) {
        this.jdbcDataSourceKey = jdbcDataSourceKey;
    }

    public String getTranKey() {
        return tranKey;
    }

    public void setTranKey(String tranKey) {
        this.tranKey = tranKey;
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

    public String getLockSql() {
        return lockSql;
    }

    public void setLockSql(String lockSql) {
        this.lockSql = lockSql;
    }

    public String getHandleLockSql() {
        return handleLockSql;
    }

    public void setHandleLockSql(String handleLockSql) {
        this.handleLockSql = handleLockSql;
    }

    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }
}
