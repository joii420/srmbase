package com.step.api.runtime.annotation;

import com.step.api.runtime.common.MicroRegister;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Inherited
public @interface MicroServer {
    MicroRegister value();
}
