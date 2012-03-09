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
package com.craftfire.authapi.scripts.forum;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.craftfire.authapi.ScriptAPI;
import com.craftfire.authapi.classes.Ban;
import com.craftfire.authapi.classes.Gender;
import com.craftfire.authapi.classes.Group;
import com.craftfire.authapi.classes.Post;
import com.craftfire.authapi.classes.PrivateMessage;
import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.classes.ScriptUser;
import com.craftfire.authapi.classes.Thread;
import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.DataManager;

public class XenForo extends Script {
    private final String scriptName = "xenforo";
    private final String shortName = "xf";
    private final String encryption = "sha1";
    private final String[] versionRanges = {"1.0.4"};
    private final String userVersion;
    private final DataManager dataManager;
    private String currentUsername = null;

    public XenForo(ScriptAPI.Scripts script, String version, DataManager dataManager) {
        super(script, version);
        this.userVersion = version;
        this.dataManager = dataManager;
    }

    public String getLatestVersion() {
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
        Blob hashBlob =
                this.dataManager.getBlobField("user_authenticate", "data", "`user_id` = '" + getUserID(username) +
                                                                           "'");
        String hash = "", salt = "";
        if (hashBlob != null) {
            int offset = - 1;
            int chunkSize = 1024;
            StringBuilder stringBuffer = new StringBuilder();
            try {
                long blobLength = hashBlob.length();
                if (chunkSize > blobLength) {
                    chunkSize = (int) blobLength;
                }
                char buffer[] = new char[chunkSize];
                Reader reader = new InputStreamReader(hashBlob.getBinaryStream());
                while ((offset = reader.read(buffer)) != - 1) {
                    stringBuffer.append(buffer, 0, offset);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String cache = stringBuffer.toString();
            hash = CraftCommons.forumCacheValue(cache, "hash");
            salt = CraftCommons.forumCacheValue(cache, "salt");
        }
        return hashPassword(salt, password).equals(hash);
    }

    public String hashPassword(String salt, String password) {
        return CraftCommons.sha256(CraftCommons.sha256(password) + salt);
    }

    public String getUsername(int userid) {
        return this.dataManager.getStringField("user", "username", "`user_id` = '" + userid + "'");
    }

    public int getUserID(String username) {
        return this.dataManager.getIntegerField("user", "username", "`username` = '" + username + "'");
    }

    public void updateUser(ScriptUser user) {
        /*TODO*/
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        if (user.getGender().equals(Gender.MALE)) {
            data.put("gender", "male");
        } else if (user.getGender().equals(Gender.FEMALE)) {
            data.put("gender", "female");
        } else {
            data.put("gender", "");
        }
        data.put("custom_title", user.getUserTitle());
        data.put("register_date", user.getRegDate().getTime() / 1000);
        data.put("last_activity", user.getLastLogin().getTime() / 1000);
        this.dataManager.updateFields(data, "user", "`user_id` = '" + user.getID() + "'");

        if (user.getBirthday() != null) {
            data = new HashMap<String, Object>();
            SimpleDateFormat format = new SimpleDateFormat("d");
            data.put("dob_day", format.format(user.getBirthday()));
            format = new SimpleDateFormat("M");
            data.put("dob_month", format.format(user.getBirthday()));
            format = new SimpleDateFormat("yyyy");
            data.put("dob_year", format.format(user.getBirthday()));
            this.dataManager.updateFields(data, "user_profile", "`user_id` = '" + user.getID() + "'");
        }

        if (user.getStatusMessage() != null && ! user.getStatusMessage().isEmpty()) {
            String temp =
                    this.dataManager.getStringField("user_profile", "status", "`user_id` = '" + user.getID() + "'");
            if (! temp.equalsIgnoreCase(user.getStatusMessage())) {
                data = new HashMap<String, Object>();
                data.put("status", user.getStatusMessage());
                data.put("status_date", new Date().getTime() / 1000);
                this.dataManager.updateFields(data, "user_profile", "`user_id` = '" + user.getID() + "'");

                /*TODO: Status field, see profile_post and user_status*/
            }
        }

        /*TODO: Make sure Blob works*/
        String stringdata =
                "a:3:{s:4:\"hash\";s:64:\"" + user.getPassword() + "\";s:4:\"salt\";s:64:\"" + user.getPasswordSalt() +
                "\";s:8:\"hashFunc\";s:6:\"sha256\";}";
        byte[] bArr = stringdata.getBytes();
        ByteArrayInputStream bIn = new ByteArrayInputStream(bArr);
        data = new HashMap<String, Object>();
        data.put("data", bIn);
        this.dataManager.updateFields(data, "user_authenticate", "`user_id` = '" + user.getID() + "'");
        data.clear();
    }

    public void createUser(ScriptUser user) {
        Random r = new Random();
        int randint = r.nextInt(1000000);
        user.setPasswordSalt(CraftCommons.sha256(CraftCommons.md5("" + randint).substring(0, 10)));
        user.setRegDate(new Date());
        user.setLastLogin(new Date());
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        if (user.getGender().equals(Gender.MALE)) {
            data.put("gender", "male");
        } else if (user.getGender().equals(Gender.FEMALE)) {
            data.put("gender", "female");
        } else {
            data.put("gender", "");
        }
        data.put("custom_title", user.getUserTitle());
        data.put("register_date", user.getRegDate().getTime() / 1000);
        data.put("last_activity", user.getLastLogin().getTime() / 1000);
        data.put("language_id", 1);
        data.put("style_id", 0);
        data.put("timezone", "Europe/London");
        data.put("user_group_id", 2);
        data.put("display_style_group_id", 2);
        data.put("permission_combination_id", 2);
        this.dataManager.insertFields(data, "user");
        user.setID(this.dataManager.getLastID("user_id", "user"));

        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("allow_post_profile", "members");
        data.put("allow_send_personal_conversation", "members");
        this.dataManager.insertFields(data, "user_privacy");

        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("default_watch_state", "watch_email");
        this.dataManager.insertFields(data, "user_option");

        data = new HashMap<String, Object>();
        /*TODO: Make sure Blob works*/
        String stringdata = "a:0:{}";
        byte[] bArr = stringdata.getBytes();
        ByteArrayInputStream bIn = new ByteArrayInputStream(bArr);
        data.put("user_id", user.getID());
        data.put("identities", bIn);
        if (user.getBirthday() != null) {
            SimpleDateFormat format = new SimpleDateFormat("d");
            data.put("dob_day", format.format(user.getBirthday()));
            format = new SimpleDateFormat("M");
            data.put("dob_month", format.format(user.getBirthday()));
            format = new SimpleDateFormat("yyyy");
            data.put("dob_year", format.format(user.getBirthday()));
        }
        if (user.getStatusMessage() != null && ! user.getStatusMessage().isEmpty()) {
            String temp =
                    this.dataManager.getStringField("user_profile", "status", "`user_id` = '" + user.getID() + "'");
            if (! temp.equalsIgnoreCase(user.getStatusMessage())) {
                data = new HashMap<String, Object>();
                data.put("status", user.getStatusMessage());
                data.put("status_date", new Date().getTime() / 1000);

                /*TODO: Status field, see profile_post and user_status*/
                HashMap<String, Object> data2 = new HashMap<String, Object>();
                data2.put("user_id", user.getID());
                data2.put("default_watch_state", "watch_email");
                this.dataManager.insertFields(data, "user_option");
            }
        }
        this.dataManager.insertFields(data, "user_profile");

        /*TODO: Make sure Blob works*/
        stringdata =
                "a:3:{s:4:\"hash\";s:64:\"" + user.getPassword() + "\";s:4:\"salt\";s:64:\"" + user.getPasswordSalt() +
                "\";s:8:\"hashFunc\";s:6:\"sha256\";}";
        bArr = stringdata.getBytes();
        bIn = new ByteArrayInputStream(bArr);
        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("scheme_class", "XenForo_Authentication_Core");
        data.put("data", bIn);
        this.dataManager.insertFields(data, "user_authenticate");
        data.clear();
    }

    public List<Group> getGroups(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<Group> groups = new ArrayList<Group>();
        List<HashMap<String, Object>> array = this.dataManager.getArrayList(
                "SELECT `user_group_id` FROM `" + this.dataManager.getPrefix() +
                "user_group` ORDER BY `user_group_id` ASC" + limitstring);
        for (int i = 0; array.size() > i; i++) {
            groups.add(getGroup(Integer.parseInt(array.get(i).get("user_group_id").toString())));
        }
        return groups;
    }

    public Group getGroup(int groupid) {
        HashMap<String, Object> array = this.dataManager.getArray(
                "SELECT * FROM `" + this.dataManager.getPrefix() + "user_group` WHERE `user_group_id` = '" + groupid +
                "'");
        List<ScriptUser> users = new ArrayList<ScriptUser>();
        List<HashMap<String, Object>> arrayList = this.dataManager.getArrayList(
                "SELECT `username` FROM `" + this.dataManager.getPrefix() + "user` WHERE `user_group_id` = '" +
                groupid + "' ORDER BY `user_id` ASC");
        for (int i = 0; arrayList.size() > i; i++) {
            String username = arrayList.get(0).get("username").toString();
            if (this.currentUsername != null && ! this.currentUsername.equalsIgnoreCase(username)) {
                users.add(getUser(username));
            }
        }
        this.currentUsername = null;
        Group group =
                new Group(this, Integer.parseInt(array.get("user_group_id").toString()),
                          array.get("title").toString());
        group.setUserCount(this.dataManager.getCount("user", "`user_group_id` = '" + groupid + "'"));
        group.setUsers(users);
        return group;
    }

    public List<Group> getUserGroups(String username) {
        this.currentUsername = username;
        List<Group> groups = new ArrayList<Group>();
        HashMap<String, Object> array = this.dataManager.getArray(
                "SELECT `user_group_id`, `secondary_group_ids` FROM `" + this.dataManager.getPrefix() +
                "user` WHERE `user_id` = '" + getUserID(username) + "' LIMIT 1");
        groups.add(getGroup(Integer.parseInt(array.get("user_group_id").toString())));
        String additional = array.get("secondary_group_ids").toString();
        if (!additional.isEmpty()) {
            if (additional.contains(",")) {
                String[] split = additional.split("\\,");
                for (int i = 0; split.length > i; i++) {
                    groups.add(getGroup(Integer.parseInt(split[i])));
                }
            } else {
                groups.add(getGroup(Integer.parseInt(additional)));
            }
        }
        return groups;
    }

    public void updateGroup(Group group) {
        /*TODO*/
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", group.getName());
        this.dataManager.updateFields(data, "user_group", "`user_group_id` = '" + group.getID() + "'");
        data.clear();
    }

    public void createGroup(Group group) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", group.getName());
        this.dataManager.insertFields(data, "user_group");
        group.setID(this.dataManager.getLastID("user_group_id", "user_group"));
        data.clear();
    }

    public PrivateMessage getPM(int pmid) {
        PrivateMessage pm = new PrivateMessage(this, pmid);
        HashMap<String, Object> array = this.dataManager.getArray(
                "SELECT * FROM `" + this.dataManager.getPrefix() + "conversation_message` WHERE `message_id` = '" +
                pmid + "' LIMIT 1");
        for (int i = 0; array.size() > i; i++) {
            pm.setDate(new Date(Long.parseLong(array.get("message_date").toString()) * 1000));
            pm.setBody(array.get("message").toString());
            pm.setSender(getUser(Integer.parseInt(array.get("user_id").toString())));
            int conversationID = Integer.parseInt(array.get("conversation_id").toString());
            pm.setSubject(this.dataManager.getStringField("conversation_master", "title", "`conversation_id` = '" + conversationID + "'"));
            List<ScriptUser> recipients = new ArrayList<ScriptUser>();
            List<HashMap<String, Object>> recipientsArray = this.dataManager.getArrayList(
                    "SELECT `user_id`, `recipient_state`, `last_read_date` FROM `" + this.dataManager.getPrefix() +
                    "conversation_recipient` WHERE `conversation_id` = '" + conversationID +
                    "' AND `user_id` != '" + pm.getSender().getID() + "'");
            for (int a = 0; recipientsArray.size() > a; a++) {
                ScriptUser recipient = getUser(Integer.parseInt(recipientsArray.get(a).get("user_id").toString()));
                recipients.add(recipient);
                if (recipientsArray.get(a).get("last_read_date").toString() == "0") {
                    pm.setRead(recipient, false);
                } else {
                    pm.setRead(recipient, true);
                }
                if (recipientsArray.get(a).get("recipient_state").toString() == "active") {
                    pm.setDeleted(recipient, false);
                } else {
                    pm.setRead(recipient, true);
                }
            }
            pm.setRecipients(recipients);
        }
        return null;
    }

    public List<PrivateMessage> getPMsSent(String username, int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        List<HashMap<String, Object>> array = this.dataManager.getArrayList(
                "SELECT `message_id` FROM `" + this.dataManager.getPrefix() +
                "conversation_message` WHERE `user_id` = '" + getUserID(username) + "' ORDER BY `message_id` ASC" +
                limitstring);
        for (int i = 0; array.size() > i; i++) {
            pms.add(getPM(Integer.parseInt(array.get(i).get("message_id").toString())));
        }
        return pms;
    }

    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        int userID = getUserID(username);
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        List<HashMap<String, Object>> array = this.dataManager.getArrayList(
                "SELECT `conversation_id` FROM `" + this.dataManager.getPrefix() +
                "conversation_recipient` WHERE `user_id` = '" + userID + "' ORDER BY `conversation_id` ASC" +
                limitstring);
        for (int i = 0; array.size() > i; i++) {
            int conversationID = Integer.parseInt(array.get(0).get("conversation_id").toString());
            List<HashMap<String, Object>> pmsArray = this.dataManager.getArrayList(
                    "SELECT `message_id` FROM `" + this.dataManager.getPrefix() +
                    "conversation_message` WHERE `conversation_id` = '" + conversationID +
                    "' AND `user_id` != '" + userID + "'");
            for (int a = 0; pmsArray.size() > a; a++) {
                int conversationStarterID = this.dataManager.getIntegerField(
                        "SELECT `user_id` FROM `" + this.dataManager.getPrefix() +
                        "conversation_master` WHERE `conversation_id` = '" + conversationID + "'");
                if (userID != conversationStarterID) {
                     pms.add(getPM(Integer.parseInt(array.get(a).get("message_id").toString())));
                }
            }
        }
        return pms;
    }

