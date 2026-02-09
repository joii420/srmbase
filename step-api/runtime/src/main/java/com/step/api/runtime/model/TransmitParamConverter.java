package com.step.api.runtime.model;

import com.step.api.runtime.template.param.DataKeyParam;
import com.step.api.runtime.template.param.HostParam;
import com.step.api.runtime.template.param.ServiceParam;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link TransmitParam}.
 * NOTE: This class has been automatically generated from the {@link TransmitParam} original class using Vert.x codegen.
 */
public class TransmitParamConverter {


    private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
    private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

    public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TransmitParam obj) {
        for (java.util.Map.Entry<String, Object> member : json) {
            switch (member.getKey()) {
                case "account":
                    if (member.getValue() instanceof String) {
                        obj.setAccount((String) member.getValue());
                    }
                    break;
                case "clientCode":
                    if (member.getValue() instanceof String) {
                        obj.setClientCode((String) member.getValue());
                    }
                    break;
                case "database":
                    if (member.getValue() instanceof String) {
                        obj.setDatabase((String) member.getValue());
                    }
                    break;
                case "programCode":
                    if (member.getValue() instanceof String) {
                        obj.setProgramCode((String) member.getValue());
                    }
                    break;
                case "actionCode":
                    if (member.getValue() instanceof String) {
                        obj.setActionCode((String) member.getValue());
                    }
                    break;
                case "dataKey":
                    if (member.getValue() instanceof JsonObject) {
                        obj.setDataKey(new DataKeyParam((JsonObject) member.getValue()));
                    }
                    break;
                case "hostInfo":
                    if (member.getValue() instanceof JsonObject) {
                        obj.setHostInfo(new HostParam((JsonObject) member.getValue()));
                    }
                    break;
                case "serviceInfo":
                    if (member.getValue() instanceof JsonObject) {
                        obj.setServiceInfo(new ServiceParam((JsonObject) member.getValue()));
                    }
                    break;
                case "deptCode":
                    if (member.getValue() instanceof String) {
                        obj.setDeptCode((String) member.getValue());
                    }
                    break;
                case "entCode":
                    if (member.getValue() instanceof String) {
                        obj.setEntCode((String) member.getValue());
                    }
                    break;
                case "langCode":
                    if (member.getValue() instanceof String) {
                        obj.setLangCode((String) member.getValue());
                    }
                    break;
                case "param":
                    if (member.getValue() instanceof JsonObject) {
                        java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
                        ((Iterable<java.util.Map.Entry<String, Object>>) member.getValue()).forEach(entry -> {
                            if (entry.getValue() instanceof Object)
                                map.put(entry.getKey(), entry.getValue());
                        });
                        obj.setParam(map);
                    }
                    break;
                case "requestKey":
                    if (member.getValue() instanceof String) {
                        obj.setRequestKey((String) member.getValue());
                    }
                    break;
                case "serveCode":
                    if (member.getValue() instanceof String) {
                        obj.setServeCode((String) member.getValue());
                    }
                    break;
                case "siteCode":
                    if (member.getValue() instanceof String) {
                        obj.setSiteCode((String) member.getValue());
                    }
                    break;
                case "tokenKey":
                    if (member.getValue() instanceof String) {
                        obj.setTokenKey((String) member.getValue());
                    }
                    break;
                case "userCode":
                    if (member.getValue() instanceof String) {
                        obj.setUserCode((String) member.getValue());
                    }
                    break;
            }
        }
    }

    public static void toJson(TransmitParam obj, JsonObject json) {
        toJson(obj, json.getMap());
    }

    public static void toJson(TransmitParam obj, java.util.Map<String, Object> json) {
        if (obj.getAccount() != null) {
            json.put("account", obj.getAccount());
        }
        if (obj.getClientCode() != null) {
            json.put("clientCode", obj.getClientCode());
        }
        if (obj.getDatabase() != null) {
            json.put("database", obj.getDatabase());
        }
        if (obj.getProgramCode() != null) {
            json.put("programCode", obj.getProgramCode());
        }
        if (obj.getActionCode() != null) {
            json.put("actionCode", obj.getActionCode());
        }
        if (obj.getDataKey() != null) {
            json.put("dataKey", obj.getDataKey().toJson());
        }
        if (obj.getServiceInfo() != null) {
            json.put("serviceInfo", obj.getServiceInfo().toJson());
        }
        if (obj.getHostInfo() != null) {
            json.put("hostInfo", obj.getHostInfo().toJson());
        }
        if (obj.getDeptCode() != null) {
            json.put("deptCode", obj.getDeptCode());
        }
        if (obj.getEntCode() != null) {
            json.put("entCode", obj.getEntCode());
        }
        if (obj.getLangCode() != null) {
            json.put("langCode", obj.getLangCode());
        }
        if (obj.getParam() != null) {
            JsonObject map = new JsonObject();
            obj.getParam().forEach((key, value) -> map.put(key, value));
            json.put("param", map);
        }
        if (obj.getRequestKey() != null) {
            json.put("requestKey", obj.getRequestKey());
        }
        if (obj.getServeCode() != null) {
            json.put("serveCode", obj.getServeCode());
        }
        if (obj.getSiteCode() != null) {
            json.put("siteCode", obj.getSiteCode());
        }
        if (obj.getTokenKey() != null) {
            json.put("tokenKey", obj.getTokenKey());
        }
        if (obj.getUserCode() != null) {
            json.put("userCode", obj.getUserCode());
        }
    }
}
