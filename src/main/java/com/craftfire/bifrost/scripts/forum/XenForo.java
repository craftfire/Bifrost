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
package com.craftfire.bifrost.scripts.forum;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.Results;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.forum.ForumUser;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Gender;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.script.ForumScript;

//TODO: Convert arrays to use Result class
public class XenForo extends ForumScript {
    private final String scriptName = "xenforo";
    private final String shortName = "xf";
    private final Encryption encryption = Encryption.SHA1;
    private final String[] versionRanges = {"1.0.4", "1.1.2"};
    private String currentUsername = null;

    public XenForo(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    @Override
    public String[] getVersionRanges() {
        return this.versionRanges;
    }

    @Override
    public String getLatestVersion() {
        return this.versionRanges[0];
    }

    @Override
    public Encryption getEncryption() {
        return this.encryption;
    }

    @Override
    public String getScriptName() {
        return this.scriptName;
    }

    @Override
    public String getScriptShortname() {
        return this.shortName;
    }

    @Override
    public boolean authenticate(String username, String password) {
        Blob hashBlob =
                this.getDataManager().getBlobField("user_authenticate", "data", "`user_id` = '" + getUserID(username) +
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

    @Override
    public String hashPassword(String salt, String password) {
        return CraftCommons.encrypt(Encryption.SHA256, CraftCommons.encrypt(Encryption.SHA256, password) + salt);
    }

    @Override
    public String getUsername(int userid) {
        return this.getDataManager().getStringField("user", "username", "`user_id` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        return this.getDataManager().getIntegerField("user", "user_id", "`username` = '" + username + "'");
    }

    @Override
    public ForumUser getLastRegUser() throws SQLException {
        return getUser(this.getDataManager().getIntegerField("SELECT `user_id` FROM `" + this.getDataManager().getPrefix() +
                "user` ORDER BY `user_id` ASC LIMIT 1"));
    }

    @Override
    public ForumUser getUser(String username) throws SQLException {
        return getUser(getUserID(username));
    }

    @Override
    public ForumUser getUser(int userid) throws SQLException {
        ForumUser user = new ForumUser(this, userid, null, null);
        Results results = this.getDataManager().getResults(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "user` WHERE `user_id` = '" +
                        userid + "' LIMIT 1");
        DataRow row = results.getFirstResult();
        if (results.getRowsCount() > 0) {
            if (row.getStringField("user_state").equalsIgnoreCase("valid")) {
                user.setActivated(true);
            } else {
                user.setActivated(false);
            }
            if (!row.getStringField("gravatar").isEmpty()) {
                user.setAvatarURL("http://www.gravatar.com/avatar/" +
                        CraftCommons.encrypt(Encryption.MD5, row.getStringField("gravatar").toLowerCase()));
            }
            user.setEmail(row.getStringField("email"));
            if (row.getStringField("gender").equalsIgnoreCase("male")) {
                user.setGender(Gender.MALE);
            } else if (row.getStringField("gender").equalsIgnoreCase("female")) {
                user.setGender(Gender.FEMALE);
            } else {
                user.setGender(Gender.UNKNOWN);
            }
            user.setLastLogin(new Date(row.getLongField("last_activity") * 1000));
            user.setRegDate(new Date(row.getLongField("register_date") * 1000));
            Blob hashBlob =
                    this.getDataManager().getBlobField("user_authenticate", "data", "`user_id` = '" + userid + "'");
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
                //TODO: php deserializer
                user.setPassword(CraftCommons.forumCacheValue(cache, "hash"));
                user.setPasswordSalt(CraftCommons.forumCacheValue(cache, "salt"));
            }
            user.setUsername(row.getStringField("username"));
            user.setUserTitle(row.getStringField("custom_title"));
            user.setLastIP(CraftCommons.long2ip((long)this.getDataManager().getIntegerField(
                    "ip",
                    "ip",
                    "`user_id` = '" + user.getID() + "'")));
            user.setRegIP(CraftCommons.long2ip((long) this.getDataManager().getIntegerField("ip", "ip",
                    "`user_id` = '" +
                            user.getID() +
                            "' AND `action` = 'register'")));
        }

        results = this.getDataManager().getResults(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "user_profile` WHERE `user_id` = '" +
                        userid + "' LIMIT 1");
        row = results.getFirstResult();
        if (results.getRowsCount() > 0) {
            String bdate = row.getStringField("dob_day") + " " + row.getStringField("dob_month") + " " +
                    row.getStringField("dob_year");
            try {
                SimpleDateFormat format = new SimpleDateFormat("d M yyyy");
                user.setBirthday(format.parse(bdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!row.getStringField("status").isEmpty()) {
                user.setStatusMessage(row.getStringField("status"));
            }
        }

        return user;
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException {
        long timestamp = new Date().getTime() / 1000;
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

        if (user.isActivated()) {
            data.put("user_state", "valid");
        } else {
            data.put("user_state", "email_confirm");
        }

        data.put("custom_title", user.getUserTitle());
        data.put("register_date", user.getRegDate().getTime() / 1000);
        data.put("last_activity", user.getLastLogin().getTime() / 1000);
        this.getDataManager().updateFields(data, "user", "`user_id` = '" + user.getID() + "'");

        if (user.getBirthday() != null) {
            data = new HashMap<String, Object>();
            SimpleDateFormat format = new SimpleDateFormat("d");
            data.put("dob_day", format.format(user.getBirthday()));
            format = new SimpleDateFormat("M");
            data.put("dob_month", format.format(user.getBirthday()));
            format = new SimpleDateFormat("yyyy");
            data.put("dob_year", format.format(user.getBirthday()));
            this.getDataManager().updateFields(data, "user_profile", "`user_id` = '" + user.getID() + "'");
        }

        if (user.getStatusMessage() != null && ! user.getStatusMessage().isEmpty()) {
            String temp =
                    this.getDataManager().getStringField("user_profile", "status", "`user_id` = '" + user.getID() + "'");
            if (! temp.equalsIgnoreCase(user.getStatusMessage())) {
                int ipID = this.insertIP(user, "profile_post", "insert");
                data = new HashMap<String, Object>();
                data.put("profile_user_id", user.getID());
                data.put("user_id", user.getID());
                data.put("username", user.getUsername());
                data.put("post_date", timestamp);
                data.put("message", user.getStatusMessage());
                data.put("ip_id", ipID);
                this.getDataManager().insertFields(data, "profile_post");

                int profilePostID = this.getDataManager().getLastID("profile_post_id", "profile_post");

                this.addSearch(user, "profile_post", 0, profilePostID, null, user.getStatusMessage());

                this.getDataManager().updateBlob("profile_post", "like_users", "`profile_post_id` = '" + profilePostID + "'", "a:0:{}");

                data = new HashMap<String, Object>();
                data.put("profile_post_id", profilePostID);
                data.put("user_id", user.getID());
                data.put("post_date", timestamp);
                this.getDataManager().insertFields(data, "user_status");

                data = new HashMap<String, Object>();
                data.put("status", user.getStatusMessage());
                data.put("status_date", timestamp);
                data.put("status_profile_post_id", profilePostID);
                this.getDataManager().updateFields(data, "user_profile", "`user_id` = '" + user.getID() + "'");
            }
        }

        if (user.getPassword().length() != 64) {
            Random r = new Random();
            user.setPasswordSalt(CraftCommons.encrypt(Encryption.SHA256, CraftCommons.encrypt(Encryption.MD5, r.nextInt(1000000)).substring(0, 10)));
            user.setPassword(hashPassword(user.getPasswordSalt(), user.getPassword()));
            String stringdata =
                    "a:3:{s:4:\"hash\";s:64:\"" + user.getPassword() + "\";s:4:\"salt\";s:64:\"" + user.getPasswordSalt() +
                            "\";s:8:\"hashFunc\";s:6:\"sha256\";}";
            this.getDataManager().updateBlob("user_authenticate", "data", "`user_id` = '" + user.getID() + "'", stringdata);
        }
        data.clear();
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException {
        long timestamp = new Date().getTime() / 1000;
        Random r = new Random();
        user.setPasswordSalt(CraftCommons.encrypt(Encryption.SHA256, CraftCommons.encrypt(Encryption.MD5, r.nextInt(1000000)).substring(0, 10)));
        user.setPassword(hashPassword(user.getPasswordSalt(), user.getPassword()));
        user.setRegDate(new Date());
        user.setLastLogin(new Date());
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        if (user.getGender() != null && user.getGender().equals(Gender.MALE)) {
            data.put("gender", "male");
        } else if (user.getGender() != null && user.getGender().equals(Gender.FEMALE)) {
            data.put("gender", "female");
        } else {
            data.put("gender", "");
        }
        data.put("custom_title", user.getUserTitle());
        data.put("register_date", timestamp);
        data.put("last_activity", timestamp);
        data.put("language_id", 1);
        data.put("style_id", 0);
        data.put("timezone", "Europe/London");
        data.put("user_group_id", 2);
        data.put("display_style_group_id", 2);
        data.put("permission_combination_id", 2);
        this.getDataManager().insertFields(data, "user");
        user.setID(this.getDataManager().getLastID("user_id", "user"));

        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("allow_post_profile", "members");
        data.put("allow_send_personal_conversation", "members");
        this.getDataManager().insertFields(data, "user_privacy");

        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("default_watch_state", "watch_email");
        this.getDataManager().insertFields(data, "user_option");

        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        if (user.getBirthday() != null) {
            SimpleDateFormat format = new SimpleDateFormat("d");
            data.put("dob_day", format.format(user.getBirthday()));
            format = new SimpleDateFormat("M");
            data.put("dob_month", format.format(user.getBirthday()));
            format = new SimpleDateFormat("yyyy");
            data.put("dob_year", format.format(user.getBirthday()));
        }
        this.getDataManager().insertFields(data, "user_profile");
        if (CraftCommons.inVersionRange(this.versionRanges[0], this.getVersion())) {
            this.getDataManager().updateBlob("user_profile", "identities", "`user_id` = '" + user.getID() + "'", "a:0:{}");
        } else if (CraftCommons.inVersionRange(this.versionRanges[1], this.getVersion())) {
            this.getDataManager().updateBlob("user_profile", "custom_fields", "`user_id` = '" + user.getID() + "'", "a:0:{}");
        }
        if (user.getStatusMessage() != null && ! user.getStatusMessage().isEmpty()) {
            int ipID = insertIP(user, "profile_post", "insert");
            data = new HashMap<String, Object>();
            data.put("profile_user_id", user.getID());
            data.put("user_id", user.getID());
            data.put("username", user.getUsername());
            data.put("post_date", timestamp);
            data.put("message", user.getStatusMessage());
            data.put("ip_id", ipID);
            this.getDataManager().insertFields(data, "profile_post");

            int profilePostID = this.getDataManager().getLastID("profile_post_id", "profile_post");

            this.getDataManager().updateBlob("profile_post", "like_users", "`profile_post_id` = '" + profilePostID + "'", "a:0:{}");

            data = new HashMap<String, Object>();
            data.put("profile_post_id", profilePostID);
            data.put("user_id", user.getID());
            data.put("post_date", timestamp);
            this.getDataManager().insertFields(data, "user_status");

            data = new HashMap<String, Object>();
            data.put("status", user.getStatusMessage());
            data.put("status_date", timestamp);
            data.put("status_profile_post_id", profilePostID);
            this.getDataManager().updateFields(data, "user_profile", "`user_id` = '" + user.getID() + "'");
        }

        //TODO: PHP deserialize?
        String stringdata =
                "a:3:{s:4:\"hash\";s:64:\"" + user.getPassword() + "\";s:4:\"salt\";s:64:\"" + user.getPasswordSalt() +
                        "\";s:8:\"hashFunc\";s:6:\"sha256\";}";
        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("scheme_class", "XenForo_Authentication_Core");
        this.getDataManager().insertFields(data, "user_authenticate");
        this.getDataManager().updateBlob("user_authenticate", "data", "`user_id` = '" + user.getID() + "'", stringdata);

        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("user_group_id", 2);
        data.put("is_primary", 1);
        this.getDataManager().insertFields(data, "user_group_relation");

        insertIP(user, "user", "register");
        data.clear();
    }

    @Override
    public List<Group> getGroups(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<Group> groups = new ArrayList<Group>();
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `user_group_id` FROM `" + this.getDataManager().getPrefix() +
                        "user_group` ORDER BY `user_group_id` ASC" + limitstring);
        for (HashMap<String, Object> map : array) {
            groups.add(getGroup(Integer.parseInt(map.get("user_group_id").toString())));
        }
        return groups;
    }

    @Override
    public Group getGroup(int groupid) {
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "user_group` WHERE `user_group_id` = '" + groupid +
                        "'");
        List<ScriptUser> users = new ArrayList<ScriptUser>();
        List<HashMap<String, Object>> arrayList = this.getDataManager().getArrayList(
                "SELECT `username` FROM `" + this.getDataManager().getPrefix() + "user` WHERE `user_group_id` = '" +
                        groupid + "' ORDER BY `user_id` ASC");
        for (HashMap<String, Object> map : arrayList) {
            String username = map.get("username").toString();
            if (this.currentUsername != null && ! this.currentUsername.equalsIgnoreCase(username)) {
                /* TODO: Fix loop */
                //System.out.println("user: " + this.currentUsername + " - " + username);
                //users.add(getUser(username));
            }
        }
        this.currentUsername = null;
        Group group =
                new Group(this, Integer.parseInt(array.get("user_group_id").toString()),
                        array.get("title").toString());
        group.setUserCount(this.getDataManager().getCount("user", "`user_group_id` = '" + groupid + "'"));
        group.setUsers(users);
        return group;
    }

    @Override
    public int getGroupID(String group) {
        /*TODO*/
        return 0;
    }

    @Override
    public Group getGroup(String group) {
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT `user_group_id` FROM `" + this.getDataManager().getPrefix() +
                        "user_group` WHERE `title` = '" + group + "'");
        return getGroup(Integer.parseInt(array.get("user_group_id").toString()));
    }

    @Override
    public List<Group> getUserGroups(String username) {
        this.currentUsername = username;
        List<Group> groups = new ArrayList<Group>();
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT `user_group_id`, `secondary_group_ids` FROM `" + this.getDataManager().getPrefix() +
                        "user` WHERE `user_id` = '" + getUserID(username) + "' LIMIT 1");
        groups.add(getGroup(Integer.parseInt(array.get("user_group_id").toString())));
        String additional = this.getDataManager().getBinaryField("user", "secondary_group_ids", "`user_id` = '" + getUserID(username) + "'");
        if (! additional.isEmpty()) {
            if (additional.contains(",")) {
                String[] split = additional.split("\\,");
                for (int i = 0; split.length > i; i++) {
                    this.currentUsername = username;
                    groups.add(getGroup(Integer.parseInt(split[i])));
                }
            } else {
                groups.add(getGroup(Integer.parseInt(additional)));
            }
        }
        return groups;
    }

    @Override
    public void updateGroup(Group group) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", group.getName());
        this.getDataManager().updateFields(data, "user_group", "`user_group_id` = '" + group.getID() + "'");
        data.clear();
    }

    @Override
    public void createGroup(Group group) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", group.getName());
        this.getDataManager().insertFields(data, "user_group");
        group.setID(this.getDataManager().getLastID("user_group_id", "user_group"));
        data.clear();
    }

    @Override
    public PrivateMessage getPM(int pmid) throws SQLException {
        PrivateMessage pm = new PrivateMessage(this, pmid);
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "conversation_message` WHERE `message_id` = '" +
                        pmid + "' LIMIT 1");
        for (int i = 0; array.size() > i; i++) {
            pm.setDate(new Date(Long.parseLong(array.get("message_date").toString()) * 1000));
            pm.setBody(array.get("message").toString());
            pm.setSender(getUser(Integer.parseInt(array.get("user_id").toString())));
            int conversationID = Integer.parseInt(array.get("conversation_id").toString());
            pm.setSubject(this.getDataManager().getStringField("conversation_master", "title",
                    "`conversation_id` = '" + conversationID + "'"));
            List<ScriptUser> recipients = new ArrayList<ScriptUser>();
            List<HashMap<String, Object>> recipientsArray = this.getDataManager().getArrayList(
                    "SELECT `user_id`, `recipient_state`, `last_read_date` FROM `" + this.getDataManager().getPrefix() +
                            "conversation_recipient` WHERE `conversation_id` = '" + conversationID +
                            "' AND `user_id` != '" + pm.getSender().getID() + "'");
            for (HashMap<String, Object> map : recipientsArray) {
                ScriptUser recipient = getUser(Integer.parseInt(map.get("user_id").toString()));
                recipients.add(recipient);
                if (map.get("last_read_date").toString().equalsIgnoreCase("0")) {
                    pm.setRead(recipient, false);
                } else {
                    pm.setRead(recipient, true);
                }
                if (map.get("recipient_state").toString().equalsIgnoreCase("0")) {
                    pm.setDeleted(recipient, false);
                } else {
                    pm.setDeleted(recipient, true);
                }
            }
            pm.setRecipients(recipients);
        }
        return pm;
    }

    @Override
    public List<PrivateMessage> getPMsSent(String username, int limit) throws SQLException {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `message_id` FROM `" + this.getDataManager().getPrefix() +
                        "conversation_message` WHERE `user_id` = '" + getUserID(username) +
                        "' ORDER BY `message_id` ASC" +
                        limitstring);
        for (HashMap<String, Object> map : array) {
            pms.add(getPM(Integer.parseInt(map.get("message_id").toString())));
        }
        return pms;
    }

    @Override
    public List<PrivateMessage> getPMsReceived(String username, int limit) throws SQLException {
        int userID = getUserID(username);
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `conversation_id` FROM `" + this.getDataManager().getPrefix() +
                        "conversation_recipient` WHERE `user_id` = '" + userID +
                        "' ORDER BY `conversation_id` ASC" +
                        limitstring);
        for (HashMap<String, Object> map : array) {
            int conversationID = Integer.parseInt(map.get("conversation_id").toString());
            List<HashMap<String, Object>> pmsArray =
                    this.getDataManager().getArrayList("SELECT `message_id` FROM `" + this.getDataManager().getPrefix() +
                            "conversation_message` WHERE `conversation_id` = '" +
                            conversationID +
                            "' AND `user_id` != '" + userID + "'");
            for (HashMap<String, Object> pm : pmsArray) {
                int conversationStarterID =
                        this.getDataManager().getIntegerField("SELECT `user_id` FROM `" + this.getDataManager().getPrefix() +
                                "conversation_master` WHERE `conversation_id` = '" +
                                conversationID + "'");
                if (userID != conversationStarterID) {
                    pms.add(getPM(Integer.parseInt(pm.get("message_id").toString())));
                }
            }
        }
        return pms;
    }

    @Override
    public int getPMSentCount(String username) {
        return this.getDataManager().getIntegerField("SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() +
                "conversation_message` WHERE `user_id` = '" + getUserID(username) +
                "'");
    }

    @Override
    public int getPMReceivedCount(String username) {
        /*TODO*/
        return this.getDataManager().getIntegerField("SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() +
                "conversation_recipient` WHERE `user_id` = '" + getUserID(username) +
                "'");
    }

    @Override
    public void updatePrivateMessage(PrivateMessage pm) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("message", pm.getBody());
        data.put("message_date", pm.getDate().getTime() / 1000);
        this.getDataManager().updateFields(data, "conversation_message", "`message_id` = '" + pm.getID() + "'");
        List<ScriptUser> recipients = pm.getRecipients();
        int conversationID = this.getDataManager().getIntegerField("conversation_message", "conversation_id",
                "`message_id` = '" + pm.getID() + "'");
        for (ScriptUser rec : recipients) {
            data = new HashMap<String, Object>();
            if (pm.isDeleted(rec)) {
                data.put("recipient_state", "deleted");
            } else {
                data.put("recipient_state", "active");
            }
            if (pm.isRead(rec)) {
                int read = this.getDataManager().getIntegerField("conversation_recipient", "last_read_date",
                        "`user_id` = '" + rec.getID() +
                                "' AND `conversation_id` = '" + conversationID + "'");
                if (read == 0) {
                    data.put("last_read_date", new Date().getTime() / 1000);
                }
            } else {
                data.put("last_read_date", "0");
            }
            this.getDataManager().updateFields(data, "conversation_recipient",
                    "`user_id` = '" + rec.getID() + "' AND `conversation_id` = '" +
                            conversationID + "'");
        }
        data.clear();
    }

