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

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;

/**
 * This class contains methods relevant to methods to use for a cms script.
 *
 * @see CMSScript Documentation of all the methods
 */
public class CMSHandle extends ScriptHandle {

    /**
     * Creates a CMSHandle.
     *
     * @see ScriptHandle#ScriptHandle(Scripts, String, DataManager) Documentation for this constructor
     */
    public CMSHandle(Scripts script, String version, DataManager dataManager) throws UnsupportedVersion {
        super(script, version, dataManager);
    }

    /**
     * Returns the CMSScript object
     *
     * @return the CMSScript object
     */
    public CMSScript getCMSScript() {
        return (CMSScript) this.getScript();
    }

    /**
     * Creates a new comment in the specified <code>articleid</code>.
     *
     * @see CMSComment#CMSComment(CMSScript, int) Documentation for this method
     */
    public CMSComment newComment(int articleid) {
        return new CMSComment(getCMSScript(), articleid);
    }

    /**
     * Creates a new article in the specified <code>categoryid</code>.
     *
     * @see CMSArticle#CMSArticle(CMSScript, int) Documentation for this method
     */
    public CMSArticle newArticle(int categoryid) {
        return new CMSArticle(getCMSScript(), categoryid);
    }

    /**
     * Creates a new category with the specified name.
     *
     * @see CMSCategory#CMSCategory(CMSScript, String, int) Documentation for this method
     */
    public CMSCategory newCategory(String name, int parentid) {
        return new CMSCategory(getCMSScript(), name, parentid);
    }

