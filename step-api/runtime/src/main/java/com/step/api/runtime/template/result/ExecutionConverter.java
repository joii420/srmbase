package com.step.api.runtime.template.result;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link Execution}.
 * NOTE: This class has been automatically generated from the {@link Execution} original class using Vert.x codegen.
 */
public class ExecutionConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Execution obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "code":
          if (member.getValue() instanceof String) {
            obj.setCode((String)member.getValue());
          }
          break;
        case "description":
          if (member.getValue() instanceof String) {
            obj.setDescription((String)member.getValue());
          }
          break;
        case "sql_code":
          if (member.getValue() instanceof String) {
            obj.setSql_code((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Execution obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Execution obj, java.util.Map<String, Object> json) {
    if (obj.getCode() != null) {
      json.put("code", obj.getCode());
    }
    if (obj.getDescription() != null) {
      json.put("description", obj.getDescription());
    }
    if (obj.getSql_code() != null) {
      json.put("sql_code", obj.getSql_code());
    }
  }
}
