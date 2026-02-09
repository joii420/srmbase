package com.step.api.runtime.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/12/14  10:44
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class RequestInfo {
    /**
     * requestInfo
     */
    private String requestIp;
    private String requestHeader;
    private String requestType;
    private String requestPath;
    private String requestBody;
    private String requestParam;
    private String requestMac;

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestMac() {
        return requestMac;
    }

    public void setRequestMac(String requestMac) {
        this.requestMac = requestMac;
    }

    public RequestInfo(){

    }
    public RequestInfo(JsonObject jsonObject) {
    	RequestInfoConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	RequestInfoConverter.toJson(this, json);
    	return json;
    }
}
