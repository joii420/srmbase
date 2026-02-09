package com.step.jdbc.runtime.txedit.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2023/3/11  15:59
 */
@DataObject(generateConverter = true ,inheritConverter = true)
public class TransactionSql {

    /**
     * 要执行的sql
     */
    private List<String> executeSqlList;
    /**
     * 要校验的sql
     */
    private List<String> verifySqlList;

    public TransactionSql() {
    }

    public TransactionSql(List<String> executeSqlList, List<String> verifySqlList) {
        this.executeSqlList = executeSqlList.stream().distinct().collect(Collectors.toList());
        this.verifySqlList = verifySqlList.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getExecuteSqlList() {
        return executeSqlList;
    }

    public void setExecuteSqlList(List<String> executeSqlList) {
        this.executeSqlList = executeSqlList;
    }

    public List<String> getVerifySqlList() {
        return verifySqlList;
    }

    public void setVerifySqlList(List<String> verifySqlList) {
        this.verifySqlList = verifySqlList;
    }

    public TransactionSql(JsonObject jsonObject) {
    	TransactionSqlConverter.fromJson(jsonObject, this);
    }
    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	TransactionSqlConverter.toJson(this, json);
    	return json;
    }
}
