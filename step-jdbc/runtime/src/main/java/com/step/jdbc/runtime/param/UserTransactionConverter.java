package com.step.jdbc.runtime.param;

import com.step.jdbc.runtime.txedit.model.enums.EditType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link UserTransaction}.
 * NOTE: This class has been automatically generated from the {@link UserTransaction} original class using Vert.x codegen.
 */
public class UserTransactionConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, UserTransaction obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "lockIp":
          if (member.getValue() instanceof String) {
            obj.setLockIp((String)member.getValue());
          }
          break;
        case "account":
          if (member.getValue() instanceof String) {
            obj.setAccount((String)member.getValue());
          }
          break;
        case "dataKey":
          if (member.getValue() instanceof String) {
            obj.setDataKey((String)member.getValue());
          }
          break;
        case "editType":
          if (member.getValue() instanceof String) {
            obj.setEditType(EditType.valueOf((String)member.getValue()));
          }
          break;
        case "oldDataKey":
          if (member.getValue() instanceof String) {
            obj.setOldDataKey((String)member.getValue());
          }
          break;
        case "lockSqls":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setLockSqls(list);
          }
          break;
        case "programCode":
          if (member.getValue() instanceof String) {
            obj.setProgramCode((String)member.getValue());
          }
          break;
        case "userCode":
          if (member.getValue() instanceof String) {
            obj.setUserCode((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(UserTransaction obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(UserTransaction obj, java.util.Map<String, Object> json) {
    if (obj.getLockIp() != null) {
      json.put("lockIp", obj.getLockIp());
    }
    if (obj.getAccount() != null) {
      json.put("account", obj.getAccount());
    }
    if (obj.getDataKey() != null) {
      json.put("dataKey", obj.getDataKey());
    }
    if (obj.getEditType() != null) {
      json.put("editType", obj.getEditType().name());
    }
    if (obj.getOldDataKey() != null) {
      json.put("oldDataKey", obj.getOldDataKey());
    }
    if (obj.getLockSqls() != null) {
      JsonArray array = new JsonArray();
      obj.getLockSqls().forEach(item -> array.add(item));
      json.put("lockSqls", array);
    }
    if (obj.getProgramCode() != null) {
      json.put("programCode", obj.getProgramCode());
    }
    if (obj.getUserCode() != null) {
      json.put("userCode", obj.getUserCode());
    }
  }
}
