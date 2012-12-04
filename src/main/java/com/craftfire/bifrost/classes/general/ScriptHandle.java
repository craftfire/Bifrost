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
package com.craftfire.bifrost.classes.general;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.ip.IPAddress;
import com.craftfire.commons.util.Version;
import com.craftfire.commons.util.VersionRange;

import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.ScriptException;

/**
 * This class contains methods relevant to methods to use for a generic script.
 *
 * @see Script Documentation of all the methods
 */
public class ScriptHandle {
    private final int handleID;
    private Script script;

    /**
     * Creates a ScriptHandle.
     *
     * @param  handleID         the ID of the handle
     * @param  script           the script
     */
    public ScriptHandle(int handleID, Script script) {
        this.handleID = handleID;
        this.script = script;
        this.script.setHandle(this);
    }

    /**
     * Returns the Script object
     *
     * @return the Script object
     */
    public Script getScript() {
        return this.script;
    }

    /**
     * Returns the ID of the handle for the script.
     *
     * @return ID of the handle for the script
     */
    public int getID() {
        return this.handleID;
    }

    /**
     * Returns the {@link DataManager} of the handle/script.
     *
     * @return DataManager of the handle/script
     */
    public DataManager getDataManager() {
        return this.script.getDataManager();
    }

    /**
     * Returns the {@link Cache} of the handle/script.
     *
     * @return Cache of the handle/Script
     */
    public Cache getCache() {
        return this.script.getCache();
    }

    /**
     * Creates a new ban in the script.
     *
     * @see Ban#Ban(ScriptHandle, String, URI, IPAddress) Documentation for this method
     */
    public Ban newBan(String name, URI email, IPAddress ip) {
        return new Ban(this, name, email, ip);
    }

    /**
     * Creates a new name/username ban in the script.
     *
     * @see Ban#Ban(ScriptHandle, String) Documentation for this method
     */
    public Ban newBan(String name) {
        return new Ban(this, name);
    }

    /**
     * Creates a new email ban in the script.
     *
     * @see Ban#Ban(ScriptHandle, URI) Documentation for this method
     */
    public Ban newBan(URI email) {
        return new Ban(this, email);
    }

    /**
     * Creates a new IPAddress ban in the script.
     *
     * @see Ban#Ban(ScriptHandle, IPAddress) Documentation for this method
     */
    public Ban newBan(IPAddress ipAddress) {
        return new Ban(this, ipAddress);
    }

    /**
     * Creates a new group in the script.
     *
     * @see Group#Group(ScriptHandle, String) Documentation for this method
     */
    public Group newGroup(String groupname) {
        return new Group(this, groupname);
    }

    /**
     * Creates a new private message in the script.
     *
     * @see PrivateMessage#PrivateMessage(ScriptHandle, ScriptUser, List) Documentation for this method
     */
    public PrivateMessage newPrivateMessage(ScriptUser sender, List<ScriptUser> recipients) {
        return new PrivateMessage(this, sender, recipients);
    }

    /**
     * Creates a new user in the script.
     *
     * @see ScriptUser#ScriptUser(ScriptHandle, String, String) Documentation for this method
     */
    public ScriptUser newScriptUser(String username, String password) {
        return new ScriptUser(this, username, password);
    }

    /**
     * @see Script#getLatestVersion() Documentation for this method
     */
    public Version getLatestVersion() {
        return this.script.getLatestVersion();
    }

    /**
     * @see Script#isSupportedVersion() Documentation for this method
     */
    public boolean isSupportedVersion() {
        return this.script.isSupportedVersion();
    }

    /**
     * @see Script#getVersion() Documentation for this method
     */
    public Version getVersion() {
        return this.script.getVersion();
    }

    /**
     * @see Script#getVersionRanges() Documentation for this method
     */
    public VersionRange[] getVersionRanges() {
        return this.script.getVersionRanges();
    }

    /**
     * @see Script#getScriptName() Documentation for this method
     */
    public String getScriptName() {
        return this.script.getScriptName();
    }

    /**
     * @see Script#getScriptShortname() Documentation for this method
     */
    public String getScriptShortname() {
        return this.script.getScriptShortname();
    }

    /**
     * @see Script#authenticate(String, String) Doucmentation for this method
     */
    public boolean authenticate(String username, String password) throws ScriptException {
        return this.script.authenticate(username, password);
    }

    /**
     * @see Script#hashPassword(String, String) Documentation for this method
     */
    public String hashPassword(String salt, String password) throws ScriptException {
        return this.script.hashPassword(salt, password);
    }

    /**
     * @see Script#getUsername(int) Documentation for this method
     */
    public String getUsername(int userid) throws ScriptException, SQLException {
        if (this.script.getCache().contains(CacheGroup.USER_USERNAME, userid)) {
            return (String) this.script.getCache().get(CacheGroup.USER_USERNAME, userid);
        }
        String username = this.script.getUsername(userid);
        this.script.getCache().put(CacheGroup.USER_USERNAME, username, userid);
        return username;
    }

