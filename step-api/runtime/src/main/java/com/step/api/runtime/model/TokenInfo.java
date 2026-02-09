package com.step.api.runtime.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/12/14  8:46
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class TokenInfo {

    /**登录账号*/
    private String account;
    /**用户工号*/
    private String userCode;;
    /**用户部门编号*/
    private String deptCode;
    /**登录系统*/
    private String serveCode;
    /**登录客户端类型*/
    private String clientCode;
    /**登录语种类型*/
    private String langCode;
    /**登录的数据库类型*/
    private String database;
    /**登录IP*/
    private String loginIp;
    /**登录时间*/
    private Long loginTime;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
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

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getServeCode() {
        return serveCode;
    }

    public void setServeCode(String serveCode) {
        this.serveCode = serveCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return JsonObject.mapFrom(this).toString();
    }

    public TokenInfo(){

    }
    public TokenInfo(JsonObject jsonObject) {
    	TokenInfoConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	TokenInfoConverter.toJson(this, json);
    	return json;
    }
}
