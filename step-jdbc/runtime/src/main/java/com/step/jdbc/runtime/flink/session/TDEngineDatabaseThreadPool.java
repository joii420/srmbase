package com.step.jdbc.runtime.flink.session;

import com.step.jdbc.runtime.flink.dictionary.database.tdengine.TDengineDatabase;
import com.step.jdbc.runtime.flink.dictionary.utils.TDengineUtils;
import com.step.jdbc.runtime.session.JdbcSessionBuilder;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TDEngineDatabaseThreadPool {
    private  int poolSize;
    private  BlockingQueue<TDengineUtils> connectionPool;

    private TDengineDatabase tDengineDatabase;
    AtomicInteger atomicInteger = new AtomicInteger(1);

    public TDEngineDatabaseThreadPool(TDengineDatabase tDengineDatabase ,int poolSize) {
//        this.jdbcUrl = jdbcUrl;
//        this.username = username;
//        this.password = password;
        this.tDengineDatabase = tDengineDatabase;
        this.poolSize = poolSize;
        this.connectionPool = new LinkedBlockingQueue<>(1000);

        initializeConnectionPool();
    }

    private void initializeConnectionPool() {
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionPool.offer(createConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private TDengineUtils createConnection() throws SQLException {
        return  new TDengineUtils(tDengineDatabase);
    }

    public TDengineUtils getConnection() {
        try {
            if (connectionPool.isEmpty() && atomicInteger.incrementAndGet() <= 1000) {
                connectionPool.offer(createConnection());
            }
            return connectionPool.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseConnection(TDengineUtils connection) {
        if (connection != null) {
            connectionPool.offer(connection);
        }
    }

    public void shutdown() {
        System.out.println("close size pool: " +connectionPool.size());
        for (TDengineUtils connection : connectionPool) {
                connection.close();
        }
        connectionPool.clear();
        connectionPool=null;
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
