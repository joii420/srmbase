package com.step.api.runtime.template.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link StdDataParam}.
 * NOTE: This class has been automatically generated from the {@link StdDataParam} original class using Vert.x codegen.
 */
public class StdDataParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, StdDataParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
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

  public static void toJson(StdDataParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(StdDataParam obj, java.util.Map<String, Object> json) {
    if (obj.getParameter() != null) {
      JsonObject map = new JsonObject();
      obj.getParameter().forEach((key, value) -> map.put(key, value));
      json.put("parameter", map);
    }
  }
}
