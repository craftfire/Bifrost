package com.craftfire.authapi.classes;

import com.craftfire.authapi.enums.CacheGroup;
import com.craftfire.commons.managers.CacheManager;

public class Cache {
    public static Script script;
    public static CacheManager cacheManager;

    public Cache(Script uScript, CacheManager cManager) {
        script = uScript;
        cacheManager = cManager;
    }

    public static Script getScript() {
        return script;
    }

    public static void put(CacheGroup group, int id, Object object) {
        cacheManager.put(group.toString(), id, object);
    }

    public static Object get(CacheGroup group, int id) {
        return cacheManager.get(group.toString(), id);
    }

    public static boolean contains(CacheGroup group, int id) {
         return cacheManager.contains(group.toString(), id);
    }
}
