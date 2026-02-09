package com.step.tool.utils;

import cn.hutool.core.collection.CollectionUtil;

import java.util.*;

public class Func {

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<String> toStrList(String str) {
        if (StringUtil.isEmpty(str)) {
            return new ArrayList<>();
        }
        return Arrays.asList(toStrArray(str));
    }

    /**
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }


    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static String[] toStrArray(String split, String str) {
        if (isEmpty(str)) {
            return new String[]{};
        }
        return str.split(split);
    }


    /**
     * 判断是否为空字符串
     * <pre class="code">
     * $.isEmpty(null)		= true
     * </pre>
     *
     * @param cs the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null},
     * its length is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean isEmpty(final CharSequence cs) {
        return StringUtil.isEmpty(cs);
    }

    public static boolean isEmpty(Map map) {
        return CollectionUtil.isEmpty(map);
    }

    /**
     * 转换为Long集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String str) {
        return Arrays.asList(toLongArray(str));
    }

    /**
     * 字符串转 int，为空则返回0
     *
     * <pre>
     *   $.toInt(null) = 0
     *   $.toInt("")   = 0
     *   $.toInt("1")  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if
     * conversion fails
     */
    public static int toInt(final Object str) {
        return toInt(str, -1);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Double</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toDouble(null, 1) = 1.0
     *   $.toDouble("", 1)   = 1.0
     *   $.toDouble("1", 0)  = 1.0
     * </pre>
     *
     * @param value the string to convert, may be null
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Double toDouble(Object value) {
        return toDouble(String.valueOf(value), -1.00);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>Double</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   $.toDouble(null, 1) = 1.0
     *   $.toDouble("", 1)   = 1.0
     *   $.toDouble("1", 0)  = 1.0
     * </pre>
     *
     * @param value        the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static Double toDouble(Object value, Double defaultValue) {
        try {
            return Double.parseDouble(toStr(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转 int，为空则返回默认值
     *
     * <pre>
     *   $.toInt(null, 1) = 1
     *   $.toInt("", 1)   = 1
     *   $.toInt("1", 0)  = 1
     * </pre>
     *
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static int toInt(final Object str, final int defaultValue) {
        try {
            return Integer.parseInt(String.valueOf(str));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Long集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Long> toLongList(String split, String str) {
        return Arrays.asList(toLongArray(split, str));
    }

    /**
     * 转换为Long数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = toLong(arr[i], 0);
            longs[i] = v;
        }
        return longs;
    }
    /**
     * 转换为Long数组<br>
     *
     * @param str   被转换的值
     * @return 结果
     */
    public static Long[] toLongArray( String str) {
        if (StringUtil.isEmpty(str)) {
            return new Long[]{};
        }
        String[] arr = str.split(",");
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Long v = toLong(arr[i], 0);
            longs[i] = v;
        }
        return longs;
    }

    /**
     * 字符串转 long，为空则返回默认值
     *
     * <pre>
     *   $.toLong(null, 1L) = 1L
     *   $.toLong("", 1L)   = 1L
     *   $.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     */
    public static long toLong(final Object str, final long defaultValue) {
        try {
            return Long.parseLong(toStr(str));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str 字符串
     * @return {String}
     */
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return {String}
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str || str.equals(StringPool.NULL)) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 将集合拼接成字符串，默认使用`,`拼接
     *
     * @param coll the {@code Collection} to convert
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll) {
        return join(coll,",");
    }

    /**
     * 将集合拼接成字符串，默认指定分隔符
     *
     * @param coll  the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll, String delim) {
        if (CollectionUtil.isEmpty(coll) || isEmpty(delim)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        coll.forEach(s -> sb.append(s).append(delim));
        return  sb.substring(0, sb.length() - delim.length());
    }

    /**
     * 分割 字符串
     *
     * @param str       字符串
     * @param delimiter 分割符
     * @return 字符串数组
     */
    public static String[] split(String str, String delimiter) {
        if (isEmpty(str)) {
            return new String[]{};
        }
        return str.split(delimiter);
    }

    /**
     * 分割 字符串
     *
     * @param str 字符串
     * @return 字符串数组
     */
    public static String[] split(String str) {
        return split(",");
    }
}
