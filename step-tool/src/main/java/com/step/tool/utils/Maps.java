package com.step.tool.utils;

import com.step.api.runtime.exception.base.BaseException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author : Sun
 * @date : 2022/6/17  8:01
 */
@Slf4j
public class Maps {

    private final Map<String, Object> param;

    private Maps(Map<String, Object> param) {
        this.param = param;
    }

    public static Maps from(Map<String, Object> param) {
        if (param == null) {
            return new Maps(new HashMap<>(4));
        }
        return new Maps(param);
    }

    public String mustGet(String key) {
        return "" + Optional.ofNullable(this.param.get("" + key)).orElseThrow(() -> new BaseException(" " + key + " must has a value!"));
    }

    public int mustGetInt(String key) {
        String value = "" + Optional.ofNullable(this.param.get("" + key)).orElseThrow(() -> new BaseException(" " + key + " must has a value!"));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error(value + " cannot convert to int ");
            throw new BaseException(e);
        }
    }

    public long mustGetLong(String key) {
        String value = "" + Optional.ofNullable(this.param.get("" + key)).orElseThrow(() -> new BaseException(" " + key + " must has a value!"));
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.error(value + " cannot convert to long ");
            throw new BaseException(e);
        }
    }

    public Maps mustGetParam(String key) {
        Object map = Optional.ofNullable(this.param.get("" + key)).orElseThrow(() -> new BaseException(" " + key + " must has a value!"));
        try {
            Map<String, Object> paramMap = (Map<String, Object>) map;
            return Maps.from(paramMap);
        } catch (Exception e) {
            log.error(" " + key + " is not a Map");
            throw new BaseException(e);
        }
    }

    public List<Maps> mustGetParamList(String key) {
        Object list = Optional.ofNullable(this.param.get("" + key)).orElseThrow(() -> new BaseException(" " + key + " must has a value!"));
        List<Map<String, Object>> paramList;
        try {
            paramList = (List<Map<String, Object>>) list;
        } catch (Exception e) {
            log.error(" " + key + " is not a List<Map>");
            throw new BaseException(e);
        }
        if (CollectionUtil.isEmpty(paramList)) {
            throw new BaseException("" + key + " source is empty!").record();
        }
        return paramList.stream().map(Maps::from).toList();
    }

    public String get(String key) {
        return get(key, null);
    }


    public Object getObject(String key) {
        return this.param.get("" + key);
    }

    public String get(String key, String defaultValue) {
        Object value = this.param.get("" + key);
        return value == null ? defaultValue : "" + value;
    }

    public Map<String, Object> getParam() {
        return this.param;
    }

    public String formatSql(String sql) {
        Set<String> keys = this.getParam().keySet();
        for (String key : keys) {
            sql = sql.replace("#" + key + "#", this.mustGet(key));
        }
        return sql;
    }

    public static Maps init() {
        return new Maps(new HashMap<>(1));
    }

    public static Maps init(String k, Object v) {
        return init().set(k, v);
    }

    public Maps set(String k, Object v) {
        this.param.put(k, v);
        return this;
    }

    public Map<String, Object> map() {
        return this.param;
    }

    public static boolean isEmpty(Maps maps) {
        return maps == null || CollectionUtil.isEmpty(maps.param);
    }

    @Override
    public String toString() {
        return JsonUtil.format(this.param);
    }

}