    /**
     * @see Script#getUserID(String) Documentation for this method
     */
    public int getUserID(String username) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.USER_ID, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.USER_ID, username);
        }
        int userID = this.script.getUserID(username);
        this.script.getCache().put(CacheGroup.USER_ID, userID, username);
        return userID;
    }

    /**
     * @see Script#getUser(String) Documentation for this method
     */
    public ScriptUser getUser(String username) throws ScriptException, SQLException {
        int id = this.script.getUserID(username);
        return this.getUser(id);
    }

    /**
     * @see Script#getUser(int) Documentation for this method
     */
    public ScriptUser getUser(int userid) throws ScriptException, SQLException {
        if (ScriptUser.hasCache(this, userid)) {
            return ScriptUser.getCache(this, userid);
        }
        ScriptUser user = this.script.getUser(userid);
        ScriptUser.addCache(this, userid, user);
        return user;
    }

    /**
     * @see Script#getLastRegUser() Documentation for this method
     */
    public ScriptUser getLastRegUser() throws ScriptException, SQLException {
        if (this.script.getCache().contains(CacheGroup.USER_LAST_REG)) {
            return (ScriptUser) this.script.getCache().get(CacheGroup.USER_LAST_REG);
        }
        ScriptUser user = this.script.getLastRegUser();
        this.script.getCache().put(CacheGroup.USER_LAST_REG, user);
        return user;
    }

    /**
     * @see Script#updateUser(ScriptUser) Documentation for this method
     */
    public void updateUser(ScriptUser user) throws SQLException, ScriptException {
        this.script.updateUser(user);
        ScriptUser.cleanupCache(this, user, CacheCleanupReason.UPDATE);
        ScriptUser.addCache(this, user);
    }

    /**
     * @see Script#createUser(ScriptUser) Documentation for this method
     */
    public void createUser(ScriptUser user) throws SQLException, ScriptException {
        this.script.createUser(user);
        ScriptUser.cleanupCache(this, user, CacheCleanupReason.CREATE);
        ScriptUser.addCache(this, user);
    }

    /**
     * @see Script#getGroups(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<Group> getGroups(int limit) throws SQLException, ScriptException {
        if (this.script.getCache().contains(CacheGroup.GROUP_LIST)) {
            List<Group> groups = (List<Group>) this.script.getCache().get(CacheGroup.GROUP_LIST);
            if (groups.size() == ((limit == 0) ? getGroupCount() : limit)) {
                return groups;
            } else if ((groups.size() > limit) && (limit != 0)) {
                return groups.subList(0, limit);
            }
        }
        List<Group> groups = this.script.getGroups(limit);
        this.script.getCache().put(CacheGroup.GROUP_LIST, groups);
        return groups;
    }

    /**
     * @see Script#getGroupID(String) Documentation for this method
     */
    public int getGroupID(String group) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.GROUP_ID, group)) {
            return (Integer) this.script.getCache().get(CacheGroup.GROUP_ID, group);
        }
        int groupID = this.getGroupID(group);
        this.script.getCache().put(CacheGroup.GROUP_ID, groupID, group);
        return groupID;
    }

    /**
     * @see Script#getGroup(int) Documentation for this method
     */
    public Group getGroup(int groupID) throws ScriptException, SQLException {
        if (Group.hasCache(this, groupID)) {
            return Group.getCache(this, groupID);
        }
        Group group = this.script.getGroup(groupID);
        Group.addCache(this, group);
        return group;
    }

    /**
     * @see Script#getGroup(String) Documentation for this method
     */
    public Group getGroup(String groupString) throws ScriptException, SQLException {
        if (Group.hasCache(this, groupString)) {
            return Group.getCache(this, groupString);
        }
        Group group = this.script.getGroup(groupString);
        Group.addCache(this, group);
        return group;
    }

    /**
     * @see Script#getUserGroups(String) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<Group> getUserGroups(String username) throws ScriptException, SQLException {
        if (this.script.getCache().contains(CacheGroup.USER_GROUP, username)) {
            return (List<Group>) this.script.getCache().get(CacheGroup.USER_GROUP, username);
        }
        List<Group> groups = this.script.getUserGroups(username);
        this.script.getCache().put(CacheGroup.USER_GROUP, username, groups);
        return groups;
    }

    /**
     * @see Script#updateGroup(Group) Documentation for this method
     */
    public void updateGroup(Group group) throws SQLException, ScriptException {
        this.script.updateGroup(group);
        Group.cleanupCache(this, group, CacheCleanupReason.UPDATE);
        Group.addCache(this, group);
    }

    /**
     * @see Script#createGroup(Group) Documentation for this method
     */
    public void createGroup(Group group) throws SQLException, ScriptException {
        this.script.createGroup(group);
        Group.cleanupCache(this, group, CacheCleanupReason.CREATE);
        Group.addCache(this, group);
    }

    /**
     * @see Script#getPM(int) Documentation for this method
     */
    public PrivateMessage getPM(int pmid) throws ScriptException, SQLException {
        if (PrivateMessage.hasCache(this, pmid)) {
            return PrivateMessage.getCache(this, pmid);
        }
        PrivateMessage pm = this.script.getPM(pmid);
        PrivateMessage.addCache(this, pm);
        return pm;
    }

    /**
     * @see Script#getPMs(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMs(int limit) throws ScriptException, SQLException {
        if (getCache().contains(CacheGroup.PM_LIST)) {
            List<PrivateMessage> pms = (List<PrivateMessage>) getCache().get(CacheGroup.PM_LIST);
            if (pms.size() == ((limit == 0) ? getPMCount() : limit)) {
                return pms;
            } else if ((pms.size() > limit) && (limit != 0)) {
                return pms.subList(0, limit);
            }
        }
        List<PrivateMessage> pms = this.script.getPMs(limit);
        getCache().put(CacheGroup.PM_LIST, pms);
        return pms;
    }

    /**
     * @see Script#getPMReplies(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMReplies(int pmid, int limit) throws ScriptException {
        if (getCache().contains(CacheGroup.PM_REPLIES, pmid) && (limit != 0)) {
            List<PrivateMessage> pms = (List<PrivateMessage>) getCache().get(CacheGroup.PM_REPLIES, pmid);
            if (pms.size() == ((limit == 0) ? getPMReplyCount(pmid) : limit)) {
                return pms;
            } else if ((pms.size() > limit) && (limit != 0)) {
                return pms.subList(0, limit);
            }
        }
        List<PrivateMessage> pms = this.script.getPMReplies(pmid, limit);
        getCache().put(CacheGroup.PM_REPLIES, pmid, pms);
        return pms;
    }

    /**
     * @see Script#getPMsSent(String, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsSent(String username, int limit) throws ScriptException, SQLException {
        if (this.script.getCache().contains(CacheGroup.PM_SENT, username)) {
            List<PrivateMessage> pms = (List<PrivateMessage>) this.script.getCache().get(CacheGroup.PM_RECEIVED, username);
            if (pms.size() == ((limit == 0) ? getPMSentCount(username) : limit)) {
                return pms;
            } else if ((pms.size() > limit) && (limit != 0)) {
                return pms.subList(0, limit);
            }
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        this.script.getCache().put(CacheGroup.PM_SENT, username, pms);
        return pms;
    }

    /**
     * @see Script#getPMsReceived(String, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws ScriptException, SQLException {
        if (this.script.getCache().contains(CacheGroup.PM_RECEIVED, username)) {
            List<PrivateMessage> pms = (List<PrivateMessage>) this.script.getCache().get(CacheGroup.PM_RECEIVED, username);
            if (pms.size() == ((limit == 0) ? getPMReceivedCount(username) : limit)) {
                return pms;
            } else if ((pms.size() > limit) && (limit != 0)) {
                return pms.subList(0, limit);
            }
        }
        List<PrivateMessage> pms = this.script.getPMsSent(username, limit);
        this.script.getCache().put(CacheGroup.PM_RECEIVED, username, pms);
        return pms;
    }

    /**
     * @see Script#getPMCount() Documentation for this method
     */
    public int getPMCount() throws ScriptException {
        if (getCache().contains(CacheGroup.PM_COUNT)) {
            return (Integer) getCache().get(CacheGroup.PM_COUNT);
        }
        int count = this.script.getPMCount();
        getCache().put(CacheGroup.PM_COUNT, count);
        return count;
    }

    /**
     * @see Script#getPMReplyCount(int) Documentation for this method
     */
    public int getPMReplyCount(int pmid) throws ScriptException {
        if (getCache().contains(CacheGroup.PM_REPLY_COUNT, pmid)) {
            return (Integer) getCache().get(CacheGroup.PM_REPLY_COUNT, pmid);
        }
        int count = this.script.getPMReplyCount(pmid);
        getCache().put(CacheGroup.PM_REPLY_COUNT, pmid, count);
        return count;
    }

    /**
     * @see Script#getPMSentCount(String) Documentation for this method
     */
    public int getPMSentCount(String username) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.PM_SENT_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.PM_SENT_COUNT, username);
        }
        int count = this.script.getPMSentCount(username);
        this.script.getCache().put(CacheGroup.PM_SENT_COUNT, username, count);
        return count;
    }

    /**
     * @see Script#getPMReceivedCount(String) Documentation for this method
     */
    public int getPMReceivedCount(String username) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.PM_RECEIVED_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.PM_RECEIVED_COUNT, username);
        }
        int count = this.script.getPMReceivedCount(username);
        this.script.getCache().put(CacheGroup.PM_RECEIVED_COUNT, username, count);
        return count;
    }

    /**
     * @see Script#updatePrivateMessage(PrivateMessage) Documentation for this method
     */
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, ScriptException {
        this.script.updatePrivateMessage(privateMessage);
        PrivateMessage.cleanupCache(this, privateMessage, CacheCleanupReason.UPDATE);
        PrivateMessage.addCache(this, privateMessage);
    }

    /**
     * @see Script#createPrivateMessage(PrivateMessage) Documentation for this method
     */
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, ScriptException {
        this.script.createPrivateMessage(privateMessage);
        PrivateMessage.cleanupCache(this, privateMessage, CacheCleanupReason.CREATE);
        PrivateMessage.addCache(this, privateMessage);
    }

    /**
     * @see Script#getUserCount() Documentation for this method
     */
    public int getUserCount() throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.USER_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.USER_COUNT);
        }
        int count = this.script.getUserCount();
        this.script.getCache().put(CacheGroup.USER_COUNT, count);
        return count;
    }

    /**
     * @see Script#getGroupCount() Documentation for this method
     */
    public int getGroupCount() throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.GROUP_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.GROUP_COUNT);
        }
        int count = this.script.getGroupCount();
        this.script.getCache().put(CacheGroup.GROUP_COUNT, count);
        return count;
    }

    /**
     * @see Script#getIPs(String) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<String> getIPs(String username) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.USER_IP, username)) {
            return (List<String>) this.script.getCache().get(CacheGroup.USER_IP, username);
        }
        List<String> ips = this.script.getIPs(username);
        this.script.getCache().put(CacheGroup.USER_IP, ips, username);
        return ips;
    }

    /**
     * @see Script#getBans(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<Ban> getBans(int limit) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.BAN_LIST)) {
            List<Ban> bans = (List<Ban>) this.script.getCache().get(CacheGroup.BAN_LIST);
            if (bans.size() == ((limit == 0) ? getBanCount() : limit)) {
                return bans;
            } else if ((bans.size() > limit) && (limit != 0)) {
                return bans.subList(0, limit);
            }
        }
        List<Ban> bans = this.script.getBans(limit);
        this.script.getCache().put(CacheGroup.BAN_LIST, bans);
        return bans;
    }

    /**
     * @see Script#updateBan(Ban) Documentation for this method
     */
    public void updateBan(Ban ban) throws SQLException, ScriptException {
        this.script.updateBan(ban);
        Ban.cleanupCache(this, ban, CacheCleanupReason.UPDATE);
        Ban.addCache(this, ban);
    }

    /**
     * @see Script#addBan(Ban) Documentation for this method
     */
    public void addBan(Ban ban) throws SQLException, ScriptException {
        this.script.addBan(ban);
        Ban.cleanupCache(this, ban, CacheCleanupReason.CREATE);
        Ban.addCache(this, ban);
    }

    /**
     * @see Script#getBanCount() Documentation for this method
     */
    public int getBanCount() throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.BAN_COUNT)) {
            return (Integer) this.script.getCache().get(CacheGroup.BAN_COUNT);
        }
        int count = this.script.getBanCount();
        this.script.getCache().put(CacheGroup.BAN_COUNT, count);
        return count;
    }

    /**
     * @see Script#isBanned(String) Documentation for this method
     */
    public boolean isBanned(String string) throws ScriptException, SQLException {
        if (this.script.getCache().contains(CacheGroup.IS_BANNED, string)) {
            return (Boolean) this.script.getCache().get(CacheGroup.IS_BANNED, string);
        }
        boolean banned = this.script.isBanned(string);
        this.script.getCache().put(CacheGroup.IS_BANNED, banned, string);
        return banned;
    }

    /**
     * @see Script#isRegistered(String) Documentation for this method
     */
    public boolean isRegistered(String username) throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.IS_REGISTERED, username)) {
            return (Boolean) this.script.getCache().get(CacheGroup.IS_REGISTERED, username);
        }
        boolean registered = this.script.isRegistered(username);
        this.script.getCache().put(CacheGroup.IS_REGISTERED, registered, username);
        return registered;
    }

    /**
     * @see Script#getHomeURL() Documentation for this method
     */
    public String getHomeURL() throws ScriptException {
        if (this.script.getCache().contains(CacheGroup.URL_HOME)) {
            return (String) this.script.getCache().get(CacheGroup.URL_HOME);
        }
        String url = this.getScript().getHomeURL();
        this.script.getCache().put(CacheGroup.URL_HOME, url);
        return url;
    }
}
