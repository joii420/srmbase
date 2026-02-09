package com.step.api.runtime.template.result;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/12/13  12:46
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class StepResult {

    public static final  String DEFAULT_SRVVER="1.0";
    public static final  String DEFAULT_ERROR_SRVCODE="100";
    public static final  String DEFAULT_SUCC_SRVCODE="000";
    /*
    {
        "srvver": "1.0",
        "srvcode": "000",
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
     }

     */


    /**
     * 服務元件版本，可追蹤是否呼叫到正確且預期的服務元件版本
     * 当前默认值 1.0
     */
    private String srvver;
    /**
     * 服務執行結果狀態代碼
     * 000: 服務按照正常流程，並產生預期的結果(Basic flow)。
     * 100: 服務未按照正常流程執行，例如發生資訊檢核不通過等情況，導致無法完成預期的結果。
     */
    private String srvcode;
    /**
     * 資料區
     */
    private Payload payload;

    public String getSrvver() {
        return srvver;
    }

    public void setSrvver(String srvver) {
        this.srvver = srvver;
    }

    public String getSrvcode() {
        return srvcode;
    }

    public void setSrvcode(String srvcode) {
        this.srvcode = srvcode;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public StepResult() {
        this.srvver = DEFAULT_SRVVER;
        this.srvcode = DEFAULT_SUCC_SRVCODE;
    }

    public StepResult(JsonObject jsonObject) {
        StepResultConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        StepResultConverter.toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        return JsonObject.mapFrom(this).toString();
    }
}
