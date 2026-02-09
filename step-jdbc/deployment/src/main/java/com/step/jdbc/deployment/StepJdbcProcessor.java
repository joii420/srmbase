package com.step.jdbc.deployment;

import com.step.jdbc.runtime.flink.process.FlinkProcessUtils;
import com.step.jdbc.runtime.session.JdbcSessionBuilder;
import com.step.jdbc.runtime.txedit.provider.*;
import com.step.jdbc.runtime.txedit.provider.test.service.impl.TxServiceImpl;
import com.step.jdbc.runtime.txedit.provider.test.txedit.factory.JdbcFactory;
import com.step.jdbc.runtime.txedit.provider.test.txedit.provider.JdbcEditor;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class StepJdbcProcessor {

    private static final String FEATURE = "step-jdbc";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem addProducer() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClass(FlinkProcessUtils.class)
////                .addBeanClass(MapperEditor.class)
//                .addBeanClass(TransactionEdit.class)
//                .addBeanClass(TransactionFactory.class)
//                .addBeanClass(JdbcTransactionProvider.class)
                .addBeanClass(JdbcEditor.class)
                .addBeanClass(JdbcFactory.class)
                .addBeanClass(TxServiceImpl.class)
                //版本2
                .addBeanClass(JdbcSessionBuilder.class)
                .addBeanClass(JdbcEditorProxy.class)
                .addBeanClass(ServiceDiscoverProvider.class)
                .addBeanClass(JdbcTransactionProvider.class)
                .addBeanClass(TransactionProvider.class)
                .addBeanClass(TransactionEdit.class)
                .setDefaultScope(DotNames.SINGLETON)
                .setUnremovable()
                .build();
    }
}
