package com.step.jdbc.runtime.txedit.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link TxInfo}.
 * NOTE: This class has been automatically generated from the {@link TxInfo} original class using Vert.x codegen.
 */
public class TxInfoConverter {


    private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
    private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

    public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TxInfo obj) {
        for (java.util.Map.Entry<String, Object> member : json) {
            switch (member.getKey()) {
                case "transactionSql":
                    if (member.getValue() instanceof JsonObject transactionSql) {
                        obj.setTransactionSql(new TransactionSql(transactionSql));
                    }
                    break;
                case "transactionKey":
                    if (member.getValue() instanceof String) {
                        obj.setTransactionKey((String) member.getValue());
                    }
                    break;
                case "lockSql":
                    if (member.getValue() instanceof String) {
                        obj.setLockSql((String) member.getValue());
                    }
                    break;
                case "serverName":
                    if (member.getValue() instanceof String) {
                        obj.setServerName((String) member.getValue());
                    }
                    break;
                case "isCommit":
                    if (member.getValue() instanceof Boolean) {
                        obj.setIsCommit((Boolean)member.getValue());
                    }
                    break;
            }
        }
    }

    public static void toJson(TxInfo obj, JsonObject json) {
        toJson(obj, json.getMap());
    }

    public static void toJson(TxInfo obj, java.util.Map<String, Object> json) {
        if (obj.getTransactionSql() != null) {
            json.put("transactionSql", obj.getTransactionSql());
        }
        if (obj.getTransactionKey() != null) {
            json.put("transactionKey", obj.getTransactionKey());
        }
        if (obj.getLockSql() != null) {
            json.put("lockSql", obj.getLockSql());
        }
        if (obj.getServerName() != null) {
            json.put("serverName", obj.getServerName());
        }
        if (obj.getIsCommit() != null) {
            json.put("isCommit", obj.getIsCommit());
        }
    }
}
