package com.step.tool.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author : Sun
 * @date : 2024/7/2  16:10
 */
public class ListUtil {

    private static final int DEFAULT_GROUP_SIZE = 50;

    public static void main(String[] args) {

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 102; i++) {
            integers.add(i);
        }
        List<Integer> integers1 = groupExecute(integers, 30, a -> {
            List<Integer> integers2 = a.stream().map(b -> b + 100).toList();
            return integers2;
        });
        System.out.println();
    }

    /**
     * 分组函数
     *
     * @param resource
     * @param groupSize 分组大小
     * @param function
     */
    public static <T, R> List<R> groupExecute(List<T> resource, int groupSize, Function<List<T>, List<R>> function) {
        if (resource == null) {
            return new ArrayList<>();
        }
        int totalSize = resource.size();
        if (totalSize <= groupSize) {
            return function.apply(resource);
        }
        int startIndex = 0;
        int endIndex;
        List<R> totalList = new ArrayList<>();
        while ((endIndex = startIndex + groupSize) < totalSize) {
            List<T> ts = resource.subList(startIndex, endIndex);
            totalList.addAll(function.apply(ts));
            startIndex += groupSize;
        }
        List<T> ts = resource.subList(startIndex, totalSize);
        totalList.addAll(function.apply(ts));
        return totalList;
    }

    /**
     * 分组函数
     *
     * @param resource
     * @param function
     */
    public static <T, R> List<R> groupExecute(List<T> resource, Function<List<T>, List<R>> function) {
        return groupExecute(resource, DEFAULT_GROUP_SIZE, function);
    }

}
