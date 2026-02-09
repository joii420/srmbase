package com.step.jdbc.runtime.session.anno;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.*;

/**
 * @author : Sun
 * @date : 2024/4/23  20:02
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface JdbcTable {
    /**
     * 表名称
     */
    String value();
}
