package com.step.api.runtime.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link com.step.tx.service.async.UserAuth}.
 * NOTE: This class has been automatically generated from the {@link com.step.tx.service.async.UserAuth} original class using Vert.x codegen.
 */
public class UserAuthConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, UserAuth obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "deptCode":
          if (member.getValue() instanceof String) {
            obj.setDeptCode((String)member.getValue());
          }
          break;
        case "entCode":
          if (member.getValue() instanceof String) {
            obj.setEntCode((String)member.getValue());
          }
          break;
        case "programAuths":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, ValidProgram> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                map.put(entry.getKey(), new ValidProgram((JsonObject)entry.getValue()));
            });
            obj.setProgramAuths(map);
          }
          break;
        case "siteCode":
          if (member.getValue() instanceof String) {
            obj.setSiteCode((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(UserAuth obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(UserAuth obj, java.util.Map<String, Object> json) {
    if (obj.getDeptCode() != null) {
      json.put("deptCode", obj.getDeptCode());
    }
    if (obj.getEntCode() != null) {
      json.put("entCode", obj.getEntCode());
    }
    if (obj.getProgramAuths() != null) {
      JsonObject map = new JsonObject();
      obj.getProgramAuths().forEach((key, value) -> map.put(key, value.toJson()));
      json.put("programAuths", map);
    }
    if (obj.getSiteCode() != null) {
      json.put("siteCode", obj.getSiteCode());
    }
  }
}
