package com.step.api.runtime.exception;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link com.step.api.runtime.exception.ResInfo}.
 * NOTE: This class has been automatically generated from the {@link com.step.api.runtime.exception.ResInfo} original class using Vert.x codegen.
 */
public class ResInfoConverter {


    private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
    private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

    public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ResInfo obj) {
        for (java.util.Map.Entry<String, Object> member : json) {
            switch (member.getKey()) {
                case "code":
                    if (member.getValue() instanceof String) {
                        obj.setCode((String) member.getValue());
                    }
                    break;
                case "lang":
                    if (member.getValue() instanceof String) {
                        obj.setLang((String) member.getValue());
                    }
                    break;
                case "message":
                    if (member.getValue() instanceof String) {
                        obj.setMessage((String) member.getValue());
                    }
                    break;
                case "params":
                    if (member.getValue() instanceof JsonArray) {
                        java.util.ArrayList<String> list = new java.util.ArrayList<>();
                        ((Iterable<Object>) member.getValue()).forEach(item -> {
                            if (item instanceof String)
                                list.add((String) item);
                        });
                        obj.setParams(list);
                    }
                    break;
                case "sqlCode":
                    if (member.getValue() instanceof String) {
                        obj.setSqlCode((String) member.getValue());
                    }
                    break;
                case "sqlMessage":
                    if (member.getValue() instanceof String) {
                        obj.setSqlMessage((String) member.getValue());
                    }
                    break;
            }
        }
    }

    public static void toJson(ResInfo obj, JsonObject json) {
        toJson(obj, json.getMap());
    }

    public static void toJson(ResInfo obj, java.util.Map<String, Object> json) {
        if (obj.getCode() != null) {
            json.put("code", obj.getCode());
        }
        if (obj.getLang() != null) {
            json.put("lang", obj.getLang());
        }
        if (obj.getMessage() != null) {
            json.put("message", obj.getMessage());
        }
        if (obj.getParams() != null) {
            JsonArray array = new JsonArray();
            obj.getParams().forEach(item -> array.add(item));
            json.put("params", array);
        }
        if (obj.getSqlCode() != null) {
            json.put("sqlCode", obj.getSqlCode());
        }
        if (obj.getSqlMessage() != null) {
            json.put("sqlMessage", obj.getSqlMessage());
        }
    }
}
