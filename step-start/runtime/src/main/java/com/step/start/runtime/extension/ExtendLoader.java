package com.step.start.runtime.extension;

import com.step.start.runtime.extension.exception.ExtensionException;
import com.step.logger.LOGGER;
import org.apache.commons.collections4.Get;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/8/28  14:05
 */
public class ExtendLoader implements BaseExtend {

    private ClassLoader classLoader;
    private final String basePath;
    private final Map<String, Execute> executeMap;

    public ExtendLoader(String basePath) {
        this.classLoader = new ExtendClassLoader(this);
        this.basePath = basePath;
        this.executeMap = new HashMap<>(0);
        LOGGER.info("extension path: %s", this.basePath);
    }

    public ExtendLoader isTest() {
        this.classLoader = Thread.currentThread().getContextClassLoader();
        return this;
    }

    @Override
    public String getBasePath() {
        return this.basePath;
    }

    public void reload() {
        this.classLoader = new ExtendClassLoader(this);
        executeMap.clear();
    }

    public Object execute(String className, Map<String, Object> params) {
        Execute execute = executeMap.get(className);
        if (execute == null) {
            execute = getExecute(className);
        }
        Object result = execute.execute(params);
        return result;
    }

    private synchronized Execute getExecute(String className) {
        Execute execute = executeMap.get(className);
        if (execute == null) {
            Class<?> clazz;
            Object obj;
            Method declaredMethod, destroyMethod;
            try {
                clazz = this.classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new ExtensionException("failed to load file: " + className);
            }
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                throw new ExtensionException("failed to instanced: " + className);
            }
            try {
                declaredMethod = clazz.getDeclaredMethod(getMethodName(), Map.class);
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new ExtensionException("method not found: " + getMethodName());
            }
            try {
                destroyMethod = clazz.getDeclaredMethod(getDestroyMethodName());
                destroyMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new ExtensionException("method not found: " + getDestroyMethodName());
            }
            execute = new Execute(obj, declaredMethod, destroyMethod);
            executeMap.put(className, execute);
        }
        return execute;
    }

    public void destroy(String className) {
        Execute execute = executeMap.get(className);
        if (execute == null) {
            return;
        }
        execute.destroy();
    }

    static class Execute {
        Object executeObject;
        Method executeMethod;
        Method destroyMethod;

        Execute(Object executeObject, Method executeMethod, Method destroyMethod) {
            this.executeObject = executeObject;
            this.executeMethod = executeMethod;
            this.destroyMethod = destroyMethod;
        }

        Object execute(Map<String, Object> params) {
            try {
                return executeMethod.invoke(executeObject, params);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new ExtensionException("Execute error : " + e.getCause().getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExtensionException("Execute error : " + e.getMessage());
            }
        }

        Object destroy() {
            System.out.println("Destroy class: " + executeObject.getClass().getName());
            try {
                return destroyMethod.invoke(executeObject);
            } catch (InvocationTargetException e) {
                throw new ExtensionException("Destroy error : " + e.getCause().getMessage());
            } catch (Exception e) {
                throw new ExtensionException("Destroy error : " + e.getMessage());
            }
        }
    }


}
