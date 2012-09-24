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
package com.craftfire.bifrost.scripts.cms;

import java.util.List;

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.script.CMSScript;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.classes.cms.CMSArticle;
import com.craftfire.bifrost.classes.cms.CMSCategory;
import com.craftfire.bifrost.classes.cms.CMSComment;
import com.craftfire.bifrost.classes.cms.CMSUser;
import com.craftfire.bifrost.enums.Scripts;

/**
 * This class contains all the methods for WordPress.
 */
public class WordPress extends CMSScript {

    public WordPress(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
        /* TODO: Edit variables */
        this.setScriptName("dupe");
        this.setShortName("dupe");
        this.setVersionRanges(new String[] {"1.0.0"});
    }

    //Start Generic Methods

    @Override
    public String getLatestVersion() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public boolean authenticate(String username, String password) {
        /* TODO: Delete this method or implement it */
        return false;
    }

    @Override
    public String hashPassword(String salt, String password) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public String getUsername(int userid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getUserID(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public CMSUser getUser(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSUser getUser(int userid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSUser getLastRegUser() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateUser(ScriptUser user) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createUser(ScriptUser user) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public List<Group> getGroups(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getGroupID(String group) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public Group getGroup(int groupid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public Group getGroup(String group) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<Group> getUserGroups(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateGroup(Group group) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createGroup(Group group) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public PrivateMessage getPM(int pmid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMs(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMReplies(int pmid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMsSent(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getPMCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getPMReplyCount(int pmid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getPMSentCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getPMReceivedCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public void updatePrivateMessage(PrivateMessage privateMessage) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createPrivateMessage(PrivateMessage privateMessage) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public List<String> getIPs(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<Ban> getBans(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateBan(Ban ban) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void addBan(Ban ban) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public int getBanCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public boolean isBanned(String string) {
        /* TODO: Delete this method or implement it */
        return false;
    }

    @Override
    public boolean isRegistered(String username) {
        /* TODO: Delete this method or implement it */
        return false;
    }

    @Override
    public int getUserCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getGroupCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public String getHomeURL() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    //End Generic Script Methods

    //Start CMS Script Methods

    @Override
    public CMSComment getComment(int commentid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<CMSComment> getComments(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCommentTotalCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSComment> getCommentsOnArticle(int articleid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCommentCount(int articleid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSComment> getCommentReplies(int commentid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCommentReplyCount(int commentid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSComment> getUserComments(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getUserCommentCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public CMSComment getLastComment() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSComment getLastUserComment(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSComment getLastCommentOnArticle(int articleid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateComment(CMSComment comment) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createComment(CMSComment comment) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public CMSArticle getArticle(int articleid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<CMSArticle> getArticles(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getArticleTotalCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSArticle> getArticlesFromCategory(int catid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getArticleCount(int catid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSArticle> getUserArticles(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getUserArticleCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public CMSArticle getLastArticle() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSArticle getLastUserArticle(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSArticle getLastArticleFromCategory(int catid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateArticle(CMSArticle article) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createArticle(CMSArticle article) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public CMSCategory getCategory(int catid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<CMSCategory> getCategories(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCategoryCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSCategory> getSubCategories(int catid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getSubCategoryCount(int catid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public void updateCategory(CMSCategory category) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createCategory(CMSCategory category) {
        /* TODO: Delete this method or implement it */
    }

    //End CMS Script Methods
}