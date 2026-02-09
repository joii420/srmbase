package com.step.api.runtime.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2022/12/14  10:10
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class UserAuth {
    /*
       缓存的权限模型:
       根据token中的serveCode和clientCode 查询出来的对应的程序权限信息,保存到缓存 List<UserAuth>
    */
    private String entCode;
    private String siteCode;
    private String deptCode;
    /**程序权限*/
    private Map<String,ValidProgram> programAuths;

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

    public Map<String, ValidProgram> getProgramAuths() {
        return programAuths;
    }

    public void setProgramAuths(Map<String, ValidProgram> programAuths) {
        this.programAuths = programAuths;
    }

    public UserAuth(){

    }
    public UserAuth(JsonObject jsonObject) {
    	UserAuthConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	UserAuthConverter.toJson(this, json);
    	return json;
    }
}
