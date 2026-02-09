package com.step.auto.deployment;

import com.step.qute.runtime.TemplateSql;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepQuteProcessor {

    private static final String FEATURE = "step-qute";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(TemplateSql.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
