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
    private final String[] versionRanges = {"1.0.4", "1.1.2"};
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
        return this.dataManager.getIntegerField("user", "user_id", "`username` = '" + username + "'");
    }

    public ScriptUser getLastRegUser() {
        return getUser(this.dataManager.getIntegerField("SELECT `user_id` FROM `" + this.dataManager.getPrefix() +
                                                        "user` ORDER BY `user_id` ASC LIMIT 1"));
    }

    public ScriptUser getUser(String username) {
        return getUser(getUserID(username));
    }

    public ScriptUser getUser(int userid) {
        if (isRegistered(getUsername(userid))) {
            ScriptUser user = new ScriptUser(this, userid, null, null);
            HashMap<String, Object> array = this.dataManager.getArray(
                    "SELECT * FROM `" + this.dataManager.getPrefix() + "user` WHERE `user_id` = '" +
                    userid + "' LIMIT 1");
            if (array.size() > 0) {
                if (array.get("user_state").toString().equalsIgnoreCase("valid")) {
                    user.setActivated(true);
                } else {
                    user.setActivated(false);
                }
                if (! array.get("gravatar").toString().isEmpty()) {
                    user.setAvatarURL("http://www.gravatar.com/avatar/" +
                                      CraftCommons.md5(array.get("gravatar").toString().toLowerCase()));
                }
                user.setEmail(array.get("email").toString());
                if (array.get("gender").toString().equalsIgnoreCase("male")) {
                    user.setGender(Gender.MALE);
                } else if (array.get("gender").toString().equalsIgnoreCase("female")) {
                    user.setGender(Gender.FEMALE);
                } else {
                    user.setGender(Gender.UNKNOWN);
                }
                user.setGroups(getUserGroups(array.get("username").toString()));
                user.setLastLogin(new Date(Long.parseLong(array.get("last_activity").toString()) * 1000));
                user.setRegDate(new Date(Long.parseLong(array.get("register_date").toString()) * 1000));
                Blob hashBlob =
                        this.dataManager.getBlobField("user_authenticate", "data", "`user_id` = '" + userid + "'");
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
                    user.setPassword(CraftCommons.forumCacheValue(cache, "hash"));
                    user.setPasswordSalt(CraftCommons.forumCacheValue(cache, "salt"));
                }
                user.setUsername(array.get("username").toString());
                user.setUserTitle(array.get("custom_title").toString());
            }

            array = this.dataManager.getArray(
                    "SELECT * FROM `" + this.dataManager.getPrefix() + "user_profile` WHERE `user_id` = '" +
                    userid + "' LIMIT 1");
            if (array.size() > 0) {
                String bdate = array.get("dob_day").toString() + " " + array.get("dob_month").toString() + " " +
                               array.get("dob_year").toString();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("d M yyyy");
                    user.setBirthday(format.parse(bdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (! array.get("status").toString().isEmpty()) {
                    user.setStatusMessage(array.get("status").toString());
                }
            }

            return user;
        }
        return null;
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

        if (user.isActivated()) {
            data.put("user_state", "valid");
        } else {
            data.put("user_state", "email_confirm");
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
        this.dataManager.updateBlob("user_authenticate", "data", "`user_id` = '" + user.getID() + "'", stringdata);
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
        if (user.getGender() != null && user.getGender().equals(Gender.MALE)) {
            data.put("gender", "male");
        } else if (user.getGender() != null && user.getGender().equals(Gender.FEMALE)) {
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
        data.put("user_id", user.getID());
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
                this.dataManager.insertFields(data2, "user_option");
                data2.clear();
            }
        }
        this.dataManager.updateBlob("user_profile", "identities", "`user_id` = '" + user.getID() + "'", "a:0:{}");

        /*TODO: Make sure Blob works*/
        String stringdata =
                "a:3:{s:4:\"hash\";s:64:\"" + user.getPassword() + "\";s:4:\"salt\";s:64:\"" + user.getPasswordSalt() +
                "\";s:8:\"hashFunc\";s:6:\"sha256\";}";
        data = new HashMap<String, Object>();
        data.put("user_id", user.getID());
        data.put("scheme_class", "XenForo_Authentication_Core");
        this.dataManager.insertFields(data, "user_authenticate");
        this.dataManager.updateBlob("user_authenticate", "data", "`user_id` = '" + user.getID() + "'", stringdata);
        data.clear();
    }

    public List<Group> getGroups(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<Group> groups = new ArrayList<Group>();
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `user_group_id` FROM `" + this.dataManager.getPrefix() +
                                              "user_group` ORDER BY `user_group_id` ASC" + limitstring);
        for (HashMap<String, Object> map : array) {
            groups.add(getGroup(Integer.parseInt(map.get("user_group_id").toString())));
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
        String additional = this.dataManager.getBinaryField("user", "secondary_group_ids", "`user_id` = '" + getUserID(username) + "'");
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

    public void updateGroup(Group group) {
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
            pm.setSubject(this.dataManager.getStringField("conversation_master", "title",
                                                          "`conversation_id` = '" + conversationID + "'"));
            List<ScriptUser> recipients = new ArrayList<ScriptUser>();
            List<HashMap<String, Object>> recipientsArray = this.dataManager.getArrayList(
                    "SELECT `user_id`, `recipient_state`, `last_read_date` FROM `" + this.dataManager.getPrefix() +
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
                    pm.setRead(recipient, true);
                }
            }
            pm.setRecipients(recipients);
        }
        return pm;
    }

    public List<PrivateMessage> getPMsSent(String username, int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `message_id` FROM `" + this.dataManager.getPrefix() +
                                              "conversation_message` WHERE `user_id` = '" + getUserID(username) +
                                              "' ORDER BY `message_id` ASC" +
                                              limitstring);
        for (HashMap<String, Object> map : array) {
            pms.add(getPM(Integer.parseInt(map.get("message_id").toString())));
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
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `conversation_id` FROM `" + this.dataManager.getPrefix() +
                                              "conversation_recipient` WHERE `user_id` = '" + userID +
                                              "' ORDER BY `conversation_id` ASC" +
                                              limitstring);
        for (HashMap<String, Object> map : array) {
            int conversationID = Integer.parseInt(map.get("conversation_id").toString());
            List<HashMap<String, Object>> pmsArray =
                    this.dataManager.getArrayList("SELECT `message_id` FROM `" + this.dataManager.getPrefix() +
                                                  "conversation_message` WHERE `conversation_id` = '" +
                                                  conversationID +
                                                  "' AND `user_id` != '" + userID + "'");
            for (int a = 0; pmsArray.size() > a; a++) {
                int conversationStarterID =
                        this.dataManager.getIntegerField("SELECT `user_id` FROM `" + this.dataManager.getPrefix() +
                                                         "conversation_master` WHERE `conversation_id` = '" +
                                                         conversationID + "'");
                if (userID != conversationStarterID) {
                    pms.add(getPM(Integer.parseInt(pmsArray.get(a).get("message_id").toString())));
                }
            }
        }
        return pms;
    }

    public int getPMSentCount(String username) {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() +
                                                "conversation_message` WHERE `user_id` = '" + getUserID(username) +
                                                "'");
    }

    public int getPMReceivedCount(String username) {
        /*TODO*/
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() +
                                                "conversation_recipient` WHERE `user_id` = '" + getUserID(username) +
                                                "'");
    }

    public void updatePrivateMessage(PrivateMessage pm) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("message", pm.getBody());
        data.put("message_date", pm.getDate().getTime() / 1000);
        this.dataManager.updateFields(data, "conversation_message", "`message_id` = '" + pm.getID() + "'");
        List<ScriptUser> recipients = pm.getRecipients();
        int conversationID = this.dataManager.getIntegerField("conversation_message", "conversation_id",
                                                              "`message_id` = '" + pm.getID() + "'");
        for (ScriptUser rec : recipients) {
            data = new HashMap<String, Object>();
            if (pm.isDeleted(rec)) {
                data.put("recipient_state", "deleted");
            } else {
                data.put("recipient_state", "active");
            }
            if (pm.isRead(rec)) {
                int read = this.dataManager.getIntegerField("conversation_recipient", "last_read_date",
                                                            "`user_id` = '" + rec.getID() +
                                                            "' AND `conversation_id` = '" + conversationID + "'");
                if (read == 0) {
                    data.put("last_read_date", new Date().getTime() / 1000);
                }
            } else {
                data.put("last_read_date", "0");
            }
            this.dataManager.updateFields(data, "conversation_recipient",
                                          "`user_id` = '" + rec.getID() + "' AND `conversation_id` = '" +
                                          conversationID + "'");
        }
        data.clear();
    }

    public void createPrivateMessage(PrivateMessage pm) {
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
        this.dataManager.insertFields(data, "conversation_master");
        int conversationID = this.dataManager.getLastID("conversation_id", "conversation_master");
        int lastIPID = this.dataManager.getLastID("content_id", "ip", "`user_id` = '" + pm.getSender().getID() + "'");
        data = new HashMap<String, Object>();
        data.put("user_id", pm.getSender().getID());
        data.put("content_type", "conversation_message");
        data.put("content_id", lastIPID + 1);
        data.put("action", "insert");
        data.put("ip", CraftCommons.ip2long(pm.getSender().getLastIP()));
        data.put("log_date", timestamp);
        this.dataManager.insertFields(data, "ip");
        int ipID = this.dataManager.getLastID("ip_id", "ip");
        data = new HashMap<String, Object>();
        data.put("conversation_id", conversationID);
        data.put("message_date", timestamp);
        data.put("user_id", pm.getSender().getID());
        data.put("username", pm.getSender().getUsername());
        data.put("message", pm.getBody());
        data.put("ip_id", ipID);
        this.dataManager.insertFields(data, "conversation_message");
        int messageID = this.dataManager.getLastID("message_id", "conversation_message");
        data = new HashMap<String, Object>();
        data.put("first_message_id", messageID);
        data.put("last_message_id", messageID);
        this.dataManager.updateFields(data, "conversation_master", "`conversation_id` = '" + conversationID + "'");
        data = new HashMap<String, Object>();
        data.put("conversation_id", conversationID);
        data.put("user_id", pm.getSender().getID());
        data.put("recipient_state", "active");
        data.put("last_read_date", timestamp);
        this.dataManager.insertFields(data, "conversation_recipient");
        data = new HashMap<String, Object>();
        data.put("conversation_id", conversationID);
        data.put("owner_user_id", pm.getSender().getID());
        data.put("is_unread", 0);
        data.put("reply_count", 0);
        data.put("last_message_date", timestamp);
        data.put("last_message_id", messageID);
        data.put("last_message_user_id", pm.getSender().getID());
        data.put("last_message_username", pm.getSender().getUsername());
        this.dataManager.insertFields(data, "conversation_user");
        for (ScriptUser recipient : pm.getRecipients()) {
            data = new HashMap<String, Object>();
            data.put("conversation_id", conversationID);
            data.put("user_id", recipient.getID());
            data.put("recipient_state", "active");
            data.put("last_read_date", 0);
            this.dataManager.insertFields(data, "conversation_recipient");
            data = new HashMap<String, Object>();
            data.put("conversation_id", conversationID);
            data.put("owner_user_id", recipient.getID());
            data.put("is_unread", 1);
            data.put("reply_count", 0);
            data.put("last_message_date", timestamp);
            data.put("last_message_id", messageID);
            data.put("last_message_user_id", pm.getSender().getID());
            data.put("last_message_username", pm.getSender().getUsername());
            this.dataManager.insertFields(data, "conversation_user");
        }
        data.clear();
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
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `post_id` FROM `" + this.dataManager.getPrefix() +
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
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `post_id` FROM `" + this.dataManager.getPrefix() +
                                              "post` WHERE `thread_id` = '" + threadid + "' ORDER BY `post_id` ASC" +
                                              limitstring);
        List<Post> posts = new ArrayList<Post>();
        for (HashMap<String, Object> map : array) {
            posts.add(getPost(Integer.parseInt(map.get("post_id").toString())));
        }
        return posts;
    }

    public Post getPost(int postid) {
        HashMap<String, Object> array = this.dataManager.getArray(
                "SELECT * FROM `" + this.dataManager.getPrefix() + "post` WHERE `post_id` = '" + postid + "' LIMIT 1");
        int nodeID = this.dataManager.getIntegerField("thread", "node_id", "`thread_id` = '" +
                                                                           Integer.parseInt(array.get("thread_id")
                                                                                                 .toString()) +
                                                                           "'");
        Post post =
                new Post(this, Integer.parseInt(array.get("post_id").toString()),
                         Integer.parseInt(array.get("thread_id").toString()), nodeID);
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
        data.put("post_date", post.getPostDate().getTime() / 1000);
        data.put("message", post.getBody());
        this.dataManager.updateFields(data, "post", "`post_id` = '" + post.getID() + "'");
        data = new HashMap<String, Object>();
        data.put("node_id", post.getBoardID());
        this.dataManager.updateFields(data, "thread", "`thread_id` = '" + post.getThreadID() + "'");
        data.clear();
    }

    public void createPost(Post post) {
        int lastIPID = this.dataManager.getLastID("content_id", "ip", "`user_id` = '" + post.getAuthor().getID() + "'");
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("thread_id", post.getThreadID());
        data.put("user_id", post.getAuthor().getID());
        data.put("username", post.getAuthor().getUsername());
        data.put("post_date", new Date().getTime() / 1000);
        data.put("message", post.getBody());
        data.put("ip_id", lastIPID);
        data.put("position", 0);
        this.dataManager.insertFields(data, "post");
        int postID = this.dataManager.getLastID("post_id", "post");
        data = new HashMap<String, Object>();
        data.put("user_id", post.getAuthor().getID());
        data.put("content_type", "post");
        data.put("content_id", lastIPID + 1);
        data.put("action", "insert");
        data.put("ip", CraftCommons.ip2long(post.getAuthor().getLastIP()));
        data.put("log_date", new Date().getTime() / 1000);
        this.dataManager.insertFields(data, "ip");
        int replyCount = this.dataManager.getIntegerField("thread", "reply_count",
                                                          "`thread_id` = '" + post.getThreadID() + "'");
        this.dataManager.updateBlob("post", "like_users", "`post_id` = '" + postID + "'", "a:0:{}");
        data = new HashMap<String, Object>();
        data.put("node_id", post.getBoardID());
        data.put("reply_count", replyCount + 1);
        data.put("last_post_date", new Date().getTime() / 1000);
        data.put("last_post_id", postID);
        data.put("last_post_user_id", post.getAuthor().getID());
        data.put("last_post_username", post.getAuthor().getUsername());
        this.dataManager.updateFields(data, "thread", "`thread_id` = '" + post.getThreadID() + "'");
        if (this.dataManager.exist("thread_user_post", "user_id", post.getAuthor().getID())) {
            data = new HashMap<String, Object>();
            int postCount = this.dataManager.getIntegerField("thread_user_post", "post_count",
                                                             "`thread_id` = '" + post.getThreadID() +
                                                             "' AND `user_id` = '" + post.getAuthor().getID() + "'");
            data.put("post_count", postCount + 1);
            this.dataManager.updateFields(data, "thread_user_post", "`thread_id` = '" +
                                                                    post.getThreadID() + "' AND `user_id` = '" +
                                                                    post.getAuthor().getID() + "'");
        } else {
            data = new HashMap<String, Object>();
            data.put("thread_id", post.getThreadID());
            data.put("user_id", post.getAuthor().getID());
            data.put("post_count", 1);
            this.dataManager.insertFields(data, "thread_user_post");
        }
        data.clear();
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
        HashMap<String, Object> array = this.dataManager.getArray(
                "SELECT * FROM `" + this.dataManager.getPrefix() + "thread` WHERE `thread_id` = '" + threadid +
                "' LIMIT 1");
        Thread thread =
                new Thread(this, Integer.parseInt(array.get("first_post_id").toString()),
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
        thread.setReplies(Integer.parseInt(array.get("reply_count").toString()));
        thread.setViews(Integer.parseInt(array.get("view_count").toString()));
        return thread;
    }

    public List<Thread> getThreads(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `thread_id` FROM `" + this.dataManager.getPrefix() +
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
        this.dataManager.insertFields(data, "thread");
        int threadID = this.dataManager.getLastID("thread_id", "thread");
        thread.setID(threadID);
        Post post = new Post(this, thread.getID(), thread.getBoardID());
        post.setAuthor(thread.getAuthor());
        post.setBody(thread.getBody());
        post.setSubject(thread.getSubject());
        post.createPost();
        data = new HashMap<String, Object>();
        data.put("first_post_id", post.getID());
        data.put("last_post_id", post.getID());
        this.dataManager.updateFields(data, "thread", "`thread_id` = '" + thread.getID() + "'");
        data.clear();
    }

    public int getUserCount() {
        return this.dataManager.getCount("user");
    }

    public int getGroupCount() {
        return this.dataManager.getCount("user_group");
    }

    public List<String> getIPs(String username) {
        List<String> ips = new ArrayList<String>();
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT `ip` FROM `" + this.dataManager.getPrefix() +
                                              "ip` WHERE `user_id` = '" + getUserID(username) + "' GROUP BY `ip`");
        for (HashMap<String, Object> map : array) {
            ips.add(CraftCommons.long2ip(Long.parseLong(map.get("ip").toString())));
        }
        return ips;
    }

    public List<Ban> getBans(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        List<Ban> bans = new ArrayList<Ban>();
        List<HashMap<String, Object>> array =
                this.dataManager.getArrayList("SELECT * FROM `" + this.dataManager.getPrefix() +
                                              "ban_email` " + limitstring);
        for (HashMap<String, Object> map : array) {
            bans.add(new Ban(this, null, map.get("banned_email").toString(), null));
        }
        array = this.dataManager.getArrayList("SELECT * FROM `" + this.dataManager.getPrefix() +
                                              "ip_match` " + limitstring);
        for (HashMap<String, Object> map : array) {
            bans.add(new Ban(this, null, null, map.get("ip").toString()));
        }
        array = this.dataManager.getArrayList("SELECT * FROM `" + this.dataManager.getPrefix() +
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

    public void updateBan(Ban ban) {
        /* TODO: Make it possible to update email bans, user and IP bans. */
    }

    public void addBan(Ban ban) {
        /*TODO*/
    }

    public int getBanCount() {
        return this.dataManager.getCount("ip_match") + this.dataManager.getCount("user_ban") +
               this.dataManager.getCount("ban_email");
    }

    public boolean isBanned(String string) {
        if (CraftCommons.isEmail(string)) {
            if (this.dataManager.exist("ban_email", "banned_email", string)) {
                return true;
            }
        } else if (CraftCommons.isIP(string)) {
            if (this.dataManager.exist("ip_match", "ip", string)) {
                return true;
            }
        } else {
            if (isRegistered(string)) {
                ScriptUser user = getUser(string);
                return this.dataManager.exist("user_ban", "user_id", user.getID());
            }
        }
        return false;
    }

    public boolean isRegistered(String username) {
        return this.dataManager.exist("user", "username", username);
    }
}
