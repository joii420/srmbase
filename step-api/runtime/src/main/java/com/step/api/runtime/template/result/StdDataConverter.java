package com.step.api.runtime.template.result;

import com.step.api.runtime.exception.Result;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link StdData}.
 * NOTE: This class has been automatically generated from the {@link StdData} original class using Vert.x codegen.
 */
public class StdDataConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, StdData obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "execution":
          if (member.getValue() instanceof JsonObject) {
            obj.setExecution(new Execution((JsonObject)member.getValue()));
          }
          break;
        case "executions":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<Result> list = new java.util.ArrayList<>();
            ((Iterable<Object>) member.getValue()).forEach(item -> {
              if (item instanceof JsonObject)
                list.add(new Result((JsonObject) item));
            });
            obj.setExecutions(list);
          }
          break;
        case "parameter":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Object)
                map.put(entry.getKey(), entry.getValue());
            });
            obj.setParameter(map);
          }
          break;
      }
    }
  }

  public static void toJson(StdData obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(StdData obj, java.util.Map<String, Object> json) {
    if (obj.getExecution() != null) {
      json.put("execution", obj.getExecution().toJson());
    }
    if (obj.getExecutions() != null) {
      JsonArray array = new JsonArray();
      obj.getExecutions().forEach(item -> array.add(item.toJson()));
      json.put("executions", array);
    }
    if (obj.getParameter() != null) {
      JsonObject map = new JsonObject();
      obj.getParameter().forEach((key, value) -> map.put(key, value));
      json.put("parameter", map);
    }
  }
}
