package com.step.start.runtime.utils;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.step.api.runtime.exception.base.BaseException;
import com.step.tool.utils.CollectionUtil;
import com.step.tool.utils.Maps;
import com.step.tool.utils.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2022/9/26  21:43
 */
public class JoiiUtil {

    private static final String DEFAULT_PARENT_ID = "0";
    private static final String DEFAULT_STR = "code";
    private static final String DEFAULT_PARENT_STR = "parentCode";
    private static final String DEFAULT_CHILD = "child";
    private static final String DEFAULT_SORT = "sort";



    public static int getMaxSort(String tableName, Maps maps) {
        return getMaxSort(DEFAULT_SORT, tableName, maps);
    }

    public static int getMaxSort(String sortStr, String tableName) {
        return getMaxSort(sortStr, tableName, null);
    }

    /**
     * 根据条件获取最大的sort
     *
     * @param sortStr   排序号字段名
     * @param tableName 表名
     * @param maps      条件
     * @return sort+1
     */
    public static int getMaxSort(String sortStr, String tableName, Maps maps) {
        StringBuilder condition = new StringBuilder();
        if (!Maps.isEmpty(maps)) {
            Map<String, Object> map = maps.map();
            map.keySet().forEach(key -> condition.append(" AND ").append(key).append("=").append(map.get(key)));
        }
//        return tableMapper.getMaxSort(sortStr, tableName, condition.toString());
        return 0;
    }


    public static int getMaxSort(Class clz, String sortStr, Maps maps) {
        if (StringUtil.isEmpty(sortStr)) {
            throw new BaseException("invalid sort column").record();
        }
        Annotation table = clz.getAnnotation(TableName.class);
        if (table == null) {
            throw new BaseException("invalid class").record();
        }
        String tableName = ((TableName) table).value();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField == null) {
                continue;
            }
            String value = tableField.value();
            if (sortStr.equals(value)) {
                return getMaxSort(value, tableName, maps);
            }
        }
        throw new BaseException(sortStr + "NotFound").record();
    }

    public static int getMaxSort(Class clz) {
        return getMaxSort(clz, DEFAULT_SORT, null);
    }
    public static int getMaxSort(Class clz, Maps maps) {
        return getMaxSort(clz, DEFAULT_SORT, maps);
    }

    public static <T> List<T> tree(List<T> resource) {
        return tree(DEFAULT_PARENT_ID, resource);
    }

    public static <T> List<T> tree(String beginCode, List<T> resource) {
        return tree(beginCode, DEFAULT_STR, DEFAULT_PARENT_STR, DEFAULT_CHILD, resource);
    }

    /**
     * 封装树结构
     *
     * @param beginCode 起始id
     * @param str       子属性名
     * @param parentStr 父属性名
     * @param childStr  子列表名
     * @param resource  资源数组
     * @param <T>       数据类型
     * @return TREE
     */
    public static <T> List<T> tree(String beginCode, String str, String parentStr, String childStr, List<T> resource) {
        if (CollectionUtil.isEmpty(resource)) {
            return new ArrayList<>();
        }
        return resource.stream().filter(e -> {
            Class clz = e.getClass();
            try {
                Field declaredField = clz.getDeclaredField(parentStr);
                declaredField.setAccessible(true);
                Object o = declaredField.get(e);
                if (o == null) {
                    return false;
                }
                String parentValue = o.toString();
                return parentValue.equals(beginCode);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new BaseException(ex);
            }
        }).peek(e -> {
            try {
                Class clz = e.getClass();
                Field childValue = clz.getDeclaredField(childStr);
                Field value = clz.getDeclaredField(str);
                value.setAccessible(true);
                childValue.setAccessible(true);
                //子属性名
                String o = value.get(e).toString();
                //原有子列表
                Object o1 = childValue.get(e);
                //子列表树资源
                List<T> newValue = Optional.ofNullable(tree(o, str, parentStr, childStr, resource)).orElse(new ArrayList<>());
                childValue.set(e, newValue);
            } catch (Exception noSuchFieldException) {
                noSuchFieldException.printStackTrace();
                throw new BaseException("tree error");
            }
        }).collect(Collectors.toList());
    }

    /**
     * 封装树结构(合并之前的子数据)
     *
     * @param beginCode 起始id
     * @param str       子属性名
     * @param parentStr 父属性名
     * @param childStr  子列表名
     * @param resource  资源数组
     * @param <T>       数据类型
     * @return TREE
     */
    public static <T> List<T> treeMerge(String beginCode, String str, String parentStr, String childStr, List<T> resource) {
        if (CollectionUtil.isEmpty(resource)) {
            return new ArrayList<>();
        }
        return resource.stream().filter(e -> {
            Class clz = e.getClass();
            try {
                Field declaredField = clz.getDeclaredField(parentStr);
                declaredField.setAccessible(true);
                Object o = declaredField.get(e);
                if (o == null) {
                    return false;
                }
                String parentValue = o.toString();
                return parentValue.equals(beginCode);
            } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
                throw new BaseException("tree error");
            }
        }).peek(e -> {
            try {
                Class clz = e.getClass();
                Field childValue = clz.getDeclaredField(childStr);
                Field value = clz.getDeclaredField(str);
                value.setAccessible(true);
                childValue.setAccessible(true);
                //子属性名
                String o = value.get(e).toString();
                //原有子列表
                Object o1 = childValue.get(e);
                //子列表树资源
                List<T> newValue = Optional.ofNullable(tree(o, str, parentStr, childStr, resource)).orElse(new ArrayList<>());
                if (o1 != null) {
                    if (!(o1 instanceof List)) {
                        throw new BaseException("child field type nonsupport");
                    }
                    List<T> childList = (List<T>) o1;
                    newValue.addAll(childList);
                }
                childValue.set(e, newValue);
            } catch (Exception noSuchFieldException) {
                noSuchFieldException.printStackTrace();
                throw new BaseException("tree error");
            }
        }).collect(Collectors.toList());
    }


    public static <T> void treeProcess(List<T> resource, Consumer<T> consumer) {
        treeProcess(DEFAULT_CHILD, resource, consumer);
    }

    /**
     * 对树形数据进行操作
     *
     * @param childStr child
     * @param resource resource
     * @param consumer operate
     * @param <T>      entity
     */
    public static <T> void treeProcess(String childStr, List<T> resource, Consumer<T> consumer) {
        resource.forEach(e -> {
            Class clz = e.getClass();
            Object o;
            try {
                Field child = clz.getDeclaredField(childStr);
                child.setAccessible(true);
                o = child.get(e);
            } catch (Exception ex) {
                throw new BaseException(ex.getMessage());
            }
            consumer.accept(e);
            if ((o instanceof List) && CollectionUtil.isNotEmpty((List) o)) {
                treeProcess(childStr, (List) o, consumer);
            }
        });
    }
}
