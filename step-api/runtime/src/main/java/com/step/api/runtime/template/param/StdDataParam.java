package com.step.api.runtime.template.param;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2022/7/14  8:07
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class StdDataParam {

	/**
	 * 參數資料
	 */
	private Map<String,Object> parameter;

	public Map<String, Object> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
	}

	public StdDataParam(){

	}
	public StdDataParam(JsonObject jsonObject) {
		StdDataParamConverter.fromJson(jsonObject, this);
	}
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		StdDataParamConverter.toJson(this, json);
		return json;
	}
}
