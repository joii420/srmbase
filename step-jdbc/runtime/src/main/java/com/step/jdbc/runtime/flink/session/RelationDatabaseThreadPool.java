package com.step.jdbc.runtime.flink.session;

import com.step.jdbc.runtime.param.JdbcParam;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.session.JdbcSession;
import com.step.jdbc.runtime.session.JdbcSessionBuilder;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RelationDatabaseThreadPool {
    //    private final String jdbcUrl;
//    private final String username;
//    private final String password;
    private int poolSize;
    private BlockingQueue<JdbcSession> connectionPool;

    private JdbcParam jdbcParam;
    private JdbcOption option;

    AtomicInteger atomicInteger = new AtomicInteger(1);

    public RelationDatabaseThreadPool(JdbcParam jdbcParam, JdbcOption option, int poolSize) {
//        this.jdbcUrl = jdbcUrl;
//        this.username = username;
//        this.password = password;
        this.jdbcParam = jdbcParam;
        this.option = option;
        this.poolSize = poolSize;
        this.connectionPool = new LinkedBlockingQueue<>(1000);

        initializeConnectionPool();
    }

    private void initializeConnectionPool() {
        for (int i = 0; i < poolSize; i++) {
            try {
//                Connection connection = createConnection();
                connectionPool.offer(createConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JdbcSession createConnection() throws SQLException {
        return JdbcSessionBuilder.buildSession(this.jdbcParam, this.option);
    }

    public JdbcSession getConnection() {
        try {
            if (connectionPool.isEmpty() && atomicInteger.incrementAndGet() <= 1000) {
                connectionPool.offer(JdbcSessionBuilder.buildSession(this.jdbcParam, this.option));
            }
            return connectionPool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void releaseConnection(JdbcSession connection) {
        if (connection != null && connection.isActive()) {
            connectionPool.offer(connection);
        }
    }

    public void shutdown() {
        System.out.println("close size pool: " + connectionPool.size());
        for (JdbcSession connection : connectionPool) {
            connection.close();
        }
        connectionPool.clear();
        connectionPool = null;
    }

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/mydb";
        String username = "username";
        String password = "password";
        int poolSize = 5;

//        RelationDatabaseThreadPool threadPool = new RelationDatabaseThreadPool(jdbcUrl, username, password, poolSize);
//
//        // Example usage
//        Connection connection = threadPool.getConnection();
//        // Perform database operations using the connection
//        // ...
//
//        // Release the connection back to the pool
//        threadPool.releaseConnection(connection);
//
//        // Shutdown the pool when done
//        threadPool.shutdown();
    }
}
