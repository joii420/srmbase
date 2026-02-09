package com.step.api.runtime.template.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link DataKeyParam}.
 * NOTE: This class has been automatically generated from the {@link DataKeyParam} original class using Vert.x codegen.
 */
public class DataKeyParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataKeyParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "EntId":
          if (member.getValue() instanceof String) {
            obj.setEntId((String)member.getValue());
          }
          break;
        case "postDate":
          if (member.getValue() instanceof String) {
            obj.setPostDate((String)member.getValue());
          }
          break;
        case "CompanyId":
          if (member.getValue() instanceof String) {
            obj.setCompanyId((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(DataKeyParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataKeyParam obj, java.util.Map<String, Object> json) {
    if (obj.getEntId() != null) {
      json.put("EntId", obj.getEntId());
    }
    if (obj.getPostDate() != null) {
      json.put("postDate", obj.getPostDate());
    }
    if (obj.getCompanyId() != null) {
      json.put("CompanyId", obj.getCompanyId());
    }
  }
}
