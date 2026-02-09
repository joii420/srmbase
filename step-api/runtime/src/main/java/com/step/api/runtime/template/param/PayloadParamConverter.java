package com.step.api.runtime.template.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link PayloadParam}.
 * NOTE: This class has been automatically generated from the {@link PayloadParam} original class using Vert.x codegen.
 */
public class PayloadParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, PayloadParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "std_data":
          if (member.getValue() instanceof JsonObject) {
            obj.setStd_data(new StdDataParam((JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(PayloadParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(PayloadParam obj, java.util.Map<String, Object> json) {
    if (obj.getStd_data() != null) {
      json.put("std_data", obj.getStd_data().toJson());
    }
  }
}
