package com.step.api.runtime.template.result;

import com.step.api.runtime.exception.Result;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2022/12/13  12:52
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class StdData {
    /*

        "std_data": {
               "execution": {
                    "code": "0",
                    "sql_code": "",  //sqlcode
                    "description": ""
                },
                "parameter": {
                "employee_name": "员工姓名"
                }
         }
     */
    /**
     * 服務執行狀態
     */
    private Execution execution;
    /**
     * 多个错误信息
     */
    private List<Result> executions;
    /**
     * 參數資料
     */
    private Map<String,Object> parameter;

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public Map<String,Object> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String,Object> parameter) {
        this.parameter = parameter;
    }

    public List<Result> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Result> executions) {
        this.executions = executions;
    }

    public StdData(){

    }
    public StdData(JsonObject jsonObject) {
    	StdDataConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	StdDataConverter.toJson(this, json);
    	return json;
    }
}
