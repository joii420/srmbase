package com.step.tool.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.step.api.runtime.exception.base.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        MAPPER.registerModule(javaTimeModule);
    }

    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        return JSONObject.toJSONString(obj);
    }

    /**
     * 将Java对象转化为JSON字符串
     */
    public static String format(Object object) {
        if (null == object) {
            return null;
        }
        try {
            //MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return object instanceof String ? (String) object : MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("json serialization exception: {}", e.getMessage(), e);
            throw new BaseException(e);
        }
    }

    public static <T> T parse(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        try {
            return MAPPER.readValue(str, new TypeReference<T>() {
            });
        } catch (IOException e) {
            log.error("json serialization exception: {}", e.getMessage(), e);
            throw new BaseException(e);
        }
    }

    public static JsonNode parseNode(Object obj) {
        if (obj == null
                || obj.getClass().getTypeName().contains("File")
        ) {
            return null;
        }
        return parseNode(JsonUtil.format(obj));
    }

    public static JsonNode parseNode(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        try {
            return MAPPER.readTree(str);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T parse(String str, Class<T> beanClass) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        try {
            return JSONObject.parseObject(str, beanClass);
//            return MAPPER.readValue(str, new TypeReference<T>() { });
//            return MAPPER.readValue(str, beanClass);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> List<T> parseList(List<Map<String, Object>> list, Class<T> clz) {
        return JSON.parseArray(JSON.toJSONString(list), clz);
    }

//	public static IPage<T> parsePage(JsonObject jsonObject, Class<T> type) {
//		IPage<T> iPage = jsonObject.mapTo(IPage.class);
//		return iPage;
//	}
}
