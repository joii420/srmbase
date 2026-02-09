package com.step.api.runtime.template.param;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/7/14  8:07
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class DataKeyParam {

	/*
	     "datakey": {
        "EntId": "wzhf12",
        "CompanyId": "WZHF12",
        "postDate": "$parameter.submitAdapterReq.postDate"
    },
	 */
	/**
	 * 请求处理的企业
	 */
	private String EntId;
	/**
	 * 请求处理的营运中心
	 */
	private String CompanyId;
	/**
	 * 过账日期
	 */
	private String postDate;

	public String getEntId() {
		return EntId;
	}

	public void setEntId(String entId) {
		this.EntId = entId;
	}

	public String getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(String companyId) {
		this.CompanyId = companyId;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}


	public DataKeyParam(){

	}
	public DataKeyParam(JsonObject jsonObject) {
		DataKeyParamConverter.fromJson(jsonObject, this);
	}
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		DataKeyParamConverter.toJson(this, json);
		return json;
	}
}
