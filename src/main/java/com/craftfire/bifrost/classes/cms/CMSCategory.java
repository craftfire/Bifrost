package com.craftfire.bifrost.classes.cms;

import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Category;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

/**
 * This class should only be used with a CMS category.
 * <p>
 * The second constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the category, run {@see #updateCategory()}.
 * <p>
 * When creating a new CMSCategory make sure you use the correct constructor:
 * {@see #CMSCategory(com.craftfire.bifrost.script.Script, String, int)}.
 * <p>
 * Remember to run {@see #createCategory()} after creating a category to insert it into the script.
 */
public class CMSCategory extends Category {
    private boolean is_public;
    
    /**
     * This constructor may be used when creating a new category for the script.
     * <p>
     * Remember to run {@see #createCategory()} after creating a board to insert it into the script.
     * 
     * @param script  the script the board is created for
     * @param name
     */
    public CMSCategory(Script script, String name) {
        super(script);
        setName(name);
    }

    /**
     * This constructor should only be used by the script and <b>not</b> by that library user.
     * 
     * @param script      the script the category comes from
     * @param categoryid  the ID of the category
     */
    public CMSCategory(Script script, int categoryid) {
        super(script, categoryid);
    }

    /**
     * This constructor should be preferred when creating a new category for the script.
     * <p>
     * Remember to run {@see #createCategory()} after creating a category to insert it into the script.
     * 
     * @param script    the script the category is created for
     * @param name      the name of the category
     * @param parentid  the ID of parent category of the category
     */
    public CMSCategory(Script script, String name, int parentid) {
        super(script, name, parentid);
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
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getArticlesFromCategory(getID(), limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Category#getParent()
     */
    @Override
    public CMSCategory getParent() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCategory(getParentID());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Category#getSubcategories(int)
     */
    @Override
    public List<CMSCategory> getSubcategories(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getSubCategories(getID(), limit);
    }

    /**
     * @see #getArticles(int)
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
        return this.is_public;
    }

    /**
     * Sets the category's published state to whatever <code>Boolean</code> the <code>isPublic</code> parameter is.
     * <p>
     * <code>true</code> = published and <code>false</code> = not published.
     *
     * @param isPublic  <code>true</code> for published, <code>false</code> for not published
     */
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