    @Override
    public void createPrivateMessage(PrivateMessage pm) throws SQLException {
        Long timestamp = new Date().getTime() / 1000;
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", pm.getSubject());
        data.put("user_id", pm.getSender().getID());
        data.put("username", pm.getSender().getUsername());
        data.put("start_date", timestamp);
        data.put("recipient_count", pm.getRecipients().size());
        data.put("last_message_date", timestamp);
        data.put("last_message_user_id", pm.getSender().getID());
        data.put("last_message_username", pm.getSender().getUsername());
        this.getDataManager().insertFields(data, "conversation_master");
        int conversationID = this.getDataManager().getLastID("conversation_id", "conversation_master");
        int ipID = this.insertIP(pm.getSender(), "conversation_message", "insert");
        data = new HashMap<String, Object>();
        data.put("conversation_id", conversationID);
        data.put("message_date", timestamp);
        data.put("user_id", pm.getSender().getID());
        data.put("username", pm.getSender().getUsername());
        data.put("message", pm.getBody());
        if (!CraftCommons.inVersionRange(this.versionRanges[0], this.getVersion())) {
            data.put("ip_id", ipID);
        }
        this.getDataManager().insertFields(data, "conversation_message");
        int messageID = this.getDataManager().getLastID("message_id", "conversation_message");
        data = new HashMap<String, Object>();
        data.put("first_message_id", messageID);
        data.put("last_message_id", messageID);
        this.getDataManager().updateFields(data, "conversation_master", "`conversation_id` = '" + conversationID + "'");
        data = new HashMap<String, Object>();
        data.put("conversation_id", conversationID);
        data.put("user_id", pm.getSender().getID());
        data.put("recipient_state", "active");
        data.put("last_read_date", timestamp);
        this.getDataManager().insertFields(data, "conversation_recipient");
        data = new HashMap<String, Object>();
        data.put("conversation_id", conversationID);
        data.put("owner_user_id", pm.getSender().getID());
        data.put("is_unread", 0);
        data.put("reply_count", 0);
        data.put("last_message_date", timestamp);
        data.put("last_message_id", messageID);
        data.put("last_message_user_id", pm.getSender().getID());
        data.put("last_message_username", pm.getSender().getUsername());
        this.getDataManager().insertFields(data, "conversation_user");
        for (ScriptUser recipient : pm.getRecipients()) {
            data = new HashMap<String, Object>();
            data.put("conversation_id", conversationID);
            data.put("user_id", recipient.getID());
            data.put("recipient_state", "active");
            data.put("last_read_date", 0);
            this.getDataManager().insertFields(data, "conversation_recipient");
            data = new HashMap<String, Object>();
            data.put("conversation_id", conversationID);
            data.put("owner_user_id", recipient.getID());
            data.put("is_unread", 1);
            data.put("reply_count", 0);
            data.put("last_message_date", timestamp);
            data.put("last_message_id", messageID);
            data.put("last_message_user_id", pm.getSender().getID());
            data.put("last_message_username", pm.getSender().getUsername());
            this.getDataManager().insertFields(data, "conversation_user");
        }
        data.clear();
    }

