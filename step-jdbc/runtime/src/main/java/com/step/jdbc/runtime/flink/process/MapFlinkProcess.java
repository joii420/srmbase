package com.step.jdbc.runtime.flink.process;

import java.util.Map;

/**
 * 1、数据源，a、b、c，标记哪个是主的数据
 * 2、数据合并逻辑 filter、map、unin等
 * 3、合并后是否需要其他逻辑操作
 */
public class MapFlinkProcess extends FlinkProcess {

    private String content; //脚本

    private Map<String, Object> result;   //每一条数据的结果

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public FlinkProcessType getFlinkProcessType() {
        return FlinkProcessType.MAP;
    }
}
