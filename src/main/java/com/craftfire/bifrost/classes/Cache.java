package com.craftfire.bifrost.classes;

import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.commons.managers.CacheManager;

/**
 * Handles all different cache within each {@link com.craftfire.bifrost.script.Script}
 * <p>
 * It uses the {@link CacheManager} to store the different objects.
 */
public class Cache {
    private final CacheManager cacheManager = new CacheManager();

    /**
     * Returns the {@link CacheManager} that is being used.
     *
     * @return {@link CacheManager}
     */
    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    /**
     * Puts an object without an ID in the specified {@link CacheGroup} group parameter.
     * <p>
     * This will override any previous object in the specified {@link CacheGroup} per {@link CacheManager}.
     *
     * @param group  the {@link CacheGroup} which the object should be placed in
     * @param object the object that is going to be stored
     */
    public void put(CacheGroup group, Object object) {
        put(group, 1, object);
    }

    /**
     * Puts an object with an ID in the specified {@link CacheGroup} group parameter.
     * <p>
     * The id parameter can be used to retrieve the object from the cache.
     *
     * @param group  the {@link CacheGroup} which the object should be placed in
     * @param id     the ID of the stored object.
     * @param object the object that is going to be stored
     */
    public void put(CacheGroup group, Object id, Object object) {
        this.cacheManager.put(group.toString(), id, object);
    }

    /**
     * Returns the object of the specified {@link CacheGroup} group parameter.
     * <p>
     * This should only be used when retrieving an object which has been stored without an ID.
     *
     * @param group the {@link CacheGroup} which the object should be grabbed from
     * @return      the object that has been stored, returns null if no stored object could be found
     */
    public Object get(CacheGroup group) {
        return get(group, 1);
    }

    /**
     * Returns the object of the specified {@link CacheGroup} group parameter by the id parameter.
     *
     * @param group the {@link CacheGroup} which the object should be grabbed from
     * @param id    the unique ID of the object
     * @return      the object that has been stored, returns null if no stored object could be found
     */
    public Object get(CacheGroup group, Object id) {
        return this.cacheManager.get(group.toString(), id);
    }

    /**
     * Returns <code>true</code> if the {@link CacheManager} contains an object with the specified group parameter,
     * returns <code>false</code> if not.
     * <p>
     * This should only be used when checking an object which has been stored without an ID.
     *
     * @param group the {@link CacheGroup} to check
     * @return <code>true</code> if contains <code>false</code> if not
     */
    public boolean contains(CacheGroup group) {
        return contains(group, 1);
    }

    /**
     * Returns <code>true</code> if the {@link CacheManager} group parameter contains the id parameter,
     * returns <code>false</code> if not.
     * <p>
     * The object id is specified by the id param and the group is specified by the group param.
     *
     * @param group the {@link CacheGroup} to check
     * @param id the ID of the object to check
     * @return <code>true</code> if contains <code>false</code> if not
     */
    public boolean contains(CacheGroup group, Object id) {
         return this.cacheManager.contains(group.toString(), id);
    }
}
