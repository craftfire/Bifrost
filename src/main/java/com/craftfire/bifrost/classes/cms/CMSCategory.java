package com.craftfire.bifrost.classes.cms;

import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Category;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

public class CMSCategory extends Category {
    private boolean is_public;

    public CMSCategory(Script script, int catid) {
        super(script, catid);
    }

    public CMSCategory(Script script, String name, int parentid) {
        super(script, name, parentid);
    }

    public List<CMSArticle> getArticles(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getArticlesFromCategory(getID(), limit);
    }

    @Override
    public CMSCategory getParent() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCategory(getParentID());
    }

    @Override
    public List<CMSCategory> getSubcategories(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getSubCategories(getID(), limit);
    }

    @Override
    public List<CMSArticle> getMessages(int limit) throws UnsupportedMethod {
        return getArticles(limit);
    }

    public boolean isPublic() {
        return this.is_public;
    }

    public void setPublic(boolean isPublic) {
        this.is_public = isPublic;
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
        handle.getCache().put(CacheGroup.CMSCAT, category.getID(), category);
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

}
