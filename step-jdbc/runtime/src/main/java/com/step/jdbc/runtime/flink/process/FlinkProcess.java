package com.step.jdbc.runtime.flink.process;

import java.io.Serializable;

public class FlinkProcess implements Serializable {
    FlinkProcessType flinkProcessType;

    public FlinkProcessType getFlinkProcessType() {
        return flinkProcessType;
    }
}
