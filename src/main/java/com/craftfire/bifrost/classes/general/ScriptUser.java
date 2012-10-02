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

import com.craftfire.bifrost.Bifrost;
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

    public ScriptUser(ScriptHandle handle, String username, String password) {
        super(handle);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public Date getRegDate() {
        return this.regdate;
    }

    public void setRegDate(Date date) {
        this.regdate = date;
    }

    public Date getLastLogin() {
        return this.lastlogin;
    }

    public void setLastLogin(Date date) {
        this.lastlogin = date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserTitle() {
        return this.title;
    }

    public void setUserTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealName() {
        return this.realname;
    }

    public void setRealName(String realname) {
        this.realname = realname;
    }

    public String getFirstName() {
        return this.firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return this.lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public List<Group> getGroups() throws UnsupportedMethod, SQLException {
        return getHandle().getUserGroups(this.username);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return this.passwordsalt;
    }

    public void setPasswordSalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date date) {
        this.birthday = date;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getStatusMessage() {
        return this.statusmessage;
    }

    public void setStatusMessage(String message) {
        this.statusmessage = message;
    }

    public Image getAvatar() {
        return CraftCommons.urlToImage(this.avatarurl);
    }

    public String getAvatarURL() {
        return this.avatarurl;
    }

    public void setAvatarURL(String url) {
        this.avatarurl = url;
    }

    public String getProfileURL() {
        return this.profileurl;
    }

    public void setProfileURL(String url) {
        this.profileurl = url;
    }

    public String getRegIP() {
        return this.regip;
    }

    public void setRegIP(String ip) {
        this.regip = ip;
    }

    public String getLastIP() {
        return this.lastip;
    }

    public void setLastIP(String ip) {
        this.lastip = ip;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public List<PrivateMessage> getPMsSent(int limit) throws UnsupportedMethod, SQLException {
        return getHandle().getPMsSent(this.username, limit);
    }

    public List<PrivateMessage> getPMsReceived(int limit) throws UnsupportedMethod, SQLException {
        return getHandle().getPMsReceived(this.username, limit);
    }

    public int getPMSentCount() throws UnsupportedMethod {
        return getHandle().getPMSentCount(this.username);
    }

    public int getPMReceivedCount() throws UnsupportedMethod {
        return getHandle().getPMReceivedCount(this.username);
    }

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

    public boolean isRegistered() throws UnsupportedMethod {
        return getHandle().isRegistered(this.username);
    }

    public List<String> getIPs() throws UnsupportedMethod {
        return getHandle().getIPs(this.username);
    }

    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getHandle().updateUser(this);
    }

    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getHandle().createUser(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.USER, id);
    }

    public static void addCache(ScriptHandle handle, ScriptUser scriptUser) {
        ScriptUser.addCache(handle, 0, scriptUser);
    }
    public static void addCache(ScriptHandle handle, int id, ScriptUser scriptUser) {
        if (scriptUser == null) {
            if (id != 0) {
                handle.getCache().putMetadatable(CacheGroup.USER, id, scriptUser);
            }
            return;
        }
        handle.getCache().setMetadata(CacheGroup.USER, scriptUser.getID(), "bifrost-cache.old-username", scriptUser.getUsername());
    }

    public static ScriptUser getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.USER, id)) {
            return (ScriptUser) handle.getCache().get(CacheGroup.USER, id);
        }
        return null;
    }

    /**
     * Removes outdated cache elements related to given {@param user} from cache.
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
