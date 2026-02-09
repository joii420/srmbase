package com.step.monitor.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepMonitorProcessor {

    private static final String FEATURE = "step-monitor";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
//                .addBeanClass()
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
