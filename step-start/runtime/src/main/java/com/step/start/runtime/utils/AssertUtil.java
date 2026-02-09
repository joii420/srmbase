package com.step.start.runtime.utils;

import com.step.api.runtime.exception.base.ParamException;
import com.step.tool.utils.StringUtil;

public class AssertUtil {



    public static void isNull(Object obj, String msg) {
        if (StringUtil.isEmpty(obj)) {
            throw new ParamException(msg);
        }
    }

    public static void isNull(Object obj) {
        isNull(obj, "缺少必要参数");
    }

    public static void isTrue(boolean condition, String msg) {
        if (condition) {
            throw new ParamException(msg);
        }
    }

}
