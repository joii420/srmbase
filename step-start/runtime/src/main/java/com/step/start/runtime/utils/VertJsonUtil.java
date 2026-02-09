package com.step.start.runtime.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/11/4  10:06
 */
public class VertJsonUtil {
    public static Map<String, Object> formatJson(JsonObject jsonObject) {
        Map<String, Object> resJson = new HashMap<>(1);
        jsonObject.getMap().forEach((k, v) -> {
            if (v instanceof JsonObject json) {
                resJson.put(k, formatJson(json));
            } else if (v instanceof JsonArray array) {
                List<Object> list = new ArrayList<>();
                array.forEach(obj -> {
                    if (obj instanceof JsonObject objJson) {
                        list.add(formatJson(objJson));
                    } else {
                        list.add(obj);
                    }
                });
                resJson.put(k, list);
            } else {
                resJson.put(k, v);
            }
        });
        return resJson;
    }
}
