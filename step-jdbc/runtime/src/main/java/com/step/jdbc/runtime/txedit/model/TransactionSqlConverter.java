package com.step.jdbc.runtime.txedit.model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link TransactionSql}.
 * NOTE: This class has been automatically generated from the {@link TransactionSql} original class using Vert.x codegen.
 */
public class TransactionSqlConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TransactionSql obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "executeSqlList":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setExecuteSqlList(list);
          }
          break;
        case "verifySqlList":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setVerifySqlList(list);
          }
          break;
      }
    }
  }

  public static void toJson(TransactionSql obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TransactionSql obj, java.util.Map<String, Object> json) {
    if (obj.getExecuteSqlList() != null) {
      JsonArray array = new JsonArray();
      obj.getExecuteSqlList().forEach(item -> array.add(item));
      json.put("executeSqlList", array);
    }
    if (obj.getVerifySqlList() != null) {
      JsonArray array = new JsonArray();
      obj.getVerifySqlList().forEach(item -> array.add(item));
      json.put("verifySqlList", array);
    }
  }
}
