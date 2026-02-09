package com.step.start.deployment;

import com.step.start.runtime.extension.ExtensionManager;

import com.step.start.runtime.inject.StaticInject;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepStartProcessor {

    private static final String FEATURE = "step-start";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }


    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(ExtensionManager.class)
                .addBeanClass(StaticInject.class)
//                .addBeanClass(RequestLogFilter.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
