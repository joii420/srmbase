package com.step.jdbc.runtime.flink.session;

import com.step.logger.LOGGER;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

import java.util.HashMap;
import java.util.Map;

public class DBSessionConnection {

    private static Map<String, Map<Long, DBSession>> dbSessionMap = new ConcurrentHashMap();

    public static Map<String, Map<Long, DBSession>> getDbSessionMap() {
        return DBSessionConnection.dbSessionMap;
    }

    public static void addDBSession(String name, Long dbId, DBSession dbSession) {
        if (dbSessionMap.containsKey(name)) {
            dbSessionMap.get(name).put(dbId, dbSession);
        } else {
            Map<Long, DBSession> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put(dbId, dbSession);
            dbSessionMap.put(name, objectObjectHashMap);
        }
    }

    public static DBSession getDBSession(String name, Long dbId) {
        if (dbSessionMap.containsKey(name)) {
            return dbSessionMap.get(name).get(dbId);
        }
        return null;
    }

    public static void close(String name) {
        //批量关闭
        Map<Long, DBSession> longDBSessionMap = dbSessionMap.get(name);

        if (longDBSessionMap != null) {
            longDBSessionMap.forEach((key, val) -> {
                try {
                    //关闭
                    val.close();
                } catch (Exception e) {
                    LOGGER.error(e, "关闭连接[ %s ]失败", key);
                }
            });
        }
        dbSessionMap.remove(name);
    }
}
