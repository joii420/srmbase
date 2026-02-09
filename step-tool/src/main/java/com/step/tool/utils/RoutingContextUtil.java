package com.step.tool.utils;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class RoutingContextUtil {

    /**
     * 获取get请求中的所有参数
     */
    public static JsonObject getParams(RoutingContext context) {
        HttpServerRequest request = context.request();
        String query = request.query();
        JsonObject params = new JsonObject();
        if (query.contains("&")) {
            String[] split = query.split("&");
            for (String kv : split) {
                String[] split1 = kv.split("=");
                params.put(split1[0], split1[1]);
            }
        }
        return params;
    }
}
