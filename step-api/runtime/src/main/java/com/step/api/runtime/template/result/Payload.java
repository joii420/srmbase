package com.step.api.runtime.template.result;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/12/13  12:51
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class Payload {
    /*
    "payload": {
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
        }
     */
    private StdData std_data;

    public StdData getStd_data() {
        return std_data;
    }

    public void setStd_data(StdData std_data) {
        this.std_data = std_data;
    }

    public Payload(){

    }
    public Payload(JsonObject jsonObject) {
    	PayloadConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	PayloadConverter.toJson(this, json);
    	return json;
    }
}