    @Override
    public int getPostCount(String username) {
        return this.getDataManager().getCount("post", "`user_id` = '" + getUserID(username) + "' AND `position` != '0'");
    }

    @Override
    public int getTotalPostCount() {
        return this.getDataManager().getCount("post", "`position` != '0'");
    }

    @Override
    public ForumPost getLastPost() throws SQLException {
        return getPost(this.getDataManager().getIntegerField(
                "SELECT `post_id` FROM `" + this.getDataManager().getPrefix() + "post` ORDER BY `post_id` ASC LIMIT 1"));
    }

    @Override
    public ForumPost getLastUserPost(String username) throws SQLException {
        return getPost(this.getDataManager().getIntegerField(
                "SELECT `post_id` FROM `" + this.getDataManager().getPrefix() + "post` WHERE `user_id` = '" +
                        getUserID(username) + "' AND `position` != '0' ORDER BY `post_id` ASC LIMIT 1"));
    }

    @Override
    public List<ForumPost> getPosts(int limit) throws SQLException {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `post_id` FROM `" + this.getDataManager().getPrefix() +
                        "post` ORDER BY `post_id` ASC" + limitstring);
        List<ForumPost> posts = new ArrayList<ForumPost>();
        for (HashMap<String, Object> map : array) {
            posts.add(getPost(Integer.parseInt(map.get("post_id").toString())));
        }
        return posts;
    }

