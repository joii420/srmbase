package com.step.api.runtime.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link RequestInfo}.
 * NOTE: This class has been automatically generated from the {@link RequestInfo} original class using Vert.x codegen.
 */
public class RequestInfoConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RequestInfo obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "requestBody":
          if (member.getValue() instanceof String) {
            obj.setRequestBody((String)member.getValue());
          }
          break;
        case "requestHeader":
          if (member.getValue() instanceof String) {
            obj.setRequestHeader((String)member.getValue());
          }
          break;
        case "requestIp":
          if (member.getValue() instanceof String) {
            obj.setRequestIp((String)member.getValue());
          }
          break;
        case "requestMac":
          if (member.getValue() instanceof String) {
            obj.setRequestMac((String)member.getValue());
          }
          break;
        case "requestParam":
          if (member.getValue() instanceof String) {
            obj.setRequestParam((String)member.getValue());
          }
          break;
        case "requestPath":
          if (member.getValue() instanceof String) {
            obj.setRequestPath((String)member.getValue());
          }
          break;
        case "requestType":
          if (member.getValue() instanceof String) {
            obj.setRequestType((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(RequestInfo obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(RequestInfo obj, java.util.Map<String, Object> json) {
    if (obj.getRequestBody() != null) {
      json.put("requestBody", obj.getRequestBody());
    }
    if (obj.getRequestHeader() != null) {
      json.put("requestHeader", obj.getRequestHeader());
    }
    if (obj.getRequestIp() != null) {
      json.put("requestIp", obj.getRequestIp());
    }
    if (obj.getRequestMac() != null) {
      json.put("requestMac", obj.getRequestMac());
    }
    if (obj.getRequestParam() != null) {
      json.put("requestParam", obj.getRequestParam());
    }
    if (obj.getRequestPath() != null) {
      json.put("requestPath", obj.getRequestPath());
    }
    if (obj.getRequestType() != null) {
      json.put("requestType", obj.getRequestType());
    }
  }
}
