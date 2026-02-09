package com.step.jdbc.runtime.flink.dictionary.datasource;

import com.step.jdbc.runtime.flink.dictionary.enums.DSType;

import java.io.Serializable;

public class JoinDatasource extends FlinkBaseDatasource implements Serializable {
    public JoinDatasource(DSType type) {
        this.setDsType(type);
        if (type==DSType.FULL_JOIN){
            this.isFull=true;
        }
    }

    public JoinDatasource() {
        this.setDsType(DSType.UNION);
    }

    private boolean isFull;
    private String mainResource;
    private String mainKey;
    private String relationResource;
    private String relationKey;

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public String getMainResource() {
        return mainResource;
    }

    public void setMainResource(String mainResource) {
        this.mainResource = mainResource;
    }

    public String getMainKey() {
        return mainKey;
    }

    public void setMainKey(String mainKey) {
        this.mainKey = mainKey;
    }

    public String getRelationResource() {
        return relationResource;
    }

    public void setRelationResource(String relationResource) {
        this.relationResource = relationResource;
    }

    public String getRelationKey() {
        return relationKey;
    }

    public void setRelationKey(String relationKey) {
        this.relationKey = relationKey;
    }
}
