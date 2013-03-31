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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.Results;
import com.craftfire.commons.encryption.Encryption;
import com.craftfire.commons.util.Version;
import com.craftfire.commons.util.VersionRange;

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
        String passwordHash = getDataManager().getStringField("users",
                "user_password", "`username` = '" + username + "'");
        return hashPassword(username, password).equals(passwordHash);
    }

    @Override
    public String hashPassword(String username, String password) {
        return CraftCommons.encrypt(Encryption.PHPASS_H, username.toLowerCase() + password, null, 0);
    }

    @Override
    public String getUsername(int userid) {
        return getDataManager().getStringField("users", "username", "`user_id` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        return getDataManager().getIntegerField("users", "user_id", "`username` = '" + username + "'");
    }

    @Override
    public ForumUser getUser(String username) throws SQLException {
        return getUser(getUserID(username));
    }

    @Override
    public ForumUser getUser(int userid) throws SQLException {
        Results results = getDataManager().getResults("SELECT * FROM `" + getDataManager().getPrefix() +
                                                      "users` WHERE `user_id` = " + userid);
        DataRow row = results.getFirstResult();
        ForumUser user = new ForumUser(this,
                                       row.getIntField("user_id"),
                                       row.getStringField("username"),
                                       row.getStringField("user_password"));
        user.setActivated(row.getIntField("user_type") == 0);
        if (!row.getStringField("user_birthday").isEmpty()) {
            try {
                user.setBirthday(new SimpleDateFormat("dd-MM-yyyy").parse(row.getStringField("user_birthday")));
            } catch (ParseException e) {
                getLoggingManager().stackTrace(e);
            }
        }
        user.setEmail(row.getStringField("user_email"));
        user.setLastLogin(new Date(row.getLongField("user_lastvisit") * 1000));
        user.setRegDate(new Date(row.getLongField("user_regdate") * 1000));
        user.setRegIP(row.getStringField("user_ip"));

        return user;
    }

    @Override
    public ForumUser getLastRegUser() throws SQLException {
        return getUser(getDataManager().getLastID("user_id", "users"));
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("username", user.getUsername());
        data.put("username_clean", user.getUsername().toLowerCase());
        data.put("user_email", user.getEmail());
        if (user.isActivated()) {
            data.put("user_inactive_reason", 0);
            data.put("user_inactive_time", 0);
            data.put("user_type", 0);
        } else {
            data.put("user_inactive_reason", 1);
            data.put("user_inactive_time", user.getRegDate().getTime() / 1000);
            data.put("user_type", 1);
        }
        data.put("user_regdate", user.getRegDate().getTime() / 1000);
        data.put("user_lastvisit", user.getLastLogin().getTime() / 1000);

        if (user.getBirthday() != null) {
            data.put("user_birthday", new SimpleDateFormat("dd-MM-yyyy").format(user.getBirthday()));
        }
        if (user.getPassword().length() != 34) {
            user.setPassword(hashPassword(user.getUsername(), user.getPassword()));
            data.put("user_password", user.getPassword());
        }
        getDataManager().updateFields(data, "users", "`user_id` = '" + user.getID() + "'");
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException {
        long timestamp = new Date().getTime() / 1000;
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("username", user.getUsername());
        data.put("username_clean", user.getUsername().toLowerCase());
        data.put("user_email", user.getEmail());
        data.put("user_email_hash", CraftCommons.encrypt(Encryption.CRC32,
                                                         user.getEmail().toLowerCase()) + user.getEmail().length());
        if (user.isActivated()) {
            data.put("user_inactive_reason", 0);
            data.put("user_inactive_time", 0);
            data.put("user_type", 0);
        } else {
            data.put("user_inactive_reason", 1);
            data.put("user_inactive_time", timestamp);
            data.put("user_type", 1);
        }
        data.put("user_regdate", timestamp);
        data.put("user_lastvisit", timestamp);
        if (user.getBirthday() != null) {
            data.put("user_birthday", new SimpleDateFormat("dd-MM-yyyy").format(user.getBirthday()));
        }
        if (user.getPassword().length() != 34) {
            user.setPassword(hashPassword(user.getUsername(), user.getPassword()));
            data.put("user_password", user.getPassword());
        }
        data.put("group_id", 2);
        data.put("user_ip", user.getRegIP());
        data.put("user_passchg", timestamp);
        data.put("user_lang", "en");
        getDataManager().insertFields(data, "users");
        user.setID(this.getDataManager().getLastID("user_id", "users"));

        data = new HashMap<String, Object>();
        data.put("group_id", 2);
        data.put("user_id", user.getID());
        data.put("user_pending", 0);
        getDataManager().insertFields(data, "user_group");

        data = new HashMap<String, Object>();
        data.put("group_id", 7);
        data.put("user_id", user.getID());
        data.put("user_pending", 0);
        getDataManager().insertFields(data, "user_group");

        getDataManager().updateField("config", "config_value", user.getID(), "`config_name` = 'newest_user_id'");
        getDataManager().updateField("config", "config_value", user.getUsername(), "`config_name` = 'newest_username'");
        getDataManager().increaseField("config", "config_value", "`config_name` = 'num_users'");
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
        return this.getDataManager().exist("users", "username", username);
    }

    @Override
    public int getUserCount() {
        return this.getDataManager().getCount("users");
    }

    @Override
    public int getGroupCount() {
        return this.getDataManager().getCount("groups");
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