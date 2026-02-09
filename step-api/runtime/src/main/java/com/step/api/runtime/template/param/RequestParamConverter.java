package com.step.api.runtime.template.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link RequestParam}.
 * NOTE: This class has been automatically generated from the {@link RequestParam} original class using Vert.x codegen.
 */
public class RequestParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RequestParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "datakey":
          if (member.getValue() instanceof JsonObject) {
            obj.setDatakey(new DataKeyParam((JsonObject)member.getValue()));
          }
          break;
        case "host":
          if (member.getValue() instanceof JsonObject) {
            obj.setHost(new HostParam((JsonObject)member.getValue()));
          }
          break;
        case "key":
          if (member.getValue() instanceof String) {
            obj.setKey((String)member.getValue());
          }
          break;
        case "payload":
          if (member.getValue() instanceof JsonObject) {
            obj.setPayload(new PayloadParam((JsonObject)member.getValue()));
          }
          break;
        case "service":
          if (member.getValue() instanceof JsonObject) {
            obj.setService(new ServiceParam((JsonObject)member.getValue()));
          }
          break;
        case "type":
          if (member.getValue() instanceof String) {
            obj.setType((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(RequestParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(RequestParam obj, java.util.Map<String, Object> json) {
    if (obj.getDatakey() != null) {
      json.put("datakey", obj.getDatakey().toJson());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost().toJson());
    }
    if (obj.getKey() != null) {
      json.put("key", obj.getKey());
    }
    if (obj.getPayload() != null) {
      json.put("payload", obj.getPayload().toJson());
    }
    if (obj.getService() != null) {
      json.put("service", obj.getService().toJson());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType());
    }
  }
}
