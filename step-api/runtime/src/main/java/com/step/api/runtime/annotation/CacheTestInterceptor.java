package com.step.api.runtime.annotation;

import com.step.api.runtime.core.CacheAPI;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.lang.reflect.Method;

//@Slf4j
@Interceptor
@CacheTest
public class CacheTestInterceptor {

    @Inject
    CacheAPI cacheManager;

    @AroundInvoke
    public Object cacheTest(InvocationContext context) {
        System.out.println("context = " + context);
//        MethodSignature signature = (MethodSignature) context.getSignature();
//        Method method = signature.getMethod();
        Method method = context.getMethod();
        CacheTest annotation = method.getAnnotation(CacheTest.class);
        String cacheName = annotation.cacheName();
        //@Cache(cacheName = "permissionGroupsForUser", key = "{#user.email + #user.tenantId}")
        //@Cache(cacheName = "objectcache1", key = "#argumentModel.name")
        //derive key
//        String[] parameterNames = signature.getParameterNames();
//        Object[] args = context.getArgs();
        Object[] args = context.getParameters();
        String key = annotation.key();

        Class<?> returnType = method.getReturnType();
        try {
            return context.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        //If method does not returns Mono<T> or Flux<T> raise exception
//        throw new SystemException("Invalid usage of @Cache annotation. Only reactive objects Mono and Flux are supported for caching.");
    }
}
