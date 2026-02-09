package com.step.jdbc.runtime.flink.dictionary.enums;

public enum DBType {
    /**
     * 数据库连接类型
     */
    MYSQL("jdbc:mysql://%s:%s/%s", "SELECT version()"),
    POSTGRES("jdbc:postgresql://%s:%s/%s", "SELECT version()"),
    ORACLE("jdbc:oracle:thin:@%s:%s:%s", "SELECT * FROM v$version"),
    ORACLE2("jdbc:oracle:thin:@%s:%s/%s", "SELECT * FROM v$version"),
    TDGENINE("jdbc:TAOS-RS://%s:%s/%s", "SHOW DATABASES"),
    ;

    DBType(String url, String testSQL) {
        this.url = url;
        this.testSQL = testSQL;
    }

    private final String url;
    private final String testSQL;

    public String getUrl() {
        return url;
    }

    public String getTestSQL() {
        return testSQL;
    }
}
