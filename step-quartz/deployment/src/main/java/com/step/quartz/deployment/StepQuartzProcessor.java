package com.step.quartz.deployment;

import com.step.quartz.runtime.TaskJobUtil;
import com.step.quartz.runtime.listener.ListenerManger;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepQuartzProcessor {

    private static final String FEATURE = "step-quartz";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(ListenerManger.class)
                .addBeanClass(TaskJobUtil.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
