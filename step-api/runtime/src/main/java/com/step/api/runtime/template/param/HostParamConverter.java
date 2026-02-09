package com.step.api.runtime.template.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link HostParam}.
 * NOTE: This class has been automatically generated from the {@link HostParam} original class using Vert.x codegen.
 */
public class HostParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, HostParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "acct":
          if (member.getValue() instanceof String) {
            obj.setAcct((String)member.getValue());
          }
          break;
        case "appmodule":
          if (member.getValue() instanceof String) {
            obj.setAppmodule((String)member.getValue());
          }
          break;
        case "version":
          if (member.getValue() instanceof String) {
            obj.setVersion((String)member.getValue());
          }
          break;
        case "ip":
          if (member.getValue() instanceof String) {
            obj.setIp((String)member.getValue());
          }
          break;
        case "lang":
          if (member.getValue() instanceof String) {
            obj.setLang((String)member.getValue());
          }
          break;
        case "macAddr":
          if (member.getValue() instanceof String) {
            obj.setMacAddr((String)member.getValue());
          }
          break;
        case "prod":
          if (member.getValue() instanceof String) {
            obj.setProd((String)member.getValue());
          }
          break;
        case "timestamp":
          if (member.getValue() instanceof String) {
            obj.setTimestamp((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(HostParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(HostParam obj, java.util.Map<String, Object> json) {
    if (obj.getAcct() != null) {
      json.put("acct", obj.getAcct());
    }
    if (obj.getAppmodule() != null) {
      json.put("appmodule", obj.getAppmodule());
    }
    if (obj.getVersion() != null) {
      json.put("version", obj.getVersion());
    }
    if (obj.getIp() != null) {
      json.put("ip", obj.getIp());
    }
    if (obj.getLang() != null) {
      json.put("lang", obj.getLang());
    }
    if (obj.getMacAddr() != null) {
      json.put("macAddr", obj.getMacAddr());
    }
    if (obj.getProd() != null) {
      json.put("prod", obj.getProd());
    }
    if (obj.getTimestamp() != null) {
      json.put("timestamp", obj.getTimestamp());
    }
  }
}
