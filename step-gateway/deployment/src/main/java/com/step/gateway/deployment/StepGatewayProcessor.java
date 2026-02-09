package com.step.gateway.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepGatewayProcessor {

    private static final String FEATURE = "step-gateway";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
//                .addBeanClass(SignVerifyFilter.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
