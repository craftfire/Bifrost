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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.JTable;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumScript;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.forum.ForumUser;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Gender;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

//TODO: Convert arrays to use Result class
/**
 * This class contains all the methods for SMF.
 */
public class SMF extends ForumScript {
    private String currentUsername = null;
    private String membernamefield = "member_name", groupfield = "additional_groups";

    /**
     * Default constructor for SMF.
     *
     * @param script       the {@link Scripts} enum
     * @param version      the version of the script
     * @param dataManager  the {@link DataManager}
     */
    public SMF(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
        this.setScriptName("simplemachines");
        this.setShortName("smf");
        this.setVersionRanges(new String[] {"1.1.16", "2.0.2"});
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            this.membernamefield = this.membernamefield.replace("_", "");
            this.groupfield = this.groupfield.replace("_", "");
        }
    }

    @Override
    public String getLatestVersion() {
        return this.getVersionRanges()[1];
    }

    @Override
    public boolean authenticate(String username, String password) {
        String passwordHash = this.getDataManager().getStringField(
                "SELECT `passwd` FROM `" + this.getDataManager().getPrefix() + "members` WHERE `" + this.membernamefield +
                        "` = '" + username + "'");
        return hashPassword(username, password).equals(passwordHash);
    }

    @Override
    public String hashPassword(String username, String password) {
        return CraftCommons.encrypt(Encryption.SHA1, username.toLowerCase() + password);
    }

    @Override
    public ForumUser getLastRegUser() {
        return getUser(this.getDataManager().getIntegerField("SELECT `id_member` FROM `" +
                this.getDataManager().getPrefix() + "members` ORDER BY `id_member` ASC LIMIT 1"));
    }

    @Override
    public ForumUser getUser(String username) {
        return getUser(getUserID(username));
    }

    @Override
    public ForumUser getUser(int userid) {
        int newUserid = userid;
        JTable userTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "members` WHERE `id_member` = '" + newUserid +
                        "' LIMIT 1"));
        String title = null, savedusername = null, nickname = null, email = null, password = null, passwordsalt = null,
                avatarurl = null, regip = null, lastip = null;
        Date regdate = null, lastlogin = null, birthday = null;
        Gender gender = null;
        boolean activated = false;
        if (userTable.getRowCount() == 1) {
            newUserid = Integer.parseInt(userTable.getModel().getValueAt(0, 0).toString());
            savedusername = userTable.getModel().getValueAt(0, 1).toString();
            nickname = userTable.getModel().getValueAt(0, 7).toString();
            regdate = new Date(Long.parseLong(userTable.getModel().getValueAt(0, 2).toString()) * 1000);
            lastlogin = new Date(Long.parseLong(userTable.getModel().getValueAt(0, 6).toString()) * 1000);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
                email = userTable.getModel().getValueAt(0, 14).toString();
                title = userTable.getModel().getValueAt(0, 34).toString();
                password = userTable.getModel().getValueAt(0, 13).toString();
                avatarurl = userTable.getModel().getValueAt(0, 30).toString();
                regip = userTable.getModel().getValueAt(0, 39).toString();
                lastip = userTable.getModel().getValueAt(0, 40).toString();
                passwordsalt = userTable.getModel().getValueAt(0, 51).toString();
                try {
                    birthday = format.parse(userTable.getModel().getValueAt(0, 17).toString());
                } catch (ParseException e) {
                    birthday = null;
                }
                int genderid = Integer.parseInt(userTable.getModel().getValueAt(0, 16).toString());
                if (genderid == 0) {
                    gender = Gender.MALE;
                } else if (genderid == 1) {
                    gender = Gender.FEMALE;
                } else {
                    gender = Gender.UNKNOWN;
                }
                int activatedid = Integer.parseInt(userTable.getModel().getValueAt(0, 44).toString());
                if (activatedid == 1) {
                    activated = true;
                }
            } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
                email = userTable.getModel().getValueAt(0, 18).toString();
                title = userTable.getModel().getValueAt(0, 38).toString();
                password = userTable.getModel().getValueAt(0, 16).toString();
                avatarurl = userTable.getModel().getValueAt(0, 34).toString();
                regip = userTable.getModel().getValueAt(0, 43).toString();
                lastip = userTable.getModel().getValueAt(0, 44).toString();
                passwordsalt = userTable.getModel().getValueAt(0, 55).toString();
                try {
                    birthday = format.parse(userTable.getModel().getValueAt(0, 21).toString());
                } catch (ParseException e) {
                    birthday = null;
                }
                int genderid = Integer.parseInt(userTable.getModel().getValueAt(0, 20).toString());
                if (genderid == 0) {
                    gender = Gender.MALE;
                } else if (genderid == 1) {
                    gender = Gender.FEMALE;
                } else {
                    gender = Gender.UNKNOWN;
                }
                int activatedid = Integer.parseInt(userTable.getModel().getValueAt(0, 48).toString());
                if (activatedid == 1) {
                    activated = true;
                }
            }
        }
        ForumUser user = new ForumUser(this, newUserid, savedusername, password);
        user.setPasswordSalt(passwordsalt);
        user.setUserTitle(title);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setAvatarURL(avatarurl);
        user.setRegIP(regip);
        user.setLastIP(lastip);
        user.setRegDate(regdate);
        user.setLastLogin(lastlogin);
        user.setBirthday(birthday);
        user.setGender(gender);
        user.setActivated(activated);
        return user;
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("membername", user.getUsername());
            data.put("realname", user.getNickname());
            data.put("emailaddress", user.getEmail());
            data.put("memberip", user.getRegIP());
            data.put("memberip2", user.getLastIP());
            data.put("dateregistered", user.getRegDate().getTime() / 1000);
            data.put("lastlogin", user.getLastLogin().getTime() / 1000);
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("member_name", user.getUsername());
            data.put("real_name", user.getNickname());
            data.put("email_address", user.getEmail());
            data.put("member_ip", user.getRegIP());
            data.put("member_ip2", user.getLastIP());
            data.put("date_registered", user.getRegDate().getTime() / 1000);
            data.put("last_login", user.getLastLogin().getTime() / 1000);
        }
        data.put("avatar", user.getAvatarURL());
        if (user.getPassword().length() != 40) {
            data.put("passwd", hashPassword(user.getUsername(), user.getPassword()));
        }
        data.put("usertitle", user.getUserTitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        data.put("birthdate", format.format(user.getBirthday()));
        int genderID = 0;
        if (user.getGender() != null && user.getGender() == Gender.FEMALE) {
            genderID = 1;
        }
        data.put("gender", genderID);
        int activated = 0;
        if (user.isActivated()) {
            activated = 1;
        }
        data.put("is_activated", activated);
        this.getDataManager().updateFields(data, "members", "`" + this.membernamefield + "` = '" + user.getUsername() +
                "'");
        data.clear();
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException {
        if (isRegistered(user.getUsername())) {
            return;
        }
        Random r = new Random();
        int rand = r.nextInt(1000000);
        String salt = CraftCommons.encrypt(Encryption.MD5, rand).substring(0, 4);
        HashMap<String, Object> data = new HashMap<String, Object>();
        user.setPasswordSalt(salt);
        user.setRegDate(new Date());
        user.setLastLogin(new Date());
        data.put(this.membernamefield, user.getUsername());
        data.put("passwd", user.getPassword());
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("dateregistered", user.getRegDate().getTime() / 1000);
            data.put("realname", user.getUsername());
            data.put("emailaddress", user.getEmail());
            data.put("memberip", user.getRegIP());
            data.put("memberip2", user.getLastIP());
            data.put("passwordsalt", user.getPasswordSalt());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("date_registered", user.getRegDate().getTime() / 1000);
            data.put("real_name", user.getUsername());
            data.put("email_address", user.getEmail());
            data.put("member_ip", user.getRegIP());
            data.put("member_ip2", user.getLastIP());
            data.put("password_salt", user.getPasswordSalt());
        }
        this.getDataManager().insertFields(data, "members");

        user.setID(getUserID(user.getUsername()));

        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "settings" + "` SET `value` =" +
                " '" + user.getUsername() + "' WHERE `variable` = 'latestRealName'");

        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "settings" + "` SET `value` =" +
                " '" + user.getID() + "' WHERE `variable` = 'latestMember'");

        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "settings" + "` SET `value` =" +
                " '" + (user.getRegDate().getTime() / 1000) +
                "' WHERE `variable` = 'memberlist_updated'");

        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "settings" + "` SET `value` =" +
                " value + 1 WHERE `variable` = 'totalMembers'");
    }

    @Override
    public String getUsername(int userid) {
        return this.getDataManager().getStringField(
                "SELECT `" + this.membernamefield + "` FROM `" + this.getDataManager().getPrefix() +
                        "members` WHERE `id_member` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        return this.getDataManager().getIntegerField(
                "SELECT `id_member` FROM `" + this.getDataManager().getPrefix() + "members` WHERE `" + this
                        .membernamefield +
                        "` = '" + username + "'");
    }

    @Override
    public List<Group> getGroups(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable groupTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT `id_group` FROM `" + this.getDataManager().getPrefix() + "membergroups` ORDER BY `id_group` ASC" +
                        limitstring));
        List<Group> groups = new ArrayList<Group>();
        for (int i = 0; groupTable.getRowCount() > i; i++) {
            groups.add(getGroup(Integer.parseInt(groupTable.getModel().getValueAt(0, 0).toString())));
        }
        return groups;
    }

    @Override
    public int getGroupID(String group) {
        /*TODO*/
        return 0;
    }

    @Override
    public Group getGroup(int groupid) {
        JTable groupTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "membergroups` WHERE `id_group` = '" + groupid +
                        "' ORDER BY `id_group` ASC LIMIT 1"));
        String groupname = null, groupdescription = null;
        List<ScriptUser> users = new ArrayList<ScriptUser>();
        int newGroupid = groupid;
        if (groupTable.getRowCount() == 1) {
            newGroupid = Integer.parseInt(groupTable.getModel().getValueAt(0, 0).toString());
            groupname = groupTable.getModel().getValueAt(0, 1).toString();
            if (! CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
                groupdescription = groupTable.getModel().getValueAt(0, 2).toString();
            }
            JTable userTable = new JTable(this.getDataManager().resultSetToTableModel(
                    "SELECT `" + this.membernamefield + "` FROM `" + this.getDataManager().getPrefix() +
                            "members` WHERE `id_group` = '" + newGroupid + "' ORDER BY `id_member` ASC"));
            for (int a = 0; groupTable.getRowCount() > a; a++) {
                String username = userTable.getModel().getValueAt(a, 0).toString();
                if (this.currentUsername != null && ! this.currentUsername.equalsIgnoreCase(username)) {
                    users.add(getUser(username));
                }
            }
        }
        this.currentUsername = null;
        Group group = new Group(this, newGroupid, groupname);
        group.setDescription(groupdescription);
        group.setUserCount(this.getDataManager().getIntegerField(
                "SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "members` WHERE `id_group` = '" + newGroupid +
                        "'"));
        group.setUsers(users);
        return group;
    }

    @Override
    public Group getGroup(String group) {
        /* TODO */
        return null;
    }

    @Override
    public List<Group> getUserGroups(String username) {
        this.currentUsername = username;
        JTable userTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT `id_group`, `" + this.groupfield + "`  FROM `" + this.getDataManager().getPrefix() +
                        "members` WHERE `" + this.membernamefield + "` = '" + username + "' ORDER BY `id_member` ASC LIMIT " +
                        "1"));
        List<Group> groups = new ArrayList<Group>();
        if (userTable.getRowCount() == 1) {
            groups.add(getGroup(Integer.parseInt(userTable.getModel().getValueAt(0, 0).toString())));
            String additional = userTable.getModel().getValueAt(0, 1).toString();
            if (! additional.isEmpty()) {
                if (additional.contains(",")) {
                    String[] split = userTable.getModel().getValueAt(0, 1).toString().split("\\,");
                    for (int i = 0; split.length > i; i++) {
                        this.currentUsername = username;
                        groups.add(getGroup(Integer.parseInt(split[i])));
                    }
                } else {
                    groups.add(getGroup(Integer.parseInt(userTable.getModel().getValueAt(0, 1).toString())));
                }
            }
        }
        return groups;
    }

    @Override
    public void updateGroup(Group group) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("groupname", group.getName());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("group_name", group.getName());
            data.put("description", group.getDescription());
        }
        this.getDataManager().updateFields(data, "membergroups", "`id_group` = '" + group.getID() + "'");
    }

    @Override
    public void createGroup(Group group) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("groupName", group.getName());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("group_name", group.getName());
            data.put("description", group.getDescription());
        }
        this.getDataManager().insertFields(data, "membergroups");
        group.setID(this.getDataManager().getLastID("id_group", "membergroups"));
    }

    @Override
    public PrivateMessage getPM(int pmid) {
        PrivateMessage pm = new PrivateMessage(this, pmid);
        HashMap<String, Object> array = this.getDataManager().getArray(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "personal_messages` WHERE `id_pm` = '" +
                        pmid + "' LIMIT 1");
        for (int i = 0; array.size() > i; i++) {
            pm.setDate(new Date(Long.parseLong(array.get("msgtime").toString()) * 1000));
            pm.setBody(array.get("body").toString());
            pm.setSubject(array.get("subject").toString());
            pm.setSender(getUser(Integer.parseInt(array.get("id_member_from").toString())));
            if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
                if (array.get("deleted_by_sender").toString().equalsIgnoreCase("0")) {
                    pm.setDeletedBySender(false);
                } else {
                    pm.setDeletedBySender(true);
                }
            } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
                if (array.get("deletedbysender").toString().equalsIgnoreCase("0")) {
                    pm.setDeletedBySender(false);
                } else {
                    pm.setDeletedBySender(true);
                }
            }
            List<ScriptUser> recipients = new ArrayList<ScriptUser>();
            List<HashMap<String, Object>> recipientsArray =
                    this.getDataManager().getArrayList("SELECT * FROM `" + this.getDataManager().getPrefix() +
                            "pm_recipients` WHERE `id_pm` = '" + pm.getID() + "'");
            for (HashMap<String, Object> map : recipientsArray) {
                ScriptUser recipient = getUser(Integer.parseInt(map.get("id_member").toString()));
                recipients.add(recipient);
                if (map.get("is_read").toString().equalsIgnoreCase("0")) {
                    pm.setRead(recipient, false);
                } else {
                    pm.setRead(recipient, true);
                }
                if (map.get("deleted").toString().equalsIgnoreCase("0")) {
                    pm.setDeleted(recipient, false);
                } else {
                    pm.setRead(recipient, true);
                }
                if (! CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
                    if (map.get("is_new").toString().equalsIgnoreCase("0")) {
                        pm.setNew(recipient, false);
                    } else {
                        pm.setNew(recipient, true);
                    }
                }
            }
            pm.setRecipients(recipients);
        }
        return pm;
    }

    @Override
    public List<PrivateMessage> getPMsSent(String username, int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable pmTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "personal_messages` WHERE `id_member_from` = '" +
                        getUserID(username) + "' ORDER BY `id_pm` ASC" + limitstring));
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        for (int i = 0; pmTable.getRowCount() > i; i++) {
            pms.add(getPM(Integer.parseInt(pmTable.getModel().getValueAt(0, 1).toString())));
        }
        return pms;
    }

    @Override
    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable pmTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "pm_recipients` WHERE `id_member` = '" +
                        getUserID(username) + "' ORDER BY `id_pm` ASC" + limitstring));
        List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
        for (int i = 0; pmTable.getRowCount() > i; i++) {
            pms.add(getPM(Integer.parseInt(pmTable.getModel().getValueAt(0, 1).toString())));
        }
        return pms;
    }

    @Override
    public int getPMSentCount(String username) {
        return this.getDataManager().getIntegerField("SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() +
                "personal_messages` WHERE `id_member_from` = '" + getUserID(username) +
                "'");
    }

    @Override
    public int getPMReceivedCount(String username) {
        return this.getDataManager().getIntegerField(
                "SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "pm_recipients` WHERE `id_member` = '" +
                        getUserID(username) + "'");
    }

    @Override
    public void updatePrivateMessage(PrivateMessage pm) throws SQLException {
        String temp;
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("id_member_from", pm.getSender().getID());
        if (pm.isDeletedBySender()) {
            temp = "1";
        } else {
            temp = "0";
        }
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("deletedbysender", temp);
            data.put("fromname", pm.getSender().getUsername());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("deleted_by_sender", temp);
            data.put("from_name", pm.getSender().getUsername());
        }
        data.put("msgtime", pm.getDate().getTime() / 1000);
        data.put("subject", pm.getSubject());
        data.put("body", pm.getBody());
        this.getDataManager().updateFields(data, "personal_messages", "`id_pm` = '" + pm.getID() + "'");

        for (ScriptUser recipient : pm.getRecipients()) {
            data = new HashMap<String, Object>();
            data.put("id_member", recipient.getID());
            if (pm.isRead(recipient)) {
                temp = "1";
            } else {
                temp = "0";
            }
            data.put("is_read", temp);
            if (pm.isDeleted(recipient)) {
                temp = "1";
            } else {
                temp = "0";
            }
            data.put("deleted", temp);
            if (! CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
                if (pm.isNew(recipient)) {
                    temp = "1";
                } else {
                    temp = "0";
                }
                data.put("is_new", temp);
            }
            this.getDataManager().updateFields(data, "pm_recipients",
                    "`id_pm` = '" + pm.getID() + "' AND `id_member` = '" + recipient.getID() +
                            "'");
        }
        data.clear();
    }

    @Override
    public void createPrivateMessage(PrivateMessage pm) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        pm.setDate(new Date());
        int temp = 1;
        data.put("id_member_from", pm.getSender().getID());
        data.put("msgtime", pm.getDate().getTime() / 1000);
        data.put("subject", pm.getSubject());
        data.put("body", pm.getBody());
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            if (! pm.isDeletedBySender()) {
                temp = 0;
            }
            data.put("deletedbysender", temp);
            data.put("fromname", pm.getSender().getUsername());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            if (! pm.isDeletedBySender()) {
                temp = 0;
            }
            data.put("deleted_by_sender", temp);
            data.put("from_name", pm.getSender().getUsername());
        }
        this.getDataManager().insertFields(data, "personal_messages");
        pm.setID(this.getDataManager().getLastID("id_pm", "personal_messages"));
        if (! CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data = new HashMap<String, Object>();
            data.put("id_pm_head", pm.getID());
            this.getDataManager().updateFields(data, "personal_messages", "`id_pm` = '" + pm.getID() + "'");
        }
        for (ScriptUser recipient : pm.getRecipients()) {
            data = new HashMap<String, Object>();
            data.put("id_pm", pm.getID());
            data.put("id_member", recipient.getID());
            temp = 0;
            if (pm.isRead(recipient)) {
                temp = 1;
            }
            data.put("is_read", temp);
            temp = 0;
            if (pm.isDeleted(recipient)) {
                temp = 1;
            }
            data.put("deleted", temp);
            if (! CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
                temp = 0;
                if (pm.isNew(recipient)) {
                    temp = 1;
                }
                data.put("is_new", temp);
            }
            this.getDataManager().insertFields(data, "pm_recipients");
        }
        data.clear();
    }

    @Override
    public int getTotalPostCount() {
        return this.getDataManager().getIntegerField("SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "messages`");
    }

    @Override
    public int getPostCount(String username) {
        return this.getDataManager().getIntegerField(
                "SELECT `posts` FROM `" + this.getDataManager().getPrefix() + "members` WHERE `" + this.membernamefield +
                        "` = '" + username + "'");
    }

    @Override
    public ForumPost getPost(int postid) {
        JTable postTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "messages` WHERE `id_msg` = '" + postid +
                        "' LIMIT 1"));
        //TODO: Figure out how to use boardid
        //boardid = 0,
        int threadid = 0, authorid = 0;
        Date postdate = null;
        String subject = null, body = null;
        if (postTable.getRowCount() == 1) {
            threadid = Integer.parseInt(postTable.getModel().getValueAt(0, 1).toString());
            //TODO: boardid = Integer.parseInt(postTable.getModel().getValueAt(0, 2).toString());
            postdate = new Date(Long.parseLong(postTable.getModel().getValueAt(0, 3).toString()) * 1000);
            authorid = Integer.parseInt(postTable.getModel().getValueAt(0, 4).toString());
            subject = postTable.getModel().getValueAt(0, 6).toString();
            body = postTable.getModel().getValueAt(0, 13).toString();
        }
        ForumPost post = new ForumPost(this, postid, threadid);
        post.setPostDate(postdate);
        post.setAuthor(getUser(authorid));
        post.setSubject(subject);
        post.setBody(body);
        return post;
    }

    @Override
    public ForumPost getLastUserPost(String username) {
        int userid = getUserID(username);
        return getPost(this.getDataManager().getIntegerField(
                "SELECT `id_msg` FROM `" + this.getDataManager().getPrefix() + "messages` WHERE `id_member` = '" + userid +
                        "' ORDER BY `id_msg` ASC LIMIT 1"));
    }

    @Override
    public ForumPost getLastPost() {
        return getPost(this.getDataManager().getIntegerField(
                "SELECT `id_msg` FROM `" + this.getDataManager().getPrefix() + "messages` ORDER BY `id_msg` ASC LIMIT 1"));
    }

    @Override
    public List<ForumPost> getPosts(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable postTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT `id_msg` FROM `" + this.getDataManager().getPrefix() + "messages` ORDER BY `id_msg` ASC" +
                        limitstring));
        List<ForumPost> posts = new ArrayList<ForumPost>();
        for (int i = 0; postTable.getRowCount() > i; i++) {
            posts.add(getPost(Integer.parseInt(postTable.getModel().getValueAt(0, 0).toString())));
        }
        return posts;
    }

    @Override
    public List<ForumPost> getPostsFromThread(int threadid, int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable postTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT `id_msg` FROM `" + this.getDataManager().getPrefix() + "messages` WHERE `id_topic` = '" + threadid +
                        "' ORDER BY `id_msg` ASC" + limitstring));
        List<ForumPost> posts = new ArrayList<ForumPost>();
        for (int i = 0; postTable.getRowCount() > i; i++) {
            posts.add(getPost(Integer.parseInt(postTable.getModel().getValueAt(0, 0).toString())));
        }
        return posts;
    }

    @Override
    public void updatePost(ForumPost post) throws SQLException, UnsupportedMethod {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("id_topic", post.getThreadID());
        data.put("id_board", post.getBoardID());
        data.put("id_member", post.getAuthor().getID());
        data.put("subject", post.getSubject());
        data.put("body", post.getBody());
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("postertime", post.getPostDate().getTime() / 1000);
            data.put("postername", post.getAuthor().getUsername());
            data.put("posteremail", post.getAuthor().getEmail());
            data.put("posterip", post.getAuthor().getLastIP());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("poster_time", post.getPostDate().getTime() / 1000);
            data.put("poster_name", post.getAuthor().getUsername());
            data.put("poster_email", post.getAuthor().getEmail());
            data.put("poster_ip", post.getAuthor().getLastIP());
        }
        this.getDataManager().updateFields(data, "messages", "`id_msg` = '" + post.getID() + "'");
    }

    @Override
    public void createPost(ForumPost post) throws SQLException, UnsupportedMethod {
        HashMap<String, Object> data = new HashMap<String, Object>();
        post.setPostDate(new Date());
        data.put("id_topic", post.getThreadID());
        data.put("id_board", post.getBoardID());
        data.put("id_member", post.getAuthor().getID());
        data.put("subject", post.getSubject());
        data.put("body", post.getBody());

        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            data.put("postertime", post.getPostDate().getTime() / 1000);
            data.put("postername", post.getAuthor().getUsername());
            data.put("posteremail", post.getAuthor().getEmail());
            data.put("posterip", post.getAuthor().getLastIP());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            data.put("poster_time", post.getPostDate().getTime() / 1000);
            data.put("poster_name", post.getAuthor().getUsername());
            data.put("poster_email", post.getAuthor().getEmail());
            data.put("poster_ip", post.getAuthor().getLastIP());
        }

        this.getDataManager().insertFields(data, "messages");
        post.setID(this.getDataManager().getLastID("id_msg", "messages"));

        data = new HashMap<String, Object>();
        data.put("id_msg_modified", post.getID());
        this.getDataManager().updateFields(data, "messages", "`id_msg` = '" + post.getID() + "'");
        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "settings" + "` SET `value` =" +
                " value + 1, `value` = '" + post.getID() + "' WHERE `variable` = 'maxMsgID'");
        data = new HashMap<String, Object>();
        data.put("id_last_msg", post.getID());
        data.put("id_member_updated", post.getAuthor().getID());
        this.getDataManager().updateFields(data, "topics", "`id_topic` = '" + post.getThreadID() + "'");
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "topics" + "` SET `numreplies` =" +
                            " numreplies + 1 WHERE `id_topic` = '" + post.getThreadID() + "'");
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "topics" + "` SET `num_replies` =" +
                            " num_replies + 1 WHERE `id_topic` = '" + post.getThreadID() + "'");
        }
        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "members" + "` SET `posts` =" +
                " posts + 1 WHERE `id_member` = '" + post.getAuthor().getID() + "'");
        this.getDataManager().executeQueryVoid(
                "UPDATE `" + this.getDataManager().getPrefix() + "boards" + "` SET `id_last_msg` =" +
                        " '" + post.getID() + "', `id_msg_updated` = '" + post.getID() + "' WHERE `id_board` = '" +
                        post.getBoardID() + "'");
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "boards" + "` SET `numposts` =" +
                            " numposts + 1 WHERE `id_board` = '" + post.getBoardID() + "'");
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "boards" + "` SET `num_posts` =" +
                            " num_posts + 1 WHERE `id_board` = '" + post.getBoardID() + "'");
        }
    }

    @Override
    public int getTotalThreadCount() {
        return this.getDataManager().getIntegerField("SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "topics`");
    }

    @Override
    public int getThreadCount(String username) {
        int userid = getUserID(username);
        return this.getDataManager().getIntegerField(
                "SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "topics` WHERE `id_member_started` = '" +
                        userid + "'");
    }

    @Override
    public ForumThread getLastThread() {
        return getThread(this.getDataManager().getIntegerField(
                "SELECT `id_topic` FROM `" + this.getDataManager().getPrefix() + "topics` ORDER BY `id_topic` ASC LIMIT " +
                        "1"));
    }

    @Override
    public ForumThread getLastUserThread(String username) {
        int userid = getUserID(username);
        return getThread(this.getDataManager().getIntegerField(
                "SELECT `id_topic` FROM `" + this.getDataManager().getPrefix() + "topics` WHERE `id_member_started` = '" +
                        userid + "' ORDER BY `id_topic` ASC LIMIT 1"));
    }

    @Override
    public ForumThread getThread(int threadid) {
        JTable threadTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "topics` WHERE `id_topic` = '" + threadid +
                        "' LIMIT 1"));
        int firstpostid = 0, lastpostid = 0, boardid = 0, authorid = 0, numreplies = 0, numviews = 0;
        Date threaddate = null;
        String subject = null, body = null;
        boolean sticky = false, locked = false, poll = false;
        if (threadTable.getRowCount() == 1) {
            int temp = Integer.parseInt(threadTable.getModel().getValueAt(0, 1).toString());
            if (temp > 0) {
                sticky = true;
            }
            boardid = Integer.parseInt(threadTable.getModel().getValueAt(0, 2).toString());
            firstpostid = Integer.parseInt(threadTable.getModel().getValueAt(0, 3).toString());
            lastpostid = Integer.parseInt(threadTable.getModel().getValueAt(0, 4).toString());
            authorid = Integer.parseInt(threadTable.getModel().getValueAt(0, 5).toString());
            temp = Integer.parseInt(threadTable.getModel().getValueAt(0, 7).toString());
            if (temp > 0) {
                poll = true;
            }
            if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
                numreplies = Integer.parseInt(threadTable.getModel().getValueAt(0, 8).toString());
                numviews = Integer.parseInt(threadTable.getModel().getValueAt(0, 9).toString());
                temp = Integer.parseInt(threadTable.getModel().getValueAt(0, 10).toString());
                if (temp > 0) {
                    locked = true;
                }
            } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
                numreplies = Integer.parseInt(threadTable.getModel().getValueAt(0, 10).toString());
                numviews = Integer.parseInt(threadTable.getModel().getValueAt(0, 11).toString());
                temp = Integer.parseInt(threadTable.getModel().getValueAt(0, 12).toString());
                if (temp > 0) {
                    locked = true;
                }
            }
            JTable postTable = new JTable(this.getDataManager().resultSetToTableModel(
                    "SELECT * FROM `" + this.getDataManager().getPrefix() + "messages` WHERE `id_msg` = '" + firstpostid +
                            "' LIMIT 1"));
            if (postTable.getRowCount() == 1) {
                threaddate = new Date(Long.parseLong(postTable.getModel().getValueAt(0, 3).toString()) * 1000);
                subject = postTable.getModel().getValueAt(0, 6).toString();
                body = postTable.getModel().getValueAt(0, 13).toString();
            }
        }
        ForumThread thread = new ForumThread(this, firstpostid, lastpostid, threadid, boardid);
        thread.setViewsCount(numviews);
        thread.setRepliesCount(numreplies);
        thread.setLocked(locked);
        thread.setSticky(sticky);
        thread.setPoll(poll);
        thread.setAuthor(getUser(authorid));
        thread.setThreadDate(threaddate);
        thread.setBody(body);
        thread.setSubject(subject);
        return thread;
    }

    @Override
    public List<ForumThread> getThreads(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable postTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT `id_topic` FROM `" + this.getDataManager().getPrefix() + "topics` ORDER BY `id_topic` ASC" +
                        limitstring));
        List<ForumThread> threads = new ArrayList<ForumThread>();
        for (int i = 0; postTable.getRowCount() > i; i++) {
            threads.add(getThread(Integer.parseInt(postTable.getModel().getValueAt(0, 0).toString())));
        }
        return threads;
    }

    @Override
    public void updateThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        String temp;
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("id_board", thread.getBoardID());
        data.put("id_first_msg", thread.getFirstPost().getID());
        data.put("id_last_msg", thread.getLastPost().getID());
        data.put("id_member_started", thread.getAuthor().getID());
        if (thread.isPoll()) {
            temp = "1";
        } else {
            temp = "0";
        }
        data.put("id_poll", temp);
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            if (thread.isSticky()) {
                temp = "1";
            } else {
                temp = "0";
            }
            data.put("issticky", temp);
            data.put("numreplies", thread.getRepliesCount());
            data.put("numviews", thread.getViewsCount());
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            if (thread.isSticky()) {
                temp = "1";
            } else {
                temp = "0";
            }
            data.put("is_sticky", temp);
            data.put("num_replies", thread.getRepliesCount());
            data.put("num_views", thread.getViewsCount());
        }
        if (thread.isLocked()) {
            temp = "1";
        } else {
            temp = "0";
        }
        data.put("locked", temp);
        this.getDataManager().updateFields(data, "topics", "`id_topic` = '" + thread.getID() + "'");
    }

    @Override
    public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        HashMap<String, Object> data = new HashMap<String, Object>();
        thread.setThreadDate(new Date());
        data.put("id_board", thread.getBoardID());
        data.put("id_member_started", thread.getAuthor().getID());
        data.put("id_member_updated", thread.getAuthor().getID());
        if (thread.isLocked()) {
            data.put("locked", "1");
        }
        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion()) && thread.isSticky()) {
            data.put("issticky", "1");
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion()) && thread.isSticky()) {
            data.put("is_sticky", "1");
        }
        this.getDataManager().insertFields(data, "topics");
        thread.setID(this.getDataManager().getLastID("id_topic", "topics"));

        ForumPost post = new ForumPost(this, thread.getID(), thread.getBoardID());
        post.setAuthor(thread.getAuthor());
        post.setBody(thread.getBody());
        post.setSubject(thread.getSubject());
        post.create();

        data = new HashMap<String, Object>();
        data.put("id_first_msg", post.getID());
        data.put("id_last_msg", post.getID());
        this.getDataManager().updateFields(data, "topics", "`id_topic` = '" + thread.getID() + "'");

        if (CraftCommons.inVersionRange(this.getVersionRanges()[0], this.getVersion())) {
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "boards" + "` SET `numtopics` =" +
                            " numtopics + 1 WHERE `id_board` = '" + post.getBoardID() + "'");
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "topics" + "` SET `numreplies` =" +
                            " '0' WHERE `id_topic` = '" + post.getThreadID() + "'");
        } else if (CraftCommons.inVersionRange(this.getVersionRanges()[1], this.getVersion())) {
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "boards" + "` SET `num_topics` =" +
                            " num_topics + 1 WHERE `id_board` = '" + post.getBoardID() + "'");
            this.getDataManager().executeQueryVoid(
                    "UPDATE `" + this.getDataManager().getPrefix() + "topics" + "` SET `num_replies` =" +
                            " '0' WHERE `id_topic` = '" + post.getThreadID() + "'");
        }
    }

    @Override
    public int getUserCount() {
        return this.getDataManager().getIntegerField("SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "members`");
    }

    @Override
    public int getGroupCount() {
        return this.getDataManager().getIntegerField(
                "SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "membergroups`");
    }

    @Override
    public List<String> getIPs(String username) {
        List<String> ips = new ArrayList<String>();
        ScriptUser user = getUser(username);
        ips.add(user.getLastIP());
        ips.add(user.getRegIP());
        return ips;
    }

    @Override
    public List<Ban> getBans(int limit) {
        String limitstring = "";
        if (limit > 0) {
            limitstring = " LIMIT 0 , " + limit;
        }
        JTable banTable = new JTable(this.getDataManager().resultSetToTableModel(
                "SELECT * FROM `" + this.getDataManager().getPrefix() + "ban_groups`" + limitstring));
        List<Ban> bans = new ArrayList<Ban>();
        for (int i = 0; banTable.getRowCount() > i; i++) {
            int banid = Integer.parseInt(banTable.getModel().getValueAt(i, 0).toString());
            String username = banTable.getModel().getValueAt(i, 1).toString();
            String email =
                    this.getDataManager().getStringField("SELECT `email_address` FROM `" + this.getDataManager().getPrefix() +
                            "ban_items` WHERE `id_ban_group` = '" + banid +
                            "' AND `email_address` != '' ORDER BY `id_ban` ASC LIMIT 1");
            JTable banIPtable = new JTable(this.getDataManager().resultSetToTableModel(
                    "SELECT `ip_low1`, `ip_high1`, `ip_low2`, `ip_high2`, `ip_low3`, `ip_high3`, `ip_low4`, " +
                            "`ip_high4` FROM `" +
                            this.getDataManager().getPrefix() + "ban_items` WHERE `id_ban_group` = '" + banid +
                            "' AND `ip_low1` != '0' ORDER BY `id_ban` ASC LIMIT 1"));
            String ip = null;
            if (banIPtable.getRowCount() == 1) {
                String ip1 =
                        banIPtable.getModel().getValueAt(0, 0) + "." + banIPtable.getModel().getValueAt(0, 2) + "." +
                                banIPtable.getModel().getValueAt(0, 4) + "." + banIPtable.getModel().getValueAt(0, 6);
                String ip2 =
                        banIPtable.getModel().getValueAt(0, 1) + "." + banIPtable.getModel().getValueAt(0, 3) + "." +
                                banIPtable.getModel().getValueAt(0, 5) + "." + banIPtable.getModel().getValueAt(0, 7);
                if (ip1.equalsIgnoreCase(ip2)) {
                    ip = ip1;
                } else {
                    ip = valuesToIP(ip1, ip2);
                }
            }
            String reason = banTable.getModel().getValueAt(i, 8).toString();
            String notes = banTable.getModel().getValueAt(i, 9).toString();
            int userid = this.getDataManager().getIntegerField(
                    "SELECT `id_member` FROM `" + this.getDataManager().getPrefix() + "ban_items` WHERE `id_ban_group` = " +
                            "'" +
                            banid + "' AND `id_member` != '0' ORDER BY `id_ban` ASC LIMIT 1");
            Date startdate = new Date(Long.parseLong(banTable.getModel().getValueAt(i, 2).toString()) * 1000);
            Date enddate;
            if (banTable.getModel().getValueAt(i, 3) == null) {
                enddate = null;
            } else {
                enddate = new Date(Long.parseLong(banTable.getModel().getValueAt(i, 3).toString()) * 1000);
            }
            Ban ban = new Ban(this, banid, username, email, ip);
            ban.setReason(reason);
            ban.setNotes(notes);
            ban.setUserID(userid);
            ban.setStartDate(startdate);
            ban.setEndDate(enddate);
            bans.add(ban);
        }
        return bans;
    }

    @Override
    public void updateBan(Ban ban) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", ban.getName());
        data.put("ban_time", ban.getStartDate().getTime() / 1000);
        if (ban.getEndDate() != null && (ban.getEndDate().getTime() / 1000) > 0) {
            data.put("expire_time", ban.getEndDate().getTime() / 1000);
        }
        data.put("reason", ban.getReason());
        data.put("notes", ban.getNotes());
        this.getDataManager().updateFields(data, "ban_groups", "`id_ban_group` = '" + ban.getID() + "'");
        if (ban.getIP() != null && ! ban.getIP().isEmpty()) {
            data = new HashMap<String, Object>();
            String[] values = ipValues(ban.getIP());
            data.put("ip_low1", values[0]);
            data.put("ip_high1", values[1]);
            data.put("ip_low2", values[2]);
            data.put("ip_high2", values[3]);
            data.put("ip_low3", values[4]);
            data.put("ip_high3", values[5]);
            data.put("ip_low4", values[6]);
            data.put("ip_high4", values[7]);
            this.getDataManager().updateFields(data, "ban_items",
                    "`id_ban_group` = '" + ban.getID() + "' AND `ip_low1` != '0'");
        }
        if (ban.getEmail() != null && ! ban.getEmail().isEmpty()) {
            data = new HashMap<String, Object>();
            data.put("email_address", ban.getEmail());
            this.getDataManager().updateFields(data, "ban_items",
                    "`id_ban_group` = '" + ban.getID() + "' AND `email_address` != ''");
        }
        if (ban.getUserID() > 0) {
            data = new HashMap<String, Object>();
            data.put("id_member", ban.getUserID());
            this.getDataManager().updateFields(data, "ban_items",
                    "`id_ban_group` = '" + ban.getID() + "' AND `id_member` != '0'");
        }
    }

    @Override
    public void addBan(Ban ban) throws SQLException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        ban.setStartDate(new Date());
        data.put("name", ban.getName());
        data.put("ban_time", ban.getStartDate().getTime() / 1000);
        if (ban.getEndDate() != null && (ban.getEndDate().getTime() / 1000) > 0) {
            data.put("expire_time", ban.getEndDate().getTime() / 1000);
        }
        data.put("reason", ban.getReason());
        data.put("notes", ban.getNotes());
        data.put("cannot_access", "1");
        this.getDataManager().insertFields(data, "ban_groups");
        ban.setID(this.getDataManager().getLastID("id_ban_group", "ban_groups"));
        if (ban.getIP() != null && ! ban.getIP().isEmpty()) {
            data = new HashMap<String, Object>();
            String[] values = ipValues(ban.getIP());
            data.put("id_ban_group", ban.getID());
            data.put("ip_low1", values[0]);
            data.put("ip_high1", values[1]);
            data.put("ip_low2", values[2]);
            data.put("ip_high2", values[3]);
            data.put("ip_low3", values[4]);
            data.put("ip_high3", values[5]);
            data.put("ip_low4", values[6]);
            data.put("ip_high4", values[7]);
            this.getDataManager().insertFields(data, "ban_items");
        }
        if (ban.getEmail() != null && ! ban.getEmail().isEmpty()) {
            data = new HashMap<String, Object>();
            data.put("id_ban_group", ban.getID());
            data.put("email_address", ban.getEmail());
            this.getDataManager().insertFields(data, "ban_items");
        }
        if (ban.getUserID() > 0) {
            data = new HashMap<String, Object>();
            data.put("id_ban_group", ban.getID());
            data.put("id_member", ban.getUserID());
            this.getDataManager().insertFields(data, "ban_items");
        }

        this.getDataManager().executeQueryVoid("UPDATE `" + this.getDataManager().getPrefix() + "settings" + "` SET `value` =" +
                " '" + (ban.getStartDate().getTime() / 1000) +
                "' WHERE `variable` = 'banLastUpdated'");
    }

    @Override
    public int getBanCount() {
        return this.getDataManager().getIntegerField(
                "SELECT COUNT(*) FROM `" + this.getDataManager().getPrefix() + "ban_groups`");
    }

    @Override
    public boolean isBanned(String string) {
        if (CraftCommons.isEmail(string)) {
            if (this.getDataManager().getStringField(
                    "SELECT `id_ban` FROM `" + this.getDataManager().getPrefix() + "ban_items` WHERE `email_address` = '" +
                            string + "'") != null) {
                return true;
            }
        } else if (CraftCommons.isIP(string)) {
            String[] values = ipValues(string);
            if (this.getDataManager().getStringField(
                    "SELECT `id_ban` FROM `" + this.getDataManager().getPrefix() + "ban_items` WHERE `ip_low1` = '" +
                            values[0] + "' AND `ip_high1` = '" + values[1] + "' AND `ip_low2` = '" + values[2] +
                            "' AND `ip_high2` = '" + values[3] + "' AND `ip_low3` = '" + values[4] + "' AND `ip_high3` = '" +
                            values[5] + "' AND `ip_low4` = '" + values[6] + "' AND `ip_high4` = '" + values[7] + "'") !=
                    null) {
                return true;
            }
        } else {
            if (this.getDataManager().getStringField(
                    "SELECT `id_ban_group` FROM `" + this.getDataManager().getPrefix() + "ban_groups` WHERE `name` = '" +
                            string + "'") != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRegistered(String username) {
        if (this.getDataManager().getStringField(
                "SELECT `" + this.membernamefield + "` FROM `" + this.getDataManager().getPrefix() + "members` WHERE `" +
                        this.membernamefield + "` = '" + username + "'") == null) {
            return true;
        }
        return false;
    }

    private String[] ipValues(String string) {
        String[] split = string.split("\\.");
        String[] values = new String[8];
        for (int i = 0; split.length > i; i++) {
            if (split[i].equals("*")) {
                values[i * 2] = "0";
                values[(i * 2) + 1] = "255";
            } else {
                values[i * 2] = split[i];
                values[(i * 2) + 1] = split[i];
            }
        }
        return values;
    }

    private String valuesToIP(String ip1, String ip2) {
        String[] split1 = ip1.split("\\.");
        String[] split2 = ip2.split("\\.");
        StringBuffer newIP = new StringBuffer();
        String delim = ".";
        for (int i = 0; split1.length > i; i++) {
            if (i == (split1.length - 1)) {
                delim = "";
            }
            if (split1[i].equalsIgnoreCase(split2[i])) {
                newIP.append(split1[i] + delim);
            } else {
                newIP.append("*" + delim);
            }
        }
        return newIP.toString();
    }
}