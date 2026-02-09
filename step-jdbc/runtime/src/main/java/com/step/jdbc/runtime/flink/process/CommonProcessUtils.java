package com.step.jdbc.runtime.flink.process;

import com.step.jdbc.runtime.flink.dictionary.datasource.FlinkBaseDatasource;
import com.step.jdbc.runtime.flink.exception.FlinkDataException;
import com.step.jdbc.runtime.flink.session.DBSessionConnection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.step.jdbc.runtime.flink.utils.ProcessDealUtils.*;

public class CommonProcessUtils implements CalculateProcess, Serializable {
    @Override
    public List<Map<String, Object>> readyColl(List<? extends FlinkBaseDatasource> datasourceList, List<ProcessStep> listSteps, Map<String, String> datas) throws Exception {
        List<Map<String, Object>> mainData2 = getMainData2(datasourceList);
//        List<Map<String, Object>> mainDataTemp = new ArrayList<>(mainData2);
//        for (int i = 0; i < 99; i++) {
////            List<Map<String, Object>> copy = new ArrayList<>(mainData2);
//            mainDataTemp.addAll(mainData2);
//        }
//        mainData2 = mainDataTemp;
        try {
            Stream<Map<String, Object>> stream = mainData2.parallelStream();
//        ProcessUtils processUtils = new ProcessUtils();
            //准备参数
            final Map<String, Object> javaObject = readyJavaObjectAndArgs(datasourceList);
            Stream<Map<String, Object>> mapStream =stream;
            for (ProcessStep processStep : listSteps) {
                //需要根据条件合并数据源
                switch (processStep.getFlinkProcessType()) {
                    case FILTER:
                        FilterFlinkProcess flinkProcess = (FilterFlinkProcess) processStep.getFlinkProcess();
                         mapStream = mapStream.filter(x -> {
                            try {
                                Map<String, String> datasTmep = new HashMap<>();
                                datasTmep.putAll(datas);
                                return (boolean) executeJavaScript(flinkProcess.getContent(), javaObject, datasTmep, x);
                            } catch (Exception e) {
                                //记录异常 返回false
                                throw new FlinkDataException("FILTER 执行失败: " + e.getMessage());
                            }
                        });
                        break;
                    case MAP:
                        MapFlinkProcess mapflinkProcess = (MapFlinkProcess) processStep.getFlinkProcess();
                        mapStream = mapStream.map(x->{
                            try {
                                Map<String, String> datasTmep = new HashMap<>();
                                datasTmep.putAll(datas);
                                return (Map<String, Object>) executeJavaScript(mapflinkProcess.getContent(), javaObject, datasTmep, x);
    //                            return deal((SelectDatasource) datasourceList.get(4),processUtils,x);
                            } catch (Exception e) {
                                //记录异常 返回false
                                throw new FlinkDataException("MAP 执行失败: " + e.getMessage());
                            }
                        });
                        break;
                }
            }
            long start = System.currentTimeMillis();
//        System.out.println("执行开始时间: " + start);
            List<Map<String, Object>> result = mapStream.collect(Collectors.toList());
            long end = System.currentTimeMillis();
//        System.out.println("执行结束时间: " + end);
            System.out.println("执行时间: " + (end - start));
            return result;
        } finally {
            DBSessionConnection.close(datasourceList.get(0).getGroupId());
        }
    }
}
