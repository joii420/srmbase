package com.step.jdbc.runtime.txedit.model;

import com.step.api.runtime.core.IResult;
import com.step.api.runtime.exception.ResInfo;
import com.step.api.runtime.exception.ServerCode;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2023/2/1  8:43
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class TxR {
    /**
     * 操作是否成功
     */
    private boolean success;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误提示信息
     */
    private String msg;

    /**
     * 事务id
     */
    private String transactionId;

    private ResInfo errorInfo;

    public ResInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ResInfo errorInfo) {
        this.success = false;
        this.errorInfo = errorInfo;
    }

    public TxR(ResInfo errorInfo) {
        this.success = false;
        this.errorInfo = errorInfo;
    }

    public TxR(String transactionId) {
        this.success = true;
        this.code = ServerCode.SUCCESS.getCode();
        this.msg = ServerCode.SUCCESS.getMessage();
        this.transactionId = transactionId;
    }

    public TxR(IResult result) {
        this.success = false;
        this.code = result.getCode();
        this.msg = result.getMessage();
        this.errorInfo = new ResInfo(result);
    }

    public TxR(Throwable throwable) {
        this.success = false;
        this.code = ServerCode.UNKNOWN_ERROR.getCode();
        this.msg = throwable.getMessage();
    }

    public TxR(JsonObject jsonObject) {
        TxRConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        TxRConverter.toJson(this, json);
        return json;
    }

    public void setResult(IResult result) {
        this.success = false;
        this.code = result.getCode();
        this.msg = result.getMessage();
        this.errorInfo = new ResInfo(result);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
