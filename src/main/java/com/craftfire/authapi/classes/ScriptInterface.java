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

import com.craftfire.authapi.ScriptAPI;

public interface ScriptInterface {
	public String getLatestVersion();

	public String getVersion();

	public String getEncryption();

	public String getScriptName();

	public String getScriptShortname();

	public boolean authenticate(String username, String password);

	public String hashPassword(String username, String password);

	public Date getRegDate(String username);

	public void setRegDate(String username, Date date);

	public Date getLastLogin(String username);

	public void setLastLogin(String username, Date date);

	public ScriptAPI.AccountStatus getAccountStatus(String username);

	public void setAccountStatus(String username, ScriptAPI.AccountStatus status);

	public String getUserName(int userid);

	public void setUserName(String username, String newUsername);

	public String getUserTitle(String username);

	public void setUserTitle(String username, String title);

	public String getNickName(String username);

	public void setNickname(String username, String nickname);

	public String getRealName(String username);

	public void setRealName(String username, String realname);

	public String getFirstName(String username);

	public void setFirstName(String username, String firstname);

	public String getLastName(String username);

	public void setLastName(String username, String lastname);

	public List<Group> getGroups(String username);

	public void setGroups(List<Group> groups);

	public String getEmail(String username);

	public void setEmail(String username, String email);

	public void setPassword(String username, String password);

	public String getPasswordHash(String username);

	public void setPasswordHash(String username, String passwordHash);

	public String getPasswordSalt(String username);

	public void setPasswordSalt(String username, String passwordSalt);

	public Date getBirthday(String username);

	public void setBirthday(String username, Date date);

	public ScriptAPI.Gender getGender(String username);

	public void setGender(String username, ScriptAPI.Gender gender);

	public String getStatusMessage(String username);

	public void setStatusMessage(String username, String message);

	public List<Post> getPosts(int amount);

	public int getPostCount(String username);

	public int getTotalPostCount();

	public Post getLastPost();

	public Post getLastUserPost(String username);

	public int getTotalThreadCount();

	public int getThreadCount(String username);

	public Thread getLastThread();

	public Thread getLastUserThread(String username);

	public List<Thread> getThreads();

	public int getUserCount();

	public int getGroupCount();

	public Image getAvatar(String username);

	public String getAvatarURL(String username);

	public String getProfileURL(String username);

	public String getHomeURL();

	public String getForumURL();

	public List<String> getIPs(String username);

	public String getRegIP(String username);

	public String getLastIP(String username);

	public List<Ban> getBans();

	public int getBanCount();

	public boolean isBanned(String string);

	public boolean isActivated(String username);

	public boolean isRegistered(String username);
}
