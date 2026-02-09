package com.step.jdbc.runtime.session.anno;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author : Sun
 * @date : 2024/4/23  20:02
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface JdbcDynamicsTable {
    /**
     * 函数名称
     */
    String method();
}
