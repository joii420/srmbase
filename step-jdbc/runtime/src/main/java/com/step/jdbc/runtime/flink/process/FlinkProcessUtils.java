package com.step.jdbc.runtime.flink.process;

import com.step.jdbc.runtime.flink.dictionary.datasource.FlinkBaseDatasource;
import com.step.jdbc.runtime.flink.exception.FlinkDataException;
import com.step.jdbc.runtime.flink.session.DBSessionConnection;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.CloseableIterator;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

//@ApplicationScoped
public class FlinkProcessUtils implements CalculateProcess, Serializable {

    private static StreamExecutionEnvironment env;

    static{
        env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(16);
    }

    /**
     * 预配置，关键在于在脚本中
     * 1、查询数据库 根据数据源
     * 2、获取其他列操作
     * 3、合并union操作
     * 4、异常处理
     * 5、在js中调用java方法并且返回值
     * 6、js使用graalvmjs
     * 7、升级jdk版本 17也能使用flink
     *
     * @param listSteps
     * @return
     */
    @Override
    public List<Map<String, Object>> readyColl(List<? extends FlinkBaseDatasource> datasourceList, List<ProcessStep> listSteps, Map<String, String> datas) throws Exception {

//        try(StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(32)) {
        try{
//            StreamExecutionEnvironment env = getExecutionEnvironment();
            //主表的内容先成数据源流
            List<Map<String, Object>> mainData2 = getMainData2(datasourceList);
            int size = mainData2.size();
//            List<Map<String, Object>> mainDataTemp = new ArrayList<>(mainData2);
//            for (int i = 0; i < 99; i++) {
////                List<Map<String, Object>> copy = new ArrayList<>(mainData2);
//                mainDataTemp.addAll(mainData2);
//            }
//            mainData2 = mainDataTemp;
            DataStreamSource<Map<String, Object>> listDataStreamSource = env.fromCollection(mainData2);
            //准备好参数
            final Map<String, Object> javaObject = readyJavaObjectAndArgs(datasourceList);
//        SingleOutputStreamOperator<Map<String, Object>> reduce=listDataStreamSource.map((Map<String,Object>x)->x);
            // 调用 map 方法并实现 MapFunction 接口，显式指定返回类型
            DataStream<Map<String, Object>> reduce = listDataStreamSource.map(new MapFunction<Map<String, Object>, Map<String, Object>>() {
                @Override
                public Map<String, Object> map(Map<String, Object> x) throws Exception {
                    // 这里执行原来的逻辑
                    // ...

                    return x; // 返回结果
                }
            });
            for (ProcessStep processStep : listSteps) {
                //需要根据条件合并数据源
                switch (processStep.getFlinkProcessType()) {
                    case FILTER:
                        FilterFlinkProcess flinkProcess = (FilterFlinkProcess) processStep.getFlinkProcess();
                        reduce = reduce.filter(new FilterFunction<Map<String, Object>>() {
                            @Override
                            public boolean filter(Map<String, Object> x) throws Exception {
                                // 这里执行原来的逻辑
                                //js脚本 java环境对象 当条数据
                                try {
                                    Map<String, String> datasTmep = new HashMap<>();
                                    datasTmep.putAll(datas);
                                    return (boolean) executeJavaScript(flinkProcess.getContent(), javaObject, datasTmep, x);
                                } catch (Exception e) {
                                    //记录异常 返回false
                                    throw new FlinkDataException("FILTER 执行失败: " + e.getMessage());
                                }
                            }
                        });
                        break;
                    case MAP:
                        MapFlinkProcess mapflinkProcess = (MapFlinkProcess) processStep.getFlinkProcess();
                        reduce = reduce.map(new MapFunction<Map<String, Object>, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> map(Map<String, Object> x) throws Exception {
                                // 这里执行原来的逻辑
                                try {
                                    Map<String, String> datasTmep = new HashMap<>();
                                    datasTmep.putAll(datas);
                                    return (Map<String, Object>) executeJavaScript(mapflinkProcess.getContent(), javaObject, datasTmep, x);
                                } catch (Exception e) {
                                    //记录异常 返回false
                                    throw new FlinkDataException("MAP 执行失败: " + e.getMessage());
                                }
                                //                            return x;
                            }
                        });
                        break;
//                    case JOIN:
//                        JoinFlinkProcess joinFlinkProcess = (JoinFlinkProcess) processStep.getFlinkProcess();
//                        String key = joinFlinkProcess.getKey();
//                        List<String> split = Arrays.asList(joinFlinkProcess.getResourceNames().split(","));
//                        //构建一个需要合并查询的多个数据源
//                        List<Map<Object, List<Map<String, Object>>>> listMoreUnionData = new ArrayList<>();
//                        for (ProcessStep processStepData : listSteps) {
//                            if (split.contains(processStepData.getProcessDatasource().getName())) {
//                                //获取数据 根据key封装
//                                Map<Object, List<Map<String, Object>>> objectListMap = tranDataByKey(key, processStepData.getProcessDatasource().getData());
//                                listMoreUnionData.add(objectListMap);
//                            }
//                        }
//                        //哪几个数据源需要合并，UNION必须是第一步 无需脚本 x是Map<String,Object>类型 数据源流如何构建
//                        reduce = reduce.map(new MapFunction<Map<String, Object>, Map<String, Object>>() {
//                            @Override
//                            public Map<String, Object> map(Map<String, Object> x) throws Exception {
//                                // 这里执行原来的逻辑
//                                // ...
//                                Object unionKeyData = x.get(key);
//                                Map<String, Object> result = new HashMap<>();
//                                result.putAll(x);
//                                for (Map<Object, List<Map<String, Object>>> unionData : listMoreUnionData) {
//                                    if (unionData.containsKey(unionKeyData)) {
//                                        //获取那条数据
//                                        Map<String, Object> map = unionData.get(x.get(key)).get(0);
//                                        //将数据合并
//                                        result.putAll(map);
//                                    }
//                                }
//                                //返回数据
//                                return result;
//    //                            return x; // 返回结果
//                            }
//                        });
//
//                        break;
                }
            }

//        Class.forName("com.step.process.FlinkProcessUtils$1");
//        reduce.print();
//        env.execute("job1");
            long start = System.currentTimeMillis();
            System.out.println("执行开始时间: " + start + "  数据处理总量: " + size);
            CloseableIterator<Map<String, Object>> mapCloseableIterator = reduce.executeAndCollect();
            List<Map<String, Object>> result = new ArrayList<>();
            mapCloseableIterator.forEachRemaining(result::add);
//        while (mapCloseableIterator.hasNext()) {
//            Map<String, Object> next = mapCloseableIterator.next();
//            result.add(next);
//        }
            long end = System.currentTimeMillis();
            System.out.println("执行结束时间: " + end);
            System.out.println("执行时间: " + (end - start));
            return result;
        } finally {
            //关闭所有数据源
            DBSessionConnection.close(datasourceList.get(0).getGroupId());
        }
    }

    private List<Map<String, Object>> getMainData2(List<? extends FlinkBaseDatasource> datasourceList) {
        //返回isMain是true的data
        return datasourceList.stream().filter(FlinkBaseDatasource::isMain).map(FlinkBaseDatasource::getData).findFirst().orElseThrow(() -> new FlinkDataException("数据源不能为空"));
    }


