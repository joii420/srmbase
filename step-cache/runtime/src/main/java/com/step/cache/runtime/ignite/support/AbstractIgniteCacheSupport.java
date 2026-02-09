package com.step.cache.runtime.ignite.support;

import cn.hutool.core.thread.ThreadUtil;
import com.step.api.runtime.exception.base.BaseException;
import com.step.api.runtime.model.ExpirePolicy;
import com.step.cache.runtime.exception.CacheErrorCode;
import com.step.tool.utils.JsonUtil;
import com.step.tool.utils.SystemUtil;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.client.*;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.expiry.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractIgniteCacheSupport {
    /**
     * 默认过期时间/秒
     */
    long DEFAULT_EXPIRE_TIME = 10;
    /**
     * 默认重试次数
     */
    int DEFAULT_RETRY_COUNT = 5;
    /**
     * 默认等待时长 单位毫秒
     */
    long DEFAULT_WAIT_TIME = 500;
    public final Logger log;
    ClientCache<Object, Object> cache;
    private final String cacheName;
    private final boolean hasExpired;
    //    Long timeOut;
    private final IgniteClient client;

    private Integer retryTimes;

    public AbstractIgniteCacheSupport(IgniteClient client, String cacheName) {
        this.client = client;
        this.cacheName = cacheName;
        this.hasExpired = false;
        log = LoggerFactory.getLogger(this.cacheName);
        cache = client.getOrCreateCache(this.cacheName);
        try {
            this.retryTimes = Integer.parseInt(SystemUtil.getValue("quarkus.cache.retry-times", "10"));
        } catch (NumberFormatException e) {
            this.retryTimes = 10;
        }
    }

    public AbstractIgniteCacheSupport(IgniteClient client, String cacheName, ExpirePolicy expirePolicy, Duration timeout) {
        this.client = client;
        this.cacheName = expirePolicy.name() + cacheName + "_" + timeout;
        this.hasExpired = true;
        log = LoggerFactory.getLogger(this.cacheName);
        ExpiryPolicy expiryPolicy = switch (expirePolicy) {
            case CREATED -> new CreatedExpiryPolicy(timeout);
            case MODIFY -> new ModifiedExpiryPolicy(timeout);
            case TOUCHED -> new TouchedExpiryPolicy(timeout);
            default -> new CreatedExpiryPolicy(timeout);
        };
        cache = client.getOrCreateCache(this.cacheName).withExpiryPolicy(expiryPolicy);
    }

    private <T> T executeWithRetry(Supplier<T> action) {
        int attempt = 0;
        while (attempt < retryTimes) {
            try {
                return action.get();
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                attempt++;
                if (attempt == retryTimes || !isRetryable(e)) {
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new BaseException("Retry interrupted").record();
                }
            }
        }
        return null;
    }

    private boolean isRetryable(Exception e) {
        String message = e.getMessage();
        return message != null && message.contains("Channel is closed");
    }

    //@Override
    public boolean removeAtomicLong(String key) {
        return Boolean.TRUE.equals(executeWithRetry(() -> {
            client.atomicLong(key, 1, true).close();
            return true;
        }));
    }

    //@Override
    public long increment(String key, long start, long quantity, boolean init) {
        return executeWithRetry(() -> {
            ClientAtomicLong clientAtomicLong = client.atomicLong(key, start, init);
            if (clientAtomicLong == null) {
                return start;
            }
            return clientAtomicLong.addAndGet(quantity);
        });
    }

    //@Override
    public Object get(Object key) {
        return executeWithRetry(() -> {
            return cache.get(key);
        });
    }

    //@Override
    public void set(Object key, Object val) {
        executeWithRetry(() -> {
            cache.put(key, val);
            return null;
        });
    }

    //@Override
    public boolean exist(Object key) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.containsKey(key)));
    }

    //@Override
    public boolean exist(Set<? extends Object> keys) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.containsKeys(keys)));
    }

    //@Override
    public Object getName() {
        return executeWithRetry(() -> cache.getName());
    }

    //@Override
    public Map<Object, Object> getAll(Set<? extends Object> keys) {
        return executeWithRetry(() -> cache.getAll(keys));
    }

    //@Override
    public void setAll(Map<? extends Object, ? extends Object> map) {
        executeWithRetry(() -> {
            cache.putAll(map);
            return true;
        });
    }

    //@Override
    public boolean replace(Object key, Object oldVal, Object newVal) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.replace(key, oldVal, newVal)));
    }

    //@Override
    public boolean replace(Object key, Object val) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.replace(key, val)));
    }

    //@Override
    public boolean delete(Object key) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.remove(key)));
    }

    //@Override
    public boolean delete(Object key, Object oldVal) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.remove(key, oldVal)));
    }

    //@Override
    public Set<String> getKeysByCondition(Function<String, Boolean> condition) {
        return executeWithRetry(() -> {
            List<Cache.Entry<Object, Object>> allKeys = cache.query(new ScanQuery<>()).getAll();
            return allKeys.stream().filter(c -> condition.apply("" + c.getKey())).map(Cache.Entry::getKey).map(Object::toString).collect(Collectors.toSet());
        });
    }

    //@Override
    public Map<String, Object> getKVByCondition(Function<String, Boolean> condition) {
        return executeWithRetry(() -> {
            List<Cache.Entry<Object, Object>> allKeys = cache.query(new ScanQuery<>()).getAll();
            return allKeys.stream().filter(c -> condition.apply("" + c.getKey())).collect(Collectors.toMap(c -> c.getKey().toString(), Cache.Entry::getValue));
        });
    }

    //@Override
    public void deleteByCondition(Function<String, Boolean> condition) {
        executeWithRetry(() -> {
            List<Cache.Entry<Object, Object>> allKeys = cache.query(new ScanQuery<>()).getAll();
            Set<Object> keys = allKeys.stream().filter(c -> condition.apply("" + c.getKey())).map(Cache.Entry::getKey).collect(Collectors.toSet());
            cache.removeAll(keys);
            return true;
        });
    }

    //@Override
    public void deleteAll(Set<? extends Object> keys) {
        executeWithRetry(() -> {
            cache.removeAll(keys);
            return true;
        });
    }

    //@Override
    public void deleteAll() {
        executeWithRetry(() -> {
            cache.removeAll();
            return true;
        });
    }

    //@Override
    public Object getAndSet(Object key, Object val) {
        return executeWithRetry(() -> cache.getAndPut(key, val));
    }

    //@Override
    public Object getAndDelete(Object key) {
        return executeWithRetry(() -> cache.getAndRemove(key));
    }

    //@Override
    public Object getAndReplace(Object key, Object val) {
        return executeWithRetry(() -> cache.getAndReplace(key, val));
    }

    //@Override
    public boolean setIfAbsent(Object key, Object val) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.putIfAbsent(key, val)));
    }

    //@Override
    public Object getAndSetIfAbsent(Object key, Object val) {
        return executeWithRetry(() -> cache.getAndPutIfAbsent(key, val));
    }

    //@Override
    public void clear() {
        executeWithRetry(() -> {
            cache.clear();
            return true;
        });
    }

    //@Override
    public void clear(Object key) {
        executeWithRetry(() -> {
            cache.clear(key);
            return true;
        });
    }

    //@Override
    public void clearAll(Set<? extends Object> keys) {
        executeWithRetry(() -> {
            cache.clearAll(keys);
            return true;
        });
    }

    //@Override
    public void setExpired(Object key, Object val, Long expiredSeconds) {
        executeWithRetry(() -> {
            setExpired(key, val, expiredSeconds, ExpirePolicy.CREATED);
            return true;
        });
    }

    //@Override
    public void setExpired(Object key, Object val, Long expiredSeconds, ExpirePolicy policy) {
        executeWithRetry(() -> {
            final Duration expiryDuration = new Duration(TimeUnit.SECONDS, expiredSeconds);
            switch (policy) {
                case MODIFY -> cache.withExpiryPolicy(new ModifiedExpiryPolicy(expiryDuration)).put(key, val);
                case TOUCHED -> new TouchedExpiryPolicy(expiryDuration);
                default -> cache.withExpiryPolicy(new CreatedExpiryPolicy(expiryDuration)).put(key, val);
            }
            return true;
        });
    }

    //@Override
    public boolean lockExpired(Object key, Object val, long expired) {
        return Boolean.TRUE.equals(executeWithRetry(() -> cache.withExpiryPolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, expired))).putIfAbsent(key, val)));
    }

    //@Override
    public boolean lock(Object key, Object val) {
        return Boolean.TRUE.equals(executeWithRetry(() -> setIfAbsent(key, val)));
    }

    //@Override
    public boolean lockWithRetry(Object key, Object val) {
        return Boolean.TRUE.equals(executeWithRetry(() -> lockWithRetry(key, val, DEFAULT_RETRY_COUNT, DEFAULT_WAIT_TIME)));
    }

    //@Override
    public boolean lockWithRetry(Object key, Object val, int retryCount) {
        return Boolean.TRUE.equals(executeWithRetry(() -> lockWithRetry(key, val, retryCount, DEFAULT_WAIT_TIME)));
    }

    //@Override
    public boolean lockWithRetry(Object key, Object val, long waitTime) {
        return Boolean.TRUE.equals(executeWithRetry(() -> lockWithRetry(key, val, DEFAULT_RETRY_COUNT, waitTime)));
    }

    //@Override
    public boolean lockWithRetry(Object key, Object val, int retryCount, long waitTime) {
        return Boolean.TRUE.equals(executeWithRetry(() -> {
            for (int i = 0; i < retryCount; i++) {
                if (lock(key, val)) {
                    return true;
                }
                ThreadUtil.safeSleep(waitTime);
            }
            return false;
        }));
    }

    //@Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds) {
        return Boolean.TRUE.equals(executeWithRetry(() -> lockExpiredWithRetry(key, val, expiredSeconds, DEFAULT_RETRY_COUNT, DEFAULT_WAIT_TIME)));
    }

    //@Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, int retryCount) {
        return Boolean.TRUE.equals(executeWithRetry(() -> lockExpiredWithRetry(key, val, expiredSeconds, retryCount, DEFAULT_WAIT_TIME)));
    }

    //@Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, long waitTime) {
        return Boolean.TRUE.equals(executeWithRetry(() -> lockExpiredWithRetry(key, val, expiredSeconds, DEFAULT_RETRY_COUNT, waitTime)));
    }

    //@Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, int retryCount, long waitTime) {
        return Boolean.TRUE.equals(executeWithRetry(() -> {
            for (int i = 0; i < retryCount; i++) {
                if (lockExpired(key, val, expiredSeconds)) {
                    return true;
                }
                ThreadUtil.safeSleep(waitTime);
            }
            return false;
        }));
    }


    //@Override
    public void replaceJson(Object key, Object val) {
        executeWithRetry(() -> {
            if (key == null) {
                throw new BaseException(CacheErrorCode.KEY_EMPTY.getMessage()).record();
            }
            if (val == null) {
                throw new BaseException(CacheErrorCode.VALUE_EMPTY.getMessage()).record();
            }
            replace(key, JsonUtil.format(val));
            return true;
        });
    }

    //@Override
    public void setJsonExpired(Object key, Object val, Long expiredSeconds) {
        executeWithRetry(() -> {
            setExpired(key, JsonUtil.format(val), expiredSeconds);
            return true;
        });
    }

    //@Override
    public void setJsonExpired(Object key, Object val, Long expiredSeconds, ExpirePolicy expirePolicy) {
        executeWithRetry(() -> {
            setExpired(key, JsonUtil.format(val), expiredSeconds, expirePolicy);
            return true;
        });
    }

    //@Override
    public void setJson(Object key, Object val) {
        executeWithRetry(() -> {
            set(key, JsonUtil.format(val));
            return true;
        });
    }

    //@Override
    public <T> T getJson(Object key, Class<T> clz) {
        return executeWithRetry(() -> {
            Object o = get(key);
            if (o == null) {
                return null;
            }
            String v = (String) o;
            return (T) JsonUtil.parse(v, clz);
        });
    }

    //@Override
    public <T> List<T> getListJson(Object key, Class<T> clz) {
        return executeWithRetry(() -> {
            Object o = get(key);
            if (o == null) {
                return null;
            }
            String v = (String) o;
            try {
                List<Map<String, Object>> objList = JsonUtil.parse(v, List.class);
                return JsonUtil.parseList(objList, clz);
            } catch (Exception e) {
                log.error("格式转换异常: {} {}", e.getMessage(), e);
                throw new BaseException(e);
            }
        });
    }

    //@Override
    public ClientTransaction txStart() {
        ClientTransactions transactions = client.transactions();
        return transactions.txStart(
                TransactionConcurrency.PESSIMISTIC,
                TransactionIsolation.REPEATABLE_READ);
    }


}
