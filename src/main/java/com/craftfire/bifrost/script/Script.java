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

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.Cache;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.ScriptType;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class contains methods relevant to direct methods for each script.
 */
public class Script {
    private final String version;
    private final Scripts script;
    private final DataManager dataManager;
    private final Cache cache;
    private String[] versionRanges;
    private String scriptName, shortName;

    protected Script(Scripts script, String version, DataManager dataManager) {
        this.version = version;
        this.script = script;
        this.dataManager = dataManager;
        this.cache = new Cache();
    }

    /**
     * Returns the {@link Bifrost} instance.
     *
     * @return {@link Bifrost} instance
     */
    public Bifrost getBifrost() {
        return Bifrost.getInstance();
    }

    /**
     * Returns the Bifrost {@link LoggingManager}.
     *
     * @return Bifrost {@link LoggingManager}
     */
    public LoggingManager getLoggingManager() {
        return Bifrost.getInstance().getLoggingManager();
    }

    /**
     * Returns the {@link Cache} of the current script.
     *
     * @return {@link Cache} of the current script
     */
    public Cache getCache() {
        return this.cache;
    }

    /**
     * Returns the {@link DataManager} of the current script.
     *
     * @return {@link DataManager} of the current script
     */
    public DataManager getDataManager() {
        return this.dataManager;
    }

    /**
     * Returns the {@link Scripts} enum of the current script.
     *
     * @return {@link Scripts} enum of the current script
     * @see Scripts
     */
    public Scripts getScript() {
        return this.script;
    }

    /**
     * Returns the {@link ScriptType} of the script.
     *
     * @return {@link ScriptType} of the script
     * @see ScriptType
     */
    public ScriptType getType() {
        return this.script.getType();
    }

    /**
     * Returns an array of version ranges which the script supports.
     *
     * @return the version ranges
     */
    public String[] getVersionRanges()  {
        return this.versionRanges;
    }

    /**
     * Sets the version ranges of the script.
     *
     * @param versionRanges  the version ranges of the script
     */
    protected void setVersionRanges(String[] versionRanges) {
        this.versionRanges = versionRanges;
    }

    /**
     * Returns the script's full name, for example SimpleMachines.
     *
     * @return the script's full name
     */
    public String getScriptName() {
        return this.scriptName;
    }

    /**
     * Sets the script's full name.
     *
     * @param scriptName  the full name of the script
     */
    protected void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    /**
     * Returns the script's short name, for example SMF.
     *
     * @return the script's short name
     */
    public String getScriptShortname() {
        return this.shortName;
    }

    /**
     * Sets the script's short name.
     *
     * @param shortName  the short name of the script
     */
    protected void setShortName(String shortName) {
        this.shortName = shortName;
    }


    /**
     * Returns the latest version which is supported by the script.
     *
     * @return latest supported version of the script
     */
    public String getLatestVersion() {
        return null;
    }

