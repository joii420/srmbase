package com.step.web.deployment;

import com.step.web.runtime.core.start.VerticleDeployer;
import com.step.web.runtime.core.utils.AsyncServiceUtil;
import com.step.web.runtime.core.utils.HotUtil;
import com.step.web.runtime.core.verticle.AsyncRegistVerticle;
import com.step.web.runtime.core.verticle.ClusterAsyncRegistVerticle;
import com.step.web.runtime.micro.MicroProvider;
import com.step.web.runtime.register.VertxRegister;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepWebProcessor {

    private static final String FEATURE = "step-web";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }


    // 这是添加的
    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(HotUtil.class)
                .addBeanClass(VerticleDeployer.class)
                .addBeanClass(MicroProvider.class)
                .addBeanClass(AsyncServiceUtil.class)
                .addBeanClass(AsyncRegistVerticle.class)
                .addBeanClass(ClusterAsyncRegistVerticle.class)
                .addBeanClass(VertxRegister.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
