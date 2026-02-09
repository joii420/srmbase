package com.step.start.runtime.extension;

import com.step.tool.utils.SystemUtil;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.Map;

/**
 * @author : Sun
 * @date : 2023/8/28  16:35
 */
@ApplicationScoped
public class ExtensionManager {

    @ConfigProperty(name = "class.basepath", defaultValue = "D:\\")
    String basePath;

    private ExtendLoader extendLoader;
    private static ExtensionManager extensionManager;
    private static boolean isLinux = SystemUtil.isLinux();

    public void init(@Observes StartupEvent startupEvent) {
        this.extendLoader = new ExtendLoader(basePath);
        ExtensionManager.extensionManager = this;
    }

    public ExtensionManager isTest() {
        this.extendLoader.isTest();
        return this;
    }

    public Object execute(String className, Map<String, Object> params) {
        return this.extendLoader.execute(className, params);
    }

    public void destroy(String className) {
        this.extendLoader.destroy(className);
    }

    public static ExtensionManager getInstance() {
        if (!isLinux) {
            return extensionManager.isTest();
        } else {
            return extensionManager;
        }

    }

    public synchronized void reload() {
        this.extendLoader.reload();
    }

    public ExtendLoader getExtendLoader() {
        return extendLoader;
    }
}
