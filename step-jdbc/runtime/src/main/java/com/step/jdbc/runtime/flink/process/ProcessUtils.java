package com.step.jdbc.runtime.flink.process;


import com.step.jdbc.runtime.flink.dictionary.database.ProcessDatabase;
import com.step.jdbc.runtime.flink.dictionary.datasource.FlinkBaseDatasource;
import com.step.jdbc.runtime.flink.dictionary.datasource.JoinDatasource;
import com.step.jdbc.runtime.flink.dictionary.datasource.SelectDatasource;
import com.step.jdbc.runtime.flink.dictionary.datasource.UnionDatasource;
import com.step.jdbc.runtime.flink.dictionary.enums.DSType;
import com.step.jdbc.runtime.flink.dictionary.search.ProcessSearchSupport;
import com.step.jdbc.runtime.flink.dictionary.search.RelationalSearch;
import com.step.jdbc.runtime.flink.dictionary.search.TDengineSearch;
import com.step.jdbc.runtime.flink.exception.FlinkDataException;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessUtils implements Serializable {

    private RelationalSearch relationalSearch;
    private TDengineSearch tDengineSearch;

    public ProcessUtils() {
        relationalSearch = new RelationalSearch();
        tDengineSearch = new TDengineSearch();
    }

    public void initDatasource(List<? extends FlinkBaseDatasource> datasourceList, Map<String, String> datas) {
        //校验循环依赖和排序

        datasourceList.parallelStream().filter(FlinkBaseDatasource::isInit).forEach(ds -> {
            //先初始化SELECT的数据
            if (ds instanceof SelectDatasource selectDatasource) {
                long start = System.currentTimeMillis();

                List<Map<String, Object>> maps = initData(selectDatasource, datas);
                long end = System.currentTimeMillis();
                System.out.println("join times name:" + ((SelectDatasource) ds).getName() + "   " + (end - start));
                selectDatasource.setData(maps);
            }
        });
        Set<Integer> orderSet = datasourceList.stream()
                .filter(ds -> ds.isInit() && ds.getDsType() != DSType.SELECT)
                .sorted(Comparator.comparingInt(FlinkBaseDatasource::getOrder))
                .map(FlinkBaseDatasource::getOrder)
                .collect(Collectors.toSet());
        orderSet.forEach(order -> {
            initDatasource(order, datasourceList);
        });
    }

    private void initDatasource(Integer order, List<? extends FlinkBaseDatasource> datasourceList) {
        Map<String, List<Map<String, Object>>> dataMap = datasourceList.stream().filter(ds -> ds.isInit() && ds.getData() != null).collect(Collectors.toMap(FlinkBaseDatasource::getName, FlinkBaseDatasource::getData));
        datasourceList.stream().filter(ds -> ds.isInit() && Objects.equals(ds.getOrder(), order)).forEach(ds -> {
            //在初始化UNION的数据
            if (ds instanceof UnionDatasource unionDatasource) {
                List<Map<String, Object>> data = new ArrayList<>();
                List<String> selectDatasourceName = unionDatasource.getDatasourceName();
                selectDatasourceName.forEach(name -> {
                    List<Map<String, Object>> maps = dataMap.getOrDefault(name, new ArrayList<>());
                    data.addAll(copy(maps));
                });
                unionDatasource.setData(data.stream().distinct().toList());
            } else if (ds instanceof JoinDatasource joinDatasource) {
                String mainResource = joinDatasource.getMainResource();
                String mainKey = joinDatasource.getMainKey();
                List<Map<String, Object>> mainMap = Optional.ofNullable(dataMap.get(mainResource)).orElseThrow(() -> new FlinkDataException("找不到有效的数据源: " + mainResource));
                List<Map<String, Object>> copyMainMap = copy(mainMap);
                String relationResource = joinDatasource.getRelationResource();
                List<Map<String, Object>> relationMap = Optional.ofNullable(dataMap.get(relationResource)).orElseThrow(() -> new FlinkDataException("找不到有效的数据源: " + relationResource));
                List<Map<String, Object>> copyRelationMap = copy(relationMap);
                String relationKey = joinDatasource.getRelationKey();
                Map<String, Map<String, Object>> relationResourceMap = copyRelationMap.stream().collect(Collectors.toMap(m -> "" + m.get(relationKey), m -> m));
                if (joinDatasource.isFull()) {
                    HashSet<String> usedKey = new HashSet<>();
                    copyMainMap.forEach(m -> {
                        String mainValue = "" + m.get(mainKey);
                        Map<String, Object> link = relationResourceMap.getOrDefault(mainValue, new HashMap<>(0));
                        usedKey.add(mainValue);
                        m.putAll(link);
                    });
                    usedKey.forEach(relationResourceMap::remove);
                    if (relationResourceMap.size() > 0) {
                        List<Map<String, Object>> copy = new ArrayList<>(copyMainMap);
                        copy.addAll(relationResourceMap.values());
                        copyMainMap = copy;
                    }
                } else {
                    copyMainMap.forEach(m -> {
                        String mainValue = "" + m.get(mainKey);
                        Map<String, Object> link = relationResourceMap.getOrDefault(mainValue, new HashMap<>(0));
                        m.putAll(link);
                    });
                }
                joinDatasource.setData(copyMainMap);
            }
        });
    }

    private List<Map<String, Object>> copy(List<Map<String, Object>> list) {
        return list.stream().map(this::copy).toList();
    }

    private Map<String, Object> copy(Map<String, Object> map) {
        return new HashMap<>(map);
    }

    /**
     * 获取单个数据源的内容信息
     *
     * @param datas
     * @return
     */
    private List<Map<String, Object>> initData(SelectDatasource processDatasource, Map<String, String> datas) {
        ProcessSearchSupport finalDatabase;
        ProcessDatabase processDatabase = processDatasource.getProcessDatabase();
        switch (processDatabase.getType()) {
            case TDGENINE:
                finalDatabase = tDengineSearch;
                break;
            default:
                finalDatabase = relationalSearch;
        }
        return finalDatabase.searchData(processDatasource, processDatabase, datas);
    }


    public List<Map<String, Object>> getData(SelectDatasource processDatasource, Map<String, String> datas) {
        ProcessSearchSupport finalDatabase = null;
        ProcessDatabase processDatabase = processDatasource.getProcessDatabase();
        switch (processDatabase.getType()) {
            case TDGENINE:
                finalDatabase = tDengineSearch;
                break;
            default:
                finalDatabase = relationalSearch;
        }
        return finalDatabase.searchData(processDatasource, processDatabase, datas);
    }

    public void execute(SelectDatasource processDatasource, Map<String, String> datas) {
        ProcessSearchSupport finalDatabase = null;
        ProcessDatabase processDatabase = processDatasource.getProcessDatabase();
        switch (processDatabase.getType()) {
            case TDGENINE:
                finalDatabase = tDengineSearch;
                break;
            default:
                finalDatabase = relationalSearch;
        }
        finalDatabase.execute(processDatasource, processDatabase, datas);
    }
}
