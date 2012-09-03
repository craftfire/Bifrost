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

    /**
     * This constructor should be used when creating a new comment for the script.
     * <p>
     * Remember to run {@see #createComment()} after creating a comment to insert it into the script.
     * 
     * @param script     the script the comment is created for
     * @param articleid  the ID of the article the comment is on
     */
    public CMSComment(Script script, int articleid) {
        super(script);
        this.articleid = articleid;
    }

    /**
     * This constructor should only be used by the script and <b>not</b> by that library user.
     * 
     * @param script     the script the article comes from
     * @param id         the ID of the comment
     * @param articleid  the ID of the article the comment is on
     */
    public CMSComment(Script script, int id, int articleid) {
        super(script, id);
        this.articleid = articleid;
    }

    /**
     * Returns the list of comments replying to this comment.
     * <p>
     * Loads it form database if not cached.
     * 
     * @see MessageParent#getChildMessages(int)
     */
    @Override
    public List<CMSComment> getChildMessages(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCommentReplies(getID(), limit);
    }

    /**
     * Returns the ID of the comment this comment replies to.
     * 
     * @return the ID of the parent comment
     */
    public int getParentID() {
        return this.parentid;
    }

    /**
     * Sets the comment this comment replies to.
     * 
     * @param parentid  the ID of the parent comment
     */
    public void setParentID(int parentid) {
        this.parentid = parentid;
    }

    /**
     * Returns the CMSComment object for the comment this comment replies to.
     * 
     * @return the CMSComment object
     * @see Message#getParent()
     */
    @Override
    public MessageParent getParent() throws UnsupportedMethod {
        if (this.parentid != 0) {
            return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getComment(this.parentid);
        } else {
            return getArticle();
        }
    }

    /**
     * Returns the ID of the article this comment is on.
     * 
     * @return the ID of the article this comment is on
     */
    public int getArticleID() {
        return this.articleid;
    }

    /**
     * Sets the article this comment is on.
     * 
     * @param articleid  the ID of the article this comment is on
     */
    public void setArticleID(int articleid) {
        this.articleid = articleid;
    }

    /**
     * Returns the CMSArticle object of the article this comment is on.
     * 
     * @return                    the CMSArticle object
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public CMSArticle getArticle() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getArticle(this.articleid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Message#getCategoryID()
     */
    @Override
    public int getCategoryID() {
        try {
            return getArticle().getCategoryID();
        } catch (UnsupportedMethod e) {
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Message#getCategory()
     */
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
