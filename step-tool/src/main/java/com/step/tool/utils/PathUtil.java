package com.step.tool.utils;

/**
 * @author : Sun
 * @date : 2023/6/28  13:43
 */
public class PathUtil {

    private static final String systemResourcesPath = System.getProperty("user.dir") + "\\src\\main\\resources\\";

    public static String getResourcePath(String path) {
        String filePath = systemResourcesPath + path;
        return filePath.replaceAll("[/\\\\]+", "\\\\");
    }

}
