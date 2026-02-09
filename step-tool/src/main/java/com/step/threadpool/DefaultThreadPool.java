package com.step.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
class DefaultThreadPool implements ThreadPool {
    private final Logger LOGGER = LoggerFactory.getLogger(DefaultThreadPool.class);
    private final String prefix;
    private final int coreSize;
    private final int maxSize;
    private final int queueSize;
    private final LocalDateTime createdAt;
    private LocalDateTime activedAt;
    private final ThreadPoolExecutor pool;

    DefaultThreadPool() {
        this("Default-Thread", CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_SIZE);
    }

    DefaultThreadPool(String prefix) {
        this(prefix, CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_SIZE);
    }

    DefaultThreadPool(String prefix, int coreSize) {
        this(prefix, coreSize, MAX_POOL_SIZE, QUEUE_SIZE);
    }

    DefaultThreadPool(String prefix, int coreSize, int maxSize) {
        this(prefix, coreSize, maxSize, QUEUE_SIZE);
    }

    DefaultThreadPool(String prefix, int coreSize, int maxSize, int queueSize) {
        this.prefix = prefix;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.queueSize = queueSize;
        this.createdAt = LocalDateTime.now();
        this.pool = new ThreadPoolExecutor(
                this.coreSize,
                this.maxSize,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(this.queueSize),
                new ThreadFactory() {
                    private int count = 0;

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, DefaultThreadPool.this.prefix + "-" + count++);
                        thread.setUncaughtExceptionHandler((t, e) -> {
                            String threadName = t.getName();
                            LOGGER.error(DefaultThreadPool.this.prefix + "- Pool error occurred! threadName: {}, error msg: {}", threadName, e.getMessage(), e);
                        });
                        return thread;
                    }
                },
                (r, executor) -> {
                    if (!executor.isShutdown()) {
                        LOGGER.warn(this.prefix + "- Pool is too busy! waiting to insert task to queue! ");
                    }
                }
        );
    }


    @Override
    public void execute(Runnable runnable) {
        this.activedAt = LocalDateTime.now();
        this.pool.execute(runnable);
    }

    @Override
    public int getCurrentSize() {
        return this.pool.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return this.pool.getActiveCount();
    }

}
