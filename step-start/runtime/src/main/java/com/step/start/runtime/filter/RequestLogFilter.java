package com.step.start.runtime.filter;

import com.step.tool.utils.JsonUtil;
import com.step.tool.utils.RequestUtil;
import com.step.tool.utils.StringUtil;
//import io.quarkus.vertx.web.RouteFilter;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * filter
 */
@Singleton
public class RequestLogFilter {
    public static final Logger log = LoggerFactory.getLogger("Request-Log");

    private static final Integer MAX_LENGTH = 500;
    private static final String EMPTY_PARAM = "{}";

    /**
     * 记录请求的访问信息
     */
//    @RouteFilter(Integer.MAX_VALUE)
//    @RouteFilter(FilterPriority.REQUEST_LOG)
    void logFilter(RoutingContext rc) {
        HttpServerRequest request = rc.request();
//        String requestId = UUID.randomUUID().toString().replace("-", "");
//        request.headers().add("requestKey", requestId);
        String params;
        String method = request.method().name();
        String nowTime = LocalDateTime.now().toString();
        String nowTimeStr = nowTime.replace("T", " ").substring(0, nowTime.lastIndexOf("."));
        String requestIp = RequestUtil.getRequestIp(request);
        switch (method) {
            case "GET", "PUT", "DELETE" -> {
                params = paramFormat(request.query());
                log.info("{} {}  {} ==> {} {} -->{}{}", "\033[36;4m", requestIp, nowTimeStr, method, request.path(), params, "\033[0m");

            }
            case "POST" -> {
                if (!request.path().startsWith("/q/dev")) {
                    request.bodyHandler(rx -> {
                        try {
                            log.info("{} {}  {} ==> {} {} -->{}{}", "\033[36;4m", requestIp, nowTimeStr, method, request.path(), rx.toJsonObject(), "\033[0m");
                        } catch (Exception e){
                            log.warn("{} {}  {} ==> {} {} -->{}", "\033[36;4m", requestIp, nowTimeStr, method, request.path(), "\033[0m");
                        }
                    });
                }
            }
            default ->  {}
        }
        rc.next();
    }


    private String paramFormat(String params) {
        Map<String, String> map = new HashMap<>(4);
        if (StringUtil.isEmpty(params)) {
            return EMPTY_PARAM;
        }
        if (params.contains("&")) {
            String[] paramList = params.split("&");
            for (String param : paramList) {
                String[] result = param.split("=");
                if (result.length > 1) {
                    map.put(result[0], result[1]);
                } else {
                    map.put(result[0], "null");
                }
            }
        } else {
            String[] result = params.split("=");
            if (result.length > 1) {
                map.put(result[0], result[1]);
            } else {
                map.put(result[0], "null");
            }
        }
        String result = Optional.ofNullable(JsonUtil.format(map)).orElse(EMPTY_PARAM);
        return result.length() <= MAX_LENGTH ? result : result.substring(0, MAX_LENGTH);
    }


}