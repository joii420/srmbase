package com.step.api.runtime.file;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link FileParam}.
 * NOTE: This class has been automatically generated from the {@link FileParam} original class using Vert.x codegen.
 */
public class FileParamConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, FileParam obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "catalog":
          if (member.getValue() instanceof String) {
            obj.setCatalog((String)member.getValue());
          }
          break;
        case "fileFullName":
          if (member.getValue() instanceof String) {
            obj.setFileFullName((String)member.getValue());
          }
          break;
        case "fileId":
          if (member.getValue() instanceof String) {
            obj.setFileId((String)member.getValue());
          }
          break;
        case "fileName":
          if (member.getValue() instanceof String) {
            obj.setFileName((String)member.getValue());
          }
          break;
        case "fileSuffix":
          if (member.getValue() instanceof String) {
            obj.setFileSuffix((String)member.getValue());
          }
          break;
        case "id":
          if (member.getValue() instanceof String) {
            obj.setId((String)member.getValue());
          }
          break;
        case "inDate":
          if (member.getValue() instanceof Number) {
            obj.setInDate(((Number)member.getValue()).intValue());
          }
          break;
        case "isRename":
          if (member.getValue() instanceof String) {
            obj.setIsRename((String)member.getValue());
          }
          break;
        case "platformCode":
          if (member.getValue() instanceof String) {
            obj.setPlatformCode((String)member.getValue());
          }
          break;
        case "publicFile":
          if (member.getValue() instanceof Boolean) {
            obj.setPublicFile((Boolean)member.getValue());
          }
          break;
        case "saveType":
          if (member.getValue() instanceof String) {
            obj.setSaveType((String)member.getValue());
          }
          break;
        case "serverCode":
          if (member.getValue() instanceof String) {
            obj.setServerCode((String)member.getValue());
          }
          break;
        case "size":
          if (member.getValue() instanceof String) {
            obj.setSize((String)member.getValue());
          }
          break;
        case "usedName":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setUsedName(list);
          }
          break;
      }
    }
  }

  public static void toJson(FileParam obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(FileParam obj, java.util.Map<String, Object> json) {
    if (obj.getCatalog() != null) {
      json.put("catalog", obj.getCatalog());
    }
    if (obj.getFileFullName() != null) {
      json.put("fileFullName", obj.getFileFullName());
    }
    if (obj.getFileId() != null) {
      json.put("fileId", obj.getFileId());
    }
    if (obj.getFileName() != null) {
      json.put("fileName", obj.getFileName());
    }
    if (obj.getFileSuffix() != null) {
      json.put("fileSuffix", obj.getFileSuffix());
    }
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getInDate() != null) {
      json.put("inDate", obj.getInDate());
    }
    if (obj.getIsRename() != null) {
      json.put("isRename", obj.getIsRename());
    }
    if (obj.getPlatformCode() != null) {
      json.put("platformCode", obj.getPlatformCode());
    }
    if (obj.getPublicFile() != null) {
      json.put("publicFile", obj.getPublicFile());
    }
    if (obj.getSaveType() != null) {
      json.put("saveType", obj.getSaveType());
    }
    if (obj.getServerCode() != null) {
      json.put("serverCode", obj.getServerCode());
    }
    if (obj.getSize() != null) {
      json.put("size", obj.getSize());
    }
    if (obj.getUsedName() != null) {
      JsonArray array = new JsonArray();
      obj.getUsedName().forEach(item -> array.add(item));
      json.put("usedName", array);
    }
  }
}
