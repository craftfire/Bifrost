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

import java.util.List;

import com.craftfire.authapi.ScriptAPI.Scripts;
import com.craftfire.commons.CraftCommons;

public class Script implements ScriptInterface {
    private final String version;
    private final Scripts script;
    private ScriptUser user;

    protected Script(Scripts script, String version) {
        this.version = version;
        this.script = script;
    }

    @Override
    public ScriptUser getUser(String username) {
        return null;
    }

    @Override
    public ScriptUser getUser(int userid) {
        return null;
    }

    @Override
    public ScriptUser getLastRegUser() {
        return null;
    }

    @Override
    public String getLatestVersion() {
        return null;
    }

    @Override
    public boolean isSupportedVersion() {
        for (int i=0; this.getVersionRanges().length > i; i++) {
            if (CraftCommons.inVersionRange(this.getVersionRanges()[i], this.version)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String[] getVersionRanges() {
        return null;
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
    public String hashPassword(String salt, String password) {
        return null;
    }

    @Override
    public String getUsername(int userid) {
        return null;
    }

    @Override
    public int getUserID(String username) {
        return 0;
    }

    @Override
    public void updateUser(ScriptUser user) {
    }

    @Override
    public void createUser(ScriptUser user) {
    }

    @Override
    public List<Group> getGroups(int limit) {
        return null;
    }

    @Override
    public Group getGroup(int groupid) {
        return null;
    }

    @Override
    public Group getGroup(String group) {
        return null;
    }

    @Override
    public List<Group> getUserGroups(String username) {
        return null;
    }

    @Override
    public void updateGroup(Group group) {
    }

    @Override
    public void createGroup(Group group) {
    }
    @Override
    public PrivateMessage getPM(int pmid) {
        return null;
    }

    @Override
    public List<PrivateMessage> getPMsSent(String username, int limit) {
        return null;
    }

    @Override
    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        return null;
    }

    @Override
    public int getPMSentCount(String username) {
        return 0;
    }

    @Override
    public int getPMReceivedCount(String username) {
        return 0;
    }

    @Override
    public void updatePrivateMessage(PrivateMessage privateMessage) {
    }

    @Override
    public void createPrivateMessage(PrivateMessage privateMessage) {
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
    public List<Post> getPosts(int limit) {
        return null;
    }

    @Override
    public List<Post> getPostsFromThread(int threadid, int limit) {
        return null;
    }

    @Override
    public Post getPost(int postid) {
        return null;
    }

    @Override
    public void updatePost(Post post) {
    }

    @Override
    public void createPost(Post post) {
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
    public Thread getThread(int threadid) {
        return null;
    }

    @Override
    public List<Thread> getThreads(int limit) {
        return null;
    }

    @Override
    public void updateThread(Thread thread) {
    }

    @Override
    public void createThread(Thread thread) {
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
    public List<Ban> getBans(int limit) {
        return null;
    }

    @Override
    public void updateBan(Ban ban) {
    }

    @Override
    public void addBan(Ban ban) {
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
    public boolean isRegistered(String username) {
        return false;
    }
}
