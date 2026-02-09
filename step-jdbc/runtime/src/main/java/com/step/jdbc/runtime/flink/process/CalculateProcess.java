package com.step.jdbc.runtime.flink.process;

import com.step.jdbc.runtime.flink.dictionary.datasource.FlinkBaseDatasource;

import java.util.List;
import java.util.Map;

/**
 * 计算接口
 * 1、flink计算
 * 2、stream流计算
 */
public interface CalculateProcess {
    List<Map<String, Object>> readyColl(List<? extends FlinkBaseDatasource> datasourceList, List<ProcessStep> listSteps, Map<String, String> datas) throws Exception;

     static CalculateProcess getInstance(boolean flink){
        if(flink){
            return new FlinkProcessUtils();
        }
        return new CommonProcessUtils();
    }

    static  CalculateProcess getInstance(){
        return getInstance(true);
    }
}