//    private List<Map<String, Object>> getMainData(List<ProcessStep> listSteps) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        for (ProcessStep processStep : listSteps) {
//            SelectDatasource processDatasource = processStep.getProcessDatasource();
//            if (!processDatasource.isMain()) {
//                continue;
//            }
//            result = processDatasource.getData();
//            break;
//        }
//        List<Map<String, Object>> union = new ArrayList<>();
//        if (unionFlinkProcess != null) {
//            List<String> strings = Arrays.asList(unionFlinkProcess.getResourceNames().split(","));
//            for (ProcessStep processStep : listSteps) {
//                //无需计算则走开
//                SelectDatasource processDatasource = processStep.getProcessDatasource();
//                if (strings.contains(processDatasource.getName())) {
//                    union.addAll(processDatasource.getData());
//                }
//            }
//            if (!unionFlinkProcess.isMain()) {
//                SelectDatasource processDatasourceStudent = new SelectDatasource(null, unionFlinkProcess.getName(), unionFlinkProcess.getName(), null, null, "Mysql", false, new String[]{"id", "name", "student_number", "age"});
//
//                ProcessStep join = new ProcessStep(10L, "data", 4, FlinkProcessType.UNION, processDatasourceStudent);
//                processDatasourceStudent.setData(union);
//                listSteps.add(join);
//            } else {
//                return union;
//            }
//        }
//
//        return result;
//    }

    //js执行通用方法
    public Object executeJavaScript(String script, Map<String, Object> javaObject, Map<String, String> datas, Object... args) {
        try (Context context = Context.newBuilder("js").allowAllAccess(true).build()) {
            // 创建 Bindings 对象用于传递 Java 对象到 JavaScript 环境
            Value js = context.getBindings("js");

            if (javaObject != null && !javaObject.isEmpty()) {
                javaObject.forEach((key, value) -> {
                    Value data = Value.asValue(value);
                    js.putMember(key, data);
                });
            }
            Value mapData = Value.asValue(datas);
            js.putMember("datas", datas);
            // 将需要传递给脚本的 Java 对象绑定到 Bindings 中
            // 示例：bindings.put("javaObject", javaObject);
            // 注意：javaObject 必须是 JavaScript 中能够识别的类型
            // 在 Context 中执行脚本
            context.eval("js", script);

            // 获取脚本中的函数
            Value function = context.getBindings("js").getMember("main");

            // 调用函数并传入参数
            Value result = function.execute(args);

            // 获取函数的返回值
            return result.as(Object.class);
        } catch (PolyglotException e) {
            // 处理异常
//            System.err.println("Error executing JavaScript: " + e.getMessage());
//            return null;
            throw new FlinkDataException("执行脚本异常: " + e.getMessage());
        }
    }
