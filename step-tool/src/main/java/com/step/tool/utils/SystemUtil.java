package com.step.tool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Sun
 * @date : 2023/9/27  15:55
 */
public class SystemUtil {
    public static final Logger log = LoggerFactory.getLogger(SystemUtil.class);
    private static final long SYSTEM_START = System.currentTimeMillis();

    public static long systemStartSeconds() {
        return (System.currentTimeMillis() - SYSTEM_START) / 1000;
    }

    private static boolean linux;


    private static Properties applicationProperties = new Properties();
    private static Properties profileProperties = new Properties();

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            log.info("current system is Windows");
            linux = false;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            log.info("current system is Linux");
            linux = true;
        } else {
            log.warn("unknown current system");
            linux = false;
        }
        String applicationFile = "application.properties";
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(applicationFile);
        try {
            if (resourceAsStream != null) {
                applicationProperties.load(resourceAsStream);
                log.info("load system applicationProperties success: " + applicationProperties.size());
            } else {
                log.warn("load system applicationProperties failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String profile = System.getProperty("quarkus.profile");
        if (StringUtil.isNotEmpty(profile)) {
            String propertyFile = "/application-" + profile + ".properties";
            InputStream extionsionStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile);
            try {
                if (extionsionStream != null) {
                    profileProperties.load(extionsionStream);
                    log.info("load system properties success: " + profileProperties.size());
                } else {
                    log.warn("load system properties failed!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getValue(String key) {
        Object value = System.getProperty(key);
        if (value == null) {
            value = profileProperties.get(key);
        }
        if (value == null) {
            value = applicationProperties.get(key);
        }
        if (value != null) {
            return resolvePlaceholders(String.valueOf(value), new HashSet<>());
        } else {
            return null;
        }
    }

    public static String getValue(String key, String defaultValue) {
        return Optional.ofNullable(getValue(key)).orElse(defaultValue);
    }

    private static Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");

    /**
     * 递归解析 ${var} 占位符
     */
    private static String resolvePlaceholders(String value, Set<String> visited) {
        Matcher matcher = pattern.matcher(value);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            if (visited.contains(placeholder)) {
                throw new IllegalStateException("Circular reference detected for placeholder: " + placeholder);
            }

            String replacement = getValue(placeholder, "${" + placeholder + "}");
            visited.add(placeholder);
            try {
                replacement = resolvePlaceholders(replacement, visited); // 递归解析嵌套
            } finally {
                visited.remove(placeholder);
            }

            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    /**
     * 是否是linux系统
     */
    public static boolean isLinux() {
        return linux;
    }
}
