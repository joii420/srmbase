package com.step.api.runtime.core;

import org.apache.ignite.client.ClientTransaction;

import java.util.*;
import java.util.function.Function;

public interface CacheAPI {


    /**
     * Gets an entry from the cache.
     *
     * @param key the key whose associated value is to be returned
     * @return the element, or null, if it does not exist.
     * @throws NullPointerException if the key is null.
     */
    Object get(Object key);


    /**
     * Associates the specified value with the specified key in the cache.
     * <p>
     * If the {ClientCache} previously contained a mapping for the key, the old
     * value is replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param val value to be associated with the specified key.
     * @throws NullPointerException if key is null or if value is null.
     */
    void set(Object key, Object val);


    /**
     * Determines if the {ClientCache} contains an entry for the specified key.
     * <p>
     * More formally, returns <tt>true</tt> if and only if this cache contains a
     * mapping for a key <tt>k</tt> such that <tt>key.equals(k)</tt>.
     * (There can be at most one such mapping)
     *
     * @param key key whose presence in this cache is to be tested.
     * @return <tt>true</tt> if this map contains a mapping for the specified key.
     */
    boolean exist(Object key);


    /**
     * Determines if the {ClientCache} contains entries for the specified keys.
     *
     * @param keys Keys whose presence in this cache is to be tested.
     * @return {@code True} if this cache contains a mapping for the specified keys.
     */
    boolean exist(Set<? extends Object> keys);


    /**
     * @return The name of the cache.
     */
    Object getName();


    /**
     * Gets a collection of entries from the {ClientCache}, returning them as
     * {Map} of the values associated with the set of keys requested.
     *
     * @param keys The keys whose associated values are to be returned.
     * @return A map of entries that were found for the given keys. Keys not found
     * in the cache are not in the returned map.
     */
    Map<Object, Object> getAll(Set<? extends Object> keys);


    /**
     * Copies all of the entries from the specified map to the {ClientCache}.
     * <p>
     * The effect of this call is equivalent to that of calling
     * {#put(Object, Object) put(k, v)} on this cache once for each mapping
     * from key <tt>k</tt> to value <tt>v</tt> in the specified map.
     * <p>
     * The order in which the individual puts occur is undefined.
     * <p>
     * The behavior of this operation is undefined if entries in the cache
     * corresponding to entries in the map are modified or removed while this
     * operation is in progress, or if map is modified while the operation is in progress.
     * <p>
     *
     * @param map Mappings to be stored in this cache.
     */
    void setAll(Map<? extends Object, ? extends Object> map);


    /**
     * Atomically replaces the entry for a key only if currently mapped to a given value.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (cache.containsKey(key) &amp;&amp; equals(cache.get(key), oldValue)) {
     *  cache.put(key, newValue);
     * return true;
     * } else {
     *  return false;
     * }
     * </code></pre>
     *
     * @param key    Key with which the specified value is associated.
     * @param oldVal Value expected to be associated with the specified key.
     * @param newVal Value to be associated with the specified key.
     * @return <tt>true</tt> if the value was replaced
     */
    boolean replace(Object key, Object oldVal, Object newVal);


    /**
     * Atomically replaces the entry for a key only if currently mapped to some
     * value.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (cache.containsKey(key)) {
     *   cache.put(key, value);
     *   return true;
     * } else {
     *   return false;
     * }</code></pre>
     *
     * @param key The key with which the specified value is associated.
     * @param val The value to be associated with the specified key.
     * @return <tt>true</tt> if the value was replaced.
     */
    boolean replace(Object key, Object val);


    /**
     * Removes the mapping for a key from this cache if it is present.
     * <p>
     * More formally, if this cache contains a mapping from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is removed.
     * (The cache can contain at most one such mapping.)
     *
     * <p>Returns <tt>true</tt> if this cache previously associated the key, or <tt>false</tt> if the cache
     * contained no mapping for the key.
     * <p>
     * The cache will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key Key whose mapping is to be removed from the cache.
     * @return <tt>false</tt> if there was no matching key.
     */
    boolean delete(Object key);


    /**
     * Atomically removes the mapping for a key only if currently mapped to the given value.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (cache.containsKey(key) &amp;&amp; equals(cache.get(key), oldValue) {
     *   cache.remove(key);
     *   return true;
     * } else {
     *   return false;
     * }
     * </code></pre>
     *
     * @param key    Key whose mapping is to be removed from the cache.
     * @param oldVal Value expected to be associated with the specified key.
     * @return <tt>false</tt> if there was no matching key.
     */
    boolean delete(Object key, Object oldVal);

    /**
     * 通过条件查找Key
     *
     * @param condition 得到满足条件的所有k
     */
    Set<String> getKeysByCondition(Function<String, Boolean> condition);

    Map<String, Object> getKVByCondition(Function<String, Boolean> condition);

    /**
     * 通过条件删除
     *
     * @param condition 删除true的所有key
     */
    void deleteByCondition(Function<String, Boolean> condition);

    /**
     * Removes entries for the specified keys.
     * <p>
     * The order in which the individual entries are removed is undefined.
     *
     * @param keys The keys to remove.
     */
    void deleteAll(Set<? extends Object> keys);


    /**
     * Removes all of the mappings from this cache.
     * <p>
     * The order that the individual entries are removed is undefined.
     * <p>
     * This operation is not transactional. It calls broadcast closure that
     * deletes all primary keys from remote nodes.
     * <p>
     * This is potentially an expensive operation as listeners are invoked.
     * Use {#clear()} to avoid this.
     */
    void deleteAll();


