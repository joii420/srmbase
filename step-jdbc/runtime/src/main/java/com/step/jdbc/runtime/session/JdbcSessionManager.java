package com.step.jdbc.runtime.session;

import cn.hutool.core.thread.ThreadUtil;
import com.step.jdbc.runtime.param.DBType;
import com.step.logger.LOGGER;
import com.step.threadpool.ThreadPool;
import com.step.tool.utils.StringUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author : Sun
 * @date : 2023/8/15  8:39
 */
public class JdbcSessionManager {
    public static final Logger log = LoggerFactory.getLogger(JdbcSessionManager.class);
    private static final Map<String, JdbcSession> SESSION_MAP;
    private static final Map<String, JdbcSessionPool> POOL_MAP;
    private static final ThreadPool POOL;
    private static final boolean OPEN_CLEANER;

    static {
        SESSION_MAP = new ConcurrentHashMap<>(4);
        POOL_MAP = new ConcurrentHashMap<>(4);
        POOL = ThreadPool.createPool("JDBC-Cleaner");
        OPEN_CLEANER = true;
        POOL.execute(() -> {
            while (OPEN_CLEANER) {
                ThreadUtil.safeSleep(10 * 60 * 1000);
                long now = System.currentTimeMillis();
                POOL_MAP.values().forEach(pool -> pool.closeFreeSession(now));
                int clearStart = SESSION_MAP.size();
                SESSION_MAP.values().stream().filter(session -> StringUtil.isEmpty(session.mountPool) || "COMMON".equals(session.mountPool)).forEach(session -> session.closeFreeSession(now));
                int clearEnd = SESSION_MAP.size();
                if (clearStart != clearEnd) {
                    log.info("JdbcCleaner: clean session {} -> {}", clearStart, clearEnd);
                }
            }
        });
    }

    public static void recordSession(JdbcSession jdbcSession) {
        SESSION_MAP.put(jdbcSession.getSessionId(), jdbcSession);
    }

    public static void removeSession(String sessionId) {
        SESSION_MAP.remove(sessionId);
        log.info(String.format("关闭会话: %s %n", sessionId));
    }

    public static void forceRemoveSession(String sessionId) {
        log.info("强制关闭连接: " + sessionId);
        JdbcSession jdbcSession = SESSION_MAP.get(sessionId);
        if (jdbcSession != null) {
            jdbcSession.close();
            log.info("关闭连接成功: " + sessionId);
            SESSION_MAP.remove(sessionId);
        } else {
            log.info("链接不存在: " + sessionId);
        }
    }

    public static void recordPool(JdbcSessionPool pool) {
        POOL_MAP.put(pool.getPoolId(), pool);
    }

    public static void removePool(JdbcSessionPool pool) {
        POOL_MAP.remove(pool.getPoolId());
    }

    public static void forceRemovePool(String poolId) {
        JdbcSessionPool pool = POOL_MAP.get(poolId);
        if (pool != null) {
            pool.shutdown();
            POOL_MAP.remove(poolId);
        }
    }

    public static Map<String, PoolView> viewPool() {
        return POOL_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new PoolView(e.getValue())));
    }

    public static Set<String> viewSession() {
        return SESSION_MAP.keySet();
    }

    @Data
    public static class PoolView {
        private String id;
        private DBType dbType;
        private String database;
        private Integer size;
        private LocalDateTime createAt;
        private LocalDateTime activeAt;

        public PoolView(JdbcSessionPool pool) {
            this.id = pool.getPoolId();
            this.size = pool.getCurrentPoolSize();
            this.createAt = pool.getCreateAt();
            this.activeAt = pool.getActiveAt();
            this.dbType = pool.getType();
            this.database = pool.getDatabase();
        }
    }

}
