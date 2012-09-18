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

import java.util.List;

import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.script.ForumScript;
import com.craftfire.bifrost.script.Script;
import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

public class MyBB extends ForumScript {
    private final String scriptName = "mybb";
    private final String shortName = "mybb";
    private final Encryption encryption = Encryption.MD5;
    private final String[] versionRanges = {"1.6.8"};
    private String currentUsername = null;

    public MyBB(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    public String[] getVersionRanges() {
        return this.versionRanges;
    }

    public String getLatestVersion() {
        /*TODO*/
        return this.versionRanges[0];
    }

    public Encryption getEncryption() {
        return this.encryption;
    }

    public String getScriptName() {
        return this.scriptName;
    }

    public String getScriptShortname() {
        return this.shortName;
    }

    @Override
    public boolean authenticate(String username, String password) {
        String passwordHash = this.getDataManager().getStringField("users", "password", "`username` = '" + username + "'");
        String passwordSalt = this.getDataManager().getStringField("users", "salt", "`username` = '" + username + "'");
        return hashPassword(passwordSalt, password).equals(passwordHash);
    }

    @Override
    public String hashPassword(String salt, String password) {
        return CraftCommons.encrypt(Encryption.MD5, CraftCommons.encrypt(Encryption.MD5, salt) +
                                                    CraftCommons.encrypt(Encryption.MD5, password));
    }

    @Override
    public boolean isRegistered(String username) {
        /*TODO*/
        return false;
    }

    @Override
    public String getUsername(int userid) {
        return this.getDataManager().getStringField("users", "username", "`uid` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        return this.getDataManager().getIntegerField("users", "uid", "`username` = '" + username + "'");
    }

    public ScriptUser getLastRegUser() {
        return getUser(this.getDataManager().getIntegerField("SELECT `uid` FROM `" +
                this.getDataManager().getPrefix() + "users` ORDER BY `uid` ASC LIMIT 1"));
    }

    @Override
    public ScriptUser getUser(String username) {
        return getUser(getUserID(username));
    }

    @Override
    public int getUserCount() {
        /*TODO*/
        return 0;
    }

    @Override
    public int getGroupCount() {
        /*TODO*/
        return 0;
    }

    @Override
    public String getHomeURL() {
        /*TODO*/
        return null;
    }

    @Override
    public String getForumURL() {
        /*TODO*/
        return null;
    }

    @Override
    public List<String> getIPs(String username) {
        /*TODO*/
        return null;
    }

    @Override
    public List<Ban> getBans(int limit) {
        /*TODO*/
        return null;
    }

    @Override
    public void updateBan(Ban ban) {
        /*TODO*/
    }

    @Override
    public void addBan(Ban ban) {
        /*TODO*/
    }

    @Override
    public int getBanCount() {
        /*TODO*/
        return 0;
    }

    @Override
    public boolean isBanned(String string) {
        /*TODO*/
        return false;
    }

    public ScriptUser getUser(int userid) {
        /*TODO*/
        return null;
    }

    public void updateUser(ScriptUser user) {
        /*TODO*/
    }

    public void createUser(ScriptUser user) {
        /*TODO*/
    }

    public List<Group> getGroups(int limit) {
        /*TODO*/
        return null;
    }

    public int getGroupID(String group) {
        /*TODO*/
        return 0;
    }

    public Group getGroup(int groupid) {
        /*TODO*/
        return null;
    }

    public Group getGroup(String group) {
        /* TODO */
        return null;
    }

    public List<Group> getUserGroups(String username) {
        /*TODO*/
        return null;
    }

    public void updateGroup(Group group) {
        /*TODO*/
    }

    public void createGroup(Group group) {
        /*TODO*/
    }

    public PrivateMessage getPM(int pmid) {
        /*TODO*/
        return null;
    }

    public List<PrivateMessage> getPMsSent(String username, int limit) {
        /*TODO*/
        return null;
    }

    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        /*TODO*/
        return null;
    }

    public int getPMSentCount(String username) {
        /*TODO*/
        return 0;
    }

    public int getPMReceivedCount(String username) {
        /*TODO*/
        return 0;
    }

    public void updatePrivateMessage(PrivateMessage privateMessage) {
        /*TODO*/
    }

    public void createPrivateMessage(PrivateMessage privateMessage) {
        /*TODO*/
    }

    public int getPostCount(String username) {
        /*TODO*/
        return 0;
    }

    public int getTotalPostCount() {
        /*TODO*/
        return 0;
    }

    public ForumPost getLastPost() {
        /*TODO*/
        return null;
    }

    public ForumPost getLastUserPost(String username) {
        /*TODO*/
        return null;
    }

    public List<ForumPost> getPosts(int limit) {
        /*TODO*/
        return null;
    }

    public List<ForumPost> getPostsFromThread(int threadid, int limit) {
        /*TODO*/
        return null;
    }

    public ForumPost getPost(int postid) {
        /*TODO*/
        return null;
    }

    public void updatePost(ForumPost post) {
        /*TODO*/
    }

    public void createPost(ForumPost post) {
        /*TODO*/
    }

    public int getThreadCount(String username) {
        /*TODO*/
        return 0;
    }

    public int getTotalThreadCount() {
        /*TODO*/
        return 0;
    }

    public ForumThread getLastThread() {
        /*TODO*/
        return null;
    }

    public ForumThread getLastUserThread(String username) {
        /*TODO*/
        return null;
    }

    public ForumThread getThread(int threadid) {
        /*TODO*/
        return null;
    }

    public List<Thread> getThreads(int limit) {
        /*TODO*/
        return null;
    }

    public void updateThread(ForumThread thread) {
        /*TODO*/
    }

    @Override
    public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        /*TODO*/
    }
}
