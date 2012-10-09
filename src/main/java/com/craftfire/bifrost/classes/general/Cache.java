/*
 * This file is part of Bifrost.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * Bifrost is licensed under the GNU Lesser General Public License.
 *
 * Bifrost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bifrost is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.bifrost.classes.general;

import com.craftfire.commons.classes.MetadatableCacheItem;
import com.craftfire.commons.managers.CacheManager;

import com.craftfire.bifrost.enums.CacheGroup;

/**
 * Handles all different cache within each {@link com.craftfire.bifrost.classes.general.Script}
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
     * @param group   the {@link CacheGroup} which the object should be placed in
     * @param object  the object that is going to be stored
     */
    public void put(CacheGroup group, Object object) {
        put(group, 1, object);
    }

    /**
     * Puts an object with an ID in the specified {@link CacheGroup} group parameter.
     * <p>
     * The id parameter can be used to retrieve the object from the cache.
     *
     * @param group   the {@link CacheGroup} which the object should be placed in
     * @param id      the ID of the stored object.
     * @param object  the object that is going to be stored
     */
    public void put(CacheGroup group, Object id, Object object) {
        this.cacheManager.put(group.toString(), id, object);
    }

    /**
     * Puts an object without an ID in the specified {@link CacheGroup} group parameter as metadatable cache item.
     * <p>
     * This will override any previous object in the specified {@link CacheGroup} per {@link CacheManager}.
     *
     * @param group   the {@link CacheGroup} which the object should be placed in
     * @param object  the object that is going to be stored
     */
    public void putMetadatable(CacheGroup group, Object object) {
        putMetadatable(group, 1, object);
    }

    /**
     * Puts an object with an ID in the specified {@link CacheGroup} group parameter as metadatable cache item.
     * <p>
     * The id parameter can be used to retrieve the object from the cache.
     *
     * @param group   the {@link CacheGroup} which the object should be placed in
     * @param id      the ID of the stored object.
     * @param object  the object that is going to be stored
     */
    public void putMetadatable(CacheGroup group, Object id, Object object) {
        this.cacheManager.putMetadatable(group.toString(), id, object);
    }

    /**
     * Returns the object of the specified {@link CacheGroup} group parameter.
     * <p>
     * This should only be used when retrieving an object which has been stored without an ID.
     *
     * @param group  the {@link CacheGroup} which the object should be grabbed from
     * @return       the object that has been stored, returns null if no stored object could be found
     */
    public Object get(CacheGroup group) {
        return get(group, 1);
    }

    /**
     * Returns the object of the specified {@link CacheGroup} group parameter by the id parameter.
     *
     * @param group  the {@link CacheGroup} which the object should be grabbed from
     * @param id     the unique ID of the object
     * @return       the object that has been stored, returns null if no stored object could be found
     */
    public Object get(CacheGroup group, Object id) {
        return this.cacheManager.get(group.toString(), id);
    }

    /**
     * Returns {@code true} if the {@link CacheManager} contains an object with the specified group parameter,
     * returns {@code false} if not.
     * <p>
     * This should only be used when checking an object which has been stored without an ID.
     *
     * @param group  the {@link CacheGroup} to check
     * @return       {@code true} if contains {@code false} if not
     */
    public boolean contains(CacheGroup group) {
        return contains(group, 1);
    }

    /**
     * Returns {@code true} if the {@link CacheManager} group parameter contains the id parameter,
     * returns {@code false} if not.
     * <p>
     * The object id is specified by the id param and the group is specified by the group param.
     *
     * @param group  the {@link CacheGroup} to check
     * @param id     the ID of the object to check
     * @return       {@code true} if contains {@code false} if not
     */
    public boolean contains(CacheGroup group, Object id) {
         return this.cacheManager.contains(group.toString(), id);
    }

    /**
     * Removes from cache all object in the cache group.
     * 
     * @param group  the {@link CacheGroup} to clear
     */
    public void clear(CacheGroup group) {
        this.cacheManager.clear(group.toString());
    }

    /**
     * Removes the object from the cache.
     * 
     * @param group  the {@link CacheGroup} which the object should be removed from
     * @param id     the id of the object
     */
    public void remove(CacheGroup group, Object id) {
        this.cacheManager.remove(group.toString(), id);
    }

    /**
     * Sets the value of the metadata key of specified cached object.
     * <p>
     * If operation succeeded, returns {@code true}
     * <p>
     * If the object is not metadatable, not present in cache, or other error occurred, returns {@code false}.
     *
     * @param group  the {@link CacheGroup} of the object
     * @param id     the id of the object
     * @param key    the meta key to set
     * @param value  the meta value
     * @return       {@code true} if succeeded, {@code false} if not
     */
    public boolean setMetadata(CacheGroup group, Object id, String key, Object value) {
        MetadatableCacheItem item = this.cacheManager.getMetadatableItem(group.toString(), id);
        if (item == null) {
            return false;
        }
        item.setMetaData(key, value);
        return true;
    }

    /**
     * Returns the value of the metadata key of specified object.
     * 
     * @param group  the {@link CacheGroup} of the object
     * @param id     the id of the object
     * @param key    the meta key to get
     * @return       value of the meta key, or null if the object is not metadatable, not present in cache, or other error occured
     */
    public Object getMetadata(CacheGroup group, Object id, String key) {
        MetadatableCacheItem item = this.cacheManager.getMetadatableItem(group.toString(), id);
        if (item == null) {
            return null;
        }
        return item.getMetaData(key);
    }

    /**
     * Removes the metadata key from specified cached object.
     * <p>
     * If operation succeeded, returns {@code true}
     * <p>
     * If the object is not metadatable, not present in cache, or other error occurred, returns {@code false}.
     *
     * @param group  the {@link CacheGroup} of the object
     * @param id     the id of the object
     * @param key    the meta key to set
     * @return       {@code true} if succeeded, {@code false} if not
     */
    public boolean removeMetadata(CacheGroup group, Object id, String key) {
        MetadatableCacheItem item = this.cacheManager.getMetadatableItem(group.toString(), id);
        if (item == null) {
            return false;
        }
        item.removeMetaData(key);
        return true;
    }
}