    /**
     * @see CMSScript#getComment(int) Documentation for this method
     */
    public CMSComment getComment(int commentid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT, commentid)) {
            return (CMSComment) getCache().get(CacheGroup.COMMENT, commentid);
        }
        CMSComment cmt = getCMSScript().getComment(commentid);
        getCache().put(CacheGroup.COMMENT, commentid, cmt);
        return cmt;
    }

    /**
     * @see CMSScript#getComments(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSComment> getComments(int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_LIST)) {
            List<CMSComment> cmts = (List<CMSComment>) getCache().get(CacheGroup.COMMENT_LIST);
            if (cmts.size() == ((limit == 0) ? getCommentTotalCount() : limit)) {
                return cmts;
            } else if ((cmts.size() > limit) && (limit != 0)) {
                return cmts.subList(0, limit);
            }
        }
        List<CMSComment> cmts = getCMSScript().getComments(limit);
        getCache().put(CacheGroup.COMMENT, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getCommentTotalCount() Documentation for this method
     */
    public int getCommentTotalCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_COUNT_TOTAL)) {
            return (Integer) getCache().get(CacheGroup.COMMENT_COUNT_TOTAL);
        }
        int cmts = getCMSScript().getCommentTotalCount();
        getCache().put(CacheGroup.COMMENT_COUNT_TOTAL, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getCommentsOnArticle(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSComment> getCommentsOnArticle(int articleid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_COMMENTS, articleid)) {
            List<CMSComment> cmts = (List<CMSComment>) getCache().get(CacheGroup.ARTICLE_COMMENTS, articleid);
            if (cmts.size() == ((limit == 0) ? getCommentCount(articleid) : limit)) {
                return cmts;
            } else if ((cmts.size() > limit) && (limit != 0)) {
                return cmts.subList(0, limit);
            }
        }
        List<CMSComment> cmts = getCMSScript().getCommentsOnArticle(articleid, limit);
        getCache().put(CacheGroup.ARTICLE_COMMENTS, articleid, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getCommentCount(int) Documentation for this method
     */
    public int getCommentCount(int articleid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_COUNT, articleid)) {
            return (Integer) getCache().get(CacheGroup.COMMENT_COUNT, articleid);
        }
        int cmts = getCMSScript().getCommentCount(articleid);
        getCache().put(CacheGroup.COMMENT_COUNT, articleid, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getCommentReplies(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSComment> getCommentReplies(int commentid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_REPLIES, commentid)) {
            List<CMSComment> cmts = (List<CMSComment>) getCache().get(CacheGroup.COMMENT_REPLIES, commentid);
            if (cmts.size() == ((limit == 0) ? getCommentReplyCount(commentid) : limit)) {
                return cmts;
            } else if ((cmts.size() > limit) && (limit != 0)) {
                return cmts.subList(0, limit);
            }
        }
        List<CMSComment> cmts = getCMSScript().getCommentReplies(commentid, limit);
        getCache().put(CacheGroup.COMMENT_REPLIES, commentid, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getCommentReplyCount(int) Documentation for this method
     */
    public int getCommentReplyCount(int commentid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_REPLY_COUNT, commentid)) {
            return (Integer) getCache().get(CacheGroup.COMMENT_REPLY_COUNT, commentid);
        }
        int cmts = getCMSScript().getCommentReplyCount(commentid);
        getCache().put(CacheGroup.COMMENT_REPLY_COUNT, commentid, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getUserComments(String, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSComment> getUserComments(String username, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_LIST_USER, username)) {
            List<CMSComment> cmts = (List<CMSComment>) getCache().get(CacheGroup.COMMENT_LIST_USER, username);
            if (cmts.size() == ((limit == 0) ? getUserCommentCount(username) : limit)) {
                return cmts;
            } else if ((cmts.size() > limit) && (limit != 0)) {
                return cmts.subList(0, limit);
            }
        }
        List<CMSComment> cmts = getCMSScript().getUserComments(username, limit);
        getCache().put(CacheGroup.COMMENT_LIST_USER, username, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getUserCommentCount(String) Documentation for this method
     */
    public int getUserCommentCount(String username) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_COUNT_USER, username)) {
            return (Integer) getCache().get(CacheGroup.COMMENT_COUNT_USER, username);
        }
        int cmts = getCMSScript().getUserCommentCount(username);
        getCache().put(CacheGroup.COMMENT_COUNT_USER, username, cmts);
        return cmts;
    }

    /**
     * @see CMSScript#getLastComment() Documentation for this method
     */
    public CMSComment getLastComment() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_LAST)) {
            return (CMSComment) getCache().get(CacheGroup.COMMENT_LAST);
        }
        CMSComment cmt = getCMSScript().getLastComment();
        getCache().put(CacheGroup.COMMENT_LAST, cmt);
        return cmt;
    }

    /**
     * @see CMSScript#getLastUserComment(String) Documentation for this method
     */
    public CMSComment getLastUserComment(String username) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_LAST_USER, username)) {
            return (CMSComment) getCache().get(CacheGroup.COMMENT_LAST_USER, username);
        }
        CMSComment cmt = getCMSScript().getLastUserComment(username);
        getCache().put(CacheGroup.COMMENT_LAST_USER, username, cmt);
        return cmt;
    }

    /**
     * @see CMSScript#getLastCommentOnArticle(int) Documentation for this method
     */
    public CMSComment getLastCommentOnArticle(int articleid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.COMMENT_LAST_ARTICLE, articleid)) {
            return (CMSComment) getCache().get(CacheGroup.COMMENT_LAST_ARTICLE, articleid);
        }
        CMSComment cmt = getCMSScript().getLastCommentOnArticle(articleid);
        getCache().put(CacheGroup.COMMENT_LAST_ARTICLE, articleid, cmt);
        return cmt;
    }

    /**
     * @see CMSScript#updateComment(CMSComment) Documentation for this method
     */
    public void updateComment(CMSComment comment) throws UnsupportedMethod {
        getCMSScript().updateComment(comment);
        CMSComment.cleanupCache(this, comment, CacheCleanupReason.UPDATE);
        CMSComment.addCache(this, comment);
    }

    /**
     * @see CMSScript#createComment(CMSComment) Documentation for this method
     */
    public void createComment(CMSComment comment) throws UnsupportedMethod {
        getCMSScript().createComment(comment);
        CMSComment.cleanupCache(this, comment, CacheCleanupReason.CREATE);
        CMSComment.addCache(this, comment);
    }

    /**
     * @see CMSScript#getArticle(int) Documentation for this method
     */
    public CMSArticle getArticle(int articleid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE, articleid)) {
            return (CMSArticle) getCache().get(CacheGroup.ARTICLE, articleid);
        }
        CMSArticle art = getCMSScript().getArticle(articleid);
        getCache().put(CacheGroup.ARTICLE, articleid, art);
        return art;
    }

    /**
     * @see CMSScript#getArticles(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSArticle> getArticles(int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_LIST)) {
            List<CMSArticle> arts = (List<CMSArticle>) getCache().get(CacheGroup.ARTICLE_LIST);
            if (arts.size() == ((limit == 0) ? getArticleTotalCount() : limit)) {
                return arts;
            } else if ((arts.size() > limit) && (limit != 0)) {
                return arts.subList(0, limit);
            }
        }
        List<CMSArticle> arts = getCMSScript().getArticles(limit);
        getCache().put(CacheGroup.ARTICLE_LIST, arts);
        return arts;
    }

    /**
     * @see CMSScript#getArticleTotalCount() Documentation for this method
     */
    public int getArticleTotalCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_COUNT_TOTAL)) {
            return (Integer) getCache().get(CacheGroup.ARTICLE_COUNT_TOTAL);
        }
        int arts = getCMSScript().getArticleTotalCount();
        getCache().put(CacheGroup.ARTICLE_COUNT_TOTAL, arts);
        return arts;
    }

    /**
     * @see CMSScript#getArticlesFromCategory(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSArticle> getArticlesFromCategory(int catid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.CMSCAT_ARTICLES, catid)) {
            List<CMSArticle> arts = (List<CMSArticle>) getCache().get(CacheGroup.CMSCAT_ARTICLES, catid);
            if (arts.size() == ((limit == 0) ? getArticleCount(catid) : limit)) {
                return arts;
            } else if ((arts.size() > limit) && (limit != 0)) {
                return arts.subList(0, limit);
            }
        }
        List<CMSArticle> arts = getCMSScript().getArticlesFromCategory(catid, limit);
        getCache().put(CacheGroup.CMSCAT_ARTICLES, catid, arts);
        return arts;
    }

    /**
     * @see CMSScript#getArticleCount(int) Documentation for this method
     */
    public int getArticleCount(int catid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_COUNT, catid)) {
            return (Integer) getCache().get(CacheGroup.ARTICLE_COUNT, catid);
        }
        int arts = getCMSScript().getArticleCount(catid);
        getCache().put(CacheGroup.ARTICLE_COUNT, catid, arts);
        return arts;
    }

    /**
     * @see CMSScript#getUserArticles(String, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSArticle> getUserArticles(String username, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_LIST_USER, username)) {
            List<CMSArticle> arts = (List<CMSArticle>) getCache().get(CacheGroup.ARTICLE_LIST_USER, username);
            if (arts.size() == ((limit == 0) ? getUserArticleCount(username) : limit)) {
                return arts;
            } else if ((arts.size() > limit) && (limit != 0)) {
                return arts.subList(0, limit);
            }
        }
        List<CMSArticle> arts = getCMSScript().getUserArticles(username, limit);
        getCache().put(CacheGroup.ARTICLE_LIST_USER, username, arts);
        return arts;
    }

    /**
     * @see CMSScript#getUserArticleCount(String) Documentation for this method
     */
    public int getUserArticleCount(String username) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_COUNT_USER, username)) {
            return (Integer) getCache().get(CacheGroup.ARTICLE_COUNT_USER, username);
        }
        int arts = getCMSScript().getUserArticleCount(username);
        getCache().put(CacheGroup.ARTICLE_COUNT_USER, username, arts);
        return arts;
    }

    /**
     * @see CMSScript#getLastArticle() Documentation for this method
     */
    public CMSArticle getLastArticle() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_LAST)) {
            return (CMSArticle) getCache().get(CacheGroup.ARTICLE_LAST);
        }
        CMSArticle art = getCMSScript().getLastArticle();
        getCache().put(CacheGroup.ARTICLE_LAST, art);
        return art;
    }

    /**
     * @see CMSScript#getLastUserArticle(String) Documentation for this method
     */
    public CMSArticle getLastUserArticle(String username) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_LAST_USER, username)) {
            return (CMSArticle) getCache().get(CacheGroup.ARTICLE_LAST_USER, username);
        }
        CMSArticle art = getCMSScript().getLastUserArticle(username);
        getCache().put(CacheGroup.ARTICLE_LAST_USER, username, art);
        return art;
    }

    /**
     * @see CMSScript#getLastArticleFromCategory(int) Documentation for this method
     */
    public CMSArticle getLastArticleFromCategory(int catid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.ARTICLE_LAST_CATEGORY, catid)) {
            return (CMSArticle) getCache().get(CacheGroup.ARTICLE_LAST_CATEGORY, catid);
        }
        CMSArticle art = getCMSScript().getLastArticleFromCategory(catid);
        getCache().put(CacheGroup.ARTICLE_LAST_CATEGORY, catid, art);
        return art;
    }

    /**
     * @see CMSScript#updateArticle(CMSArticle) Documentation for this method
     */
    public void updateArticle(CMSArticle article) throws UnsupportedMethod, SQLException {
        getCMSScript().updateArticle(article);
        CMSArticle.cleanupCache(this, article, CacheCleanupReason.UPDATE);
        CMSArticle.addCache(this, article);
    }

    /**
     * @see CMSScript#createArticle(CMSArticle) Documentation for this method
     */
    public void createArticle(CMSArticle article) throws UnsupportedMethod, SQLException {
        getCMSScript().createArticle(article);
        CMSArticle.cleanupCache(this, article, CacheCleanupReason.CREATE);
        CMSArticle.addCache(this, article);
    }

    /**
     * @see CMSScript#getCategory(int) Documentation for this method
     */
    public CMSCategory getCategory(int catid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.CMSCAT, catid)) {
            return (CMSCategory) getCache().get(CacheGroup.CMSCAT, catid);
        }
        CMSCategory cat = getCMSScript().getCategory(catid);
        getCache().put(CacheGroup.CMSCAT, catid, cat);
        return cat;
    }

    /**
     * @see CMSScript#getCategories(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSCategory> getCategories(int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.CMSCAT_LIST)) {
            List<CMSCategory> cats = (List<CMSCategory>) getCache().get(CacheGroup.CMSCAT_LIST);
            if (cats.size() == ((limit == 0) ? getCategoryCount() : limit)) {
                return cats;
            } else if ((cats.size() > limit) && (limit != 0)) {
                return cats.subList(0, limit);
            }
        }
        List<CMSCategory> cats = getCMSScript().getCategories(limit);
        getCache().put(CacheGroup.CMSCAT_LIST, cats);
        return cats;
    }

    /**
     * @see CMSScript#getCategoryCount() Documentation for this method
     */
    public int getCategoryCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.CMSCAT_COUNT)) {
            return (Integer) getCache().get(CacheGroup.CMSCAT_COUNT);
        }
        int cats = getCMSScript().getCategoryCount();
        getCache().put(CacheGroup.CMSCAT_COUNT, cats);
        return cats;
    }

    /**
     * @see CMSScript#getSubCategories(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<CMSCategory> getSubCategories(int catid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.CMSCAT_SUBS, catid)) {
            List<CMSCategory> cats = (List<CMSCategory>) getCache().get(CacheGroup.CMSCAT_SUBS, catid);
            if (cats.size() == ((limit == 0) ? getSubCategoryCount(catid) : limit)) {
                return cats;
            } else if ((cats.size() > limit) && (limit != 0)) {
                return cats.subList(0, limit);
            }
        }
        List<CMSCategory> cats = getCMSScript().getSubCategories(catid, limit);
        getCache().put(CacheGroup.CMSCAT_SUBS, catid, cats);
        return cats;
    }

    /**
     * @see CMSScript#getSubCategoryCount(int) Documentation for this method
     */
    public int getSubCategoryCount(int catid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.CMSCAT_SUB_COUNT, catid)) {
            return (Integer) getCache().get(CacheGroup.CMSCAT_SUB_COUNT, catid);
        }
        int cats = getCMSScript().getSubCategoryCount(catid);
        getCache().put(CacheGroup.CMSCAT_SUB_COUNT, catid, cats);
        return cats;
    }

    /**
     * @see CMSScript#updateCategory(CMSCategory) Documentation for this method
     */
    public void updateCategory(CMSCategory category) throws UnsupportedMethod {
        getCMSScript().updateCategory(category);
        CMSCategory.cleanupCache(this, category, CacheCleanupReason.UPDATE);
        CMSCategory.addCache(this, category);
    }

    /**
     * @see CMSScript#createCategory(CMSCategory) Documentation for this method
     */
    public void createCategory(CMSCategory category) throws UnsupportedMethod {
        getCMSScript().createCategory(category);
        CMSCategory.cleanupCache(this, category, CacheCleanupReason.CREATE);
        CMSCategory.addCache(this, category);
    }

    /**
     * @see CMSScript#getUser(String) Documentation for this method
     */
    @Override
    public CMSUser getUser(String username) throws UnsupportedMethod, SQLException {
        return (CMSUser) super.getUser(username);
    }

    /**
     * @see CMSScript#getUser(int) Documentation for this method
     */
    @Override
    public CMSUser getUser(int userid) throws UnsupportedMethod, SQLException {
        return (CMSUser) super.getUser(userid);
    }

    /**
     * @see CMSScript#getLastRegUser() Documentation for this method
     */
    @Override
    public CMSUser getLastRegUser() throws UnsupportedMethod, SQLException {
        return (CMSUser) super.getLastRegUser();
    }

    /**
     * @see CMSScript#updateUser(CMSUser) Documentation for this method
     */
    public void updateUser(CMSUser user) throws SQLException, UnsupportedMethod {
        getCMSScript().updateUser(user);
        ScriptUser.cleanupCache(this, user, CacheCleanupReason.UPDATE);
        ScriptUser.addCache(this, user);
    }

    /**
     * @see CMSScript#createUser(CMSUser) Documentation for this method
     */
    public void createUser(CMSUser user) throws SQLException, UnsupportedMethod {
        getCMSScript().createUser(user);
        ScriptUser.cleanupCache(this, user, CacheCleanupReason.CREATE);
        ScriptUser.addCache(this, user);
    }
}
