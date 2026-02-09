package com.step.tool.utils;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import com.step.api.runtime.exception.base.BaseException;
import com.step.api.runtime.model.TransmitParam;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;


public class BeanUtil extends cn.hutool.core.bean.BeanUtil {
    private static final Random ran = new Random();
    public static final Logger log = LoggerFactory.getLogger(BeanUtil.class);
    public static CopyOptions copyOptions = new CopyOptions();

    static {
        copyOptions.setIgnoreError(false);
        copyOptions.setIgnoreCase(true);
        copyOptions.setIgnoreNullValue(true);
    }

    public static void copy(Object source, Object target) {
        copyProperties(source, target);
    }

    public static <T> T copy(Object source, Class<T> tClass) {
        return copyProperties(source, tClass);
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> tClass) {
        return fillBeanWithMap(map, ReflectUtil.newInstanceIfPossible(tClass), true, copyOptions);
    }

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BaseException(e);
        }
    }


    public static int random(int start, int end) {
        return ran.nextInt(end - start) + start;
    }


    public static <T> T getParam(TransmitParam dto, Class<T> obj) {
        Map<String, Object> param = dto.getParam();
        if (param == null || param.size() == 0) {
            return null;
        }
        return (T) JsonObject.mapFrom(param).mapTo(obj);
    }


}
