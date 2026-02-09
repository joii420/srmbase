package com.step.web.runtime.micro;

import com.step.api.runtime.annotation.MicroServer;
import com.step.api.runtime.exception.base.BaseException;
import com.step.web.runtime.register.VertxRegister;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.impl.VertxThread;
import io.vertx.mutiny.core.Vertx;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author : Sun
 * @date : 2023/1/31  10:32
 */

@Singleton
public class MicroProvider {

    @Inject
    Vertx vertx;
    @Inject
    VertxRegister register;
    private static final Logger LOGGER = LoggerFactory.getLogger("MICRO-SERVER");
    private static final int DEFAULT_CORE_POOL_SIZE = 1 << 7;
    private static final int DEFAULT_MAX_POOL_SIZE = 1 << 8;
    private static final int DEFAULT_QUEUE_SIZE = 1 << 10;
    private static final String PREFIX = "Micro-Server-Thread";
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            DEFAULT_CORE_POOL_SIZE,
            DEFAULT_MAX_POOL_SIZE,
            60,
            TimeUnit.SECONDS,
            new LinkedTransferQueue<>(),
            new ThreadFactory() {
                private int count = 0;

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, PREFIX + " - " + count++);
                    thread.setUncaughtExceptionHandler((t, e) -> {
                        String threadName = t.getName();
                        LOGGER.error(PREFIX + "- Pool error occurred! threadName: {}, error msg: {}", threadName, e.getMessage(), e);
                    });
                    return thread;
                }
            },
            (r, executor) -> {
                if (!executor.isShutdown()) {
                    LOGGER.warn(PREFIX + "- Pool is too busy! waiting to insert task to queue! ");
                }
            }
    );

    public MicroProvider() {

    }

    public <T> T createInstance(Class<T> asClazz) {
        String address = getAddress(asClazz);
        return (new ServiceProxyBuilder(vertx.getDelegate())).setAddress(address).build(asClazz);
    }

    public <T> T createInstance(String serve, Class<T> asClazz) {
        String address = register.MICRO_GROUP + "." + serve + "." + asClazz.getSimpleName();
        return (new ServiceProxyBuilder(vertx.getDelegate())).setAddress(address).build(asClazz);
    }

    private <T> String getAddress(Class<T> asClazz) {
        MicroServer microServer = asClazz.getDeclaredAnnotation(MicroServer.class);
        String serve = register.REGISTRY_SERVE;
        if (register.IS_CLUSTER && microServer != null && !"".equals(microServer.value().getServerName())) {
            serve = microServer.value().getServerName();
        }
        return register.MICRO_GROUP + "." + serve + "." + asClazz.getSimpleName();
    }

    /**
     * 阻塞回调异步结果
     *
     * @param future         执行方法块
     * @param <T>            结果类型
     * @param timeoutSeconds 阻塞时间
     * @return 回调结果
     */
    public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> future, long timeoutSeconds, Function<Throwable, T> failedComplete) {
        try {
            return toFuture(future, failedComplete).get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("请求异常", e);
            throw new BaseException(e);
        } catch (TimeoutException e) {
            LOGGER.error("读取请求结果超时", e);
            throw new BaseException(e);
        }
    }

    /* 阻塞回调异步结果
     *
     * @param future         执行方法块
     * @param <T>            结果类型
     * @param timeoutSeconds 阻塞时间
     * @return 回调结果
     */
    public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> future, long timeoutSeconds) {
        try {
            return toFuture(future, null).get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("请求异常", e);
            throw new BaseException(e);
        } catch (TimeoutException e) {
            LOGGER.error("读取请求结果超时", e);
            throw new BaseException(e);
        }
    }

    /**
     * 阻塞回调异步结果
     *
     * @param future 执行方法块
     * @param <T>    结果类型
     * @return 回调结果
     */
    public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> future, Function<Throwable, T> failedComplete) {
        return awaitResult(future, 10, failedComplete);
    }

    /**
     * 阻塞回调异步结果
     *
     * @param future 执行方法块
     * @param <T>    结果类型
     * @return 回调结果
     */
    public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> future) {
        return awaitResult(future, 10, null);
    }

    /**
     * 创建阻塞执行器
     */
    private static <T> Handler<AsyncResult<T>> createHandler(CompletableFuture<T> future, Function<Throwable, T> failedMsg) {
        return ar -> {
            if (ar.succeeded()) {
                future.complete(ar.result());
            } else {
                if (failedMsg != null) {
                    future.complete(failedMsg.apply(ar.cause()));
                } else {
                    future.completeExceptionally(ar.cause());
                }
            }
        };
    }

    /**
     * 创建阻塞回调
     */
    private static <T> CompletableFuture<T> toFuture(Consumer<Handler<AsyncResult<T>>> future, Function<Throwable, T> failedComplete) {
        CompletableFuture<T> blockingWait = new CompletableFuture<>();
        EXECUTOR.execute(() -> {
            checkVertxEventLoop();
            Handler<AsyncResult<T>> handler = createHandler(blockingWait, failedComplete);
            future.accept(handler);
        });
        return blockingWait;
    }

    /**
     * 校验线程是否允许发起异步回调
     */
    private static void checkVertxEventLoop() {
        if (isInVertxEventLoop()) {
            throw new BaseException("not support blocking call in vertx thread").record();
        }
    }

    private static boolean isInVertxEventLoop() {
        return Thread.currentThread() instanceof VertxThread thread && thread.isWorker();
    }
}
