package com.step.api.runtime.template.result;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link Payload}.
 * NOTE: This class has been automatically generated from the {@link Payload} original class using Vert.x codegen.
 */
public class PayloadConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Payload obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "std_data":
          if (member.getValue() instanceof JsonObject) {
            obj.setStd_data(new StdData((JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(Payload obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Payload obj, java.util.Map<String, Object> json) {
    if (obj.getStd_data() != null) {
      json.put("std_data", obj.getStd_data().toJson());
    }
  }
}
