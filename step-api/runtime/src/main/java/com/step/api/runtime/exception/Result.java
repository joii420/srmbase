package com.step.api.runtime.exception;


import com.step.api.runtime.core.IResult;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * description :
 *
 * @author : Joii-AutoVO
 * 2022-12-03 11:09:36
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class Result implements Serializable {
    /**
     * 默认sqlCode 即sql执行正常
     */
    public static final String DEFAULT_SQL_CODE = "0";
    /**
     * 默认语种
     */
    public static final String DEFAULT_LANG_CODE = "zh_CN";
    public static final String DEFAULT_ERROR_CODE = ServerCode.UNKNOWN_ERROR.getCode();
    public static final String DEFAULT_SUCCESS_CODE = ServerCode.SUCCESS.getCode();
    public static final String DEFAULT_ERROR_TYPE = "error";
    public static final String DEFAULT_SUCCESS_TYPE = "success";
    public static final String DEFAULT_ERROR_INFO = ServerCode.UNKNOWN_ERROR.getMessage();
    private static final long serialVersionUID = 1L;
    /**
     * 类型
     **/
    private String type;
    private String typeName;
    /**
     * 属性编号
     */
    private String code;
    /**
     * 语种代码 1-ZH  2-EN  3-JP  4-DE
     */
    private String language;
    /**
     * 错误显示值
     */
    private String message;
    /**
     * 建议信息
     */
    private String proposal;
    /**
     * 建议运行程序
     */
    private String proposalProgram;
    /**
     * 高端信息 sql错误代码
     */
    private String sqlCode;
    /**
     * 高端信息 sql错误代码
     */
    private String sqlMessage;
    /**
     * 按钮1
     */
    private String btn1;
    /**
     * 按钮2
     */
    private String btn2;

    public String getSqlMessage() {
        return sqlMessage;
    }

    public void setSqlMessage(String sqlMessage) {
        this.sqlMessage = sqlMessage;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProposalProgram() {
        return proposalProgram;
    }

    public void setProposalProgram(String proposalProgram) {
        this.proposalProgram = proposalProgram;
    }

    public String getSqlCode() {
        return sqlCode;
    }

    public void setSqlCode(String sqlCode) {
        this.sqlCode = sqlCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLangCode() {
        return this.language;
    }

    public void setLangCode(String langCode) {
        this.language = langCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getBtn1() {
        return btn1;
    }

    public void setBtn1(String btn1) {
        this.btn1 = btn1;
    }

    public String getBtn2() {
        return btn2;
    }

    public void setBtn2(String btn2) {
        this.btn2 = btn2;
    }

    public Result() {

    }

    public Result(IResult result) {
        this.type = DEFAULT_ERROR_TYPE;
        this.language = DEFAULT_LANG_CODE;
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    public Result(String code, String message) {
        this.type = DEFAULT_ERROR_TYPE;
        this.language = DEFAULT_LANG_CODE;
        this.code = code;
        this.message = message;
    }

    public Result(String type, String code, String language, String message) {
        this.type = type;
        this.code = code;
        this.language = language;
        this.message = message;
        this.sqlCode = DEFAULT_SQL_CODE;
    }

    public Result(String type, String code, String language, String message, String sqlCode) {
        this.type = type;
        this.code = code;
        this.language = language;
        this.message = message;
        this.sqlCode = sqlCode;
    }

    public Result(JsonObject jsonObject) {
        ResultConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ResultConverter.toJson(this, json);
        return json;
    }

    public static Result error(IResult errorCode, String sqlCode) {
        Result errorInfo = new Result();
        errorInfo.setCode(errorCode.getCode());
        errorInfo.setSqlCode(sqlCode);
        return errorInfo;
    }
}
