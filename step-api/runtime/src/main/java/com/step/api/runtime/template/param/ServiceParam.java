package com.step.api.runtime.template.param;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/7/14  8:08
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ServiceParam {

	/*
	    "service": {
        "ip": "10.40.40.18",
        "prod": "T100",
        "id": "topprd",
        "name": "cwsp106"
    },
	 */
    /**
     * 访问ip
     */
    private String ip;
    /**
     * 访问系统别
     */
    private String id;
    /**
     * 程序功能名称
     */
    private String name;
    /**
     * 访问库(正式库,测试库)
     */
    private String prod;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceParam() {

    }

    public ServiceParam(JsonObject jsonObject) {
        ServiceParamConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ServiceParamConverter.toJson(this, json);
        return json;
    }
}
