package com.step.jdbc.runtime.flink.dictionary.datasource;


import com.step.jdbc.runtime.flink.dictionary.enums.DSType;

import java.io.Serializable;
import java.util.List;

public class UnionDatasource extends FlinkBaseDatasource implements Serializable {

    public UnionDatasource() {
        this.setDsType(DSType.UNION);
    }

    private List<String> datasourceName;

    public List<String> getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(List<String> datasourceName) {
        this.datasourceName = datasourceName;
    }
}
