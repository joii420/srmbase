package com.step.cache.runtime.ignite.support;

import com.step.api.runtime.core.CacheAPI;
import org.apache.ignite.client.IgniteClient;

public class IgniteCacheSupport extends AbstractIgniteCacheSupport implements CacheAPI {

    final IgniteClient client;
    public IgniteCacheSupport(IgniteClient client, String cacheName) {
        super(client, cacheName);
        this.client = client;
    }

}
