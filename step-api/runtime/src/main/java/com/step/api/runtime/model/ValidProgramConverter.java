package com.step.api.runtime.model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link ValidProgram}.
 * NOTE: This class has been automatically generated from the {@link ValidProgram} original class using Vert.x codegen.
 */
public class ValidProgramConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ValidProgram obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "expiredTime":
          if (member.getValue() instanceof Number) {
            obj.setExpiredTime(((Number)member.getValue()).longValue());
          }
          break;
        case "functions":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setFunctions(list);
          }
          break;
        case "program":
          if (member.getValue() instanceof String) {
            obj.setProgram((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ValidProgram obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ValidProgram obj, java.util.Map<String, Object> json) {
    if (obj.getExpiredTime() != null) {
      json.put("expiredTime", obj.getExpiredTime());
    }
    if (obj.getFunctions() != null) {
      JsonArray array = new JsonArray();
      obj.getFunctions().forEach(item -> array.add(item));
      json.put("functions", array);
    }
    if (obj.getProgram() != null) {
      json.put("program", obj.getProgram());
    }
  }
}
