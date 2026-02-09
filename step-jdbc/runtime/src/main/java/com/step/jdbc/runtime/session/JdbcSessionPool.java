package com.step.jdbc.runtime.session;

import com.step.api.runtime.exception.base.BaseException;
import com.step.jdbc.runtime.param.DBType;
import com.step.jdbc.runtime.param.JdbcOption;
import com.step.jdbc.runtime.param.JdbcParam;
import com.step.logger.LOGGER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class JdbcSessionPool {
    public static final Logger log = LoggerFactory.getLogger(JdbcSessionPool.class);
    private final String poolId;
    private final int initSize;
    private final JdbcOption option;
    private final BlockingQueue<JdbcSession> connectionPool;
    private final JdbcParam jdbcParam;
    private LocalDateTime createAt;
    private LocalDateTime activeAt;
    private boolean active;
    private int minPoolSize;

    AtomicInteger atomicInteger = new AtomicInteger(1);

    JdbcSessionPool(JdbcParam jdbcParam, JdbcOption option, int poolSize) {
        this.poolId = UUID.randomUUID().toString().replace("-", "");
        this.jdbcParam = jdbcParam;
        this.option = option;
        this.initSize = poolSize;
        this.connectionPool = new LinkedBlockingQueue<>(1000);
        this.createAt = LocalDateTime.now();
        this.active = true;
        this.minPoolSize = Math.max(poolSize, 1);
        refreshActive();
        initializeConnectionPool();
        JdbcSessionManager.recordPool(this);
    }

    protected void closeFreeSession(long now) {
        if (!this.active) {
            this.shutdown();
            return;
        }
        int clearStart = this.getCurrentPoolSize();
        int poolSize = this.getCurrentPoolSize();
        while (this.getCurrentPoolSize() > minPoolSize && --poolSize > 0) {
            JdbcSession session = this.getSession();
            session.closeFreeSession(now);
            if (session.isActive()) {
                releaseSession(session);
            }
        }
        int clearEnd = this.getCurrentPoolSize();
//        if (clearStart != clearEnd) {
        log.info("JdbcCleaner: clean sessionPool: {} | {} -> {}", this.poolId, clearStart, clearEnd);
//        }
    }

    private void initializeConnectionPool() {
        checkActive(false);
        for (int i = 0; i < initSize; i++) {
            JdbcSession session = createConnection();
            if (!connectionPool.offer(session)) {
                log.warn("Failed to offer session to connectionPool!!!!!!!");
            }
        }
    }

    private JdbcSession createConnection() {
        checkActive(false);
        return JdbcSessionBuilder.buildSession(this.jdbcParam, this.option, this.poolId);
    }

    private synchronized void checkActive(boolean isShutdown) {
        if (!this.active) {
            throw new BaseException("This sessionPool is not activity!!!").record();
        }
        if (isShutdown) {
            this.active = false;
        }
    }

    public JdbcSession getSession() {
        checkActive(false);
        refreshActive();
        try {
//            if (connectionPool.isEmpty() && atomicInteger.incrementAndGet() <= 1000) {
            if (connectionPool.isEmpty()) {
                JdbcSession session = createConnection();
                log.info("Create session: {} | {}", session.getPoolName(), session.getSessionId());
                if (!connectionPool.offer(session)) {
                    log.warn("Failed to offer session to connectionPool!!!!!!!");
                    return session;
                } else {
//                    log.info("Offer session: {} | {}", session.getPoolName(), session.getSessionId());
                }
            }
            JdbcSession session = connectionPool.take();
//            log.info("Take session: {} | {}", session.getPoolName(), session.getSessionId());
            if (!session.isActive()) {
                session.close();
                log.warn("Retrieve session: {} | {}", session.getPoolName(), session.getSessionId());
                session = getSession();
            }
            return session;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Get session error: {}", e.getMessage());
            return createConnection();
        }
    }

    public void releaseSession(JdbcSession session) {
        if (session != null && session.isActive()) {
            if (!this.active) {
                log.warn("This sessionPool is already closed: " + this.poolId);
                session.close();
                return;
            }
            if (connectionPool.size() >= 1000) {
                log.warn("This connectionPool is full: " + this.poolId);
                session.close();
                return;
            }
            if (!connectionPool.offer(session)) {
                log.warn("Failed to offer session to connectionPool!!!!!!!");
                return;
            }
//            log.info("Offer session: {} | {}", session.getPoolName(), session.getSessionId());
        } else {
            log.warn("Failed to offer session to connectionPool, because session is not active!");
        }
    }

    public <R> R useSession(Function<JdbcSession, R> sessionConsumer) {
        checkActive(false);
        JdbcSession jdbcSession = getSession();
        try {
            R apply = sessionConsumer.apply(jdbcSession);
            if (jdbcSession.isActive() && jdbcSession.hasUnCommit()) {
                jdbcSession.commit();
                log.warn("JdbcSessionPool not support transaction, please don't use function with unCommit()");
            }
            return apply;
        } finally {
            releaseSession(jdbcSession);
        }
    }

    public void useSession(Consumer<JdbcSession> sessionConsumer) {
        checkActive(false);
        JdbcSession jdbcSession = getSession();
        try {
            sessionConsumer.accept(jdbcSession);
            if (jdbcSession.isActive() && jdbcSession.hasUnCommit()) {
                jdbcSession.commit();
                log.warn("JdbcSessionPool not support transaction, please don't use function with unCommit()");
            }
        } finally {
            releaseSession(jdbcSession);
        }
    }

    public void shutdown() {
        checkActive(true);
        log.info("close size pool: " + connectionPool.size());
        for (BaseCloseConnectJdbcSession connection : connectionPool) {
            connection.close();
        }
        connectionPool.clear();
        JdbcSessionManager.removePool(this);
    }

    public String getPoolId() {
        return poolId;
    }

    private void refreshActive() {
        this.activeAt = LocalDateTime.now();
    }

    public LocalDateTime getActiveAt() {
        return activeAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public int getCurrentPoolSize() {
        return this.connectionPool.size();
    }

    public DBType getType() {
        return this.jdbcParam.getDbType();
    }

    public String getDatabase() {
        return this.jdbcParam.getDatabase();
    }
}
