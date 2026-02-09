package com.step.api.runtime.template.result;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link StepResult}.
 * NOTE: This class has been automatically generated from the {@link StepResult} original class using Vert.x codegen.
 */
public class StepResultConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, StepResult obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "payload":
          if (member.getValue() instanceof JsonObject) {
            obj.setPayload(new Payload((JsonObject)member.getValue()));
          }
          break;
        case "srvcode":
          if (member.getValue() instanceof String) {
            obj.setSrvcode((String)member.getValue());
          }
          break;
        case "srvver":
          if (member.getValue() instanceof String) {
            obj.setSrvver((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(StepResult obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(StepResult obj, java.util.Map<String, Object> json) {
    if (obj.getPayload() != null) {
      json.put("payload", obj.getPayload().toJson());
    }
    if (obj.getSrvcode() != null) {
      json.put("srvcode", obj.getSrvcode());
    }
    if (obj.getSrvver() != null) {
      json.put("srvver", obj.getSrvver());
    }
  }
}
