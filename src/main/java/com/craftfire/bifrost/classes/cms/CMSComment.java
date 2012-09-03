package com.craftfire.bifrost.classes.cms;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Category;
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.classes.general.MessageParent;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

public class CMSComment extends Message {
    private int articleid, parentid;

    public CMSComment(Script script, int articleid) {
        super(script);
        this.articleid = articleid;
    }

    public CMSComment(Script script, int id, int articleid) {
        super(script, id);
        this.articleid = articleid;
    }

    @Override
    public List<CMSComment> getChildMessages(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCommentReplies(getID(), limit);
    }

    public int getParentID() {
        return this.parentid;
    }

    public void setParentID(int parentid) {
        this.parentid = parentid;
    }

    @Override
    public MessageParent getParent() throws UnsupportedMethod {
        if (this.parentid != 0) {
            return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getComment(this.parentid);
        } else {
            return getArticle();
        }
    }

    public int getArticleID() {
        return this.articleid;
    }

    public void setArticleID(int articleid) {
        this.articleid = articleid;
    }

    public CMSArticle getArticle() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getArticle(this.articleid);
    }

    @Override
    public int getCategoryID() {
        try {
            return getArticle().getCategoryID();
        } catch (UnsupportedMethod e) {
            return 0;
        }
    }

    @Override
    public Category getCategory() throws UnsupportedMethod {
        return getArticle().getCategory();
    }

    /**
     * This method should be run after changing any comment values.
     * <p>
     * It should <b>not</b> be run when creating a new comment, only when editing an already existing comment.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void updateComment() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).updateComment(this);
    }

    /**
     * This method should be run after creating a new comment.
     * <p>
     * It should <b>not</b> be run when updating a comment, only when creating a new comment.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void createComment() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).createComment(this);
    }

    /**
     * Returns <code>true</code> if the handle contains a comment cache with the given id parameter,
     * <code>false</code> if not.
     *
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, int id) {
        return handle.getCache().contains(CacheGroup.COMMENT, id);
    }

    /**
     * Adds a CMSComment to the cache with the given script handle
     *
     * @param handle   the script handle
     * @param comment  the CMSComment object
     */
    public static void addCache(ScriptHandle handle, CMSComment comment) {
        handle.getCache().put(CacheGroup.COMMENT, comment.getID(), comment);
    }

    /**
     * Returns the ForumPost object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the post
     * @return        ForumPost object if cache was found, <code>null</code> if no cache was found
     */
    public static CMSComment getCache(ScriptHandle handle, int id) {
        if (handle.getCache().contains(CacheGroup.COMMENT, id)) {
            return (CMSComment) handle.getCache().get(CacheGroup.COMMENT, id);
        }
        return null;
    }
}
