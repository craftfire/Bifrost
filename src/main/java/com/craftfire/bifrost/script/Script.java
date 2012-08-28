/*
 * This file is part of Bifrost <http://www.craftfire.com/>.
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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.Cache;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.ScriptType;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

import java.sql.SQLException;
import java.util.List;

public class Script {
    private final String version;
    private final Scripts script;
    private final DataManager dataManager;
    private final Cache cache;

    protected Script(Scripts script, String version, DataManager dataManager) {
        this.version = version;
        this.script = script;
        this.dataManager = dataManager;
        this.cache = new Cache();
    }

    public Bifrost getBifrost() {
        return Bifrost.getInstance();
    }

    public LoggingManager getLoggingManager() {
        return Bifrost.getInstance().getLoggingManager();
    }

    public Cache getCache() {
        return this.cache;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }

    public Scripts getScript() {
        return this.script;
    }

    public ScriptType getType() {
        return this.script.getType();
    }

    /**
     * Returns the latest version which is supported by the script.
     *
     * @return Latest supported version of the script.
     */
    public String getLatestVersion() {
        return null;
    }

    /**
     * Returns {@code true} if the user version is supported by the script.
     *
     * @return true if supported, false if not.
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
     * @return the version.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns an array of version ranges which the script supports.
     *
     * @return the version ranges.
     */
    public String[] getVersionRanges()  {
        return null;
    }

    /**
     * Returns which encryption method is used by the script to hash the passwords.
     *
     * This can be sha-1, sha-256, sha-512, whirpool, md5 and so on.
     *
     * @return Encryption method.
     * @throws com.craftfire.bifrost.exceptions.UnsupportedFunction if the function is not supported by the script.
     */
    public Encryption getEncryption() throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the script's full name, for example SimpleMachines.
     *
     * @return The script's full name.
     */
    public String getScriptName() {
        return null;
    }

    /**
     * Returns the script's short name, for example SMF.
     *
     * @return The script's short name.
     */
    public String getScriptShortname() {
        return null;
    }

    /**
     * Returns true if {@param username} and {@param password} matches the username and password for the user in the
     * script.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return         True if the username and password matches.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public boolean authenticate(String username, String password) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Hashes the password of the user with whatever encryption the script uses and returns the hashed string.
     *
     * @param salt     The salt of the user to hash.
     * @param password The password of the user to hash.
     * @return         A hashed string.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public String hashPassword(String salt, String password) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the username of {@param userid}, if none is found it returns null.
     *
     * @param userid The userid to get the username of.
     * @return       Username of {@param userid} or null if nothing was found.
     * @throws       UnsupportedFunction if the function is not supported by the script.
     */
    public String getUsername(int userid) throws UnsupportedFunction, SQLException {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the user ID of {@param username}, if none is found it returns 0.
     *
     * @param username The user ID to get the username of.
     * @return         User ID of {@param username} or 0 if nothing was found.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public int getUserID(String username) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the {@link com.craftfire.bifrost.classes.general.ScriptUser} object of the given username, returns null if nothing was found.
     *
     * @param username The username to get the ScriptUser object from.
     * @return         A ScriptUser object of the given username, returns null if nothing was found.
     * @see            com.craftfire.bifrost.classes.general.ScriptUser
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public ScriptUser getUser(String username) throws UnsupportedFunction, SQLException {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the ScriptUser object of the given user ID, returns null if nothing was found.
     *
     * @param userid The user ID to get the ScriptUser object from.
     * @return       A ScriptUser object of the given user ID, returns null if nothing was found.
     * @see          ScriptUser
     * @throws       UnsupportedFunction if the function is not supported by the script.
     */
    public ScriptUser getUser(int userid) throws UnsupportedFunction, SQLException {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the {@link ScriptUser} object of the latest registered user.
     *
     * @return  A ScriptUser object of the latest registered user.
     * @see     ScriptUser
     * @throws  UnsupportedFunction if the function is not supported by the script.
     */
    public ScriptUser getLastRegUser() throws UnsupportedFunction, SQLException {
        throw new UnsupportedFunction();
    }

    /**
     * Updates the ScriptUser object with whatever values set by the user.
     *
     * @param user The ScriptUser object.
     * @see        ScriptUser
     * @throws     SQLException if a MySQL exception occurred.
     * @throws     UnsupportedFunction if the function is not supported by the script.
     */
    public void updateUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Creates a ScriptUser object with whatever values set by the user.
     *
     * @param user The ScriptUser object.
     * @see        ScriptUser
     * @throws     SQLException if a MySQL exception occurred.
     * @throws     UnsupportedFunction if the function is not supported by the script.
     */
    public void createUser(ScriptUser user) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a List with {@link com.craftfire.bifrost.classes.general.Group} objects.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the returned List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return      List with Group objects, if none are found it returns an empty List.
     * @see         com.craftfire.bifrost.classes.general.Group
     * @throws      SQLException if a MySQL exception occurred.
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
    public List<Group> getGroups(int limit) throws UnsupportedFunction, SQLException {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a Integer which holds the ID of the group.
     *
     * @param group Name of the group.
     * @return      ID of the group.
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
    public int getGroupID(String group) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a Group object of the given group id, if nothing is found it returns null.
     *
     * @param groupid The group ID.
     * @return        Group object, null if nothing was found.
     * @see           Group
     * @throws        UnsupportedFunction if the function is not supported by the script.
     */
    public Group getGroup(int groupid) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a Group object of the given group name, if nothing is found it returns null.
     *
     * @param group The group name.
     * @return      Group object, null if nothing was found.
     * @see         Group
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
    public Group getGroup(String group) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a List with the Group objects that the user is a part of.
     * If none are found, the List will be empty.
     *
     * @param username The username to grab the groups of.
     * @return         List with Group objects, if none are found it returns an empty List.
     * @see            Group
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public List<Group> getUserGroups(String username) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Updates the Group object with whatever values set by the user.
     *
     * @param group The Group object.
     * @see         Group
     * @throws      SQLException if a MySQL exception occurred.
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
    public void updateGroup(Group group) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Creates the Group object with whatever values set by the user.
     *
     * @param group The Group object.
     * @see         Group
     * @throws      SQLException if a MySQL exception occurred.
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
    public void createGroup(Group group) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a PrivateMessage object of the given private message id, if nothing is found it returns null.
     *
     * @param pmid The private message ID.
     * @return     PrivateMessage object
     * @see        com.craftfire.bifrost.classes.general.PrivateMessage
     * @throws     UnsupportedFunction if the function is not supported by the script.
     */
    public PrivateMessage getPM(int pmid) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a List with PrivateMessage objects that the user has sent.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username The username to get the PrivateMessage objects from.
     * @param limit    The limit. Set to 0 if you want to return all.
     * @return         List with Group objects, if none are found it returns an empty List.
     * @see            PrivateMessage
     * @see            List
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public List<PrivateMessage> getPMsSent(String username, int limit) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a List with PrivateMessage objects that the user has received.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username The username to get the PrivateMessage objects from.
     * @param limit    The limit. Set to 0 if you want to return all.
     * @return         List with Group objects, if none are found it returns an empty List.
     * @see            PrivateMessage
     * @see            List
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns an amount of how many private messages {@param username} has sent.
     *
     * @param username The username to get the count from.
     * @return         The amount of how many private messages the username has sent, returns 0 if none.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public int getPMSentCount(String username) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns an amount of how many private messages {@param username} has received.
     *
     * @param username The username to get the count from.
     * @return         The amount of how many private messages the username has received, returns 0 if none.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public int getPMReceivedCount(String username) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Updates the PrivateMessage object with whatever values set by the user.
     *
     * @param privateMessage The PrivateMessage object.
     * @see                  PrivateMessage
     * @throws               SQLException if a MySQL exception occurred.
     * @throws               UnsupportedFunction if the function is not supported by the script.
     */
    public void updatePrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Creates the PrivateMessage object with whatever values set by the user.
     *
     * @param privateMessage The PrivateMessage object.
     * @see                  PrivateMessage
     * @throws               SQLException if a MySQL exception occurred.
     * @throws               UnsupportedFunction if the function is not supported by the script.
     */
    public void createPrivateMessage(PrivateMessage privateMessage) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a List with {@param username}'s IP addresses.
     *
     * @param username The username to get the IP addresses from.
     * @return         List with {@param username}'s IP addresses.
     * @see            List
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public List<String> getIPs(String username) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns a List with Ban objects.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return      List with Ban objects, if none are found it returns an empty List.
     * @see         com.craftfire.bifrost.classes.general.Ban
     * @see         List
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
    public List<Ban> getBans(int limit) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Creates the Ban object with whatever values set by the user.
     *
     * @param ban The Ban object.
     * @see       Ban
     * @throws    SQLException if a MySQL exception occurred.
     * @throws    UnsupportedFunction if the function is not supported by the script.
     */
    public void updateBan(Ban ban) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Creates the Ban object with whatever values set by the user.
     *
     * @param ban The Ban object.
     * @see       Ban
     * @throws    SQLException if a MySQL exception occurred.
     * @throws    UnsupportedFunction if the function is not supported by the script.
     */
    public void addBan(Ban ban) throws SQLException, UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the complete count of how many bans that have been made.
     *
     * @return The amount of how many bans that have been made, returns 0 if none.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
    public int getBanCount() throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns true if {@param string} matches a ban in the script.
     *
     * @param string String to search for.
     * @return       True if {@param string} is banned.
     * @throws       UnsupportedFunction if the function is not supported by the script.
     */
    public boolean isBanned(String string) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns true if {@param username} is already registered.
     *
     * @param username Username to check if is registered.
     * @return         True if {@param username} is registered.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
    public boolean isRegistered(String username) throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the complete count of how many users the script has registered.
     *
     * @return The amount of how many users the script has registered, returns 0 if none.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
    public int getUserCount() throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }

    /**
     * Returns the complete count of how many groups the script has.
     *
     * @return The amount of how how many groups the script has, returns 0 if none.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
    public int getGroupCount() throws UnsupportedFunction {
        throw new UnsupportedFunction();
    }
}
