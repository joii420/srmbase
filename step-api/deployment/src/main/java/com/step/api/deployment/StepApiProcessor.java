package com.step.api.deployment;

import com.step.api.runtime.utils.HostConfig;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepApiProcessor {

    private static final String FEATURE = "step-api";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    // 这是添加的
    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
//                .addBeanClass(CacheAPI.class)
//                .addBeanClass(Transmit.class)
                .addBeanClass(HostConfig.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
