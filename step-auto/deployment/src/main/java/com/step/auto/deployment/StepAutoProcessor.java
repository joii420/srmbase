package com.step.auto.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepAutoProcessor {

    private static final String FEATURE = "step-auto";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
//                .addBeanClass(UtilConfig.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
