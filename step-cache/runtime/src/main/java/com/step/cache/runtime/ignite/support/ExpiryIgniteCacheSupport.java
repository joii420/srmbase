package com.step.cache.runtime.ignite.support;

import com.step.api.runtime.core.CacheAPI;
import com.step.api.runtime.core.ExpiryCacheAPI;
import com.step.api.runtime.model.ExpirePolicy;
import org.apache.ignite.client.IgniteClient;

import javax.cache.expiry.Duration;

//@ApplicationScoped
public class ExpiryIgniteCacheSupport extends AbstractIgniteCacheSupport implements ExpiryCacheAPI {

    final IgniteClient client;

    public ExpiryIgniteCacheSupport(IgniteClient client, String cacheName, ExpirePolicy expirePolicy, Duration timeout) {
        super(client, cacheName, expirePolicy, timeout);
        this.client = client;
    }

}
