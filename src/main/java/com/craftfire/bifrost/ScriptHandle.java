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
package com.craftfire.bifrost;

import com.craftfire.bifrost.classes.*;
import com.craftfire.bifrost.classes.Thread;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.exceptions.UnsupportedScript;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.commons.managers.DataManager;

import java.sql.SQLException;
import java.util.List;

public class ScriptHandle implements ScriptInterface {
    private Script script;
    private final Scripts scriptName;
    private final String version;
    private final DataManager dataManager;

    /**
     * @param script  The script using the enum list, for example: Scripts.SMF.
     * @param version The version that the user has set in his config.
     * @throws UnsupportedVersion if the input version is not found in the list of supported versions.
     */
    public ScriptHandle(Scripts script, String version, DataManager dataManager) throws UnsupportedVersion {
        this.scriptName = script;
        this.version = version;
        this.dataManager = dataManager;
        this.script = ScriptAPI.setScript(this.scriptName, version, dataManager);
        if (!this.script.isSupportedVersion()) {
            throw new UnsupportedVersion();
        }
    }

    /**
     * @param script  The script in a string, for example: smf.
     * @param version The version that the user has set in his config.
     * @throws com.craftfire.bifrost.exceptions.UnsupportedScript if the input string is not found in the list of supported scripts.
     * @throws UnsupportedVersion if the input version is not found in the list of supported versions.
     */
    public ScriptHandle(String script, String version, DataManager dataManager) throws UnsupportedScript,
            UnsupportedVersion {
        this.scriptName = ScriptAPI.stringToScript(script);
        this.version = version;
        this.dataManager = dataManager;
        this.script = ScriptAPI.setScript(this.scriptName, version, dataManager);
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

    public Cache getCache() {
        return this.script.getCache();
    }

    public Ban newBan(String name, String email, String ip) {
        return new Ban(this.script, name, email, ip);
    }

    public Group newGroup(String groupname) {
        return new Group(this.script, groupname);
    }

    public Post newPost(int threadid, int boardid) {
        return new Post(this.script, threadid, boardid);
    }

    public PrivateMessage newPrivateMessage(ScriptUser sender, List<ScriptUser> recipients) {
        return new PrivateMessage(this.script, sender, recipients);
    }

    public ScriptUser newScriptUser(String username, String password) {
        return new ScriptUser(this.script, username, password);
    }

    public Thread newThread(int boardid) {
        return new Thread(this.script, boardid);
    }

    @Override
    public ScriptUser getUser(String username) throws UnsupportedFunction {
        int id = this.script.getUserID(username);
        return this.getUser(id);
    }

    @Override
    public ScriptUser getUser(int userid) throws UnsupportedFunction {
        if (ScriptUser.hasCache(this, userid)) {
            return ScriptUser.getCache(this, userid);
        }
        ScriptUser user = this.script.getUser(userid);
        ScriptUser.addCache(this, user);
        return user;
    }

    @Override
    public ScriptUser getLastRegUser() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_LAST_REG)) {
            return (ScriptUser) this.script.getCache().get(CacheGroup.USER_LAST_REG);
        }
        ScriptUser user = this.script.getLastRegUser();
        this.script.getCache().put(CacheGroup.USER_LAST_REG, user);
        return user;
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        this.script.updateUser(user);
        ScriptUser.addCache(this, user);
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        this.script.createUser(user);
        ScriptUser.addCache(this, user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getGroups(int limit) throws SQLException, UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.GROUP_LIST)) {
            return (List<Group>) this.script.getCache().get(CacheGroup.GROUP_LIST);
        }
        List<Group> groups = this.script.getGroups(limit);
        this.script.getCache().put(CacheGroup.GROUP_LIST, groups);
        return groups;
    }

    @Override
    public int getGroupID(String group) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.GROUP_ID, group)) {
            return (Integer) this.script.getCache().get(CacheGroup.GROUP_ID, group);
        }
        int groupID = this.getGroupID(group);
        this.script.getCache().put(CacheGroup.GROUP_ID, groupID, group);
        return groupID;
    }

    @Override
    public Group getGroup(int groupID) throws UnsupportedFunction {
        if (Group.hasCache(this, groupID)) {
            return Group.getCache(this, groupID);
        }
        Group group = this.script.getGroup(groupID);
        Group.addCache(this, group);
        return group;
    }

    @Override
    public Group getGroup(String groupString) throws UnsupportedFunction {
        if (Group.hasCache(this, groupString)) {
            return Group.getCache(this, groupString);
        }
        Group group = this.script.getGroup(groupString);
        Group.addCache(this, group);
        return group;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Group> getUserGroups(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_GROUP, username)) {
            return (List<Group>) this.script.getCache().get(CacheGroup.USER_GROUP, username);
        }
        List<Group> groups = this.script.getUserGroups(username);
        this.script.getCache().put(CacheGroup.USER_GROUP, username, groups);
        return groups;
    }

    @Override
    public void updateGroup(Group group) throws SQLException, UnsupportedFunction {
        this.script.updateGroup(group);
        Group.addCache(this, group);
    }

    @Override
    public void createGroup(Group group) throws SQLException, UnsupportedFunction {
        this.script.createGroup(group);
        Group.addCache(this, group);
    }

    @Override
    public PrivateMessage getPM(int pmid) throws UnsupportedFunction {
        if (PrivateMessage.hasCache(this, pmid)) {
            return PrivateMessage.getCache(this, pmid);
        }
        PrivateMessage pm = this.script.getPM(pmid);
        PrivateMessage.addCache(this, pm);
        return pm;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsSent(String username, int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_SENT, username)) {
            return (List<PrivateMessage>) this.script.getCache().get(CacheGroup.PM_RECEIVED, username);
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        this.script.getCache().put(CacheGroup.PM_SENT, username, pms);
        return pms;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_RECEIVED, username)) {
            return (List<PrivateMessage>) this.script.getCache().get(CacheGroup.PM_RECEIVED, username);
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        this.script.getCache().put(CacheGroup.PM_RECEIVED, username, pms);
        return pms;
    }

    @Override
    public int getPMSentCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_SENT_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.PM_SENT_COUNT, username);
        }
        int count = this.script.getPMSentCount(username);
        this.script.getCache().put(CacheGroup.PM_SENT_COUNT, username, count);
        return count;
    }

    @Override
    public int getPMReceivedCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.PM_RECEIVED_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.PM_RECEIVED_COUNT, username);
        }
        int count = this.script.getPMReceivedCount(username);
        this.script.getCache().put(CacheGroup.PM_RECEIVED_COUNT, username, count);
        return count;
    }

    @Override
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        this.script.updatePrivateMessage(privateMessage);
        PrivateMessage.addCache(this, privateMessage);
    }

    @Override
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        this.script.createPrivateMessage(privateMessage);
        PrivateMessage.addCache(this, privateMessage);
    }

    @Override
    public Post getPost(int postID) throws UnsupportedFunction {
        if (Post.hasCache(this, postID)) {
            return Post.getCache(this, postID);
        }
        Post post = this.script.getPost(postID);
        Post.addCache(this, post);
        return post;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> getPosts(int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_LIST)) {
            return (List<Post>) this.script.getCache().get(CacheGroup.POST_LIST);
        }
        List<Post> posts = this.script.getPosts(limit);
        this.script.getCache().put(CacheGroup.POST_LIST, posts);
        return posts;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> getPostsFromThread(int threadid, int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_POSTS, threadid)) {
            return (List<Post>) this.script.getCache().get(CacheGroup.THREAD_POSTS, threadid);
        }
        List<Post> posts = this.script.getPostsFromThread(threadid, limit);
        this.script.getCache().put(CacheGroup.THREAD_POSTS, posts);
        return posts;
    }

    @Override
    public void updatePost(Post post) throws SQLException, UnsupportedFunction {
        this.script.updatePost(post);
        Post.addCache(this, post);
    }

    @Override
    public void createPost(Post post) throws SQLException, UnsupportedFunction {
        this.script.createPost(post);
        Post.addCache(this, post);
    }

    @Override
    public int getPostCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.POST_COUNT, username);
        }
        int count = this.script.getPostCount(username);
        this.script.getCache().put(CacheGroup.POST_COUNT, username, count);
        return count;
    }

    @Override
    public int getTotalPostCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_COUNT_TOTAL)) {
            return (Integer) this.script.getCache().get(CacheGroup.POST_COUNT_TOTAL);
        }
        int count = this.script.getTotalPostCount();
        this.script.getCache().put(CacheGroup.POST_COUNT_TOTAL, count);
        return count;
    }

    @Override
    public Post getLastPost() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_LAST)) {
            return (Post) this.script.getCache().get(CacheGroup.POST_LAST);
        }
        Post post = this.script.getLastPost();
        this.script.getCache().put(CacheGroup.POST_LAST, post);
        return post;
    }

    @Override
    public Post getLastUserPost(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_LAST_USER, username)) {
            return (Post) this.script.getCache().get(CacheGroup.POST_LAST_USER, username);
        }
        Post post = this.script.getLastUserPost(username);
        this.script.getCache().put(CacheGroup.POST_LAST_USER, username, post);
        return post;
    }

    @Override
    public int getTotalThreadCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_COUNT_TOTAL)) {
            return (Integer) this.script.getCache().get(CacheGroup.THREAD_COUNT_TOTAL);
        }
        int count = this.script.getTotalThreadCount();
        this.script.getCache().put(CacheGroup.THREAD_COUNT_TOTAL, count);
        return count;
    }

    @Override
    public int getThreadCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.THREAD_COUNT, username);
        }
        int count = this.script.getThreadCount(username);
        this.script.getCache().put(CacheGroup.THREAD_COUNT, username, count);
        return count;
    }

    @Override
    public com.craftfire.bifrost.classes.Thread getLastThread() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_LAST)) {
            return (Thread) this.script.getCache().get(CacheGroup.THREAD_LAST);
        }
        Thread thread = this.script.getLastThread();
        this.script.getCache().put(CacheGroup.THREAD_LAST, thread);
        return thread;
    }

    @Override
    public Thread getLastUserThread(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_LAST_USER, username)) {
            return (Thread) this.script.getCache().get(CacheGroup.THREAD_LAST_USER, username);
        }
        Thread thread = this.script.getLastUserThread(username);
        this.script.getCache().put(CacheGroup.THREAD_LAST_USER, username, thread);
        return thread;
    }

    @Override
    public Thread getThread(int threadID) throws UnsupportedFunction {
        if (Thread.hasCache(this, threadID)) {
            return Thread.getCache(this, threadID);
        }
        Thread thread = this.script.getThread(threadID);
        Thread.addCache(this, thread);
        return thread;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Thread> getThreads(int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_LIST)) {
            return (List<Thread>) this.script.getCache().get(CacheGroup.THREAD_LIST);
        }
        List<Thread> threads = this.script.getThreads(limit);
        this.script.getCache().put(CacheGroup.THREAD_LIST, threads);
        return threads;
    }

    @Override
    public void updateThread(Thread thread) throws SQLException, UnsupportedFunction {
        this.script.updateThread(thread);
        Thread.addCache(this, thread);
    }

    @Override
    public void createThread(Thread thread) throws SQLException, UnsupportedFunction {
        this.script.createThread(thread);
        Thread.addCache(this, thread);
    }

    @Override
    public int getUserCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.USER_COUNT);
        }
        int count = this.script.getUserCount();
        this.script.getCache().put(CacheGroup.USER_COUNT, count);
        return count;
    }

    @Override
    public int getGroupCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.GROUP_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.GROUP_COUNT);
        }
        int count = this.script.getGroupCount();
        this.script.getCache().put(CacheGroup.GROUP_COUNT, count);
        return count;
    }

    @Override
    public String getHomeURL() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.URL_HOME)) {
            return (String) this.script.getCache().get(CacheGroup.URL_HOME);
        }
        String url = this.script.getHomeURL();
        this.script.getCache().put(CacheGroup.URL_HOME, url);
        return url;
    }

    @Override
    public String getForumURL() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.URL_FORUM)) {
            return (String) this.script.getCache().get(CacheGroup.URL_FORUM);
        }
        String url = this.script.getForumURL();
        this.script.getCache().put(CacheGroup.URL_FORUM, url);
        return url;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getIPs(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_IP, username)) {
            return (List<String>) this.script.getCache().get(CacheGroup.USER_IP, username);
        }
        List<String> ips = this.script.getIPs(username);
        this.script.getCache().put(CacheGroup.USER_IP, ips, username);
        return ips;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ban> getBans(int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.BAN_LIST)) {
            return (List<Ban>) this.script.getCache().get(CacheGroup.BAN_LIST);
        }
        List<Ban> bans = this.script.getBans(limit);
        this.script.getCache().put(CacheGroup.BAN_LIST, bans);
        return bans;
    }

    @Override
    public void updateBan(Ban ban) throws SQLException, UnsupportedFunction {
        this.script.updateBan(ban);
        Ban.addCache(this, ban);
    }

    @Override
    public void addBan(Ban ban) throws SQLException, UnsupportedFunction {
        this.script.addBan(ban);
        Ban.addCache(this, ban);
    }

    @Override
    public int getBanCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.BAN_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.BAN_COUNT);
        }
        int count = this.script.getBanCount();
        this.script.getCache().put(CacheGroup.BAN_COUNT, count);
        return count;
    }

    @Override
    public boolean isBanned(String string) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.IS_BANNED, string)) {
            return (Boolean) this.script.getCache().get(CacheGroup.IS_BANNED, string);
        }
        boolean banned = this.script.isBanned(string);
        this.script.getCache().put(CacheGroup.IS_BANNED, banned, string);
        return banned;
    }

    @Override
    public boolean isRegistered(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.IS_REGISTERED, username)) {
            return (Boolean) this.script.getCache().get(CacheGroup.IS_REGISTERED, username);
        }
        boolean registered = this.script.isRegistered(username);
        this.script.getCache().put(CacheGroup.IS_REGISTERED, registered, username);
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
        if (this.script.getCache().contains(CacheGroup.USER_USERNAME, userid)) {
            return (String) this.script.getCache().get(CacheGroup.USER_USERNAME, userid);
        }
        String username = this.script.getUsername(userid);
        this.script.getCache().put(CacheGroup.USER_USERNAME, username, userid);
        return username;
    }

    @Override
    public int getUserID(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.USER_ID, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.USER_ID, username);
        }
        int userID = this.script.getUserID(username);
        this.script.getCache().put(CacheGroup.USER_ID, userID, username);
        return userID;
    }
}