//
//    /**
//     * 传参通用方法
//     * 1、将所有数据源放入js环境
//     * 2、process方法放入环境，便于动态查询调用
//     *
//     * @param processSteps
//     * @return
//     */
//    public Map<String, Object> readyJavaObjectAndArgs(List<ProcessStep> processSteps) {
//        Map<String, Object> map = new HashMap<>();
//        processSteps.forEach(x -> {
//            SelectDatasource processDatasource = x.getProcessDatasource();
//            map.put(processDatasource.getName(), processDatasource);
//        });
//        ProcessUtils processUtils = new ProcessUtils();
//        map.put("process", processUtils);
//        return map;
//    }

    /**
     * 传参通用方法
     * 1、将所有数据源放入js环境
     * 2、process方法放入环境，便于动态查询调用
     *
     * @param datasourceList 数据源
     * @return
     */
    public Map<String, Object> readyJavaObjectAndArgs(List<? extends FlinkBaseDatasource> datasourceList) {
        Map<String, Object> map = new HashMap<>();
        datasourceList.forEach(datasource -> {
            String name = datasource.getName();
            map.put(name, datasource);
        });
//        processSteps.forEach(x -> {
//            SelectDatasource processDatasource = x.getProcessDatasource();
//            map.put(processDatasource.getName(), processDatasource);
//        });
        ProcessUtils processUtils = new ProcessUtils();
        map.put("process", processUtils);
        return map;
    }


    /**
     * 识别唯一的可能是各种类型
     * 将参与合并的数据源进行转换
     *
     * @param key
     * @param data
     * @return
     */
    public Map<Object, List<Map<String, Object>>> tranDataByKey(String key, List<Map<String, Object>> data) {
        return data.stream().filter(x -> x.containsKey(key)).collect(Collectors.groupingBy(m -> m.get(key)));
    }

}
