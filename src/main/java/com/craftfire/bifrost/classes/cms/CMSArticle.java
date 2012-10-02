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
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.classes.general.ViewsCounter;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class should only be used with a CMS article.
 * <p>
 * The second constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the article, run {@link #update()}.
 * <p>
 * When creating a new CMSArticle make sure you use the correct constructor:
 * {@link #CMSArticle(CMSScript, int)}.
 * <p>
 * Remember to run {@link #create()} after creating an article to insert it into the script.
 */
public class CMSArticle extends Message implements ViewsCounter {
    private String intro, url;
    private int views;
    private boolean isPublic, featured, allowComments;

    /**
     * This constructor should be used when creating a new article for the script.
     * <p>
     * Remember to run {@link #create()} after creating an article to insert it into the script.
     * 
     * @param script      the script the article is created for
     * @param categoryid  the id of the category of the script
     */
    public CMSArticle(CMSScript script, int categoryid) {
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
    public CMSArticle(CMSScript script, int id, int categoryid) {
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
     * Returns the list of messages whose parent is this object.
     * <p>
     * For CMSArticle it always has the same result as {@see #getComments(int)}.
     */
    @Override
    public List<CMSComment> getChildMessages(int limit) throws UnsupportedMethod {
        return getComments(limit);
    }

    /**
     * Returns the parent of the message.
     * <p>
     * For CMSArticle it always has the same result as {@see #getCategory()}.
     */
    @Override
    public CMSCategory getParent() throws UnsupportedMethod, SQLException {
        return getCategory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Message#getCategory()
     */
    @Override
    public CMSCategory getCategory() throws UnsupportedMethod, SQLException {
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
        return this.isPublic;
    }

    /**
     * Sets the article's published state to whatever <code>Boolean</code> the <code>isPublic</code> parameter is.
     * <p>
     * <code>true</code> = published and <code>false</code> = not published.
     *
     * @param isPublic  <code>true</code> for published, <code>false</code> for not published
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
    @Override
    public void update() throws SQLException, UnsupportedMethod {
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
    @Override
    public void create() throws SQLException, UnsupportedMethod {
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
        handle.getCache().putMetadatable(CacheGroup.ARTICLE, article.getID(), article);
        handle.getCache().setMetadata(CacheGroup.ARTICLE, article.getID(), "bifrost-cache.old-category", article.getCategoryID());
        if (article.getAuthor() != null) {
            handle.getCache().setMetadata(CacheGroup.ARTICLE, article.getID(), "bifrost-cache.old-author", article.getAuthor().getUsername());
        } else {
            handle.getCache().removeMetadata(CacheGroup.ARTICLE, article.getID(), "bifrost-cache.old-author");
        }
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

    /**
     * Removes outdated cache elements related to given {@param article} from cache.
     * <p>
     * The method should be called when updating or creating a {@link CMSArticle}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     * 
     * @param handle   the handle the method is called from
     * @param article  the article to cleanup related cache
     * @param reason   the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     * @see            com.craftfire.bifrost.classes.Cache
     */
    public static void cleanupCache(ScriptHandle handle, CMSArticle article, CacheCleanupReason reason) {
        handle.getCache().remove(CacheGroup.CMSCAT_ARTICLES, article.getCategoryID());
        handle.getCache().remove(CacheGroup.ARTICLE_COUNT, article.getCategoryID());
        handle.getCache().remove(CacheGroup.ARTICLE_LAST_CATEGORY, article.getCategoryID());
        if (article.getAuthor() != null) {
            String username = article.getAuthor().getUsername();
            handle.getCache().remove(CacheGroup.ARTICLE_LIST_USER, username);
            handle.getCache().remove(CacheGroup.ARTICLE_COUNT_USER, username);
            handle.getCache().remove(CacheGroup.ARTICLE_LAST_USER, username);
        }
        switch (reason) {
        case CREATE:
            handle.getCache().clear(CacheGroup.ARTICLE_COUNT);
            handle.getCache().clear(CacheGroup.ARTICLE_LIST);
            break;
        case OTHER:
            handle.getCache().clear(CacheGroup.ARTICLE_COUNT);
            handle.getCache().clear(CacheGroup.ARTICLE_LIST);
            /* Passes through */
        case UPDATE:
            Object oldCategory = handle.getCache().getMetadata(CacheGroup.ARTICLE, article.getID(), "bifrost-cache.old-category");
            Object oldUsername = handle.getCache().getMetadata(CacheGroup.ARTICLE, article.getID(), "bifrost-cache.old-author");
            handle.getCache().remove(CacheGroup.CMSCAT_ARTICLES, oldCategory);
            handle.getCache().remove(CacheGroup.ARTICLE_COUNT, oldCategory);
            handle.getCache().remove(CacheGroup.ARTICLE_LAST_CATEGORY, oldCategory);
            handle.getCache().remove(CacheGroup.ARTICLE_LIST_USER, oldUsername);
            handle.getCache().remove(CacheGroup.ARTICLE_COUNT_USER, oldUsername);
            handle.getCache().remove(CacheGroup.ARTICLE_LAST_USER, oldUsername);
            break;
        }

    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.classes.general.Message#getScript()
     */
    @Override
    public CMSScript getScript() {
        return (CMSScript) super.getScript();
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.classes.general.Message#getAuthor()
     */
    @Override
    public CMSUser getAuthor() {
        return (CMSUser) super.getAuthor();
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.classes.general.Message#setAuthor(com.craftfire.bifrost.classes.general.ScriptUser)
     */
    @Override
    public void setAuthor(ScriptUser author) {
        if (author instanceof CMSUser) {
            super.setAuthor(author);
        }
    }
}
