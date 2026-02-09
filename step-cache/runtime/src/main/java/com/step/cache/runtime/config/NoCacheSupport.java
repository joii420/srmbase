package com.step.cache.runtime.config;

import com.step.api.runtime.core.CacheAPI;
import org.apache.ignite.client.ClientTransaction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author : Sun
 * @date : 2025/1/3  15:10
 */
public class NoCacheSupport implements CacheAPI {
    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public void set(Object key, Object val) {

    }

    @Override
    public boolean exist(Object key) {
        return false;
    }

    @Override
    public boolean exist(Set<?> keys) {
        return false;
    }

    @Override
    public Object getName() {
        return null;
    }

    @Override
    public Map<Object, Object> getAll(Set<?> keys) {
        return null;
    }

    @Override
    public void setAll(Map<?, ?> map) {

    }

    @Override
    public boolean replace(Object key, Object oldVal, Object newVal) {
        return false;
    }

    @Override
    public boolean replace(Object key, Object val) {
        return false;
    }

    @Override
    public boolean delete(Object key) {
        return false;
    }

    @Override
    public boolean delete(Object key, Object oldVal) {
        return false;
    }

    @Override
    public Set<String> getKeysByCondition(Function<String, Boolean> condition) {
        return null;
    }

    @Override
    public Map<String, Object> getKVByCondition(Function<String, Boolean> condition) {
        return null;
    }

    @Override
    public void deleteByCondition(Function<String, Boolean> condition) {

    }

    @Override
    public void deleteAll(Set<?> keys) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Object getAndSet(Object key, Object val) {
        return null;
    }

    @Override
    public Object getAndDelete(Object key) {
        return null;
    }

    @Override
    public Object getAndReplace(Object key, Object val) {
        return null;
    }

    @Override
    public boolean setIfAbsent(Object key, Object val) {
        return false;
    }

    @Override
    public Object getAndSetIfAbsent(Object key, Object val) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void clear(Object key) {

    }

    @Override
    public void clearAll(Set<?> keys) {

    }

    @Override
    public void setExpired(Object key, Object val, Long expiredSeconds) {

    }

    @Override
    public boolean lock(Object key, Object val) {
        return false;
    }

    @Override
    public boolean lockWithRetry(Object key, Object val) {
        return false;
    }

    @Override
    public boolean lockWithRetry(Object key, Object val, int retryCount) {
        return false;
    }

    @Override
    public boolean lockWithRetry(Object key, Object val, long waitTime) {
        return false;
    }

    @Override
    public boolean lockWithRetry(Object key, Object val, int retryCount, long waitTime) {
        return false;
    }

    @Override
    public boolean lockExpired(Object key, Object val, long expiredSeconds) {
        return false;
    }

    @Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds) {
        return false;
    }

    @Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, int retryCount) {
        return false;
    }

    @Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, long waitTime) {
        return false;
    }

    @Override
    public boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, int retryCount, long waitTime) {
        return false;
    }

    @Override
    public void replaceJson(Object key, Object val) {

    }

    @Override
    public void setJsonExpired(Object key, Object val, Long expiredSeconds) {

    }

    @Override
    public void setJson(Object key, Object val) {

    }

    @Override
    public <T> T getJson(Object key, Class<T> clz) {
        return null;
    }

    @Override
    public <T> List<T> getListJson(Object key, Class<T> clz) {
        return null;
    }

    @Override
    public long increment(String key, long start, long quantity, boolean init) {
        return 0;
    }

    @Override
    public boolean removeAtomicLong(String key) {
        return false;
    }

    @Override
    public ClientTransaction txStart() {
        return null;
    }
}
