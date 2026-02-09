package com.step.jdbc.runtime.txedit.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link TxR}.
 * NOTE: This class has been automatically generated from the {@link TxR} original class using Vert.x codegen.
 */
public class TxRConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TxR obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "code":
          if (member.getValue() instanceof String) {
            obj.setCode((String)member.getValue());
          }
          break;
        case "msg":
          if (member.getValue() instanceof String) {
            obj.setMsg((String)member.getValue());
          }
          break;
        case "success":
          if (member.getValue() instanceof Boolean) {
            obj.setSuccess((Boolean)member.getValue());
          }
          break;
        case "transactionId":
          if (member.getValue() instanceof String) {
            obj.setTransactionId((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(TxR obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TxR obj, java.util.Map<String, Object> json) {
    if (obj.getCode() != null) {
      json.put("code", obj.getCode());
    }
    if (obj.getMsg() != null) {
      json.put("msg", obj.getMsg());
    }
    if (obj.getTransactionId() != null) {
      json.put("transactionId", obj.getTransactionId());
    }
    json.put("success", obj.isSuccess());
  }
}
