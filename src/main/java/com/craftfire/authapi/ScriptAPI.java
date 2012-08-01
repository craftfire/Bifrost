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
        return getUser(id);
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
        return null;  //TODO: Finish
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        ScriptUser.addCache(user);
        this.script.updateUser(user);
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        ScriptUser.addCache(user);
        this.script.createUser(user);
    }

    @Override
    public List<Group> getGroups(int limit) throws SQLException, UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public Group getGroup(int groupid) throws UnsupportedFunction {
        if (Group.hasCache(groupid)) {
            return Group.getCache(groupid);
        }
        Group group = this.script.getGroup(groupid);
        Group.addCache(group);
        return group;
    }

    @Override
    public Group getGroup(String group) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<Group> getUserGroups(String username) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public void updateGroup(Group group) throws SQLException, UnsupportedFunction {
        Group.addCache(group);
        this.script.updateGroup(group);
    }

    @Override
    public void createGroup(Group group) throws SQLException, UnsupportedFunction {
        Group.addCache(group);
        this.script.createGroup(group);
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
    public List<PrivateMessage> getPMsSent(String username, int limit) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public int getPMSentCount(String username) throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public int getPMReceivedCount(String username) throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public Post getPost(int postid) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<Post> getPosts(int limit) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<Post> getPostsFromThread(int threadid, int limit) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public void updatePost(Post post) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public void createPost(Post post) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public int getPostCount(String username) throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public int getTotalPostCount() throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public Post getLastPost() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public Post getLastUserPost(String username) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public int getTotalThreadCount() throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public int getThreadCount(String username) throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public com.craftfire.authapi.classes.Thread getLastThread() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public com.craftfire.authapi.classes.Thread getLastUserThread(String username) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public com.craftfire.authapi.classes.Thread getThread(int threadid) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<Thread> getThreads(int limit) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public void updateThread(Thread thread) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public void createThread(Thread thread) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public int getUserCount() throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public int getGroupCount() throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public String getHomeURL() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public String getForumURL() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<String> getIPs(String username) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public List<Ban> getBans(int limit) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public void updateBan(Ban ban) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public void addBan(Ban ban) throws SQLException, UnsupportedFunction {
        //TODO: Finish
    }

    @Override
    public int getBanCount() throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }

    @Override
    public boolean isBanned(String string) throws UnsupportedFunction {
        return false;  //TODO: Finish
    }

    @Override
    public boolean isRegistered(String username) throws UnsupportedFunction {
        return false;  //TODO: Finish
    }

    @Override
    public String getLatestVersion() {
        return null;  //TODO: Finish
    }

    @Override
    public boolean isSupportedVersion() {
        return false;  //TODO: Finish
    }

    @Override
    public String getVersion() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public String[] getVersionRanges() {
        return new String[0];  //TODO: Finish
    }

    @Override
    public String getEncryption() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public String getScriptName() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public String getScriptShortname() throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    public boolean authenticate(String username, String password) throws UnsupportedFunction {
        return this.script.authenticate(username, password);
    }

    @Override
    public String hashPassword(String salt, String password) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public String getUsername(int userid) throws UnsupportedFunction {
        return null;  //TODO: Finish
    }

    @Override
    public int getUserID(String username) throws UnsupportedFunction {
        return 0;  //TODO: Finish
    }
}
