package com.step.jdbc.runtime.flink.dictionary.datasource;


import com.step.jdbc.runtime.flink.dictionary.enums.DSType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FlinkBaseDatasource implements Serializable {

    private Long id;

    private String groupId;

    private DSType dsType;

    private  String name;

    private List<Map<String, Object>> data;

    private  boolean isMain; //是否为主表

    private  boolean isInit;//是否初始化

    private Integer order;

    private String resultFields[];  //查询需要的字段

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public DSType getDsType() {
        return dsType;
    }

    public void setDsType(DSType dsType) {
        this.dsType = dsType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public String[] getResultFields() {
        return resultFields;
    }

    public void setResultFields(String[] resultFields) {
        this.resultFields = resultFields;
    }
}
