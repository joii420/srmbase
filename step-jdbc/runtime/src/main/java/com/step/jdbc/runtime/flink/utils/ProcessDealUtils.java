package com.step.jdbc.runtime.flink.utils;

import com.step.jdbc.runtime.flink.dictionary.datasource.FlinkBaseDatasource;
import com.step.jdbc.runtime.flink.dictionary.datasource.SelectDatasource;
import com.step.jdbc.runtime.flink.exception.FlinkDataException;
import com.step.jdbc.runtime.flink.process.ProcessUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessDealUtils {
//    private static Context context;
//    static {
//
//    }
    public static List<Map<String, Object>> getMainData2(List<? extends FlinkBaseDatasource> datasourceList) {
        //返回isMain是true的data
        return datasourceList.stream().filter(FlinkBaseDatasource::isMain).map(FlinkBaseDatasource::getData).findFirst().orElseThrow(() -> new FlinkDataException("数据源不能为空"));
    }
    public static Object executeJavaScript(String script, Map<String, Object> javaObject, Map<String, String> datas, Object... args) {
        return executeJavaScript(script,"main",javaObject,datas,args);
    }
    public static Object executeJavaScript(String script, String processMethod, Map<String, Object> javaObject, Map<String, String> datas, Object... args) {
        try(Context context  = Context.newBuilder("js").allowAllAccess(true).build()) {
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
            Value function = context.getBindings("js").getMember(processMethod);

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


    /**
     * 传参通用方法
     * 1、将所有数据源放入js环境
     * 2、process方法放入环境，便于动态查询调用
     *
     * @param datasourceList 数据源
     * @return
     */
    public static Map<String, Object> readyJavaObjectAndArgs(List<? extends FlinkBaseDatasource> datasourceList) {
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
    public static Map<Object, List<Map<String, Object>>> tranDataByKey(String key, List<Map<String, Object>> data) {
        return data.stream().filter(x -> x.containsKey(key)).collect(Collectors.groupingBy(m -> m.get(key)));
    }

    public static Map<String, Object> deal(SelectDatasource selectDatasource,ProcessUtils processUtils, Map<String,Object> x){
        String student_number = (String) x.get("student_number");
        Map<String,String> search =new HashMap<>();
        search.put("studentNumber",student_number);
        List<Map<String, Object>> data = processUtils.getData(selectDatasource, search);
        x.put("id_card",data.get(0).get("id_card"));
        return x;
    }
}
