package com.step.tool.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.function.Function;

/**
 * @author : Sun
 * @date : 2023/9/7  10:20
 */
public class JoiiUtil {
    /**
     * 读取文件的每一行内容,使用function对内容进行识别和修改,保存到新的路径
     *
     * @param sourceFilePath 资源文件路径
     * @param targetFilePath 新文件路径
     * @param lineEdit       行编辑function
     */
    public static void editTxt(String sourceFilePath, String targetFilePath, Function<String, String> lineEdit) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(targetFilePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = lineEdit.apply(currentLine);
                writer.write(currentLine);
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }
}
