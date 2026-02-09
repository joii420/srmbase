package com.step.jdbc.runtime.session.anno;

import com.step.jdbc.runtime.session.support.IndexType;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.*;

/**
 * @author : Sun
 * @date : 2024/4/23  20:02
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface JdbcIndex {
    /**
     * 索引名称
     */
    String name();

    /**
     * 是否唯一键
     */
    boolean unique() default false;

    /**
     * 是否并发
     */
    boolean concurrently() default false;

    /**
     * 索引类型
     */
    IndexType type() default IndexType.B_TREE;

}
