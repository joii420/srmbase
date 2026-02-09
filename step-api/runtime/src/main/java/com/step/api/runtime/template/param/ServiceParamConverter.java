package com.step.api.runtime.template.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link ServiceParam}.
 * NOTE: This class has been automatically generated from the {@link ServiceParam} original class using Vert.x codegen.
 */
public class ServiceParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ServiceParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "ip":
          if (member.getValue() instanceof String) {
            obj.setIp((String)member.getValue());
          }
          break;
        case "name":
          if (member.getValue() instanceof String) {
            obj.setName((String)member.getValue());
          }
          break;
        case "prod":
          if (member.getValue() instanceof String) {
            obj.setProd((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ServiceParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ServiceParam obj, java.util.Map<String, Object> json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getIp() != null) {
      json.put("ip", obj.getIp());
    }
    if (obj.getName() != null) {
      json.put("name", obj.getName());
    }
    if (obj.getProd() != null) {
      json.put("prod", obj.getProd());
    }
  }
}
