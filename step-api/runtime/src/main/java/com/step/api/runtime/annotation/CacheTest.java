package com.step.api.runtime.annotation;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * This annotation is used to mark a method to cache the result of a method call.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Inherited
public @interface CacheTest {

    /**
     * SPEL expression used to generate the key for the method call
     * All method arguments can be used in the expression
     */
    @Nonbinding
    String key() default "";

    /*
     * This is the name of the cache.
     */
    @Nonbinding
    String cacheName() default "";

}
