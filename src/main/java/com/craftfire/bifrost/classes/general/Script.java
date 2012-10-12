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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.craftfire.commons.classes.Version;
import com.craftfire.commons.classes.VersionRange;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

import com.craftfire.bifrost.enums.ScriptType;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.ScriptException;

/**
 * This class contains methods relevant to direct methods for each script.
 */
public class Script {
    private final Version version;
    private final Scripts script;
    private final DataManager dataManager;
    private final Cache cache;
    private VersionRange[] versionRanges;
    private String scriptName, shortName;
    private ScriptHandle handle;

    protected Script(Scripts script, String version, DataManager dataManager) {
        this(script, new Version(version), dataManager);
    }

    protected Script(Scripts script, Version version, DataManager dataManager) {
        this.version = version;
        this.script = script;
        this.dataManager = dataManager;
        this.cache = new Cache();
    }

    /**
     * Returns the Bifrost {@link LoggingManager}.
     *
     * @return Bifrost {@link LoggingManager}
     */
    public LoggingManager getLoggingManager() {
        return null;
        //TODO: return Bifrost.getInstance().getLoggingManager();
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
     * @see     Scripts
     */
    public Scripts getScript() {
        return this.script;
    }

    /**
     * Returns the {@link ScriptHandle} of the current script.
     *
     * @return {@link ScriptHandle} of the current script
     * @see    ScriptHandle
     */
    public ScriptHandle getHandle() {
        return this.handle;
    }

    /**
     * Sets the {@link ScriptHandle} for the current script.
     *
     * @param handle  {@link ScriptHandle} of the current script
     */
    public void setHandle(ScriptHandle handle) {
        this.handle = handle;
    }

    /**
     * Returns the {@link ScriptType} of the script.
     *
     * @return {@link ScriptType} of the script
     * @see    ScriptType
     */
    public ScriptType getType() {
        return this.script.getType();
    }

    /**
     * Returns an array of version ranges which the script supports.
     *
     * @return the version ranges
     */
    public VersionRange[] getVersionRanges() {
        return this.versionRanges;
    }

    /**
     * Sets the version ranges of the script.
     *
     * @param newVersionRanges  the version ranges of the script
     */
    protected void setVersionRanges(VersionRange[] newVersionRanges) {
        if (newVersionRanges == null) {
            this.versionRanges = new VersionRange[0];
        } else {
            this.versionRanges = Arrays.copyOf(newVersionRanges, newVersionRanges.length);
        }
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
    public Version getLatestVersion() {
        return null;
    }

    /**
     * Returns {@code true} if the user version is supported by the script.
     *
     * @return {@code true} if supported, {@code false} if not
     */
    public boolean isSupportedVersion() {
        for (int i = 0; this.getVersionRanges().length > i; i++) {
            if (this.getVersionRanges()[i].inVersionRange(this.version)) {
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
    public Version getVersion() {
        return this.version;
    }

    /**
     * Returns {@code true} if {@code username} and {@code password}
     * matches the username and password for the user in the script.
     *
     * @param  username           the username of the user
     * @param  password           the password of the user
     * @return                    {@code true} if the password matches with the username's password
     * @throws ScriptException  if the method is not supported by the script
     */
    public boolean authenticate(String username, String password) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Hashes the {@code password} of the user with whatever encryption the script uses and
     * returns the hashed string.
     *
     * @param  salt               the salt of the user to hash
     * @param  password           the password of the user to hash
     * @return                    a hashed string
     * @throws ScriptException  if the method is not supported by the script
     */
    public String hashPassword(String salt, String password) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the username of {@code userid}, returns returns {@code null} if none were found.
     *
     * @param  userid             the userid to get the username from
     * @return                    username of {@code userid}, returns {@code null} if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     */
    public String getUsername(int userid) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the user ID of {@code username}, returns {@code 0} if none were found.
     *
     * @param  username           the username to get the user ID from
     * @return                    user ID of {@code username}, returns {@code 0} if nothing was found
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getUserID(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the {@link ScriptUser} object of the given {@code username},
     * returns {@code null} if nothing was found.
     *
     * @param username            the username to get the {@link ScriptUser} object from
     * @return                    a {@link ScriptUser} object of the given username,
     *                            returns {@code null} if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       ScriptUser
     */
    public ScriptUser getUser(String username) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the {@link ScriptUser} object of the given {@code userid},
     * returns {@code null} if nothing was found.
     *
     * @param  userid             the user ID to get the {@link ScriptUser} object from
     * @return                    a {@link ScriptUser} object of the given user ID,
     *                            returns {@code null} if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       ScriptUser
     */
    public ScriptUser getUser(int userid) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the {@link ScriptUser} object of the latest registered user.
     *
     * @return                    a {@link ScriptUser} object of the latest registered user
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       ScriptUser
     */
    public ScriptUser getLastRegUser() throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Updates the {@link ScriptUser} object with whatever values set by the user.
     *
     * @param  user               the {@link ScriptUser} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       ScriptUser
     */
    public void updateUser(ScriptUser user) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates a {@link ScriptUser} object with whatever values set by the user.
     *
     * @param user                the {@link ScriptUser} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       ScriptUser
     */
    public void createUser(ScriptUser user) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with {@link Group} objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the returned List will be empty.
     *
     * @param  limit              the limit, set to {@code 0} if you want to return all
     * @return                    List with {@link Group} objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     * @throws SQLException       if a SQL exception occurred
     * @see                       Group
     */
    public List<Group> getGroups(int limit) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a {@code Integer} which holds the ID of the group.
     *
     * @param  group              name of the group
     * @return                    id of the group
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getGroupID(String group) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a {@link Group} object of the given {@code groupid},
     * returns {@code null} if nothing is found
     *
     * @param  groupid            the group ID
     * @return                    {@link Group} object, returns {@code null} if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       Group
     */
    public Group getGroup(int groupid) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a {@link Group} object of the given {@code group} name,
     * returns {@code null} if nothing is found.
     *
     * @param  group              the group name
     * @return                    {@link Group} object, returns {@code null} if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       Group
     */
    public Group getGroup(String group) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with the {@link Group} objects that the {@code username} is a part of.
     * <p>
     * The List will be empty if none are found.
     *
     * @param  username           the username to grab the groups of.
     * @return                    List with {@link Group} objects, returns an empty List if none are found.
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       Group
     */
    public List<Group> getUserGroups(String username) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Updates the {@link Group} object with whatever values set by the user.
     *
     * @param  group              the {@link Group} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       Group
     */
    public void updateGroup(Group group) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the {@link Group} object with whatever values set by the user.
     *
     * @param  group              the {@link Group} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       Group
     */
    public void createGroup(Group group) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a {@link PrivateMessage} object of the given {@code pmid},
     * returns {@code null} if nothing was found.
     *
     * @param  pmid               the private message ID
     * @return                    {@link PrivateMessage} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       PrivateMessage
     */
    public PrivateMessage getPM(int pmid) throws SQLException, ScriptException {
        throw new ScriptException();
    }
    
    /**
     * Returns a List with {@link PrivateMessage} objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit            the limit, set to {@code 0} if you want to return all
     * @return                  List with PrivateMessage objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     * @throws SQLException     if a SQL exception occurred
     * @see                     PrivateMessage
     */
    public List<PrivateMessage> getPMs(int limit) throws ScriptException, SQLException {
        throw new ScriptException();
    }

    /**
     * Returns a List with {@link PrivateMessage} objects replying to specified private message.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  pmid               the ID of the private message
     * @param  limit              the limit, set to {@code 0} if you want to return all
     * @return                    List with {@link PrivateMessage} objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     * @see                       PrivateMessage
     */
    public List<PrivateMessage> getPMReplies(int pmid, int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with {@link PrivateMessage} objects that the user has sent.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  username           the username to get the {@link PrivateMessage} objects from
     * @param  limit              the limit, set to {@code 0} if you want to return all
     * @return                    List with {@link PrivateMessage} objects, if none are found it returns an empty List
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       PrivateMessage
     */
    public List<PrivateMessage> getPMsSent(String username, int limit) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with {@link PrivateMessage} objects that the user has received.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  username           the username to get the {@link PrivateMessage} objects from
     * @param  limit              the limit, set to {@code 0} if you want to return all
     * @return                    List with {@link PrivateMessage} objects, if none are found it returns an empty List
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       PrivateMessage
     */
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a total amount of how many private messages are there.
     * 
     * @return                    total amount of private messages
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getPMCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns an amount of how many private messages are replying to specified private message.
     * 
     * @param  pmid               the ID of the message
     * @return                    an amount of private message replying to the message
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getPMReplyCount(int pmid) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns an amount of how many private messages {@code username} has sent.
     *
     * @param  username           the username to get the count from
     * @return                    the amount of how many private messages the {@code username} has sent,
     *                            returns {@code 0} if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getPMSentCount(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns an amount of how many private messages {@code username} has received.
     *
     * @param  username           the {@code username} to get the count from
     * @return                    the amount of how many private messages the {@code username} has received,
     *                            returns {@code 0} if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getPMReceivedCount(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Updates the {@link PrivateMessage} object with whatever values set by the user.
     *
     * @param  privateMessage     the {@link PrivateMessage} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       PrivateMessage
     */
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the {@link PrivateMessage} object with whatever values set by the user.
     *
     * @param  privateMessage     the {@link PrivateMessage} object
     * @throws SQLException       if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                       PrivateMessage
     */
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with {@code username}'s IP addresses.
     *
     * @param  username                    the username to get the IP addresses from
     * @return                             List with {@code username}'s IP addresses
     * @throws ScriptException  if the method is not supported by the script
     */
    public List<String> getIPs(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns a List with {@link Ban} objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit                      the limit, set to 0 if you want to return all
     * @return                             List with {@link Ban} objects, if none are found it returns an empty List
     * @throws ScriptException  if the method is not supported by the script
     * @see                                Ban
     */
    public List<Ban> getBans(int limit) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the {@link Ban} object with whatever values set by the user.
     *
     * @param  ban                         the {@link Ban} object
     * @throws SQLException                if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                                Ban
     */
    public void updateBan(Ban ban) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Creates the {@link Ban} object with whatever values set by the user.
     *
     * @param  ban                         the {@link Ban} object
     * @throws SQLException                if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     * @see                                Ban
     */
    public void addBan(Ban ban) throws SQLException, ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the complete count of how many bans that have been made.
     *
     * @return                             the amount of how many bans that have been made, returns {@code 0} if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getBanCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns {@code true} if {@code string} matches a ban in the script, returns {@code false} if not.
     *
     * @param  string                      string to search for
     * @return                             {@code true} if {@code string} is banned, returns {@code false} if not
     * @throws SQLException                if a SQL exception occurred
     * @throws ScriptException  if the method is not supported by the script
     */
    public boolean isBanned(String string) throws ScriptException, SQLException {
        throw new ScriptException();
    }

    /**
     * Returns {@code true} if {@code username} is registered, returns {@code false} if not.
     *
     * @param  username                    username to check if is registered
     * @return                             {@code true} if {@code username} is registered, {@code false} if not
     * @throws ScriptException  if the method is not supported by the script
     */
    public boolean isRegistered(String username) throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the complete count of how many users the script has registered.
     *
     * @return                             the amount of how many users the script has registered, returns {@code 0} if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getUserCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the complete count of how many groups the script has.
     *
     * @return                             the amount of how how many groups the script has, returns {@code 0} if none
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getGroupCount() throws ScriptException {
        throw new ScriptException();
    }

    /**
     * Returns the script's home URL.
     *
     * @return                             home URL of the script
     * @throws ScriptException  if the method is not supported by the script
     */
    public String getHomeURL() throws ScriptException {
        throw new ScriptException();
    }
}
