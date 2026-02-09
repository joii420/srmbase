package com.step.start.runtime.extension;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/8/28  15:36
 */
public interface ExtendHandler {
    /**
     * 扩展的方法入口
     *
     * @param params 方法参数
     */
    Object execute(Map<String, Object> params);

    void destroy();

}
