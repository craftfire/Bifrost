package com.craftfire.bifrost.classes.cms;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.classes.general.ViewsCounter;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

/**
 * This class should only be used with a CMS article.
 * <p>
 * The second constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the article, run {@see #updateArticle()}.
 * <p>
 * When creating a new CMSArticle make sure you use the correct constructor:
 * {@see #CMSArticle(com.craftfire.bifrost.script.Script, int)}.
 * <p>
 * Remember to run {@see #createArticle()} after creating an article to insert it into the script.
 */
public class CMSArticle extends Message implements ViewsCounter {
    private String intro, url;
    private int views;
    private boolean is_public, featured, allowComments;

    /**
     * This constructor should be used when creating a new article for the script.
     * <p>
     * Remember to run {@see #createArticle()} after creating an article to insert it into the script.
     * 
     * @param script      the script the article is created for
     * @param categoryid  the id of the category of the script
     */
    public CMSArticle(Script script, int categoryid) {
        super(script);
        setCategoryID(categoryid);
    }

    /**
     * This constructor should only be used by the script and <b>not</b> by that library user.
     * 
     * @param script      the script the article comes from
     * @param id          the ID of the article
     * @param categoryid  the ID of the category of the article
     */
    public CMSArticle(Script script, int id, int categoryid) {
        super(script, id, categoryid);
    }

    /**
     * Returns the list of comments on the article.
     * <p>
     * Loads the comments from a database if not cached.
     * 
     * @param limit               how many comments should be returned, 0 = returns all
     * @return                    the list of comments
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<CMSComment> getComments(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCommentsOnArticle(getID(), limit);
    }

    /**
     * @see #getComments(int)
     */
    @Override
    public List<CMSComment> getChildMessages(int limit) throws UnsupportedMethod {
        return getComments(limit);
    }

    /**
     * @see #getCategory()
     */
    @Override
    public CMSCategory getParent() throws UnsupportedMethod {
        return getCategory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Message#getCategory()
     */
    @Override
    public CMSCategory getCategory() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getCategory(getCategoryID());
    }

    /**
     * Returns the intro text of the article.
     * 
     * @return the intro text
     */
    public String getIntro() {
        return this.intro;
    }

    /**
     * Sets the intro text of the article.
     * 
     * @param intro  the intro text
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * Returns <code>true</code> if article has been published, <code>false</code> if not published.
     *
     * @return <code>true</code> if published, <code>false</code> if not published
     */
    public boolean isPublic() {
        return this.is_public;
    }

    /**
     * Sets the article's published state to whatever <code>Boolean</code> the <code>isPublic</code> parameter is.
     * <p>
     * <code>true</code> = published and <code>false</code> = not published.
     *
     * @param isPublic  <code>true</code> for published, <code>false</code> for not published
     */
    public void setPublic(boolean isPublic) {
        this.is_public = isPublic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.ViewsCounter#getViewsCount()
     */
    @Override
    public int getViewsCount() {
        return this.views;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.craftfire.bifrost.classes.general.ViewsCounter#setViewsCount(int)
     */
    @Override
    public void setViewsCount(int views) {
        this.views = views;
    }

    /**
     * Returns <code>true</code> if article is featured, <code>false</code> if not featured.
     *
     * @return <code>true</code> if featured, <code>false</code> if not featured
     */
    public boolean isFeatured() {
        return this.featured;
    }

    /**
     * Sets the article's featured flag to whatever <code>Boolean</code> the <code>isFeatured</code> parameter is.
     * <p>
     * <code>true</code> = featured and <code>false</code> = not featured.
     *
     * @param isFeatured  <code>true</code> for featured, <code>false</code> for not featured
     */
    public void setFeatured(boolean isFeatured) {
        this.featured = isFeatured;
    }

    /**
     * Returns <code>true</code> if article is allowing comments, <code>false</code> if not allowing.
     *
     * @return <code>true</code> if allowing comments, <code>false</code> if not allowing
     */
    public boolean isAllowingComments() {
        return this.allowComments;
    }

    /**
     * Sets the if the article should allow comments or not.
     * <p>
     * <code>true</code> = allow comments and <code>false</code> = deny comments.
     *
     * @param allowComments  <code>true</code> to allow, <code>false</code> to deny
     */
    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    /**
     * Returns the URL of the article.
     * 
     * @return the URL of the article
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Sets the URL of the article.
     * 
     * @param url  the URL of the article
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method should be run after changing any article values.
     * <p>
     * It should <b>not</b> be run when creating a new article, only when editing an already existing article.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void updateArticle() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).updateArticle(this);
    }

    /**
     * This method should be run after creating a new article.
     * <p>
     * It should <b>not</b> be run when updating an article, only when creating a new article.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void createArticle() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).createArticle(this);
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
