package com.craftfire.authapi.classes;

import com.craftfire.authapi.AuthAPI;
import com.craftfire.authapi.enums.CacheGroup;

public class Cache {

    public static void put(CacheGroup group, int id, Object object) {
        AuthAPI.getInstance().getCacheManager().put(group.toString(), id, object);
    }

    public static Object get(CacheGroup group, int id) {
        return AuthAPI.getInstance().getCacheManager().get(group.toString(), id);
    }

    public static boolean contains(CacheGroup group, int id) {
         return AuthAPI.getInstance().getCacheManager().contains(group.toString(), id);
    }
}
