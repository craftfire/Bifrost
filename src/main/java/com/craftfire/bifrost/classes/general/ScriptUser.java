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

import java.awt.Image;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.craftfire.commons.CraftCommons;

import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Gender;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class should only be used with a script user.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@link #update()}.
 * <p>
 * When creating a new script user make sure you use the correct constructor:
 * {@link #ScriptUser(ScriptHandle, String, String)}.
 * <p>
 * Remember to run {@link #create()} after creating a user to insert it into the script.
 */
public class ScriptUser extends GenericMethods {
    private Date regdate, lastlogin, birthday;
    private Gender gender;
    private String username, title, nickname, realname, firstname, lastname, email, password, passwordsalt,
            statusmessage, avatarurl, profileurl, regip, lastip;
    private boolean activated, anonymous;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script    the script
     * @param userid    the ID of the user
     * @param username  the username of the user
     * @param password  the password for the user
     */
    public ScriptUser(Script script, int userid, String username, String password) {
        super(script.getHandle());
        this.username = username;
        this.setID(userid);
        this.password = password;
    }

    /**
     * This constructor should be used when creating a new user for the script.
     * <p>
     * Remember to run {@link #create()} after creating a user to insert it into the script.
     *
     * @param handle    the handle
     * @param username  the username of the user
     * @param password  the password of the user
     */
    public ScriptUser(ScriptHandle handle, String username, String password) {
        super(handle);
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username of the user.
     *
     * @return username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username  username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the registration date of the user.
     *
     * @return registration date of the user
     */
    public Date getRegDate() {
        return this.regdate;
    }

    /**
     * Sets the registration date of the user.
     *
     * @param date  registration date of the user
     */
    public void setRegDate(Date date) {
        this.regdate = date;
    }

    /**
     * Returns the last login date of the user.
     *
     * @return last login date of the user
     */
    public Date getLastLogin() {
        return this.lastlogin;
    }

    /**
     * Sets the last login date of the user.
     *
     * @param date  last login date of the user
     */
    public void setLastLogin(Date date) {
        this.lastlogin = date;
    }

    /**
     * Returns the title of the user.
     *
     * @return title of the user
     */
    public String getUserTitle() {
        return this.title;
    }

    /**
     * Sets the title of the user.
     *
     * @param title  title of the user
     */
    public void setUserTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the nickname of the user.
     *
     * @return nickname of the user
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Sets the nickname of the user.
     *
     * @param nickname  nickname of the user
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the real name of the user.
     *
     * @return real name of the user
     */
    public String getRealName() {
        return this.realname;
    }

    /**
     * Sets the real name of the user.
     *
     * @param realname  real name of the user
     */
    public void setRealName(String realname) {
        this.realname = realname;
    }

    /**
     * Returns the first name of the user.
     *
     * @return first name of the user
     */
    public String getFirstName() {
        return this.firstname;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstname  first name of the user
     */
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Returns the last name of the user.
     *
     * @return last name of the user
     */
    public String getLastName() {
        return this.lastname;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastname  last name of the user
     */
    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Returns the list of Groups that the user is a member of.
     * <p>
     * Loads the groups from a database if not cached.
     *
     * @return                    the list of groups
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @throws SQLException       if a SQL error concurs
     */
    public List<Group> getGroups() throws UnsupportedMethod, SQLException {
        return getHandle().getUserGroups(this.username);
    }

    /**
     * Returns the email of the user.
     *
     * @return email of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email  email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     *
     * @return password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password  password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password salt of the user.
     *
     * @return password salt of the user
     */
    public String getPasswordSalt() {
        return this.passwordsalt;
    }

    /**
     * Sets the password salt of the user.
     *
     * @param passwordsalt  password salt of the user
     */
    public void setPasswordSalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    /**
     * Returns the birthday date of the user.
     *
     * @return birthday date of the user
     */
    public Date getBirthday() {
        return this.birthday;
    }

    /**
     * Sets the birthday date of the user.
     *
     * @param date  birthday date of the user
     */
    public void setBirthday(Date date) {
        this.birthday = date;
    }

    /**
     * Returns the gender of the user.
     *
     * @return gender of the user
     */
    public Gender getGender() {
        return this.gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender  gender of the user
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Returns the status message of the user.
     *
     * @return status message of the user
     */
    public String getStatusMessage() {
        return this.statusmessage;
    }

    /**
     * Sets the status message of the user.
     *
     * @param message  status message of the user
     */
    public void setStatusMessage(String message) {
        this.statusmessage = message;
    }

    /**
     * Returns the avatar of the user.
     *
     * @return avatar of the user
     */
    public Image getAvatar() {
        return CraftCommons.urlToImage(this.avatarurl);
    }

    /**
     * Returns the avatar URL of the user.
     *
     * @return avatar URL of the user
     */
    public String getAvatarURL() {
        return this.avatarurl;
    }

    /**
     * Sets the avatar URL of the user.
     *
     * @param url  avatar URL of the user
     */
    public void setAvatarURL(String url) {
        this.avatarurl = url;
    }

    /**
     * Returns the profile URL of the user.
     *
     * @return profile URL of the user
     */
    public String getProfileURL() {
        return this.profileurl;
    }

    /**
     * Sets the profile URL of the user.
     *
     * @param url  profile URL of the user
     */
    public void setProfileURL(String url) {
        this.profileurl = url;
    }

    /**
     * Returns the registration IP-address of the user.
     *
     * @return registration IP-address of the user
     */
    public String getRegIP() {
        return this.regip;
    }

    /**
     * Sets the registration IP-address of the user.
     *
     * @param ip  registration IP-address of the user
     */
    public void setRegIP(String ip) {
        this.regip = ip;
    }

    /**
     * Returns the last IP-address of the user.
     *
     * @return last IP-address of the user
     */
    public String getLastIP() {
        return this.lastip;
    }

    /**
     * Sets the last IP-address of the user.
     *
     * @param ip  last IP-address of the user
     */
    public void setLastIP(String ip) {
        this.lastip = ip;
    }

    /**
     * Returns if an user is activated or not, {@code true} if activated, {@code false} if not.
     *
     * @return {@code true} if user is activated, {@code false} if not
     */
    public boolean isActivated() {
        return this.activated;
    }

    /**
     * Sets if an user is activated or not, {@code true} if activated, {@code false} if not.
     *
     * @param activated  {@code true} if user is activated, {@code false} if not
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * Returns if an user is anonymous or not, {@code true} if anonymous, {@code false} if not.
     *
     * @return {@code true} if user is anonymous, {@code false} if not
     */
    public boolean isAnonymous() {
        return this.anonymous;
    }

    /**
     * Sets if an user is anonymous or not, {@code true} if anonymous, {@code false} if not.
     *
     * @param anonymous  {@code true} if user is anonymous, {@code false} if not
     */
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * Returns the list of private messages that the user has sent.
     * <p>
     * Loads the private messages from a database if not cached.
     *
     * @param limit               how many private messages should be returned, 0 = returns all
     * @return                    the list of sent private messages
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @throws SQLException       if a SQL error concurs
     */
    public List<PrivateMessage> getPMsSent(int limit) throws UnsupportedMethod, SQLException {
        return getHandle().getPMsSent(this.username, limit);
    }

    /**
     * Returns the list of private messages that the user has received.
     * <p>
     * Loads the private messages from a database if not cached.
     *
     * @param limit               how many private messages should be returned, 0 = returns all
     * @return                    the list of received private messages
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @throws SQLException       if a SQL error concurs
     */
    public List<PrivateMessage> getPMsReceived(int limit) throws UnsupportedMethod, SQLException {
        return getHandle().getPMsReceived(this.username, limit);
    }

    /**
     * Returns the amount private messages that the user has sent.
     *
     * @return                    amount of sent private messages
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPMSentCount() throws UnsupportedMethod {
        return getHandle().getPMSentCount(this.username);
    }

    /**
     * Returns the amount private messages that the user has received.
     *
     * @return                    amount of received private messages
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPMReceivedCount() throws UnsupportedMethod {
        return getHandle().getPMReceivedCount(this.username);
    }

    /**
     * Returns if an user is banned or not, {@code true} if banned, {@code false} if not.
     *
     * @return {@code true} if user is banned, {@code false} if not
     */
    public boolean isBanned() throws UnsupportedMethod, SQLException {
        if (getHandle().isBanned(this.username)) {
            return true;
        } else if (getHandle().isBanned(this.email)) {
            return true;
        } else if (getHandle().isBanned(this.lastip)) {
            return true;
        } else {
            return getHandle().isBanned(this.regip);
        }
    }

    /**
     * Returns if an user is registered or not, {@code true} if registered, {@code false} if not.
     *
     * @return {@code true} if user is registered, {@code false} if not
     */
    public boolean isRegistered() throws UnsupportedMethod {
        return getHandle().isRegistered(this.username);
    }

    /**
     * Returns the list of IP-addresses that the user has been seen with.
     * <p>
     * Loads the IP-addresses from a database if not cached.
     *
     * @return                    the list of IP-addresses
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<String> getIPs() throws UnsupportedMethod {
        return getHandle().getIPs(this.username);
    }

    /**
     * This method should be run after changing any user values.
     * <p>
     * It should <b>not</b> be run when creating a new user,
     * only when editing an already existing users.
     * <p>
     * Use {@link #create()} instead if you wish to update the user.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getHandle().updateUser(this);
    }

    /**
     * This method should be run after creating a new user.
     * <p>
     * It should <b>not</b> be run when updating a user, only when creating a new user.
     * <p>
     * Use {@link #update()} instead if you wish to update the user.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getHandle().createUser(this);
    }

    /**
     * Returns {@code true} if the handle contains a user cache with the given id parameter,
     * {@code false} if not.
     *
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        {@code true} if contains, {@code false} if not
     */
    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.USER, id);
    }

    /**
     * Adds a ScriptUser to the cache with the given script handle.
     *
     * @param handle      the script handle
     * @param scriptUser  the ScriptUser object
     */
    public static void addCache(ScriptHandle handle, ScriptUser scriptUser) {
        ScriptUser.addCache(handle, 0, scriptUser);
    }

    /**
     * Adds a ScriptUser to the cache with the given script handle and id.
     *
     * @param handle      the script handle
     * @param id          the id of the user
     * @param scriptUser  the ScriptUser object
     */
    public static void addCache(ScriptHandle handle, int id, ScriptUser scriptUser) {
        if (scriptUser == null) {
            if (id != 0) {
                handle.getCache().putMetadatable(CacheGroup.USER, id, scriptUser);
            }
            return;
        }
        handle.getCache().setMetadata(CacheGroup.USER, scriptUser.getID(), "bifrost-cache.old-username", scriptUser.getUsername());
    }

    /**
     * Returns the ScriptUser object by the given id if found, returns {@code null} if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the user
     * @return ScriptUser object if cache was found, {@code null} if no cache was found
     */
    public static ScriptUser getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.USER, id)) {
            return (ScriptUser) handle.getCache().get(CacheGroup.USER, id);
        }
        return null;
    }

    /**
     * Removes outdated cache elements related to given {@code user} from cache.
     * <p>
     * The method should be called when updating or creating a {@link ScriptUser}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     * 
     * @param handle  the handle the method is called from
     * @param user    the user to cleanup related cache
     * @param reason  the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     * @see           Cache
     */
    public static void cleanupCache(ScriptHandle handle, ScriptUser user, CacheCleanupReason reason) {
        handle.getCache().remove(CacheGroup.USER_ID, user.getUsername());
        handle.getCache().remove(CacheGroup.USER_USERNAME, user.getID());
        handle.getCache().remove(CacheGroup.IS_BANNED, user.getUsername());
        handle.getCache().remove(CacheGroup.IS_REGISTERED, user.getUsername());
        switch (reason) {
        case CREATE:
            handle.getCache().clear(CacheGroup.USER_COUNT);
            break;
        case OTHER:
            handle.getCache().clear(CacheGroup.USER_COUNT);
            /* Passes through */
        case UPDATE:
            Object oldUsername = handle.getCache().getMetadata(CacheGroup.USER, user.getID(), "bifrost-cache.old-username");
            if (!user.getUsername().equals(oldUsername)) {
                handle.getCache().remove(CacheGroup.USER_IP, user.getUsername());
                handle.getCache().remove(CacheGroup.ARTICLE_COUNT_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.ARTICLE_LIST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.ARTICLE_LAST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.COMMENT_COUNT_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.COMMENT_LIST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.COMMENT_LAST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.POST_COUNT, user.getUsername());
                handle.getCache().remove(CacheGroup.POST_LIST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.POST_LAST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.THREAD_COUNT, user.getUsername());
                handle.getCache().remove(CacheGroup.THREAD_LIST_USER, user.getUsername());
                handle.getCache().remove(CacheGroup.THREAD_LAST_USER, user.getUsername());

                handle.getCache().remove(CacheGroup.USER_USERNAME, user.getID());
                handle.getCache().remove(CacheGroup.IS_BANNED, oldUsername);
                handle.getCache().remove(CacheGroup.IS_REGISTERED, oldUsername);
                handle.getCache().remove(CacheGroup.USER_IP, oldUsername);
                handle.getCache().remove(CacheGroup.ARTICLE_COUNT_USER, oldUsername);
                handle.getCache().remove(CacheGroup.ARTICLE_LIST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.ARTICLE_LAST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.COMMENT_COUNT_USER, oldUsername);
                handle.getCache().remove(CacheGroup.COMMENT_LIST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.COMMENT_LAST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.POST_COUNT, oldUsername);
                handle.getCache().remove(CacheGroup.POST_LIST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.POST_LAST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.THREAD_COUNT, oldUsername);
                handle.getCache().remove(CacheGroup.THREAD_LIST_USER, oldUsername);
                handle.getCache().remove(CacheGroup.THREAD_LAST_USER, oldUsername);
            }

        }
    }

}
