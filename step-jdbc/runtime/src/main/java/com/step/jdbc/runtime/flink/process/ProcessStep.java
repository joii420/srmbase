package com.step.jdbc.runtime.flink.process;


import com.step.jdbc.runtime.flink.dictionary.datasource.FlinkBaseDatasource;

/**
 * 聚合
 */
public class ProcessStep {

    private Long id;

    private String group;

    private Integer num; //顺序号

    private FlinkProcessType flinkProcessType; //类型

    private FlinkProcess flinkProcess;

    public ProcessStep(Long id, String group, Integer num, FlinkProcessType flinkProcessType, FlinkBaseDatasource processDatasource) {
        this.id = id;
        this.group = group;
        this.num = num;
        this.flinkProcessType = flinkProcessType;
//        this.flinkProcess = flinkProcess;
    }

    public ProcessStep() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public FlinkProcessType getFlinkProcessType() {
        return flinkProcessType;
    }

    public void setFlinkProcessType(FlinkProcessType flinkProcessType) {
        this.flinkProcessType = flinkProcessType;
    }

    public void setFlinkProcess(FlinkProcess flinkProcess) {
        this.flinkProcess = flinkProcess;
    }

    public FlinkProcess getFlinkProcess() {
        return flinkProcess;
    }
}
