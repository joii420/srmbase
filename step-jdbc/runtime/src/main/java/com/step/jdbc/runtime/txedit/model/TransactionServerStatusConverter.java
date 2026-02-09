package com.step.jdbc.runtime.txedit.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link com.step.common.transaction.TransactionServerStatus}.
 * NOTE: This class has been automatically generated from the {@link com.step.common.transaction.TransactionServerStatus} original class using Vert.x codegen.
 */
public class TransactionServerStatusConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TransactionServerStatus obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "active":
          if (member.getValue() instanceof Boolean) {
            obj.setActive((Boolean)member.getValue());
          }
          break;
        case "busy":
          if (member.getValue() instanceof Boolean) {
            obj.setBusy((Boolean)member.getValue());
          }
          break;
        case "currentCount":
          if (member.getValue() instanceof Number) {
            obj.setCurrentCount(((Number)member.getValue()).longValue());
          }
          break;
        case "maxCount":
          if (member.getValue() instanceof Number) {
            obj.setMaxCount(((Number)member.getValue()).longValue());
          }
          break;
        case "transactionServerName":
          if (member.getValue() instanceof String) {
            obj.setTransactionServerName((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(TransactionServerStatus obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TransactionServerStatus obj, java.util.Map<String, Object> json) {
    json.put("active", obj.isActive());
    json.put("busy", obj.isBusy());
    json.put("currentCount", obj.getCurrentCount());
    json.put("maxCount", obj.getMaxCount());
    if (obj.getTransactionServerName() != null) {
      json.put("transactionServerName", obj.getTransactionServerName());
    }
  }
}
