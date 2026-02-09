package com.step.jdbc.runtime.txedit.model;

import com.step.jdbc.runtime.param.JdbcParam;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;

import java.util.Base64;

/**
 * Converter and mapper for {@link com.joii.test.po.LockParam}.
 * NOTE: This class has been automatically generated from the {@link com.joii.test.po.LockParam} original class using Vert.x codegen.
 */
public class LockParamConverter {


    private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
    private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

    public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, LockParam obj) {
        for (java.util.Map.Entry<String, Object> member : json) {
            switch (member.getKey()) {
                case "dataKey":
                    if (member.getValue() instanceof String) {
                        obj.setDataKey((String) member.getValue());
                    }
                    break;
                case "handleLockSql":
                    if (member.getValue() instanceof String) {
                        obj.setHandleLockSql((String) member.getValue());
                    }
                case "serverName":
                    if (member.getValue() instanceof String) {
                        obj.setServerName((String) member.getValue());
                    }
                    break;
                case "jdbcDataSourceKey":
                    if (member.getValue() instanceof String) {
                        obj.setJdbcDataSourceKey((String) member.getValue());
                    }
                    break;
                case "lockTime":
                    if (member.getValue() instanceof Number) {
                        obj.setLockTime(((Number) member.getValue()).longValue());
                    }
                    break;
                case "programCode":
                    if (member.getValue() instanceof String) {
                        obj.setProgramCode((String) member.getValue());
                    }
                    break;
                case "lockSql":
                    if (member.getValue() instanceof String) {
                        obj.setLockSql((String) member.getValue());
                    }
                    break;
                case "tranKey":
                    if (member.getValue() instanceof String) {
                        obj.setTranKey((String) member.getValue());
                    }
                    break;
                case "jdbcParam":
                    if (member.getValue() instanceof JsonObject) {
                        obj.setJdbcParam(new JdbcParam((JsonObject) member.getValue()));
                    }
                    break;
            }
        }
    }

    public static void toJson(LockParam obj, JsonObject json) {
        toJson(obj, json.getMap());
    }

    public static void toJson(LockParam obj, java.util.Map<String, Object> json) {
        if (obj.getDataKey() != null) {
            json.put("dataKey", obj.getDataKey());
        }
        if (obj.getHandleLockSql() != null) {
            json.put("handleLockSql", obj.getHandleLockSql());
        }
        if (obj.getServerName() != null) {
            json.put("serverName", obj.getServerName());
        }
        if (obj.getJdbcDataSourceKey() != null) {
            json.put("jdbcDataSourceKey", obj.getJdbcDataSourceKey());
        }
        if (obj.getLockTime() != null) {
            json.put("lockTime", obj.getLockTime());
        }
        if (obj.getProgramCode() != null) {
            json.put("programCode", obj.getProgramCode());
        }
        if (obj.getLockSql() != null) {
            json.put("lockSql", obj.getLockSql());
        }
        if (obj.getTranKey() != null) {
            json.put("tranKey", obj.getTranKey());
        }
        if (obj.getJdbcParam() != null) {
            json.put("jdbcParam", obj.getJdbcParam());
        }
    }
}
