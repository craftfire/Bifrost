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
package com.craftfire.bifrost.classes.cms;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Cache;
import com.craftfire.bifrost.classes.general.Category;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class should only be used with a CMS category.
 * <p>
 * The second constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the category, run {@link #update()}.
 * <p>
 * When creating a new CMSCategory make sure you use the correct constructor:
 * {@link #CMSCategory(CMSHandle, String, int)}.
 * <p>
 * Remember to run {@link #create()} after creating a category to insert it into the script.
 */
public class CMSCategory extends Category {
    private boolean isPublic;
    
    /**
     * This constructor may be used when creating a new category for the script.
     * <p>
     * Remember to run {@link #create()} after creating a category to insert it into the script.
     * 
     * @param script  the script the board is created for
     * @param name    the name of the category
     */
    public CMSCategory(CMSScript script, String name) {
        super(script.getHandle());
        setName(name);
    }

    /**
     * This constructor should only be used by the script and <b>not</b> by that library user.
     * 
     * @param script      the script the category comes from
     * @param categoryid  the ID of the category
     */
    public CMSCategory(CMSScript script, int categoryid) {
        super(script.getHandle(), categoryid);
    }

    /**
     * This constructor should be preferred when creating a new category for the script.
     * <p>
     * Remember to run {@see #createCategory()} after creating a category to insert it into the script.
     * 
     * @param handle    the handle the category is created for
     * @param name      the name of the category
     * @param parentid  the ID of parent category of the category
     */
    public CMSCategory(CMSHandle handle, String name, int parentid) {
        super(handle, name, parentid);
    }

    @Override
    public CMSHandle getHandle() {
        return (CMSHandle) super.getHandle();
    }

    /**
     * Returns the list of articles contained in the category.
     * <p>
     * Loads the articles from a database if not cached.
     * 
     * @param limit               how many articles should be returned, 0 = returns all
     * @return                    the list of articles
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<CMSArticle> getArticles(int limit) throws UnsupportedMethod {
        return getHandle().getArticlesFromCategory(getID(), limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Category#getParent()
     */
    @Override
    public CMSCategory getParent() throws UnsupportedMethod {
        return getHandle().getCategory(getParentID());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Category#getSubcategories(int)
     */
    @Override
    public List<CMSCategory> getSubcategories(int limit) throws UnsupportedMethod {
        return getHandle().getSubCategories(getID(), limit);
    }

    /**
     * Returns the list of messages contained in this category.
     * <p>
     * For CMSCategory it always has the same result as {@see #getArticles(int)}.
     */
    @Override
    public List<CMSArticle> getMessages(int limit) throws UnsupportedMethod {
        return getArticles(limit);
    }

    /**
     * Returns <code>true</code> if category has been published, <code>false</code> if not published.
     *
     * @return <code>true</code> if published, <code>false</code> if not published
     */
    public boolean isPublic() {
        return this.isPublic;
    }

    /**
     * Sets the category's published state to whatever <code>Boolean</code> the <code>isPublic</code> parameter is.
     * <p>
     * <code>true</code> = published and <code>false</code> = not published.
     *
     * @param isPublic  <code>true</code> for published, <code>false</code> for not published
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * This method should be run after changing any category values.
     * <p>
     * It should <b>not</b> be run when creating a new category, only when editing an already existing category.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getHandle().updateCategory(this);
    }

    /**
     * This method should be run after creating a new category.
     * <p>
     * It should <b>not</b> be run when updating an category, only when creating a new category.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getHandle().createCategory(this);
    }

    /**
     * Returns <code>true</code> if the handle contains a category cache with the given id parameter,
     * <code>false</code> if not.
     * 
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, int id) {
        return handle.getCache().contains(CacheGroup.CMSCAT, id);
    }

    /**
     * Adds a category to the cache with the given script handle
     *
     * @param handle    the script handle
     * @param category  the category object
     */
    public static void addCache(ScriptHandle handle, CMSCategory category) {
        handle.getCache().putMetadatable(CacheGroup.CMSCAT, category.getID(), category);
        handle.getCache().setMetadata(CacheGroup.CMSCAT, category.getID(), "bifrost-cache.old-parent", category.getParentID());
    }

    /**
     * Returns the category object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the category
     * @return        category object if cache was found, <code>null</code> if no cache was found
     */
    public static CMSCategory getCache(ScriptHandle handle, int id) {
        if (handle.getCache().contains(CacheGroup.CMSCAT, id)) {
            return (CMSCategory) handle.getCache().get(CacheGroup.CMSCAT, id);
        }
        return null;
    }

    /**
     * Removes outdated cache elements related to given {@param category} from cache.
     * <p>
     * The method should be called when updating or creating a {@link CMSCategory}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     * 
     * @param handle    the handle the method is called from
     * @param category  the category to cleanup related cache
     * @param reason    the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     * @see             Cache
     */
    public static void cleanupCache(ScriptHandle handle, CMSCategory category, CacheCleanupReason reason) {
        handle.getCache().remove(CacheGroup.CMSCAT_SUBS, category.getParentID());
        handle.getCache().remove(CacheGroup.CMSCAT_SUB_COUNT, category.getParentID());
        switch (reason) {
        case CREATE:
            handle.getCache().clear(CacheGroup.CMSCAT_LIST);
            handle.getCache().clear(CacheGroup.CMSCAT_COUNT);
            break;
        case OTHER:
            handle.getCache().clear(CacheGroup.CMSCAT_LIST);
            handle.getCache().clear(CacheGroup.CMSCAT_COUNT);
            /* Passes through */
        case UPDATE:
            Object oldParent = handle.getCache().getMetadata(CacheGroup.CMSCAT, category.getID(), "bifrost-cache.old-parent");
            handle.getCache().remove(CacheGroup.CMSCAT_SUBS, oldParent);
            handle.getCache().remove(CacheGroup.CMSCAT_SUB_COUNT, oldParent);
            break;
        }
    }
}
