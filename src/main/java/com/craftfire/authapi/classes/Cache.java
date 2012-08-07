package com.craftfire.authapi.classes;

import com.craftfire.authapi.enums.CacheGroup;
import com.craftfire.commons.managers.CacheManager;

public class Cache {
    private final CacheManager cacheManager;

    public Cache() {
        this.cacheManager = new CacheManager();
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void put(CacheGroup group, Object object) {
        put(group, 1, object);
    }

    public void put(CacheGroup group, Object id, Object object) {
        this.cacheManager.put(group.toString(), id, object);
    }

    public Object get(CacheGroup group) {
        return get(group, 1);
    }

    public Object get(CacheGroup group, Object id) {
        return this.cacheManager.get(group.toString(), id);
    }

    public boolean contains(CacheGroup group) {
        return contains(group, 1);
    }

    public boolean contains(CacheGroup group, Object id) {
         return this.cacheManager.contains(group.toString(), id);
    }
}
