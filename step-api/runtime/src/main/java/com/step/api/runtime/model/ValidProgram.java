package com.step.api.runtime.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author : Sun
 * @date : 2022/11/19  9:31
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class ValidProgram {

    /**程序代码*/
    private String program;
    /**功能*/
    private List<String> functions;
    /**程序过期时间*/
    private Long expiredTime;

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public List<String> getFunctions() {
        return functions;
    }

    public void setFunctions(List<String> functions) {
        this.functions = functions;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public ValidProgram(){
    }

    public ValidProgram(String program, Long expiredTime,List<String> functions) {
        this.program = program;
        this.expiredTime = expiredTime;
        this.functions = functions;
    }

    public ValidProgram(JsonObject jsonObject) {
    	ValidProgramConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	ValidProgramConverter.toJson(this, json);
    	return json;
    }
}
