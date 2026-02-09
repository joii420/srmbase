package com.step.api.runtime.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.step.api.runtime.common.R;
import com.step.api.runtime.core.IResult;
import com.step.api.runtime.exception.base.BaseException;
import com.step.api.runtime.exception.ResInfo;
import com.step.api.runtime.exception.Result;
import com.step.api.runtime.exception.ServerCode;
import com.step.api.runtime.template.result.Payload;
import com.step.api.runtime.template.result.StdData;
import com.step.api.runtime.template.result.StepResult;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.step.api.runtime.utils.AesGetWay.ENCRY_PT_KEY;

/**
 * @author : Sun
 * @date : 2022/12/13  15:08
 */
public class FormatUtil {
    public static final Logger log = LoggerFactory.getLogger(FormatUtil.class.getSimpleName());

    public static StepResult succeed(List<Result> results, R r) {
        long formatStart = System.currentTimeMillis();
        try {
            StepResult res = new StepResult();
            Payload payload = new Payload();
            StdData data = new StdData();
            if (r.isSuccess()) {
                //设置返回数据
                Object resource = r.getData();
                if (resource != null) {
                    if (resource instanceof List) {
                        Map<String, Object> resMap = new HashMap<>();
                        resMap.put("result", resource);
                        data.setParameter(resMap);
                    } else if (resource instanceof String || resource instanceof Long || resource instanceof Boolean) {
                        Map<String, Object> resMap = new HashMap<>();
                        resMap.put("result", "" + resource);
                        resMap.put("warn", "不建议的返回类型:" + resource.getClass().getSimpleName());
                        data.setParameter(resMap);
                    } else {
                        Map<String, Object> map = JsonObject.mapFrom(resource).getMap();
                        long l = System.currentTimeMillis();
                        if (map.containsKey("base64")) {
                            if (!ObjectUtils.isEmpty(map.get("base64"))) {
                                //需要加密
                                if (map.containsKey("data")) {
                                    List<Map<String, Object>> mapList = new ArrayList<>();
                                    mapList = (List<Map<String, Object>>) map.get("data");
                                    List<String> base = (List<String>) map.get("base64");
//                                List<String> base = new ArrayList<>();
//                                base.add("syscl02");
//                                base.add("syscl03");
                                    for (Map<String, Object> resultMap : mapList) {
                                        //加密测试
                                        if (!CollectionUtils.isEmpty(base)) {
                                            Map<String, Object> map1 = baseEntry(resultMap, base);
                                        }
                                    }
                                }
                            }
                        }
                        long l1 = System.currentTimeMillis();
                        log.info("加密执行时间:" + String.valueOf(l1 - l) + "毫秒");
                        data.setParameter(map);
                    }
                }
            }
            //设置请求成功信息
            data.setExecutions(results);
            payload.setStd_data(data);
            res.setPayload(payload);
            long formatEnd = System.currentTimeMillis();
            System.out.println("FORMAT结果耗时: " + (formatEnd - formatStart));
            return res;
        } catch (Exception e) {
            if (r == null) {
                return errorResultFormat(ServerCode.INVALID_PATH);
            }
            BaseException.log.error("未知异常: {} {} ", e.getMessage(), e);
            return errorResultFormat(ServerCode.UNKNOWN_ERROR);
        }
    }

    public static Map<String, Object> baseEntry(Map<String, Object> map, List<String> keys) {
        for (String key : map.keySet()) {
            if (map.get(key).getClass().getName().equals("com.alibaba.fastjson.JSONObject") || map.get(key).getClass().getName().equals("java.util.HashMap") || map.get(key).getClass().getName().equals("java.util.LinkedHashMap") || map.get(key).getClass().getName().equals("io.vertx.core.json.JsonObject")) {
                Map<String, Object> maps = JSONObject.parseObject(JSONObject.toJSONString(map.get(key)), Map.class);
                maps.remove("empty");
                Map<String, Object> map1 = baseEntry(maps, keys);
                if (map1.containsKey("map") && map1.size() == 1) {
                    Object map2 = map1.remove("map");
                    map.replace(key, map2);
                } else {
                    map.replace(key, map1);
                }
            } else if (map.get(key).getClass().getName().equals("com.alibaba.fastjson.JSONArray") || map.get(key).getClass().getName().equals("java.util.ArrayList")) {
                List<Map> maps = JSONArray.parseArray(JSONObject.toJSONString(map.get(key)), Map.class);
                for (int i = 0; i < maps.size(); i++) {
                    Map map1 = maps.get(i);
                    map1.remove("empty");
                    maps.set(i, baseEntry(map1, keys));
                }
                map.replace(key, maps);
            } else {
                if (keys.contains(key)) {
                    if (map.get(key) instanceof BigDecimal) {
                        BigDecimal bd = null;
                        bd = (BigDecimal) map.get(key);
                        String bdString = bd.toString();
                        String s = AesGetWay.encodeToBase64(bdString, ENCRY_PT_KEY);
                        map.put(key, s);
                    } else if (map.get(key) instanceof Integer) {
                        Object o = map.get(key);
                        Integer o1 = (Integer) o;
                        String s = AesGetWay.encodeToBase64(String.valueOf(o1), ENCRY_PT_KEY);
                        map.put(key, s);
                    } else {
                        String s = AesGetWay.encodeToBase64((String) map.get(key), ENCRY_PT_KEY);
                        map.put(key, s);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 异常参数封装
     *
     * @param errorCode 错误码
     */
    public static StepResult errorResultFormat(IResult errorCode) {
        return errorResultFormat(List.of(new Result(errorCode)));
    }

    public static StepResult errorResultFormat(List<Result> message) {
        StepResult result = new StepResult();
        result.setSrvver(StepResult.DEFAULT_SRVVER);
        result.setSrvcode(StepResult.DEFAULT_ERROR_SRVCODE);
        Payload payload = new Payload();
        StdData data = new StdData();
        data.setExecutions(message);
        payload.setStd_data(data);
        result.setPayload(payload);
        return result;
    }

    /**
     * 解析ResInfo信息
     *
     * @param msg resInfo Json船
     * @return
     */
    public static List<ResInfo> formatResInfo(String msg) {
        List<ResInfo> resInfos;
        try {
            resInfos = JSON.parseArray(msg, ResInfo.class);
        } catch (Exception e) {
            BaseException.log.error("ERROR_MSG: {} 解析异常: {} {} ", msg, e.getMessage(), e);
            resInfos = List.of(new ResInfo(msg));
        }
        return resInfos;
    }

    public static List<ResInfo> formatResInfo(IResult result) {
        return List.of(new ResInfo(result));
    }

    public static List<ResInfo> formatResInfo(List<IResult> results) {
        return results.stream().map(ResInfo::new).collect(Collectors.toList());
    }


    public static StepResult failed() {
        StepResult stepResult = new StepResult();
        stepResult.setSrvver(StepResult.DEFAULT_SRVVER);
        stepResult.setSrvcode(StepResult.DEFAULT_ERROR_SRVCODE);
        return stepResult;
    }
}
