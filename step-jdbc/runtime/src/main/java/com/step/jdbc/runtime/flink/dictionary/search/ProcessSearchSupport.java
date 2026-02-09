package com.step.jdbc.runtime.flink.dictionary.search;

import com.step.jdbc.runtime.flink.dictionary.database.ProcessDatabase;
import com.step.jdbc.runtime.flink.dictionary.datasource.SelectDatasource;
import com.step.tool.utils.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProcessSearchSupport {

    List<Map<String, Object>> searchData(SelectDatasource processDatasource, ProcessDatabase processDatabase, Map<String, String> datas);

    void execute(SelectDatasource processDatasource, ProcessDatabase processDatabase, Map<String, String> datas);

    default String replacePlaceholders(String sql, Map<String, String> datas) {
        Map<String, Object> dataMap = new HashMap<>(datas);
        if (CollectionUtil.isEmpty(dataMap)) {
            return sql;
        }
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            if (sql.contains(placeholder)) {
                String value = "" + entry.getValue();
                value = "null".equals(value) ? "" : value;
                sql = sql.replace(placeholder, value);
            }
        }
        return sql;
    }
}
