package com.step.cache.runtime.redis;


import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.list.ListCommands;
import io.quarkus.redis.datasource.set.SetCommands;
import io.quarkus.redis.datasource.value.ValueCommands;

import java.util.List;
import java.util.Set;

//@ApplicationScoped
public class RedisCache  {
    private final ValueCommands<String, Object> valueCommands;
    private final KeyCommands<String> keyCommands;

    private final ListCommands<String, List> listCommands;

    private final SetCommands<String, Set> setCommands;
    private final HashCommands<String, String, Integer> hashCommands;


    public RedisCache(RedisDataSource ds) {
        valueCommands = ds.value(Object.class);
        keyCommands = ds.key();
        listCommands = ds.list(List.class);
        setCommands = ds.set(Set.class);
        hashCommands = ds.hash(Integer.class);
    }

}