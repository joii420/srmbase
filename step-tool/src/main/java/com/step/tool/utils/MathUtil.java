package com.step.tool.utils;


import com.step.api.runtime.exception.base.BaseException;

import java.math.BigDecimal;

/**
 * @author : Sun
 * @date : 2022/8/10  8:46
 */
public class MathUtil {

    /**
     * 除法 不保留小数
     * a / b
     */
    public static String divide(long a, long b) {
        if (b == 0) {
            throw new BaseException("error divide zero").record();
        }
        return String.valueOf(BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 2, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100)).intValue());
    }


    /**
     * 除法 不保留小数(计算百分比)
     * a / b
     */
    public static Integer divideInt(long a, long b) {
        if (b == 0) {
            throw new BaseException("error divide zero").record();
        }
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 2, BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(100)).intValue();
    }

    public static double sacleTwo(double a) {
        return BigDecimal.valueOf(a).divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }
}
