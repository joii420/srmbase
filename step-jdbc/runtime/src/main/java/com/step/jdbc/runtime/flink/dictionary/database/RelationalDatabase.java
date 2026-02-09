package com.step.jdbc.runtime.flink.dictionary.database;

import com.step.jdbc.runtime.param.JdbcParam;

public class RelationalDatabase extends ProcessDatabase {
    private String url;
    private String databaseId;
    private String driverType;
    private String driver;
    private String username;
    private String password;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabaseId() {
        return this.databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDriverType() {
        return this.driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getDriver() {
        return this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RelationalDatabase() {
        this.databaseId = "default";
    }

    public JdbcParam tranJdbcParm(){
        JdbcParam jdbcParam =new JdbcParam();
        jdbcParam.setPassword(password);
        jdbcParam.setUrl(url);
        jdbcParam.setUsername(username);
        return jdbcParam;
    }

    public RelationalDatabase(String url, String databaseId, String driverType, String driver, String username, String password) {
        this.url = url;
        this.databaseId = databaseId;
        this.driverType = driverType;
        this.driver = driver;
        this.username = username;
        this.password = password;
    }

}
