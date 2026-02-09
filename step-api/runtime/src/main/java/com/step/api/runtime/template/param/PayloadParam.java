package com.step.api.runtime.template.param;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/7/14  9:03
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class PayloadParam {
	/**
	 * 请求参数
	 */
	private StdDataParam std_data;

	public StdDataParam getStd_data() {
		return std_data;
	}

	public void setStd_data(StdDataParam std_data) {
		this.std_data = std_data;
	}

	public PayloadParam(){

	}
	public PayloadParam(JsonObject jsonObject) {
		PayloadParamConverter.fromJson(jsonObject, this);
	}
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		PayloadParamConverter.toJson(this, json);
		return json;
	}
}
