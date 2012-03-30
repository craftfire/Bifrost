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
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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

    public List<Group> getUserGroups();

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

    public List<PrivateMessage> getPMsSent(int limit);

    public List<PrivateMessage> getPMsReceived(int limit);

    public int getPMSentCount();

    public int getPMReceivedCount();

    public int getPostCount();

    public int getThreadCount();

    public boolean isBanned();

    public boolean isRegistered();

    public List<String> getIPs();

    public Thread getLastThread();

    public Post getLastPost();

    public void updateUser() throws SQLException;

    public void createUser() throws SQLException;
}
