/*
 * This file is part of AuthAPI.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * AuthAPI is licensed under the GNU Lesser General Public License.
 *
 * AuthAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.authapi;

import com.craftfire.authapi.classes.*;
import com.craftfire.authapi.classes.Thread;
import com.craftfire.authapi.enums.CacheGroup;
import com.craftfire.authapi.exceptions.UnsupportedFunction;
import com.craftfire.authapi.exceptions.UnsupportedScript;
import com.craftfire.authapi.exceptions.UnsupportedVersion;
import com.craftfire.authapi.scripts.forum.SMF;
import com.craftfire.authapi.scripts.forum.XenForo;
import com.craftfire.commons.managers.CacheManager;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

import java.sql.SQLException;
import java.util.List;

public class ScriptAPI implements ScriptInterface {
    private Script script;
    private final Scripts scriptName;
    private final String version;

    public enum Scripts {
        SMF("simplemachines"),
        XF("xenforo");

        public String alias;
        Scripts(String alias) {
            this.alias = alias;
        }
    }

    /**
     * @param script  The script using the enum list, for example: Scripts.SMF.
     * @param version The version that the user has set in his config.
     * @throws UnsupportedVersion if the input version is not found in the list of supported versions.
     */
    public ScriptAPI(Scripts script, String version) throws UnsupportedVersion {
        this.scriptName = script;
        this.version = version;
        setScript();
        if (!this.script.isSupportedVersion()) {
            throw new UnsupportedVersion();
        }
    }

    /**
     * @param script  The script in a string, for example: smf.
     * @param version The version that the user has set in his config.
     * @throws UnsupportedScript if the input string is not found in the list of supported scripts.
     * @throws UnsupportedVersion if the input version is not found in the list of supported versions.
     */
    public ScriptAPI(String script, String version) throws UnsupportedScript, UnsupportedVersion {
        this.scriptName = stringToScript(script);
        this.version = version;
        setScript();
        if (!this.script.isSupportedVersion()) {
            throw new UnsupportedVersion();
        }
    }

    /**
     * @return The Script object.
     */
    public Script getScript() {
        return this.script;
    }

    /**
     * Sets the script which is being used.
     */
    private void setScript() {
        switch (this.scriptName) {
            case SMF:
                this.script = new SMF(this.scriptName, this.version);
            case XF:
                this.script = new XenForo(this.scriptName, this.version);
        }
    }

    /**
     * Converts a string into a script enum.
     *
     * @param string The string which contains the script name
     * @return The script for the string, if none are found it returns null.
     * @throws UnsupportedScript if the input string is not found in the list of supported scripts.
     */
    private Scripts stringToScript(String string) throws UnsupportedScript {
        for (Scripts script : Scripts.values()) {
            if (string.equalsIgnoreCase(script.toString()) || string.equalsIgnoreCase(script.alias)) {
                return script;
            } else if (script.alias.contains(",")) {
                String[] aliases = script.alias.split(",");
                for (int i=0; aliases.length>i; i++) {
                    if (string.equalsIgnoreCase(aliases[i])) {
                        return script;
                    }
                }
            }
        }
        throw new UnsupportedScript();
    }

    public AuthAPI getAuthAPI() {
        return AuthAPI.getInstance();
    }

    public LoggingManager getLoggingManager() {
        return AuthAPI.getInstance().getLoggingManager();
    }

    public CacheManager getCacheManager() {
        return AuthAPI.getInstance().getCacheManager();
    }

    public Cache getCache() {
        return AuthAPI.getInstance().getCache();
    }

    public DataManager getDataManager() {
        return AuthAPI.getInstance().getDataManager();
    }

    @Override
    public ScriptUser getUser(String username) throws UnsupportedFunction {
        int id = this.script.getUserID(username);
        return this.getUser(id);
    }

    @Override
    public ScriptUser getUser(int userid) throws UnsupportedFunction {
        if (ScriptUser.hasCache(userid)) {
            return ScriptUser.getCache(userid);
        }
        ScriptUser user = this.script.getUser(userid);
        ScriptUser.addCache(user);
        return user;
    }

    @Override
    public ScriptUser getLastRegUser() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.USER_LAST_REG)) {
            return (ScriptUser) Cache.get(CacheGroup.USER_LAST_REG);
        }
        ScriptUser user = this.script.getLastRegUser();
        Cache.put(CacheGroup.USER_LAST_REG, user);
        return user;
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        this.script.updateUser(user);
        ScriptUser.addCache(user);
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        this.script.createUser(user);
        ScriptUser.addCache(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getGroups(int limit) throws SQLException, UnsupportedFunction {
        if (Cache.contains(CacheGroup.GROUP_LIST)) {
            return (List<Group>) Cache.get(CacheGroup.GROUP_LIST);
        }
        List<Group> groups = this.script.getGroups(limit);
        Cache.put(CacheGroup.GROUP_LIST, groups);
        return groups;
    }

    @Override
    public int getGroupID(String group) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.GROUP_ID, group)) {
            return (Integer) Cache.get(CacheGroup.GROUP_ID, group);
        }
        int groupID = this.getGroupID(group);
        Cache.put(CacheGroup.GROUP_ID, groupID, group);
        return groupID;
    }

    @Override
    public Group getGroup(int groupID) throws UnsupportedFunction {
        if (Group.hasCache(groupID)) {
            return Group.getCache(groupID);
        }
        Group group = this.script.getGroup(groupID);
        Group.addCache(group);
        return group;
    }

    @Override
    public Group getGroup(String groupString) throws UnsupportedFunction {
        if (Group.hasCache(groupString)) {
            return Group.getCache(groupString);
        }
        Group group = this.script.getGroup(groupString);
        Group.addCache(group);
        return group;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getUserGroups(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.USER_GROUP, username)) {
            return (List<Group>) Cache.get(CacheGroup.USER_GROUP, username);
        }
        List<Group> groups = this.script.getUserGroups(username);
        Cache.put(CacheGroup.USER_GROUP, username, groups);
        return groups;
    }

    @Override
    public void updateGroup(Group group) throws SQLException, UnsupportedFunction {
        this.script.updateGroup(group);
        Group.addCache(group);
    }

    @Override
    public void createGroup(Group group) throws SQLException, UnsupportedFunction {
        this.script.createGroup(group);
        Group.addCache(group);
    }

    @Override
    public PrivateMessage getPM(int pmid) throws UnsupportedFunction {
        if (PrivateMessage.hasCache(pmid)) {
            return PrivateMessage.getCache(pmid);
        }
        PrivateMessage pm = this.script.getPM(pmid);
        PrivateMessage.addCache(pm);
        return pm;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsSent(String username, int limit) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.PM_SENT, username)) {
            return (List<PrivateMessage>) Cache.get(CacheGroup.PM_RECEIVED, username);
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        Cache.put(CacheGroup.PM_SENT, username, pms);
        return pms;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.PM_RECEIVED, username)) {
            return (List<PrivateMessage>) Cache.get(CacheGroup.PM_RECEIVED, username);
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        Cache.put(CacheGroup.PM_RECEIVED, username, pms);
        return pms;
    }

    @Override
    public int getPMSentCount(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.PM_SENT_COUNT, username)) {
            return (Integer) Cache.get(CacheGroup.PM_SENT_COUNT, username);
        }
        int count = this.script.getPMSentCount(username);
        Cache.put(CacheGroup.PM_SENT_COUNT, username, count);
        return count;
    }

    @Override
    public int getPMReceivedCount(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.PM_RECEIVED_COUNT, username)) {
            return (Integer) Cache.get(CacheGroup.PM_RECEIVED_COUNT, username);
        }
        int count = this.script.getPMReceivedCount(username);
        Cache.put(CacheGroup.PM_RECEIVED_COUNT, username, count);
        return count;
    }

    @Override
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        this.script.updatePrivateMessage(privateMessage);
        PrivateMessage.addCache(privateMessage);
    }

    @Override
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        this.script.createPrivateMessage(privateMessage);
        PrivateMessage.addCache(privateMessage);
    }

    @Override
    public Post getPost(int postID) throws UnsupportedFunction {
        if (Post.hasCache(postID)) {
            return Post.getCache(postID);
        }
        Post post = this.script.getPost(postID);
        Post.addCache(post);
        return post;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> getPosts(int limit) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.POST_LIST)) {
            return (List<Post>) Cache.get(CacheGroup.POST_LIST);
        }
        List<Post> posts = this.script.getPosts(limit);
        Cache.put(CacheGroup.POST_LIST, posts);
        return posts;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> getPostsFromThread(int threadid, int limit) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.THREAD_POSTS, threadid)) {
            return (List<Post>) Cache.get(CacheGroup.THREAD_POSTS, threadid);
        }
        List<Post> posts = this.script.getPostsFromThread(threadid, limit);
        Cache.put(CacheGroup.THREAD_POSTS, posts);
        return posts;
    }

    @Override
    public void updatePost(Post post) throws SQLException, UnsupportedFunction {
        this.script.updatePost(post);
        Post.addCache(post);
    }

    @Override
    public void createPost(Post post) throws SQLException, UnsupportedFunction {
        this.script.createPost(post);
        Post.addCache(post);
    }

    @Override
    public int getPostCount(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.POST_COUNT, username)) {
            return (Integer) Cache.get(CacheGroup.POST_COUNT, username);
        }
        int count = this.script.getPostCount(username);
        Cache.put(CacheGroup.POST_COUNT, username, count);
        return count;
    }

    @Override
    public int getTotalPostCount() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.POST_COUNT_TOTAL)) {
            return (Integer) Cache.get(CacheGroup.POST_COUNT_TOTAL);
        }
        int count = this.script.getTotalPostCount();
        Cache.put(CacheGroup.POST_COUNT_TOTAL, count);
        return count;
    }

    @Override
    public Post getLastPost() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.POST_LAST)) {
            return (Post) Cache.get(CacheGroup.POST_LAST);
        }
        Post post = this.script.getLastPost();
        Cache.put(CacheGroup.POST_LAST, post);
        return post;
    }

    @Override
    public Post getLastUserPost(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.POST_LAST_USER, username)) {
            return (Post) Cache.get(CacheGroup.POST_LAST_USER, username);
        }
        Post post = this.script.getLastUserPost(username);
        Cache.put(CacheGroup.POST_LAST_USER, username, post);
        return post;
    }

    @Override
    public int getTotalThreadCount() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.THREAD_COUNT_TOTAL)) {
            return (Integer) Cache.get(CacheGroup.THREAD_COUNT_TOTAL);
        }
        int count = this.script.getTotalThreadCount();
        Cache.put(CacheGroup.THREAD_COUNT_TOTAL, count);
        return count;
    }

    @Override
    public int getThreadCount(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.THREAD_COUNT, username)) {
            return (Integer) Cache.get(CacheGroup.THREAD_COUNT, username);
        }
        int count = this.script.getThreadCount(username);
        Cache.put(CacheGroup.THREAD_COUNT, username, count);
        return count;
    }

    @Override
    public Thread getLastThread() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.THREAD_LAST)) {
            return (Thread) Cache.get(CacheGroup.THREAD_LAST);
        }
        Thread thread = this.script.getLastThread();
        Cache.put(CacheGroup.THREAD_LAST, thread);
        return thread;
    }

    @Override
    public Thread getLastUserThread(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.THREAD_LAST_USER, username)) {
            return (Thread) Cache.get(CacheGroup.THREAD_LAST_USER, username);
        }
        Thread thread = this.script.getLastUserThread(username);
        Cache.put(CacheGroup.THREAD_LAST_USER, username, thread);
        return thread;
    }

    @Override
    public Thread getThread(int threadID) throws UnsupportedFunction {
        if (Thread.hasCache(threadID)) {
            return Thread.getCache(threadID);
        }
        Thread thread = this.script.getThread(threadID);
        Thread.addCache(thread);
        return thread;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Thread> getThreads(int limit) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.THREAD_LIST)) {
            return (List<Thread>) Cache.get(CacheGroup.THREAD_LIST);
        }
        List<Thread> threads = this.script.getThreads(limit);
        Cache.put(CacheGroup.THREAD_LIST, threads);
        return threads;
    }

    @Override
    public void updateThread(Thread thread) throws SQLException, UnsupportedFunction {
        this.script.updateThread(thread);
        Thread.addCache(thread);
    }

    @Override
    public void createThread(Thread thread) throws SQLException, UnsupportedFunction {
        this.script.createThread(thread);
        Thread.addCache(thread);
    }

    @Override
    public int getUserCount() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.USER_COUNT)) {
            return (Integer) Cache.get(CacheGroup.USER_COUNT);
        }
        int count = this.script.getUserCount();
        Cache.put(CacheGroup.USER_COUNT, count);
        return count;
    }

    @Override
    public int getGroupCount() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.GROUP_COUNT)) {
            return (Integer) Cache.get(CacheGroup.GROUP_COUNT);
        }
        int count = this.script.getGroupCount();
        Cache.put(CacheGroup.GROUP_COUNT, count);
        return count;
    }

    @Override
    public String getHomeURL() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.URL_HOME)) {
            return (String) Cache.get(CacheGroup.URL_HOME);
        }
        String url = this.script.getHomeURL();
        Cache.put(CacheGroup.URL_HOME, url);
        return url;
    }

    @Override
    public String getForumURL() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.URL_FORUM)) {
            return (String) Cache.get(CacheGroup.URL_FORUM);
        }
        String url = this.script.getForumURL();
        Cache.put(CacheGroup.URL_FORUM, url);
        return url;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getIPs(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.USER_IP, username)) {
            return (List<String>) Cache.get(CacheGroup.USER_IP, username);
        }
        List<String> ips = this.script.getIPs(username);
        Cache.put(CacheGroup.USER_IP, ips, username);
        return ips;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ban> getBans(int limit) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.BAN_LIST)) {
            return (List<Ban>) Cache.get(CacheGroup.BAN_LIST);
        }
        List<Ban> bans = this.script.getBans(limit);
        Cache.put(CacheGroup.BAN_LIST, bans);
        return bans;
    }

    @Override
    public void updateBan(Ban ban) throws SQLException, UnsupportedFunction {
        this.script.updateBan(ban);
        Ban.addCache(ban);
    }

    @Override
    public void addBan(Ban ban) throws SQLException, UnsupportedFunction {
        this.script.addBan(ban);
        Ban.addCache(ban);
    }

    @Override
    public int getBanCount() throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.BAN_COUNT)) {
            return (Integer) Cache.get(CacheGroup.BAN_COUNT);
        }
        int count = this.script.getBanCount();
        Cache.put(CacheGroup.BAN_COUNT, count);
        return count;
    }

    @Override
    public boolean isBanned(String string) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.IS_BANNED, string)) {
            return (Boolean) Cache.get(CacheGroup.IS_BANNED, string);
        }
        boolean banned = this.script.isBanned(string);
        Cache.put(CacheGroup.IS_BANNED, banned, string);
        return banned;
    }

    @Override
    public boolean isRegistered(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.IS_REGISTERED, username)) {
            return (Boolean) Cache.get(CacheGroup.IS_REGISTERED, username);
        }
        boolean registered = this.script.isRegistered(username);
        Cache.put(CacheGroup.IS_REGISTERED, registered, username);
        return registered;
    }

    @Override
    public String getLatestVersion() {
        return this.script.getLatestVersion();
    }

    @Override
    public boolean isSupportedVersion() {
        return this.script.isSupportedVersion();
    }

    @Override
    public String getVersion() throws UnsupportedFunction {
        return this.script.getVersion();
    }

    @Override
    public String[] getVersionRanges() {
        return this.script.getVersionRanges();
    }

    @Override
    public String getEncryption() throws UnsupportedFunction {
        return this.script.getEncryption();
    }

    @Override
    public String getScriptName() throws UnsupportedFunction {
        return this.script.getScriptName();
    }

    @Override
    public String getScriptShortname() throws UnsupportedFunction {
        return this.script.getScriptShortname();
    }

    public boolean authenticate(String username, String password) throws UnsupportedFunction {
        return this.script.authenticate(username, password);
    }

    @Override
    public String hashPassword(String salt, String password) throws UnsupportedFunction {
        return this.script.hashPassword(salt, password);
    }

    @Override
    public String getUsername(int userid) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.USER_USERNAME, userid)) {
            return (String) Cache.get(CacheGroup.USER_USERNAME, userid);
        }
        String username = this.script.getUsername(userid);
        Cache.put(CacheGroup.USER_USERNAME, username, userid);
        return username;
    }

    @Override
    public int getUserID(String username) throws UnsupportedFunction {
        if (Cache.contains(CacheGroup.USER_ID, username)) {
            return (Integer) Cache.get(CacheGroup.USER_ID, username);
        }
        int userID = this.script.getUserID(username);
        Cache.put(CacheGroup.USER_ID, userID, username);
        return userID;
    }
}
