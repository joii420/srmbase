package com.step.jdbc.runtime.flink.process;

public class UnionFlinkProcess extends FlinkProcess {
    //合并识别键
   private String key;

   private boolean isMain;

   private String resourceNames; //逗号隔开

    private String name;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResourceNames() {
        return resourceNames;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceNames(String resourceNames) {
        this.resourceNames = resourceNames;
    }
}
