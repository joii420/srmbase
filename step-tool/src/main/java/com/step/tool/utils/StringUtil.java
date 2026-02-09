package com.step.tool.utils;


import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtil {

    public static String valueOf(Object str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 判断对象是否为空
     **/
    public static boolean isEmpty(Object... objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }
        for (Object obj : objs) {
            if (obj == null || "null".equals(obj)) {
                return true;
            } else if ("".equals(obj.toString().trim())) {
                return true;
            } else if (obj instanceof Collection) {
                Collection collection = (Collection) obj;
                if (collection.isEmpty()) {
                    return true;
                }
            } else if (obj instanceof Map) {
                Map map = (Map) obj;
                if (map.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断字符串是否存在不为空
     **/
    public static boolean hasNotEmpty(String... objs) {
        if (objs == null || objs.length == 0) {
            return false;
        }
        for (Object obj : objs) {
            if (obj != null && obj != "") {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为空
     **/
    public static boolean isNotEmpty(Object... objs) {
        return !isEmpty(objs);
    }

    /**
     * 对象为空 则替换另外一个对象
     */
    public static <T> T isEmptyReplace(T checkStr, T replace) {
        return isEmpty(checkStr) ? replace : checkStr;
    }

    /**
     * 判断字符串是否超过len个长度 不超过直接返回 超过则截取前len个长度返回
     *
     * @param src 原字符串
     * @param len 需要判断的航都
     * @return 返回结果字符串
     */
    public static String isEnoughSubstr(String src, int len) {
        return null == src ? null : src.length() <= len ? src : src.substring(0, len);
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 字符串补全
     *
     * @param src  原字符串
     * @param len  长度
     * @param flag 0 左边 否则 右边
     * @param c    填补字符
     * @return 补全后字符串
     */
    public static String stringPadding(String src, int len, int flag, String c) {
        StringBuffer result = new StringBuffer();
        if (c.length() != 1) {
            return src;
        }
        int num = 0;
        try {
            num = src.getBytes("gbk").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (result.length() < len) {
            if (0 == flag) {
                for (int i = 0; i < len - num; i++) {
                    result.append(c);
                }
                result.append(src);
            } else {
                result.append(src);
                for (int i = 0; i < len - num; i++) {
                    result.append(c);
                }
            }
            return result.toString();
        } else {
            return result.toString();
        }
    }

    /**
     * 是否有一个不为空
     */
    public static boolean isAnyNotEmpty(Object... objs) {
        if (objs == null || objs.length == 0) {
            return false;
        }
        for (Object obj : objs) {
            if (!isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 同 log 格式的 format 规则
     * <p>
     * use: format("my name is {}, and i like {}!", "L.cm", "Java")
     *
     * @param message   需要转换的字符串
     * @param arguments 需要替换的变量
     * @return 转换后的字符串
     */
    public static String format(String message, Object... arguments) {
        // message 为 null 返回空字符串
        if (message == null) {
            return StringPool.EMPTY;
        }
        // 参数为 null 或者为空
        if (arguments == null || arguments.length == 0) {
            return message;
        }
        StringBuilder sb = new StringBuilder((int) (message.length() * 1.5));
        int cursor = 0;
        int index = 0;
        int argsLength = arguments.length;
        for (int start, end; (start = message.indexOf('{', cursor)) != -1 && (end = message.indexOf('}', start)) != -1 && index < argsLength; ) {
            sb.append(message, cursor, start);
            sb.append(arguments[index]);
            cursor = end + 1;
            index++;
        }
        sb.append(message.substring(cursor));
        return sb.toString();
    }

    /**
     * 获取标识符 用于参数清理
     *
     * @param param
     * @return
     */
    public static String cleanIdentifier(String param) {
        if (param == null) {
            return null;
        }
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                paramBuilder.append(c);
            }
        }
        return paramBuilder.toString();
    }

    public static String convertStrToSnakeCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar) && i != 0) {
                result.append("_");
            }
            result.append(Character.toLowerCase(currentChar));
        }
        return result.toString();
    }
}
