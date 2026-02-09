package com.step.jdbc.runtime.flink.process;

/**
 * filter操作
 */
public class FilterFlinkProcess extends FlinkProcess {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
