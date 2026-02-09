package com.step.jdbc.runtime.param;

import com.step.api.runtime.exception.base.BaseException;
import com.step.tool.utils.StringUtil;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author : Sun
 * @date : 2023/1/28  10:10
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class JdbcParam {
    /**
     * 连接url
     */
    private String url;
    /**
     * 主机IP
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 数据库
     */
    private String database;
    /**
     * 数据库类型
     */
    private DBType dbType;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;

    public String getUrl() {
        if (StringUtil.isNotEmpty(this.url)) {
            return url;
        }
        if (StringUtil.isNotEmpty(this.host, this.port, this.database)) {
            if (this.dbType == null) {
                throw new BaseException("Failed to init connection URL param").record();
            }
            String urlTemplate = dbType.getUrlTemplate();
            return String.format(urlTemplate, this.host, this.port, this.database);
        }
        throw new BaseException("Failed to init connection URL param").record();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public DBType getDbType() {
        if (this.dbType == null) {
            return DBType.POSTGRESQL;
        }
        return dbType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Deprecated
    public JdbcParam() {

    }

    JdbcParam(DBType dbType) {
        this.dbType = dbType;
        this.port = dbType.getDefaultPort();
    }

    public static JdbcParam create(DBType dbType, String host, int port, String database, String username, String password) {
        JdbcParam jdbcParam = create(dbType, host, database, username, password);
        jdbcParam.setPort(port);
        return jdbcParam;
    }

    public static JdbcParam create(DBType dbType, String host, String database, String username, String password) {
        JdbcParam jdbcParam = new JdbcParam(dbType);
        jdbcParam.setHost(host);
        jdbcParam.setDatabase(database);
        jdbcParam.setUsername(username);
        jdbcParam.setPassword(password);
        return jdbcParam;
    }

    public static JdbcParam postgres(String host, int port, String database, String username, String password) {
        JdbcParam jdbcParam = postgres(host, database, username, password);
        jdbcParam.setPort(port);
        return jdbcParam;
    }

    public static JdbcParam postgres(String host, String database, String username, String password) {
        JdbcParam jdbcParam = new JdbcParam(DBType.POSTGRESQL);
        jdbcParam.setHost(host);
        jdbcParam.setDatabase(database);
        jdbcParam.setUsername(username);
        jdbcParam.setPassword(password);
        return jdbcParam;
    }

    public static JdbcParam mysql(String host, int port, String database, String username, String password) {
        JdbcParam jdbcParam = mysql(host, database, username, password);
        jdbcParam.setPort(port);
        return jdbcParam;
    }

    public static JdbcParam mysql(String host, String database, String username, String password) {
        JdbcParam jdbcParam = new JdbcParam(DBType.MYSQL);
        jdbcParam.setHost(host);
        jdbcParam.setDatabase(database);
        jdbcParam.setUsername(username);
        jdbcParam.setPassword(password);
        return jdbcParam;
    }

    public JdbcParam(JsonObject jsonObject) {
        JdbcParamConverter.fromJson(jsonObject, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JdbcParamConverter.toJson(this, json);
        return json;
    }
}
