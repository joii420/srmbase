package com.step.jdbc.runtime.param;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link com.step.develop.JdbcParam}.
 * NOTE: This class has been automatically generated from the {@link com.step.develop.JdbcParam} original class using Vert.x codegen.
 */
public class JdbcParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, JdbcParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "database":
          if (member.getValue() instanceof String) {
            obj.setDatabase((String)member.getValue());
          }
          break;
        case "dbType":
          if (member.getValue() instanceof String) {
            obj.setDbType(com.step.jdbc.runtime.param.DBType.valueOf((String)member.getValue()));
          }
          break;
        case "host":
          if (member.getValue() instanceof String) {
            obj.setHost((String)member.getValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
        case "port":
          if (member.getValue() instanceof Number) {
            obj.setPort(((Number)member.getValue()).intValue());
          }
          break;
        case "url":
          if (member.getValue() instanceof String) {
            obj.setUrl((String)member.getValue());
          }
          break;
        case "username":
          if (member.getValue() instanceof String) {
            obj.setUsername((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(JdbcParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(JdbcParam obj, java.util.Map<String, Object> json) {
    if (obj.getDatabase() != null) {
      json.put("database", obj.getDatabase());
    }
    if (obj.getDbType() != null) {
      json.put("dbType", obj.getDbType().name());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
    if (obj.getPort() != null) {
      json.put("port", obj.getPort());
    }
    if (obj.getUrl() != null) {
      json.put("url", obj.getUrl());
    }
    if (obj.getUsername() != null) {
      json.put("username", obj.getUsername());
    }
  }
}