    /**
     * Returns <code>true</code> if the user version is supported by the script.
     *
     * @return <code>true</code> if supported, <code>false</code> if not
     */
    public boolean isSupportedVersion() {
        for (int i = 0; this.getVersionRanges().length > i; i++) {
            if (CraftCommons.inVersionRange(this.getVersionRanges()[i], this.version)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the version which the script has been set to, this is usually the user's version.
     *
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns <code>true</code> if <code>username</code> and <code>password</code>
     * matches the username and password for the user in the script.
     *
     * @param username  the username of the user
     * @param password  the password of the user
     * @return          <code>true</code> if the password matches with the username's password
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public boolean authenticate(String username, String password) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Hashes the <code>password</code> of the user with whatever encryption the script uses and
     * returns the hashed string.
     *
     * @param salt      the salt of the user to hash
     * @param password  the password of the user to hash
     * @return          a hashed string
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public String hashPassword(String salt, String password) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the username of <code>userid</code>, returns returns <code>null</code> if none were found.
     *
     * @param userid  the userid to get the username from
     * @return        username of <code>userid</code>, returns <code>null</code> if nothing was found
     * @throws        UnsupportedMethod if the method is not supported by the script
     */
    public String getUsername(int userid) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the user ID of <code>username</code>, returns <code>0</code> if none were found.
     *
     * @param username  the username to get the user ID from
     * @return          user ID of <code>username</code>, returns <code>0</code> if nothing was found
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public int getUserID(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the {@link ScriptUser} object of the given <code>username</code>,
     * returns <code>null</code> if nothing was found.
     *
     * @param username  the username to get the {@link ScriptUser} object from
     * @return          a {@link ScriptUser} object of the given username,
     *                  returns <code>null</code> if nothing was found
     * @see             ScriptUser
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public ScriptUser getUser(String username) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the {@link ScriptUser} object of the given <code>userid</code>,
     * returns <code>null</code> if nothing was found.
     *
     * @param userid  the user ID to get the {@link ScriptUser} object from
     * @return        a {@link ScriptUser} object of the given user ID,
     *                returns <code>null</code> if nothing was found
     * @see           ScriptUser
     * @throws        UnsupportedMethod if the method is not supported by the script
     */
    public ScriptUser getUser(int userid) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the {@link ScriptUser} object of the latest registered user.
     *
     * @return  a {@link ScriptUser} object of the latest registered user
     * @see     ScriptUser
     * @throws  UnsupportedMethod if the method is not supported by the script
     */
    public ScriptUser getLastRegUser() throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Updates the {@link ScriptUser} object with whatever values set by the user.
     *
     * @param user  the {@link ScriptUser} object
     * @see         ScriptUser
     * @throws      SQLException if a SQL exception occurred
     * @throws      UnsupportedMethod if the method is not supported by the script
     */
    public void updateUser(ScriptUser user) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Creates a {@link ScriptUser} object with whatever values set by the user.
     *
     * @param user  the {@link ScriptUser} object
     * @see         ScriptUser
     * @throws      SQLException if a SQL exception occurred
     * @throws      UnsupportedMethod if the method is not supported by the script
     */
    public void createUser(ScriptUser user) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with {@link Group} objects.
     * <p>
     * Parameter <code>limit</code> can be used as a limit of how many objects should be returned.
     * <p>
     * Set <code>limit</code> to <code>0</code> to return all the objects.
     * If none are found, the returned List will be empty.
     *
     * @param limit  the limit, set to <code>0</code> if you want to return all
     * @return       List with {@link Group} objects, if none are found it returns an empty List
     * @see          Group
     * @throws       SQLException if a SQL exception occurred
     * @throws       UnsupportedMethod if the method is not supported by the script
     */
    public List<Group> getGroups(int limit) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a <code>Integer</code> which holds the ID of the group.
     *
     * @param group  name of the group
     * @return       id of the group
     * @throws       UnsupportedMethod if the method is not supported by the script
     */
    public int getGroupID(String group) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a {@link Group} object of the given <code>groupid</code>,
     * returns <code>null</code> if nothing is found
     *
     * @param groupid  the group ID
     * @return         {@link Group} object, returns <code>null</code> if nothing was found
     * @see            Group
     * @throws         UnsupportedMethod if the method is not supported by the script
     */
    public Group getGroup(int groupid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a {@link Group} object of the given <code>group</code> name,
     * returns <code>null</code> if nothing is found.
     *
     * @param group  the group name
     * @return       {@link Group} object, returns <code>null</code> if nothing was found
     * @see          Group
     * @throws       UnsupportedMethod if the method is not supported by the script
     */
    public Group getGroup(String group) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with the {@link Group} objects that the <code>username</code> is a part of.
     * <p>
     * The List will be empty if none are found.
     *
     * @param username  the username to grab the groups of.
     * @return          List with {@link Group} objects, returns an empty List if none are found.
     * @see             Group
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public List<Group> getUserGroups(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Updates the {@link Group} object with whatever values set by the user.
     *
     * @param group  the {@link Group} object
     * @see          Group
     * @throws       SQLException if a SQL exception occurred
     * @throws       UnsupportedMethod if the method is not supported by the script
     */
    public void updateGroup(Group group) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Creates the {@link Group} object with whatever values set by the user.
     *
     * @param group  the {@link Group} object
     * @see          Group
     * @throws       SQLException if a SQL exception occurred
     * @throws       UnsupportedMethod if the method is not supported by the script
     */
    public void createGroup(Group group) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a {@link PrivateMessage} object of the given <code>pmid</code>,
     * returns <code>null</code> if nothing was found.
     *
     * @param pmid  the private message ID
     * @return      {@link PrivateMessage} object
     * @see         PrivateMessage
     * @throws      UnsupportedMethod if the method is not supported by the script
     * @throws      SQLException if a SQL exception occurred
     */
    public PrivateMessage getPM(int pmid) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }
    
    /**
     * Returns a List with {@link PrivateMessage} objects.
     * <p>
     * Parameter <code>limit</code> can be used as a limit of how many objects should be returned.
     * <p>
     * Set <code>limit</code> to <code>0</code> to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit, set to <code>0</code> if you want to return all
     * @return                    List with Group objects, if none are found it returns an empty List
     * @see                       PrivateMessage
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<PrivateMessage> getPMs(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with {@link PrivateMessage} objects replying to specified private message.
     * <p>
     * Parameter <code>limit</code> can be used as a limit of how many objects should be returned.
     * <p>
     * Set <code>limit</code> to <code>0</code> to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param pmid                the ID of the private message
     * @param limit               the limit, set to <code>0</code> if you want to return all
     * @return                    List with {@link PrivateMessage} objects, if none are found it returns an empty List
     * @see                       PrivateMessage
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<PrivateMessage> getPMReplies(int pmid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with {@link PrivateMessage} objects that the user has sent.
     * <p>
     * Parameter <code>limit</code> can be used as a limit of how many objects should be returned.
     * <p>
     * Set <code>limit</code> to <code>0</code> to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username  the username to get the {@link PrivateMessage} objects from
     * @param limit     the limit, set to <code>0</code> if you want to return all
     * @return          List with {@link PrivateMessage} objects, if none are found it returns an empty List
     * @see             PrivateMessage
     * @throws          UnsupportedMethod if the method is not supported by the script
     * @throws          SQLException if a SQL exception occurred
     */
    public List<PrivateMessage> getPMsSent(String username, int limit) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with {@link PrivateMessage} objects that the user has received.
     * <p>
     * Parameter <code>limit</code> can be used as a limit of how many objects should be returned.
     * <p>
     * Set <code>limit</code> to <code>0</code> to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username  the username to get the {@link PrivateMessage} objects from
     * @param limit     the limit, set to <code>0</code> if you want to return all
     * @return          List with {@link PrivateMessage} objects, if none are found it returns an empty List
     * @see             PrivateMessage
     * @throws          UnsupportedMethod if the method is not supported by the script
     * @throws          SQLException if a SQL exception occurred
     */
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a total amount of how many private messages are there.
     * 
     * @return                    total amount of private messages
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPMCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns an amount of how many private messages are replying to specified private message.
     * 
     * @param  pmid               the ID of the message
     * @return                    an amount of private message replying to the message
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPMReplyCount(int pmid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns an amount of how many private messages <code>username</code> has sent.
     *
     * @param username  the username to get the count from
     * @return          the amount of how many private messages the <code>username</code> has sent,
     *                  returns <code>0</code> if none
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public int getPMSentCount(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns an amount of how many private messages <code>username</code> has received.
     *
     * @param username  the <code>username</code> to get the count from
     * @return          the amount of how many private messages the <code>username</code> has received,
     *                  returns <code>0</code> if none
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public int getPMReceivedCount(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Updates the {@link PrivateMessage} object with whatever values set by the user.
     *
     * @param privateMessage  the {@link PrivateMessage} object
     * @see                   PrivateMessage
     * @throws                SQLException if a SQL exception occurred
     * @throws                UnsupportedMethod if the method is not supported by the script
     */
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Creates the {@link PrivateMessage} object with whatever values set by the user.
     *
     * @param privateMessage  the {@link PrivateMessage} object
     * @see                   PrivateMessage
     * @throws                SQLException if a SQL exception occurred
     * @throws                UnsupportedMethod if the method is not supported by the script
     */
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with <code>username</code>'s IP addresses.
     *
     * @param username  the username to get the IP addresses from
     * @return          List with <code>username</code>'s IP addresses
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public List<String> getIPs(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with {@link Ban} objects.
     * <p>
     * Parameter <code>limit</code> can be used as a limit of how many objects should be returned.
     * <p>
     * Set <code>limit</code> to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit  the limit, set to 0 if you want to return all
     * @return       List with {@link Ban} objects, if none are found it returns an empty List
     * @see          Ban
     * @throws       UnsupportedMethod if the method is not supported by the script
     */
    public List<Ban> getBans(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Creates the {@link Ban} object with whatever values set by the user.
     *
     * @param ban  the {@link Ban} object
     * @see        Ban
     * @throws     SQLException if a SQL exception occurred
     * @throws     UnsupportedMethod if the method is not supported by the script
     */
    public void updateBan(Ban ban) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Creates the {@link Ban} object with whatever values set by the user.
     *
     * @param ban  the {@link Ban} object.
     * @see        Ban
     * @throws     SQLException if a SQL exception occurred
     * @throws     UnsupportedMethod if the method is not supported by the script
     */
    public void addBan(Ban ban) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the complete count of how many bans that have been made.
     *
     * @return the amount of how many bans that have been made, returns <code>0</code> if none
     * @throws UnsupportedMethod if the method is not supported by the script
     */
    public int getBanCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns <code>true</code> if <code>string</code> matches a ban in the script, returns <code>false</code> if not.
     *
     * @param string  string to search for
     * @return        <code>true</code> if <code>string</code> is banned, returns <code>false</code> if not
     * @throws        UnsupportedMethod if the method is not supported by the script
     * @throws        SQLException if a SQL exception occurred
     */
    public boolean isBanned(String string) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Returns <code>true</code> if <code>username</code> is registered, returns <code>false</code> if not.
     *
     * @param username  username to check if is registered
     * @return          <code>true</code> if <code>username</code> is registered, <code>false</code> if not
     * @throws          UnsupportedMethod if the method is not supported by the script
     */
    public boolean isRegistered(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the complete count of how many users the script has registered.
     *
     * @return the amount of how many users the script has registered, returns <code>0</code> if none
     * @throws UnsupportedMethod if the method is not supported by the script
     */
    public int getUserCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the complete count of how many groups the script has.
     *
     * @return the amount of how how many groups the script has, returns <code>0</code> if none
     * @throws UnsupportedMethod if the method is not supported by the script
     */
    public int getGroupCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the script's home URL.
     *
     * @return home URL of the script
     * @throws UnsupportedMethod if the method is not supported by the script
     */
    public String getHomeURL() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }
}
