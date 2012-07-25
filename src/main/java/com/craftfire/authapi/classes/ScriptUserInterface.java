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
package com.craftfire.authapi.classes;

import java.awt.Image;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.craftfire.authapi.exceptions.UnsupportedFunction;

public abstract interface ScriptUserInterface {
    public int getID();

    public void setID(int id);

    public Date getRegDate();

    public void setRegDate(Date date);

    public Date getLastLogin();

    public void setLastLogin(Date date);

    public String getUsername();

    public void setUsername(String username);

    public String getUserTitle();

    public void setUserTitle(String title);

    public String getNickname();

    public void setNickname(String nickname);

    public String getRealName();

    public void setRealName(String realname);

    public String getFirstName();

    public void setFirstName(String firstname);

    public String getLastName();

    public void setLastName(String lastname);

    public List<Group> getUserGroups() throws UnsupportedFunction;

    public void setGroups(List<Group> groups);

    public String getEmail();

    public void setEmail(String email);

    public String getPassword();

    public void setPassword(String password);

    public String getPasswordSalt();

    public void setPasswordSalt(String passwordsalt);

    public Date getBirthday();

    public void setBirthday(Date date);

    public Gender getGender();

    public void setGender(Gender gender);

    public String getStatusMessage();

    public void setStatusMessage(String message);

    public Image getAvatar();

    public String getAvatarURL();

    public void setAvatarURL(String url);

    public String getProfileURL();

    public void setProfileURL(String url);

    public String getRegIP();

    public void setRegIP(String ip);

    public String getLastIP();

    public void setLastIP(String ip);

    public boolean isActivated();

    public void setActivated(boolean activated);

    public List<PrivateMessage> getPMsSent(int limit) throws UnsupportedFunction;

    public List<PrivateMessage> getPMsReceived(int limit) throws UnsupportedFunction;

    public int getPMSentCount() throws UnsupportedFunction;

    public int getPMReceivedCount() throws UnsupportedFunction;

    public int getPostCount() throws UnsupportedFunction;

    public int getThreadCount() throws UnsupportedFunction;

    public boolean isBanned() throws UnsupportedFunction;

    public boolean isRegistered() throws UnsupportedFunction;

    public List<String> getIPs() throws UnsupportedFunction;

    public Thread getLastThread() throws UnsupportedFunction;

    public Post getLastPost() throws UnsupportedFunction;

    public void updateUser() throws SQLException, UnsupportedFunction;

    public void createUser() throws SQLException, UnsupportedFunction;
}
