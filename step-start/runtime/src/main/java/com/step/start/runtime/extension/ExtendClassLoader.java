package com.step.start.runtime.extension;


import com.step.logger.LOGGER;

/**
 * @author : Sun
 * @date : 2023/8/28  14:09
 */
public class ExtendClassLoader extends ClassLoader {

    private BaseExtend extendInterface;

    public ExtendClassLoader(BaseExtend extendInterface) {
        this.extendInterface = extendInterface;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        if (!className.startsWith(extendInterface.getBasePackage())) {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        LOGGER.info("load extension: %s", className);
        byte[] classBytes = extendInterface.getClassBytes(className);
        return defineClass(className, classBytes, 0, classBytes.length);
    }


}
