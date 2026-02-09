package com.step.jdbc.runtime.txedit.model;

import java.sql.Connection;
import java.sql.Statement;

/**
 * 保存在服务器端的用户编辑
 * @author : Sun
 * @date : 2023/1/28  10:40
 */
public class UserTx {
    /**
     * 用户的数据库连接
     */
    private Connection connection;
    /**
     * sql查询器
     */
    private Statement stat;
    public UserTx(Connection connection, Statement stat) {
        this.connection = connection;
        this.stat = stat;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStat() {
        return stat;
    }

    public void setStat(Statement stat) {
        this.stat = stat;
    }
}
