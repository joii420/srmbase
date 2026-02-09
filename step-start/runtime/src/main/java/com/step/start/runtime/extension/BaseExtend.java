package com.step.start.runtime.extension;

import com.step.start.runtime.extension.exception.ExtensionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author : Sun
 * @date : 2023/8/28  14:21
 */
public interface BaseExtend {

    String getBasePath();

    default String getMethodName() {
        return "execute";
    }
    default String getDestroyMethodName() {
        return "destroy";
    }

    default String getBasePackage() {
        return "extend.java";
    }

    default byte[] getClassBytes(String className) {
        String replace = isLinux() ? "/" : "\\";
        String classPath = className.replace(".", replace);
        File file = new File(getBasePath() + classPath + ".class");
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new ExtensionException("找不到的文件: " + e.getMessage());
        }
    }

    /**
     * 是否是linux系统
     */
    private static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return false;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return true;
        } else {
            System.out.println("无法确定当前系统");
            return false;
        }
    }
}
