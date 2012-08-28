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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Gender;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;
import com.craftfire.commons.CraftCommons;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScriptUser {
    private int userid;
    private Date regdate, lastlogin, birthday;
    private Gender gender;
    private String username, title, nickname, realname, firstname, lastname, email, password, passwordsalt,
            statusmessage, avatarurl, profileurl, regip, lastip;
    private boolean activated;
    private List<Group> groups = new ArrayList<Group>();
    private final Script script;

    public ScriptUser(Script script, int userid, String username, String password) {
        this.script = script;
        this.username = username;
        this.userid = userid;
        this.password = password;
    }

    public ScriptUser(Script script, String username, String password) {
        this.script = script;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public int getID() {
        return this.userid;
    }

    public void setID(int id) {
        this.userid = id;
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

    @SuppressWarnings("unchecked")
    public List<Group> getGroups() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).getUserGroups(this.username);
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
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

    public List<PrivateMessage> getPMsSent(int limit) throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).getPMsSent(this.username, limit);
    }

    public List<PrivateMessage> getPMsReceived(int limit) throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).getPMsReceived(this.username, limit);
    }

    public int getPMSentCount() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).getPMSentCount(this.username);
    }

    public int getPMReceivedCount() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).getPMReceivedCount(this.username);
    }

    public int getPostCount() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getPostCount(this.username);
    }

    public int getThreadCount() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getThreadCount(this.username);
    }

    public boolean isBanned() throws UnsupportedFunction {
        if (Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).isBanned(this.username)) {
            return true;
        } else if (Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).isBanned(this.email)) {
            return true;
        } else if (Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).isBanned(this.lastip)) {
            return true;
        } else return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).isBanned(this.regip);
    }

    public boolean isRegistered() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).isRegistered(this.username);
    }

    public List<String> getIPs() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).getIPs(this.username);
    }

    public ForumThread getLastThread() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getLastUserThread(this.username);
    }

    public ForumPost getLastPost() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getLastUserPost(this.username);
    }

    public void updateUser() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).updateUser(this);
    }

    public void createUser() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).createUser(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.USER, id);
    }

    public static void addCache(ScriptHandle handle, ScriptUser scriptUser) {
        handle.getCache().put(CacheGroup.USER, scriptUser.getID(), scriptUser);
    }

    @SuppressWarnings("unchecked")
    public static ScriptUser getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.USER, id)) {
            return (ScriptUser) handle.getCache().get(CacheGroup.USER, id);
        }
        return null;
    }
}
