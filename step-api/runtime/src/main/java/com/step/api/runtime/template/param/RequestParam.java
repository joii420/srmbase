package com.step.api.runtime.template.param;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2022/7/14  8:16
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class RequestParam {
	/*
	{
    "key": "d41d8cd98f00b204e9800998ecf8427e",
    "type": "sync",
    "host": {
        "prod": "APP",
        "ip": "192.168.10.1",
        "lang": "zh_CN",
        "acct": "tiptop",
        "timestamp": "86952503780471902:00:00:00:00:003fe4b9567a30f1a0K011220712091607",
        "appid": "78:8E:33:13:DE:39",
        "appmodule": "K011"
    },
    "datakey": {
        "EntId": "wzhf12",
        "CompanyId": "WZHF12",
        "postDate": "$parameter.submitAdapterReq.postDate"
    },
    "service": {
        "ip": "10.40.40.18",
        "prod": "T100",
        "id": "topprd",
        "name": "cwsp106"
    },
    "payload": {
        "std_data": {
            "parameter": {
                "wo": [{
                        "subopNo": "Z029",
                        "seqNo": "10",
                        "runcardNo": "C142-2207000945-00",
                        "checkPlan": "",
                        "employeeNo": "1905101",
                        "taEcb07": ""
                    }
                ]
            }
        }
    }
}
	 */
	/**
	 * 请求key
	 */
	private String key;
	/**
	 * 请求类型 sync
	 */
	private String type;
	/**
	 * 请求设备ip参数
	 */
	private HostParam host;
	/**
	 * 请求服务参数
	 */
	private ServiceParam service;
	/**
	 * 请求方资源参数
	 */
	private DataKeyParam datakey;
	/**
	 * 请求参数
	 */
	private PayloadParam payload;
	/**
	 * 扩展参数
	 */
	private Object extend;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HostParam getHost() {
		return host;
	}

	public void setHost(HostParam host) {
		this.host = host;
	}

	public ServiceParam getService() {
		return service;
	}

	public void setService(ServiceParam service) {
		this.service = service;
	}

	public DataKeyParam getDatakey() {
		return datakey;
	}

	public void setDatakey(DataKeyParam datakey) {
		this.datakey = datakey;
	}

	public PayloadParam getPayload() {
		return payload;
	}

	public void setPayload(PayloadParam payload) {
		this.payload = payload;
	}

	public Object getExtend() {
		return extend;
	}

	public void setExtend(Object extend) {
		this.extend = extend;
	}

	public RequestParam(){

	}
	public RequestParam(JsonObject jsonObject) {
		RequestParamConverter.fromJson(jsonObject, this);
	}
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		RequestParamConverter.toJson(this, json);
		return json;
	}
}
