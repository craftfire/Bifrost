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
package com.craftfire.bifrost.script;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.cms.CMSArticle;
import com.craftfire.bifrost.classes.cms.CMSCategory;
import com.craftfire.bifrost.classes.cms.CMSComment;
import com.craftfire.bifrost.classes.cms.CMSUser;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

public class CMSScript extends Script {
    protected CMSScript(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    public CMSComment getComment(int commentid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSComment> getComments(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getCommentTotalCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSComment> getCommentsOnArticle(int articleid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getCommentCount(int articleid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSComment> getCommentReplies(int commentid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getCommentReplyCount(int commentid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSComment> getUserComments(String username, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getUserCommentCount(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSComment getLastComment() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSComment getLastUserComment(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSComment getLastCommentOnArticle(int articleid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public void updateComment(CMSComment comment) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public void createComment(CMSComment comment) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSArticle getArticle(int articleid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSArticle> getArticles(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getArticleTotalCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSArticle> getArticlesFromCategory(int catid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getArticleCount(int catid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSArticle> getUserArticles(String username, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getUserArticleCount(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSArticle getLastArticle() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSArticle getLastUserArticle(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSArticle getLastArticleFromCategory(int catid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public void updateArticle(CMSArticle article) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public void createArticle(CMSArticle article) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public CMSCategory getCategory(int catid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSCategory> getCategories(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getCategoryCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public List<CMSCategory> getSubCategories(int catid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public int getSubCategoryCount(int catid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public void updateCategory(CMSCategory category) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    public void createCategory(CMSCategory category) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    @Override
    public CMSUser getUser(String username) throws UnsupportedMethod, SQLException {
        return (CMSUser) super.getUser(username);
    }

    @Override
    public CMSUser getUser(int userid) throws UnsupportedMethod, SQLException {
        return (CMSUser) super.getUser(userid);
    }

    @Override
    public CMSUser getLastRegUser() throws UnsupportedMethod, SQLException {
        return (CMSUser) super.getLastRegUser();
    }

    public void updateUser(CMSUser user) throws SQLException, UnsupportedMethod {
        updateUser((ScriptUser) user);
    }

    public void createUser(CMSUser user) throws SQLException, UnsupportedMethod {
        createUser((ScriptUser) user);
    }
}
