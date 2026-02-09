package com.step.threadpool;

/**
 * @author : Sun
 * @date : 2024/3/19  18:51
 */
public interface ThreadPool {
    int CORE_POOL_SIZE = 8;
    int MAX_POOL_SIZE = 16;
    int QUEUE_SIZE = 1 << 5;

    static ThreadPool createPool() {
        return new DefaultThreadPool();
    }

    static ThreadPool createPool(String name) {
        return new DefaultThreadPool(name);
    }

    static ThreadPool createPool(String prefix, int coreSize, int maxSize) {
        return new DefaultThreadPool(prefix, coreSize, maxSize);
    }

    void execute(Runnable runnable);

    int getCurrentSize();

    int getActiveCount();
}
