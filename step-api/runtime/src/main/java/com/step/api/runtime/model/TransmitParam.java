package com.step.api.runtime.model;

import com.step.api.runtime.template.param.DataKeyParam;
import com.step.api.runtime.template.param.HostParam;
import com.step.api.runtime.template.param.ServiceParam;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2022/12/14  10:02
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class TransmitParam {

    /**请求key*/
    private String requestKey;
    /**令牌缓存key*/
    private String tokenKey;
    /**程序代码*/
    private String programCode;
    /**行为代码*/
    private String actionCode;
    /**用户账号*/
    private String account;
    /**系统码*/
    private String serveCode;
    /**平台码*/
    private String clientCode;
    /**语种码*/
    private String langCode;
    /**用户工号*/
    private String userCode;
    /**用户集团*/
    private String entCode;
    /**用户据点*/
    private String siteCode;
    /**用户部门*/
    private String deptCode;
    /**登录数据库   测试库/正式库*/
    private String database;
    /**service服务信息*/
    private ServiceParam serviceInfo;
    /**请求据点信息*/
    private DataKeyParam dataKey;
    /**请求据点信息*/
    private HostParam hostInfo;
    /**
     * 请求对象参数
     */
    private Map<String,Object> param;

    public HostParam getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(HostParam hostInfo) {
        this.hostInfo = hostInfo;
    }

    public ServiceParam getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceParam serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getEntCode() {
        return entCode;
    }

    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public DataKeyParam getDataKey() {
        return dataKey;
    }

    public void setDataKey(DataKeyParam dataKey) {
        this.dataKey = dataKey;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public TransmitParam(){

    }
    public TransmitParam(JsonObject jsonObject) {
    	TransmitParamConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	TransmitParamConverter.toJson(this, json);
    	return json;
    }
}