    public int getPMSentCount(String username) {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() +
                                                "conversation_message` WHERE `user_id` = '" + getUserID(username) + "'");
    }

    public int getPMReceivedCount(String username) {
        /*TODO*/
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() +
                                                "conversation_recipient` WHERE `user_id` = '" + getUserID(username) + "'");
    }

    public void updatePrivateMessage(PrivateMessage pm) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("message", pm.getBody());
        data.put("message_date", pm.getDate().getTime() / 1000);
        this.dataManager.updateFields(data, "conversation_message", "`message_id` = '" + pm.getID() + "'");
        List<ScriptUser> recipients = pm.getRecipients();
        int conversationID = this.dataManager.getIntegerField("conversation_message", "conversation_id", "`message_id` = '" + pm.getID() + "'");
        for (ScriptUser rec : recipients) {
            data = new HashMap<String, Object>();
            if (pm.isDeleted(rec)) {
                data.put("recipient_state", "deleted");
            } else {
                data.put("recipient_state", "active");
            }
            if (pm.isRead(rec)) {
                int read = this.dataManager.getIntegerField("conversation_recipient", "last_read_date", "`user_id` = '" + rec.getID() + "' AND `conversation_id` = '" + conversationID + "'");
                if (read == 0) {
                    data.put("last_read_date", new Date().getTime() / 1000);
                }
            } else {
                data.put("last_read_date", "0");
            }
            this.dataManager.updateFields(data, "conversation_recipient", "`user_id` = '" + rec.getID() + "' AND `conversation_id` = '" + conversationID + "'");
        }
        data.clear();
    }

    public void createPrivateMessage(PrivateMessage pm) {
        /*TODO*/
    }

    public int getPostCount(String username) {
        return this.dataManager.getCount("post", "`user_id` = '" + getUserID(username) + "' AND `position` != '0'");
    }

    public int getTotalPostCount() {
        return this.dataManager.getCount("post", "`position` != '0'");
    }

    public Post getLastPost() {
        return getPost(this.dataManager.getIntegerField(
                "SELECT `post_id` FROM `" + this.dataManager.getPrefix() + "post` ORDER BY `post_id` ASC LIMIT 1"));
    }

    public Post getLastUserPost(String username) {
        return getPost(this.dataManager.getIntegerField(
                "SELECT `post_id` FROM `" + this.dataManager.getPrefix() + "post` WHERE `user_id` = '" +
                getUserID(username) + "' AND `position` != '0' ORDER BY `post_id` ASC LIMIT 1"));
    }

    public List<Post> getPosts(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array = this.dataManager.getArrayList(
                "SELECT `post_id` FROM `" + this.dataManager.getPrefix() +
                "post` ORDER BY `post_id` ASC" + limitstring);
        List<Post> posts = new ArrayList<Post>();
        for (HashMap<String, Object> map : array) {
            posts.add(getPost(Integer.parseInt(map.get("post_id").toString())));
        }
        return posts;
    }

    public List<Post> getPostsFromThread(int threadid, int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array = this.dataManager.getArrayList(
                "SELECT `post_id` FROM `" + this.dataManager.getPrefix() +
                "post` WHERE `thread_id` = '" + threadid + "' ORDER BY `post_id` ASC" + limitstring);
        List<Post> posts = new ArrayList<Post>();
        for (HashMap<String, Object> map : array) {
            posts.add(getPost(Integer.parseInt(map.get("post_id").toString())));
        }
        return posts;
    }

    public Post getPost(int postid) {
        HashMap<String, Object> array = this.dataManager.getArray("SELECT * FROM `" + this.dataManager.getPrefix() + "post` WHERE `post_id` = '" + postid + "' LIMIT 1");
        int nodeID = this.dataManager.getIntegerField("thread", "node_id", "`thread_id` = '" + Integer.parseInt(array.get("thread_id").toString()) + "'");
        Post post = new Post(this, Integer.parseInt(array.get("post_id").toString()), Integer.parseInt(array.get("thread_id").toString()), nodeID);
        post.setBody(array.get("message").toString());
        post.setAuthor(getUser(Integer.parseInt(array.get("user_id").toString())));
        post.setPostDate(new Date(Long.parseLong(array.get("post_date").toString()) * 1000));
        return post;
    }

    public void updatePost(Post post) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("thread_id", post.getThreadID());
        data.put("user_id", post.getAuthor().getID());
        data.put("username", post.getAuthor().getUsername());
        data.put("post_date", post.getThreadID());
        data.put("post_date", post.getPostDate().getTime() / 1000);
        data.put("message", post.getBody());
        this.dataManager.updateFields(data, "post", "`post_id` = '" + post.getID() + "'");
        data = new HashMap<String, Object>();
        data.put("node_id", post.getBoardID());
        this.dataManager.updateFields(data, "thread", "`thread_id` = '" + post.getThreadID() + "'");
        data.clear();
    }

    public void createPost(Post post) {
        /*TODO*/
    }

    public int getThreadCount(String username) {
        return this.dataManager.getCount("thread", "`user_id` = '" + getUserID(username) + "'");
    }

    public int getTotalThreadCount() {
        return this.dataManager.getCount("thread");
    }

    public Thread getLastThread() {
        return getThread(this.dataManager.getIntegerField("SELECT `thread_id` FROM `" + this.dataManager.getPrefix() +
                                                          "thread` ORDER BY `thread_id` ASC LIMIT 1"));
    }

    public Thread getLastUserThread(String username) {
        return getThread(this.dataManager.getIntegerField(
                "SELECT `thread_id` FROM `" + this.dataManager.getPrefix() + "thread` WHERE `user_id` = '" +
                getUserID(username) + "' ORDER BY `thread_id` ASC LIMIT 1"));
    }

    public Thread getThread(int threadid) {
        HashMap<String, Object> array = this.dataManager.getArray("SELECT * FROM `" + this.dataManager.getPrefix() + "thread` WHERE `thread_id` = '" + threadid + "' LIMIT 1");
        Thread thread = new Thread(this, 
                                   Integer.parseInt(array.get("first_post_id").toString()), 
                                   Integer.parseInt(array.get("last_post_id").toString()), 
                                   Integer.parseInt(array.get("thread_id").toString()), 
                                   Integer.parseInt(array.get("node_id").toString()));
        thread.setThreadDate(new Date(Long.parseLong(array.get("post_date").toString()) * 1000));
        thread.setAuthor(getUser(Integer.parseInt(array.get("user_id").toString())));
        thread.setBody(getPost(Integer.parseInt(array.get("first_post_id").toString())).getBody());
        thread.setSubject(array.get("title").toString());
        if (Integer.parseInt(array.get("discussion_open").toString()) == 0) {
            thread.setLocked(true);
        } else {
            thread.setLocked(false);
        }
        if (Integer.parseInt(array.get("user_id").toString()) > 0) {
            thread.setSticky(true);
        } else {
            thread.setSticky(false);
        }
        thread.setReplies(Integer.parseInt(array.get("reply_count").toString()));
        thread.setViews(Integer.parseInt(array.get("view_count").toString()));
        return thread;
    }

    public List<Thread> getThreads(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array = this.dataManager.getArrayList(
                "SELECT `thread_id` FROM `" + this.dataManager.getPrefix() +
                "thread` ORDER BY `thread_id` ASC" + limitstring);
        List<Thread> threads = new ArrayList<Thread>();
        for (HashMap<String, Object> map : array) {
            threads.add(getThread(Integer.parseInt(map.get("thread_id").toString())));
        }
        return threads;
    }

    public void updateThread(Thread thread) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("node_id", thread.getBoardID());
        data.put("title", thread.getSubject());
        data.put("reply_count", thread.getReplies());
        data.put("view_count", thread.getViews());
        data.put("user_id", thread.getAuthor().getID());
        data.put("username", thread.getAuthor().getUsername());
        data.put("post_date", thread.getThreadDate().getTime() / 1000);
        if (thread.isSticky()) {
            data.put("sticky", "1");
        } else {
            data.put("sticky", "0");
        }
        if (thread.isLocked()) {
            data.put("discussion_open", "0");
        } else {
            data.put("discussion_open", "1");
        }
        data.put("first_post_id", thread.getFirstPost().getID());
        data.put("last_post_date", thread.getLastPost().getPostDate().getTime() / 1000);
        data.put("last_post_id", thread.getLastPost().getID());
        data.put("last_post_user_id", thread.getLastPost().getAuthor().getID());
        data.put("last_post_username", thread.getLastPost().getAuthor().getUsername());
        this.dataManager.updateFields(data, "thread", "`thread_id` = '" + thread.getID() + "'");
    }

    public void createThread(Thread thread) {
        /*TODO*/
    }

    public int getUserCount() {
        return this.dataManager.getCount("user");
    }

    public int getGroupCount() {
        return this.dataManager.getCount("user_group");
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
