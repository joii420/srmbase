package com.step.api.runtime.model;

/**
 * @author : Sun
 * @date : 2024/4/1  18:48
 */
public enum ExpirePolicy {
    /**
     * 过期策略
     */
    //创建固定
    CREATED,
    //更新刷新
    MODIFY,
    //访问刷新
    TOUCHED
}