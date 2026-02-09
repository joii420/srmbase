package com.step.api.runtime.exception;

import com.step.api.runtime.core.IResult;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * @author : Sun
 * @date : 2023/2/3  13:02
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ResInfo implements Serializable {
    private String code;
    private String lang;
    private String message;
    private String sqlCode;
    private String sqlMessage;
    private List<String> params;

    private ResInfo() {
    }

    public ResInfo(IResult result) {
        this.code = result.getCode();
        this.lang = Result.DEFAULT_LANG_CODE;
        this.message = result.getMessage();
    }

    public ResInfo(IResult result, String lang) {
        this.code = result.getCode();
        this.lang = lang;
        this.message = result.getMessage();
    }

    public String getSqlMessage() {
        return sqlMessage;
    }

    public void setSqlMessage(String sqlMessage) {
        this.sqlMessage = sqlMessage;
    }

    public String getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String getMessage() {
        return message;
    }

    public String getSqlCode() {
        return sqlCode;
    }

    public List<String> getParams() {
        return params;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSqlCode(String sqlCode) {
        this.sqlCode = sqlCode;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public ResInfo(String message) {
        this.code = Result.DEFAULT_ERROR_CODE;
        this.lang = Result.DEFAULT_LANG_CODE;
        this.message = message;
    }

    public ResInfo(JsonObject jsonObject) {
        ResInfoConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ResInfoConverter.toJson(this, json);
        return json;
    }

    public void formatSqlException(SQLException e) {
        this.setSqlCode(e.getErrorCode() == 0 ? e.getSQLState() : String.valueOf(e.getErrorCode()));
        this.setSqlMessage(e.getMessage());
    }
}
