package com.project.User_Authentication.Config;


import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.concurrent.*;

public class TTLConcurrentMapCache extends ConcurrentMapCache {

    private final long ttlInMillis;
    private final ConcurrentMap<Object, Long> expiryMap = new ConcurrentHashMap<>();

    public TTLConcurrentMapCache(String name, long ttl, TimeUnit timeUnit) {
        super(name);
        this.ttlInMillis = timeUnit.toMillis(ttl);
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        expiryMap.put(key, System.currentTimeMillis() + ttlInMillis);
    }

    @Override
    public ValueWrapper get(Object key) {
        Long expiryTime = expiryMap.get(key);
        if(expiryTime == null || System.currentTimeMillis() > expiryTime) {
            super.evict(key);
            expiryMap.remove(key);
            return null;
        }
        return super.get(key);
    }

    @Override
    public void evict(Object key) {
        super.evict(key);
        expiryMap.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
        expiryMap.clear();
    }
}
