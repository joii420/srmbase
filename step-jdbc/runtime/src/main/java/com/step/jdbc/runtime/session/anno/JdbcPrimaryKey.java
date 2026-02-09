package com.step.jdbc.runtime.session.anno;

import com.step.jdbc.runtime.session.support.PrimaryKeyType;
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
public @interface JdbcPrimaryKey {

    PrimaryKeyType value() default PrimaryKeyType.NORMAL;

}