    @Override
    public List<ForumPost> getPostsFromThread(int threadid, int limit) throws SQLException {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `post_id` FROM `" + this.getDataManager().getPrefix() +
                        "post` WHERE `thread_id` = '" + threadid + "' ORDER BY `post_id` ASC" +
                        limitstring);
        List<ForumPost> posts = new ArrayList<ForumPost>();
        for (HashMap<String, Object> map : array) {
            posts.add(getPost(Integer.parseInt(map.get("post_id").toString())));
        }
        return posts;
    }

    @Override
    public ForumPost getPost(int postid) throws SQLException {
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "post` WHERE `post_id` = '" + postid + "' LIMIT 1");
        int nodeID = this.getDataManager().getIntegerField("thread", "node_id", "`thread_id` = '" +
                Integer.parseInt(array.get("thread_id")
                        .toString()) +
                "'");
        ForumPost post =
                new ForumPost(this, Integer.parseInt(array.get("post_id").toString()),
                        Integer.parseInt(array.get("thread_id").toString()));
        post.setBody(array.get("message").toString());
        post.setAuthor(getUser(Integer.parseInt(array.get("user_id").toString())));
        post.setPostDate(new Date(Long.parseLong(array.get("post_date").toString()) * 1000));
        return post;
    }

    @Override
    public void updatePost(ForumPost post) throws SQLException, UnsupportedMethod {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("thread_id", post.getThreadID());
        data.put("user_id", post.getAuthor().getID());
        data.put("username", post.getAuthor().getUsername());
        data.put("post_date", post.getPostDate().getTime() / 1000);
        data.put("message", post.getBody());
        this.getDataManager().updateFields(data, "post", "`post_id` = '" + post.getID() + "'");
        data = new HashMap<String, Object>();
        data.put("node_id", post.getBoardID());
        this.getDataManager().updateFields(data, "thread", "`thread_id` = '" + post.getThreadID() + "'");
        data.clear();
    }

    @Override
    public void createPost(ForumPost post) throws SQLException, UnsupportedMethod {
        int ipID = this.insertIP(post.getAuthor(), "post", "insert");
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("thread_id", post.getThreadID());
        data.put("user_id", post.getAuthor().getID());
        data.put("username", post.getAuthor().getUsername());
        data.put("post_date", new Date().getTime() / 1000);
        data.put("message", post.getBody());
        data.put("ip_id", ipID);
        if (this.getDataManager().exist("post", "thread_id", post.getThreadID())) {
            data.put("position", this.getDataManager().getLastID("position",
                    "post",
                    "`thread_id` = '" + post.getThreadID() + "'") + 1);
        } else {
            data.put("position", 0);
        }
        int postID = this.getDataManager().getLastID("post_id", "post");
        post.setID(postID);
        int replyCount = this.getDataManager().getIntegerField("thread", "reply_count",
                "`thread_id` = '" + post.getThreadID() + "'");
        this.addSearch(post.getAuthor(), "post", post.getBoardID(), post.getID(), post.getSubject(), post.getBody());
        this.getDataManager().updateBlob("post", "like_users", "`post_id` = '" + postID + "'", "a:0:{}");
        data = new HashMap<String, Object>();
        data.put("node_id", post.getBoardID());
        data.put("reply_count", replyCount + 1);
        data.put("last_post_date", new Date().getTime() / 1000);
        data.put("last_post_id", postID);
        data.put("last_post_user_id", post.getAuthor().getID());
        data.put("last_post_username", post.getAuthor().getUsername());
        this.getDataManager().updateFields(data, "thread", "`thread_id` = '" + post.getThreadID() + "'");
        if (this.getDataManager().exist("thread_user_post", "user_id", post.getAuthor().getID()) &&
                this.getDataManager().exist("thread_user_post", "thread_id", post.getID())) {
            data = new HashMap<String, Object>();
            int postCount = this.getDataManager().getIntegerField("thread_user_post", "post_count",
                    "`thread_id` = '" + post.getThreadID() +
                            "' AND `user_id` = '" + post.getAuthor().getID() + "'");
            data.put("post_count", postCount + 1);
            this.getDataManager().updateFields(data, "thread_user_post", "`thread_id` = '" +
                    post.getThreadID() + "' AND `user_id` = '" +
                    post.getAuthor().getID() + "'");
        } else {
            data = new HashMap<String, Object>();
            data.put("thread_id", post.getThreadID());
            data.put("user_id", post.getAuthor().getID());
            data.put("post_count", 1);
            this.getDataManager().insertFields(data, "thread_user_post");
        }
        this.getDataManager().increaseField("user", "message_count", "`user_id` = '" + post.getAuthor().getID() + "'");
        data.clear();
    }

    @Override
    public int getThreadCount(String username) {
        return this.getDataManager().getCount("thread", "`user_id` = '" + getUserID(username) + "'");
    }

    @Override
    public int getTotalThreadCount() {
        return this.getDataManager().getCount("thread");
    }

    @Override
    public ForumThread getLastThread() throws SQLException {
        return getThread(this.getDataManager().getIntegerField("SELECT `thread_id` FROM `" + this.getDataManager().getPrefix() +
                "thread` ORDER BY `thread_id` ASC LIMIT 1"));
    }

    @Override
    public ForumThread getLastUserThread(String username) throws SQLException {
        return getThread(this.getDataManager().getIntegerField(
                "SELECT `thread_id` FROM `" + this.getDataManager().getPrefix() + "thread` WHERE `user_id` = '" +
                        getUserID(username) + "' ORDER BY `thread_id` ASC LIMIT 1"));
    }

    @Override
    public ForumThread getThread(int threadid) throws SQLException {
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "thread` WHERE `thread_id` = '" + threadid +
                        "' LIMIT 1");
        ForumThread thread =
                new ForumThread(this, Integer.parseInt(array.get("first_post_id").toString()),
                        Integer.parseInt(array.get("last_post_id").toString()),
                        Integer.parseInt(array.get("thread_id").toString()), Integer.parseInt(array.get("node_id")
                        .toString()));
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
        thread.setRepliesCount(Integer.parseInt(array.get("reply_count").toString()));
        thread.setViewsCount(Integer.parseInt(array.get("view_count").toString()));
        return thread;
    }

    @Override
    public List<ForumThread> getThreads(int limit) throws SQLException {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `thread_id` FROM `" + this.getDataManager().getPrefix() +
                        "thread` ORDER BY `thread_id` ASC" + limitstring);
        List<ForumThread> threads = new ArrayList<ForumThread>();
        for (HashMap<String, Object> map : array) {
            threads.add(getThread(Integer.parseInt(map.get("thread_id").toString())));
        }
        return threads;
    }

    @Override
    public void updateThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("node_id", thread.getBoardID());
        data.put("title", thread.getSubject());
        data.put("reply_count", thread.getRepliesCount());
        data.put("view_count", thread.getViewsCount());
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
        this.getDataManager().updateFields(data, "thread", "`thread_id` = '" + thread.getID() + "'");
    }

    @Override
    public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        this.insertIP(thread.getAuthor(), "thread", "insert");
        long timestamp = new Date().getTime() / 1000;
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("node_id", thread.getBoardID());
        data.put("title", thread.getSubject());
        data.put("user_id", thread.getAuthor().getID());
        data.put("username", thread.getAuthor().getUsername());
        data.put("post_date", timestamp);
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
        data.put("last_post_date", timestamp);
        data.put("last_post_user_id", thread.getAuthor().getID());
        data.put("last_post_username", thread.getAuthor().getUsername());
        this.getDataManager().insertFields(data, "thread");
        int threadID = this.getDataManager().getLastID("thread_id", "thread");
        thread.setID(threadID);
        this.addSearch(thread.getAuthor(), "thread", thread.getBoardID(), thread.getID(),
                thread.getSubject(), thread.getBody());
        ForumPost post = new ForumPost(this, thread.getID(), thread.getBoardID());
        post.setAuthor(thread.getAuthor());
        post.setBody(thread.getBody());
        post.setSubject(thread.getSubject());
        post.create();
        data = new HashMap<String, Object>();
        data.put("first_post_id", post.getID());
        data.put("last_post_id", post.getID());
        this.getDataManager().updateFields(data, "thread", "`thread_id` = '" + thread.getID() + "'");
        data.clear();
    }

    @Override
    public int getUserCount() {
        return this.getDataManager().getCount("user");
    }

    @Override
    public int getGroupCount() {
        return this.getDataManager().getCount("user_group");
    }

    @Override
    public List<String> getIPs(String username) {
        List<String> ips = new ArrayList<String>();
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT `ip` FROM `" + this.getDataManager().getPrefix() +
                        "ip` WHERE `user_id` = '" + getUserID(username) + "' GROUP BY `ip`");
        for (HashMap<String, Object> map : array) {
            ips.add(CraftCommons.long2ip(Long.parseLong(map.get("ip").toString())));
        }
        return ips;
    }

    @Override
    public List<Ban> getBans(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<Ban> bans = new ArrayList<Ban>();
        List<HashMap<String, Object>> array =
                this.getDataManager().getArrayList("SELECT * FROM `" + this.getDataManager().getPrefix() +
                        "ban_email` " + limitstring);
        for (HashMap<String, Object> map : array) {
            bans.add(new Ban(this, null, map.get("banned_email").toString(), null));
        }
        array = this.getDataManager().getArrayList("SELECT * FROM `" + this.getDataManager().getPrefix() +
                "ip_match` " + limitstring);
        for (HashMap<String, Object> map : array) {
            bans.add(new Ban(this, null, null, map.get("ip").toString()));
        }
        array = this.getDataManager().getArrayList("SELECT * FROM `" + this.getDataManager().getPrefix() +
                "user_ban` " + limitstring);
        for (HashMap<String, Object> map : array) {
            Ban ban = new Ban(this, null, null, null);
            ban.setUserID(Integer.parseInt(map.get("ban_user_id").toString()));
            ban.setReason(map.get("user_reason").toString());
            ban.setStartDate(new Date(Long.parseLong(map.get("ban_date").toString()) * 1000));
            if (map.get("end_date").toString().equalsIgnoreCase("0")) {
                ban.setEndDate(null);
            } else {
                ban.setStartDate(new Date(Long.parseLong(map.get("end_date").toString()) * 1000));
            }
            bans.add(ban);
        }
        return bans;
    }

    @Override
    public void updateBan(Ban ban) {
        /* TODO: Make it possible to update email bans, user and IP bans. */
    }

    @Override
    public void addBan(Ban ban) {
        /* TODO: Make it possible to add email bans, user and IP bans. */
    }

    @Override
    public int getBanCount() {
        return this.getDataManager().getCount("ip_match") + this.getDataManager().getCount("user_ban") +
                this.getDataManager().getCount("ban_email");
    }

    @Override
    public boolean isBanned(String string) throws SQLException {
        if (CraftCommons.isEmail(string)) {
            if (this.getDataManager().exist("ban_email", "banned_email", string)) {
                return true;
            }
        } else if (CraftCommons.isIP(string)) {
            if (this.getDataManager().exist("ip_match", "ip", string)) {
                return true;
            }
        } else {
            if (isRegistered(string)) {
                ScriptUser user = getUser(string);
                return this.getDataManager().exist("user_ban", "user_id", user.getID());
            }
        }
        return false;
    }

    @Override
    public boolean isRegistered(String username) {
        return this.getDataManager().exist("user", "username", username);
    }

    private int insertIP(ScriptUser user, String content, String action) throws SQLException {
        int lastIPID = this.getDataManager().getLastID("content_id", "ip",
                "`content_type` = '" + content + "' AND `action` = '" + action + "'");
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("content_type", content);
        data.put("content_id", lastIPID + 1);
        data.put("action", action);
        data.put("ip", CraftCommons.ip2long(user.getLastIP()));
        data.put("log_date", new Date().getTime() / 1000);
        this.getDataManager().insertFields(data, "ip");
        return this.getDataManager().getLastID("ip_id", "ip");
    }

    private void addSearch(ScriptUser user, String type, int node, int discussionID, String title, String message)
            throws SQLException {
        int contentID = this.getDataManager().getLastID("content_id", "ip", "`content_type` = '" + type + "'");
        if (contentID == 0) {
            contentID = 1;
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (type.equalsIgnoreCase("profile_post")) {
            data.put("message", message);
            data.put("metadata", "_md_user_" + user.getID() + " _md_content_" + type +
                    " _md_profile_user_" + user.getID());
        } else {
            if (type.equalsIgnoreCase("post")) {
                data.put("message", message);
            }
            data.put("title", title);
            data.put("metadata", "_md_user_" + user.getID() + " _md_content_" + type + " _md_node_" + node +
                    " _md_thread_" + discussionID);
        }
        data.put("content_type", type);
        data.put("content_id", contentID);
        data.put("user_id", user.getID());
        data.put("item_date", new Date().getTime() / 1000);
        data.put("discussion_id", discussionID);
        this.getDataManager().insertFields(data, "search_index");
    }
}