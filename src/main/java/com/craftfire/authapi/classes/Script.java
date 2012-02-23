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
import com.craftfire.authapi.ScriptAPI.Scripts;

public class Script implements ScriptInterface {
	private final String version;
	private final Scripts script;
	private ScriptUser user;

	protected Script(Scripts script, String version) {
		this.version = version;
		this.script = script;
	}

	public void setUser(String username, String password) {
		this.user = new ScriptUser(this, username, password);
	}

	public ScriptUser getUser() {
		return this.user;
	}

	@Override
	public String getLatestVersion() {
		return null;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public String getEncryption() {
		return null;
	}

	@Override
	public String getScriptName() {
		return null;
	}

	@Override
	public String getScriptShortname() {
		return null;
	}

	@Override
	public boolean authenticate(String username, String password) {
		return false;
	}

	@Override
	public String hashPassword(String username, String password) {
		return null;
	}

	@Override
	public Date getRegDate(String username) {
		return null;
	}

	@Override
	public void setRegDate(String username, Date date) {
	}

	@Override
	public Date getLastLogin(String username) {
		return null;
	}

	@Override
	public void setLastLogin(String username, Date date) {
	}

	@Override
	public ScriptAPI.AccountStatus getAccountStatus(String username) {
		return null;
	}

	@Override
	public void setAccountStatus(String username, ScriptAPI.AccountStatus status) {
	}

	@Override
	public String getUserName(int userid) {
		return null;
	}

    @Override
    public int getUserId(String username) {
        return 0;
    }

	@Override
	public void setUserName(String username, String newUsername) {
	}

	@Override
	public String getUserTitle(String username) {
		return null;
	}

	@Override
	public void setUserTitle(String username, String title) {

	}

	@Override
	public String getNickName(String username) {
		return null;
	}

	@Override
	public void setNickname(String username, String nickname) {
	}

	@Override
	public String getRealName(String username) {
		return null;
	}

	@Override
	public void setRealName(String username, String realname) {
	}

	@Override
	public String getFirstName(String username) {
		return null;
	}

	@Override
	public void setFirstName(String username, String firstname) {
	}

	@Override
	public String getLastName(String username) {
		return null;
	}

	@Override
	public void setLastName(String username, String lastname) {
	}

	@Override
	public List<Group> getGroups(String username) {
		return null;
	}

	@Override
	public void setGroups(List<Group> groups) {
	}

	@Override
	public String getEmail(String username) {
		return null;
	}

	@Override
	public void setEmail(String username, String email) {
	}

	@Override
	public void setPassword(String username, String password) {
	}

	@Override
	public String getPasswordHash(String username) {
		return null;
	}

	@Override
	public void setPasswordHash(String username, String passwordHash) {

	}

	@Override
	public String getPasswordSalt(String username) {
		return null;
	}

	@Override
	public void setPasswordSalt(String username, String passwordSalt) {

	}

	@Override
	public Date getBirthday(String username) {
		return null;
	}

	@Override
	public void setBirthday(String username, Date date) {
	}

	@Override
	public ScriptAPI.Gender getGender(String username) {
		return null;
	}

	@Override
	public void setGender(String username, ScriptAPI.Gender gender) {
	}

	@Override
	public String getStatusMessage(String username) {
		return null;
	}

	@Override
	public void setStatusMessage(String username, String message) {
	}

	@Override
	public int getPostCount(String username) {
		return 0;
	}

	@Override
	public int getTotalPostCount() {
		return 0;
	}

	@Override
	public Post getLastPost() {
		return null;
	}

	@Override
	public Post getLastUserPost(String username) {
		return null;
	}

	@Override
	public List<Post> getPosts(int amount) {
		return null;
	}

	@Override
	public int getThreadCount(String username) {
		return 0;
	}

	@Override
	public int getTotalThreadCount() {
		return 0;
	}

	@Override
	public Thread getLastThread() {
		return null;
	}

	@Override
	public Thread getLastUserThread(String username) {
		return null;
	}

	@Override
	public List<Thread> getThreads() {
		return null;
	}

	@Override
	public int getUserCount() {
		return 0;
	}

	@Override
	public int getGroupCount() {
		return 0;
	}

	@Override
	public Image getAvatar(String username) {
		return null;
	}

	@Override
	public String getAvatarURL(String username) {
		return null;
	}

	@Override
	public String getProfileURL(String username) {
		return null;
	}

	@Override
	public String getHomeURL() {
		return null;
	}

	@Override
	public String getForumURL() {
		return null;
	}

	@Override
	public List<String> getIPs(String username) {
		return null;
	}

	@Override
	public String getRegIP(String username) {
		return null;
	}

	@Override
	public String getLastIP(String username) {
		return null;
	}

	@Override
	public List<Ban> getBans() {
		return null;
	}

	@Override
	public int getBanCount() {
		return 0;
	}

	@Override
	public boolean isBanned(String string) {
		return false;
	}
	
	@Override
	public boolean isActivated(String username) {
		return false;
	}

	@Override
	public boolean isRegistered(String username) {
		return false;
	}
}
