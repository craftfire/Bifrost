package com.craftfire.bifrost.classes.cms;

import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

public class CMSArticle extends Message {

    public CMSArticle(Script script, int catid) {
        super(script);
        setCategoryID(catid);
    }

    public CMSArticle(Script script, int id, int catid) {
        super(script, id, catid);
    }

    public List<CMSComment> getComments(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCommentsOnArticle(getID(), limit);
    }

    @Override
    public List<CMSComment> getChildMessages(int limit) throws UnsupportedMethod {
        return getComments(limit);
    }

    @Override
    public CMSCategory getParent() throws UnsupportedMethod {
        return getCategory();
    }

    @Override
    public CMSCategory getCategory() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCategory(getCategoryID());
    }

    /**
     * Returns <code>true</code> if the handle contains an article cache with the given id parameter,
     * <code>false</code> if not.
     *
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, int id) {
        return handle.getCache().contains(CacheGroup.ARTICLE, id);
    }

    /**
     * Adds a CMSArticle to the cache with the given script handle
     *
     * @param handle   the script handle
     * @param article  the CMSArticle object
     */
    public static void addCache(ScriptHandle handle, CMSArticle article) {
        handle.getCache().put(CacheGroup.ARTICLE, article.getID(), article);
    }

    /**
     * Returns the CMSArticle object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the article
     * @return        CMSArticle object if cache was found, <code>null</code> if no cache was found
     */
    public static CMSArticle getCache(ScriptHandle handle, int id) {
        if (handle.getCache().contains(CacheGroup.ARTICLE, id)) {
            return (CMSArticle) handle.getCache().get(CacheGroup.ARTICLE, id);
        }
        return null;
    }

}