    /**
     * Associates the specified value with the specified key in this cache, returning an existing value if one existed.
     * <p>
     * If the cache previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A cache
     * <tt>c</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {#containsKey(Object) c.containsKey(k)} would return
     * <tt>true</tt>.)
     * <p>
     * The previous value is returned, or null if there was no value associated
     * with the key previously.
     *
     * @param key Key with which the specified value is to be associated.
     * @param val Value to be associated with the specified key.
     * @return The value associated with the key at the start of the operation or
     * null if none was associated.
     */
    Object getAndSet(Object key, Object val);


    /**
     * Atomically removes the entry for a key only if currently mapped to some value.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (cache.containsKey(key)) {
     *   Object oldValue = cache.get(key);
     *   cache.remove(key);
     *   return oldValue;
     * } else {
     *   return null;
     * }
     * </code></pre>
     *
     * @param key Key with which the specified value is associated.
     * @return The value if one existed or null if no mapping existed for this key.
     */
    Object getAndDelete(Object key);


    /**
     * Atomically replaces the value for a given key if and only if there is a value currently mapped by the key.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (cache.containsKey(key)) {
     *   Object oldValue = cache.get(key);
     *   cache.put(key, value);
     *   return oldValue;
     * } else {
     *   return null;
     * }
     * </code></pre>
     *
     * @param key Key with which the specified value is associated.
     * @param val Value to be associated with the specified key.
     * @return The previous value associated with the specified key, or
     * <tt>null</tt> if there was no mapping for the key.
     */
    Object getAndReplace(Object key, Object val);


    /**
     * Atomically associates the specified key with the given value if it is not already associated with a value.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (!cache.containsKey(key)) {
     *   cache.put(key, value);
     *   return true;
     * } else {
     *   return false;
     * }
     * </code></pre>
     *
     * @param key Key with which the specified value is to be associated.
     * @param val Value to be associated with the specified key.
     * @return <tt>true</tt> if a value was set.
     */
    boolean setIfAbsent(Object key, Object val);


    /**
     * Atomically associates the specified key with the given value if it is not already associated with a value.
     * <p>
     * This is equivalent to performing the following operations as a single atomic action:
     * <pre><code>
     * if (!cache.containsKey(key)) {
     *   cache.put(key, value);
     *   return null;
     * } else {
     *   return cache.get(key);
     * }
     * </code></pre>
     *
     * @param key Key with which the specified value is to be associated.
     * @param val Value to be associated with the specified key.
     * @return Value that is already associated with the specified key, or {@code null} if no value was associated
     * with the specified key and a value was set.
     */
    Object getAndSetIfAbsent(Object key, Object val);


    /**
     * Clears the contents of the cache.
     * In contrast to {#removeAll()}, this method does not notify event listeners and cache writers.
     */
    void clear();


    /**
     * Clears entry with specified key from the cache.
     * In contrast to {#remove(Object)}, this method does not notify event listeners and cache writers.
     *
     * @param key Cache entry key to clear.
     */
    void clear(Object key);


    /**
     * Clears entries with specified keys from the cache.
     * In contrast to {#removeAll(Set)}, this method does not notify event listeners and cache writers.
     *
     * @param keys Cache entry keys to clear.
     */
    void clearAll(Set<? extends Object> keys);


    void setExpired(Object key, Object val, Long expiredSeconds);

    boolean lock(Object key, Object val);

    /**
     * 锁定+重试 默认重试3次  等待500ms
     */
    boolean lockWithRetry(Object key, Object val);

    boolean lockWithRetry(Object key, Object val, int retryCount);

    boolean lockWithRetry(Object key, Object val, long waitTime);

    boolean lockWithRetry(Object key, Object val, int retryCount, long waitTime);

    /**
     * 锁定+重试 默认重试3次  等待500ms
     */
    boolean lockExpired(Object key, Object val, long expiredSeconds);

    boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds);

    boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, int retryCount);

    boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, long waitTime);

    boolean lockExpiredWithRetry(Object key, Object val, long expiredSeconds, int retryCount, long waitTime);

    /**
     * JSON对象 接口
     */
    void replaceJson(Object key, Object val);

    void setJsonExpired(Object key, Object val, Long expiredSeconds);


    void setJson(Object key, Object val);

    <T> T getJson(Object key, Class<T> clz);

    <T> List<T> getListJson(Object key, Class<T> clz);

    /**
     * 自增
     *
     * @param key      自增主键
     * @param start    默认值
     * @param quantity 自增量
     * @param init     如果值已存在是否初始化值
     */
    long increment(String key, long start, long quantity, boolean init);

    default long increment(String key) {
        return increment(key, 0, 1, true);
    }

    default long increment(String key, long start) {
        return increment(key, start, 1, true);
    }

    default long increment(String key, long start, long quantity) {
        return increment(key, start, quantity, true);
    }

    default long getNumber(String key) {
        return increment(key, 0, 0);
    }

    default long decrement(String key) {
        return increment(key, 0, -1, true);
    }

    default long decrement(String key, long start) {
        return increment(key, start, -1, true);
    }

    default long decrement(String key, long start, long quantity) {
        return increment(key, start, quantity, true);
    }

    /**
     * 自减
     *
     * @param key      自减主键
     * @param start    默认值
     * @param quantity 自减量
     * @param init     如果值已存在是否初始化值
     */
    default long decrement(String key, long start, long quantity, boolean init) {
        return increment(key, start, quantity, init);
    }


    /**
     * 移除ignite原子锁
     */
    boolean removeAtomicLong(String key);

    /**
     * 开启缓存事务
     */
    ClientTransaction txStart();

}
