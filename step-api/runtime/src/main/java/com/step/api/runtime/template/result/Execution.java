package com.step.api.runtime.template.result;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/12/13  12:49
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class Execution {
    /*
     "execution": {
                        "code": "0",
                        "sql_code": "",  //sqlcode
                        "description": ""
                    },
     */
    /**
     * code: 錯誤碼, 0表示成功正常，非 0 則為錯誤
     */
    private String code;
    /**
     * sql_code: 資料庫回傳代碼
     */
    private String sql_code;
    /**
     * description: 錯誤訊息
     */
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSql_code() {
        return sql_code;
    }

    public void setSql_code(String sql_code) {
        this.sql_code = sql_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Execution(){

    }
    public Execution(JsonObject jsonObject) {
    	ExecutionConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	ExecutionConverter.toJson(this, json);
    	return json;
    }
}
