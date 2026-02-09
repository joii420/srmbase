package com.step.api.runtime.exception;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link Result}.
 * NOTE: This class has been automatically generated from the {@link Result} original class using Vert.x codegen.
 */
public class ResultConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, Result obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "code":
          if (member.getValue() instanceof String) {
            obj.setCode((String)member.getValue());
          }
          break;
        case "message":
          if (member.getValue() instanceof String) {
            obj.setMessage((String)member.getValue());
          }
          break;
        case "langCode":
          if (member.getValue() instanceof String) {
            obj.setLangCode((String)member.getValue());
          }
          break;
        case "language":
          if (member.getValue() instanceof String) {
            obj.setLanguage((String)member.getValue());
          }
          break;
        case "proposal":
          if (member.getValue() instanceof String) {
            obj.setProposal((String)member.getValue());
          }
          break;
        case "proposalProgram":
          if (member.getValue() instanceof String) {
            obj.setProposalProgram((String)member.getValue());
          }
          break;
        case "sqlCode":
          if (member.getValue() instanceof String) {
            obj.setSqlCode((String)member.getValue());
          }
          break;
        case "sqlMessage":
          if (member.getValue() instanceof String) {
            obj.setSqlMessage((String)member.getValue());
          }
          break;
        case "type":
          if (member.getValue() instanceof String) {
            obj.setType((String)member.getValue());
          }
          break;
        case "typeName":
          if (member.getValue() instanceof String) {
            obj.setTypeName((String)member.getValue());
          }
          break;
        case "btn1":
          if (member.getValue() instanceof String) {
            obj.setBtn1((String)member.getValue());
          }
          break;
        case "btn2":
          if (member.getValue() instanceof String) {
            obj.setBtn2((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(Result obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(Result obj, java.util.Map<String, Object> json) {
    if (obj.getCode() != null) {
      json.put("code", obj.getCode());
    }
    if (obj.getMessage() != null) {
      json.put("message", obj.getMessage());
    }
    if (obj.getLangCode() != null) {
      json.put("langCode", obj.getLangCode());
    }
    if (obj.getLanguage() != null) {
      json.put("language", obj.getLanguage());
    }
    if (obj.getProposal() != null) {
      json.put("proposal", obj.getProposal());
    }
    if (obj.getProposalProgram() != null) {
      json.put("proposalProgram", obj.getProposalProgram());
    }
    if (obj.getSqlCode() != null) {
      json.put("sqlCode", obj.getSqlCode());
    }
    if (obj.getSqlMessage() != null) {
      json.put("sqlMessage", obj.getSqlMessage());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType());
    }
    if (obj.getTypeName() != null) {
      json.put("typeName", obj.getTypeName());
    }
    if (obj.getBtn1() != null) {
      json.put("btn1", obj.getBtn1());
    }
    if (obj.getBtn2() != null) {
      json.put("btn2", obj.getBtn2());
    }
  }
}
