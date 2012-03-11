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
package com.craftfire.authapi.scripts.cms;

import java.util.List;

import com.craftfire.authapi.ScriptAPI;
import com.craftfire.authapi.classes.Ban;
import com.craftfire.authapi.classes.Group;
import com.craftfire.authapi.classes.Post;
import com.craftfire.authapi.classes.PrivateMessage;
import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.classes.ScriptUser;
import com.craftfire.authapi.classes.Thread;
import com.craftfire.commons.DataManager;

public class Drupal extends Script {
    private final String scriptName = "drupal";
    private final String shortName = "dru";
    private final String encryption = "sha1"; /*TODO*/
    private final String[] versionRanges = {"1.0.4"}; /*TODO*/
    private final String userVersion;
    private final DataManager dataManager;
    private String currentUsername = null;

    public Drupal(ScriptAPI.Scripts script, String version, DataManager dataManager) {
        super(script, version);
        this.userVersion = version;
        this.dataManager = dataManager;
    }

    public String getLatestVersion() {
        /*TODO*/
        return this.versionRanges[0];
    }

    public String getVersion() {
        return this.userVersion;
    }

    public String getEncryption() {
        return this.encryption;
    }

    public String getScriptName() {
        return this.scriptName;
    }

    public String getScriptShortname() {
        return this.shortName;
    }

    public boolean authenticate(String username, String password) {
        /*TODO*/
        return false;
    }

    public String hashPassword(String salt, String password) {
        /*TODO*/
        return null;
    }

    public String getUsername(int userid) {
        /*TODO*/
        return null;
    }

    public int getUserID(String username) {
        /*TODO*/
        return 0;
    }

    public ScriptUser getLastRegUser() {
        /*TODO*/
        return null;
    }

    public ScriptUser getUser(String username) {
        /*TODO*/
        return null;
    }

    public ScriptUser getUser(int userid) {
        /*TODO*/
        return null;
    }

    public void updateUser(ScriptUser user) {
        /*TODO*/
    }

    public void createUser(ScriptUser user) {
        /*TODO*/
    }

    public List<Group> getGroups(int limit) {
        /*TODO*/
        return null;
    }

    public Group getGroup(int groupid) {
        /*TODO*/
        return null;
    }

    public List<Group> getUserGroups(String username) {
        /*TODO*/
        return null;
    }

    public void updateGroup(Group group) {
        /*TODO*/
    }

    public void createGroup(Group group) {
        /*TODO*/
    }

    public PrivateMessage getPM(int pmid) {
        /*TODO*/
        return null;
    }

    public List<PrivateMessage> getPMsSent(String username, int limit) {
        /*TODO*/
        return null;
    }

    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        /*TODO*/
        return null;
    }

    public int getPMSentCount(String username) {
        /*TODO*/
        return 0;
    }

    public int getPMReceivedCount(String username) {
        /*TODO*/
        return 0;
    }

    public void updatePrivateMessage(PrivateMessage privateMessage) {
        /*TODO*/
    }

    public void createPrivateMessage(PrivateMessage privateMessage) {
        /*TODO*/
    }

    public int getPostCount(String username) {
        /*TODO*/
        return 0;
    }

    public int getTotalPostCount() {
        /*TODO*/
        return 0;
    }

    public Post getLastPost() {
        /*TODO*/
        return null;
    }

    public Post getLastUserPost(String username) {
        /*TODO*/
        return null;
    }

    public List<Post> getPosts(int limit) {
        /*TODO*/
        return null;
    }

    public List<Post> getPostsFromThread(int threadid, int limit) {
        /*TODO*/
        return null;
    }

    public Post getPost(int postid) {
        /*TODO*/
        return null;
    }

    public void updatePost(Post post) {
        /*TODO*/
    }

    public void createPost(Post post) {
        /*TODO*/
    }

    public int getThreadCount(String username) {
        /*TODO*/
        return 0;
    }

    public int getTotalThreadCount() {
        /*TODO*/
        return 0;
    }

    public com.craftfire.authapi.classes.Thread getLastThread() {
        /*TODO*/
        return null;
    }

    public Thread getLastUserThread(String username) {
        /*TODO*/
        return null;
    }

    public Thread getThread(int threadid) {
        /*TODO*/
        return null;
    }

    public List<Thread> getThreads(int limit) {
        /*TODO*/
        return null;
    }

    public void updateThread(Thread thread) {
        /*TODO*/
    }

    public void createThread(Thread thread) {
        /*TODO*/
    }

    public int getUserCount() {
        /*TODO*/
        return 0;
    }

    public int getGroupCount() {
        /*TODO*/
        return 0;
    }

    public String getHomeURL() {
        /*TODO*/
        return null;
    }

    public String getForumURL() {
        /*TODO*/
        return null;
    }

    public List<String> getIPs(String username) {
        /*TODO*/
        return null;
    }

    public List<Ban> getBans(int limit) {
        /*TODO*/
        return null;
    }

    public void updateBan(Ban ban) {
        /*TODO*/
    }

    public void addBan(Ban ban) {
        /*TODO*/
    }

    public int getBanCount() {
        /*TODO*/
        return 0;
    }

    public boolean isBanned(String string) {
        /*TODO*/
        return false;
    }

    public boolean isRegistered(String username) {
        /*TODO*/
        return false;
    }
}
