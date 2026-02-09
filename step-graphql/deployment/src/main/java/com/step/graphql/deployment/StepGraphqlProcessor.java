package com.step.graphql.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepGraphqlProcessor {

    private static final String FEATURE = "step-graphql";

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
