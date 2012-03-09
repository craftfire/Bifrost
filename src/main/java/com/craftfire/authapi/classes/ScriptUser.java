/*
 * This file is part of AuthAPI <http://www.craftfire.com/>.
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
package com.craftfire.authapi.classes;

import java.awt.Image;
import java.util.Date;
import java.util.List;

import com.craftfire.commons.CraftCommons;

public class ScriptUser implements ScriptUserInterface {
    private final Script script;
    private int userid;
    private Date regdate, lastlogin, birthday;
    private Gender gender;
    private String username, title, nickname, realname, firstname, lastname, email, password, passwordsalt,
            statusmessage, avatarurl, profileurl, regip, lastip;
    private boolean activated;
    private List<Group> groups;

    public ScriptUser(Script script, int userid, String username, String password) {
        this.script = script;
        this.username = username;
        this.userid = userid;
        this.password = password;
    }

    public ScriptUser(Script script, String username, String password) {
        this.script = script;
        this.username = username;
        this.password = this.script.hashPassword(username, password);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public int getID() {
        return this.userid;
    }

    @Override
    public void setID(int id) {
        this.userid = id;
    }

    @Override
    public Date getRegDate() {
        return this.regdate;
    }

    @Override
    public void setRegDate(Date date) {
        this.regdate = date;
    }

    @Override
    public Date getLastLogin() {
        return this.lastlogin;
    }

    @Override
    public void setLastLogin(Date date) {
        this.lastlogin = date;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUserTitle() {
        return this.title;
    }

    @Override
    public void setUserTitle(String title) {
        this.title = title;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getRealName() {
        return this.realname;
    }

    @Override
    public void setRealName(String realname) {
        this.realname = realname;
    }

    @Override
    public String getFirstName() {
        return this.firstname;
    }

    @Override
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getLastName() {
        return this.lastname;
    }

    @Override
    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public List<Group> getUserGroups() {
        return this.groups;
    }

    @Override
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPasswordSalt() {
        return this.passwordsalt;
    }

    @Override
    public void setPasswordSalt(String passwordsalt) {
        this.passwordsalt = passwordsalt;
    }

    @Override
    public Date getBirthday() {
        return this.birthday;
    }

    @Override
    public void setBirthday(Date date) {
        this.birthday = date;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String getStatusMessage() {
        return this.statusmessage;
    }

    @Override
    public void setStatusMessage(String message) {
        this.statusmessage = message;
    }

    @Override
    public Image getAvatar() {
        return CraftCommons.urlToImage(this.avatarurl);
    }

    @Override
    public String getAvatarURL() {
        return this.avatarurl;
    }

    @Override
    public void setAvatarURL(String url) {
        this.avatarurl = url;
    }

    @Override
    public String getProfileURL() {
        return this.profileurl;
    }

    @Override
    public void setProfileURL(String url) {
        this.profileurl = url;
    }

    @Override
    public String getRegIP() {
        return this.regip;
    }

    @Override
    public void setRegIP(String ip) {
        this.regip = ip;
    }

    @Override
    public String getLastIP() {
        return this.lastip;
    }

    @Override
    public void setLastIP(String ip) {
        this.lastip = ip;
    }

    @Override
    public boolean isActivated() {
        return this.activated;
    }

    @Override
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public List<PrivateMessage> getPMsFromUser(int limit) {
        return this.script.getPMsSent(this.username, limit);
    }

    @Override
    public List<PrivateMessage> getPMsToUser(int limit) {
        return this.script.getPMsReceived(this.username, limit);
    }

    @Override
    public int getPMSentCount() {
        return this.script.getPMSentCount(this.username);
    }

    @Override
    public int getPMReceivedCount() {
        return this.script.getPMReceivedCount(this.username);
    }

    @Override
    public int getPostCount() {
        return this.script.getPostCount(this.username);
    }

    @Override
    public int getThreadCount() {
        return this.script.getThreadCount(this.username);
    }

    @Override
    public boolean isBanned() {
        if (this.script.isBanned(this.username)) {
            return true;
        } else if (this.script.isBanned(this.email)) {
            return true;
        } else if (this.script.isBanned(this.lastip)) {
            return true;
        } else if (this.script.isBanned(this.regip)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isRegistered() {
        return this.script.isRegistered(this.username);
    }

    @Override
    public List<String> getIPs() {
        return this.script.getIPs(this.username);
    }

    @Override
    public Thread getLastThread() {
        return this.script.getLastUserThread(this.username);
    }

    @Override
    public Post getLastPost() {
        return this.script.getLastUserPost(this.username);
    }

    @Override
    public void updateUser() {
        this.script.updateUser(this);
    }

    @Override
    public void createUser() {
        this.script.createUser(this);
    }
}