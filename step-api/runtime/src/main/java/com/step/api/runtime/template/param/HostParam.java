package com.step.api.runtime.template.param;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/7/14  8:05
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class HostParam {
	/*
	"host": {
			"prod": "APP",
			"ip": "192.168.10.1",
			"lang": "zh_CN",
			"acct": "tiptop",
			"timestamp": "86952503780471902:00:00:00:00:003fe4b9567a30f1a0K011220712091607",
			"appid": "78:8E:33:13:DE:39",   12/14 改名为 macAddr
			"appmodule": "K011"
		},
		*/
	/**
	 * 请求平台
	 */
	private String prod;
	/**
	 * 请求ip
	 */
	private String ip;
	/**
	 * 请求语种
	 */
	private String lang;
	/**
	 * 请求账号
	 */
	private String acct;
	/**
	 * 临时数据key
	 */
	private String timestamp;
	/**
	 * 设备地址 mac地址
	 */
	private String macAddr;
	/**
	 * 请求menuCode  程序代码
	 */
	private String appmodule;
	/**
	 * 版本号
	 */
	private String version;

	public String getProd() {
		return prod;
	}

	public void setProd(String prod) {
		this.prod = prod;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getAppmodule() {
		return appmodule;
	}

	public void setAppmodule(String appmodule) {
		this.appmodule = appmodule;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public HostParam(){

	}
	public HostParam(JsonObject jsonObject) {
		HostParamConverter.fromJson(jsonObject, this);
	}
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		HostParamConverter.toJson(this, json);
		return json;
	}
}
