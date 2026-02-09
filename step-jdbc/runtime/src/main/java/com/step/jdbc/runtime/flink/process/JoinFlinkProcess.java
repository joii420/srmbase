package com.step.jdbc.runtime.flink.process;

public class JoinFlinkProcess extends FlinkProcess {
    //合并识别键
   private String key;

   private String resourceNames; //逗号隔开


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResourceNames() {
        return resourceNames;
    }

    public void setResourceNames(String resourceNames) {
        this.resourceNames = resourceNames;
    }
}
