package com.step.api.runtime.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link com.step.api.runtime.model.TokenInfo}.
 * NOTE: This class has been automatically generated from the {@link com.step.api.runtime.model.TokenInfo} original class using Vert.x codegen.
 */
public class TokenInfoConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TokenInfo obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "account":
          if (member.getValue() instanceof String) {
            obj.setAccount((String)member.getValue());
          }
          break;
        case "database":
          if (member.getValue() instanceof String) {
            obj.setDatabase((String)member.getValue());
          }
        case "deptCode":
          if (member.getValue() instanceof String) {
            obj.setDeptCode((String)member.getValue());
          }
          break;
        case "loginIp":
          if (member.getValue() instanceof String) {
            obj.setLoginIp((String)member.getValue());
          }
          break;
        case "loginTime":
          if (member.getValue() instanceof Number) {
            obj.setLoginTime(((Number)member.getValue()).longValue());
          }
          break;
        case "serveCode":
          if (member.getValue() instanceof String) {
            obj.setServeCode(((String)member.getValue()));
          }
          break;
        case "clientCode":
          if (member.getValue() instanceof String) {
            obj.setClientCode(((String)member.getValue()));
          }
          break;
        case "langCode":
          if (member.getValue() instanceof String) {
            obj.setLangCode(((String)member.getValue()));
          }
          break;
        case "userCode":
          if (member.getValue() instanceof String) {
            obj.setUserCode((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(TokenInfo obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TokenInfo obj, java.util.Map<String, Object> json) {
    if (obj.getAccount() != null) {
      json.put("account", obj.getAccount());
    }
    if (obj.getDatabase() != null) {
      json.put("database", obj.getDatabase());
    }
    if (obj.getDeptCode() != null) {
      json.put("deptCode", obj.getDeptCode());
    }
    if (obj.getLoginIp() != null) {
      json.put("loginIp", obj.getLoginIp());
    }
    if (obj.getLoginTime() != null) {
      json.put("loginTime", obj.getLoginTime());
    }
    if (obj.getUserCode() != null) {
      json.put("userCode", obj.getUserCode());
    }
    if (obj.getServeCode() != null) {
      json.put("serveCode", obj.getServeCode());
    }
    if (obj.getClientCode() != null) {
      json.put("clientCode", obj.getClientCode());
    }
    if (obj.getLangCode() != null) {
      json.put("langCode", obj.getLangCode());
    }
  }
}
