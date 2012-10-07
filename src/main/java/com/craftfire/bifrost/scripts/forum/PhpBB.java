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
package com.craftfire.bifrost.scripts.forum;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.classes.Version;
import com.craftfire.commons.classes.VersionRange;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.Results;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.forum.ForumBoard;
import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumScript;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.forum.ForumUser;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Scripts;

/**
 * This class contains all the methods for phpBB.
 */
public class PhpBB extends ForumScript {

    /**
     * Default constructor for phpBB.
     *
     * @param script       the {@link Scripts} enum
     * @param version      the version of the script
     * @param dataManager  the {@link DataManager}
     */
    public PhpBB(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
        /* TODO: Edit variables */
        setScriptName("phpbb");
        setShortName("phpbb");
        setVersionRanges(new VersionRange[]{new VersionRange("3.0.0", "3.0.11")});
    }

    //Start Generic Script Methods

    @Override
    public Version getLatestVersion() {
        return getVersionRanges()[0].getMax();
    }

    @Override
    public boolean authenticate(String username, String password) {
        String passwordHash = this.getDataManager().getStringField("users",
                "user_password", "`username` = '" + username + "'");
        return hashPassword(username, password).equals(passwordHash);
    }

    @Override
    public String hashPassword(String username, String password) {
        return CraftCommons.encrypt(Encryption.PHPASS, username.toLowerCase() + password, null, 0);
    }

    @Override
    public String getUsername(int userid) {
        return this.getDataManager().getStringField("users", "username", "`user_id` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        return this.getDataManager().getIntegerField("users", "user_id", "`username` = '" + username + "'");
    }

    @Override
    public ForumUser getUser(String username) throws SQLException {
        return getUser(getUserID(username));
    }

    @Override
    public ForumUser getUser(int userid) throws SQLException {
        /*TODO*/
        Results results = getDataManager().getResults("SELECT * FROM `" + getDataManager().getPrefix() +
                                                      "` WHERE `user_id` = " + userid);
        DataRow row = results.getFirstResult();
        ForumUser user = new ForumUser(this,
                                       row.getIntField("user_id"),
                                       row.getStringField("username"),
                                       row.getStringField("user_password"));
        //user.setActivated();
        //user.setAvatarURL();
        //user.setBirthday();
        user.setEmail(row.getStringField("user_email"));
        //user.setGender();
        //user.setLastIP();
        //user.setLastLogin();

        user.setRegDate(new Date(row.getIntField("user_regdate")));
        user.setRegIP(row.getStringField("user_regdate"));
        return user;
    }

    @Override
    public ForumUser getLastRegUser() throws SQLException {
        return getUser(getDataManager().getLastID("user_id", "users"));
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
    public List<Group> getGroups(int limit) throws SQLException {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<Group> groups = new ArrayList<Group>();
        Results results = getDataManager().getResults("SELECT `group_id` FROM `" +
                          getDataManager().getPrefix() + "groups` ORDER BY `group_id` ASC" + limitstring);
        for (DataRow row : results.getArray()) {
            groups.add(getGroup(row.getIntField("group_id")));
        }
        return groups;
    }

    @Override
    public int getGroupID(String group) {
        return this.getDataManager().getIntegerField("groups", "group_id", "`group_name` = '" + group + "'");
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

    //Start Forum Script Methods

    @Override
    public int getPostCountInThread(int threadid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getPostCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getTotalPostCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public ForumPost getLastPost() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public ForumPost getLastUserPost(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumPost> getPosts(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumPost> getPostsFromThread(int threadid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumPost> getUserPosts(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public ForumPost getPost(int postid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updatePost(ForumPost post) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createPost(ForumPost post) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public int getThreadCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getTotalThreadCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public ForumThread getLastThread() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public ForumThread getLastUserThread(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public ForumThread getThread(int threadid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumThread> getThreadsFromBoard(int boardid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumThread> getUserThreads(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumThread> getThreads(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateThread(ForumThread thread) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createThread(ForumThread thread) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public int getBoardCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getSubBoardCount(int boardid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public ForumBoard getBoard(int boardid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumBoard> getSubBoards(int boardid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<ForumBoard> getBoards(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateBoard(ForumBoard board) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createBoard(ForumBoard board) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public String getForumURL() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    //End Forum Script Methods
}