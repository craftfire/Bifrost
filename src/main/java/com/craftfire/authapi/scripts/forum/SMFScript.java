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

import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;

import com.craftfire.authapi.ScriptAPI;
import com.craftfire.authapi.ScriptAPI.Scripts;
import com.craftfire.authapi.classes.Ban;
import com.craftfire.authapi.classes.Group;
import com.craftfire.authapi.classes.Post;
import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.classes.Thread;
import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.DataManager;

public class SMFScript extends Script {
    private final String scriptName = "simplemachines";
    private final String shortName = "smf";
    private final String encryption = "sha1";
    private final String[] versionRanges = {"1.0-1.1.16", "2.0-2.0.2"};
    private final String userVersion;
    private final DataManager dataManager;

    public SMFScript(Scripts script, String version, DataManager dataManager) {
        super(script, version);
        this.userVersion = version;
        this.dataManager = dataManager;
    }

    public String getLatestVersion() {
        return this.versionRanges[1];
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
        return hashPassword(username, password).equals(getPasswordHash(username));
    }

    public String hashPassword(String username, String password) {
        return CraftCommons.sha1(username.toLowerCase() + password);
    }

    public Date getRegDate(String username) {
        return new Date(this.dataManager.getIntegerField("SELECT date_registered FROM " + this.dataManager.getPrefix() + "members WHERE member_name = '" + username + "'") * 1000);
    }

    public void setRegDate(String username, Date date) {
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `date_registered` = '" + (date.getTime() / 1000) + "' WHERE `member_name` = '" + username + "'");
    }

    public Date getLastLogin(String username) {
        return new Date(this.dataManager.getIntegerField("SELECT last_login FROM " + this.dataManager.getPrefix() + "members WHERE member_name = '" + username + "'") * 1000);
    }

    public void setLastLogin(String username, Date date) {
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `last_login` = '" + (date.getTime() / 1000) + "' WHERE `member_name` = '" + username + "'");
    }

    public ScriptAPI.AccountStatus getAccountStatus(String username) {
        return null;
    }

    public void setAccountStatus(String username, ScriptAPI.AccountStatus status) {
    }

    public String getUserName(int userid) {
        return this.dataManager.getStringField("SELECT `member_name` FROM `" + this.dataManager.getPrefix() + "members` WHERE `id_member` = '" + userid + "'");
    }

    public int getUserId(String username) {
        return this.dataManager.getIntegerField("SELECT `id_member` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public void setUserName(String username, String newUsername) {
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `member_name` = '" + newUsername + "' WHERE `member_name` = '" + username + "'");
    }

    public String getUserTitle(String username) {
        return this.dataManager.getStringField("SELECT `usertitle` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public void setUserTitle(String username, String title) {
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `usertitle` = '" + title + "' WHERE `member_name` = '" + username + "'");
    }

    public String getNickName(String username) {
        return this.dataManager.getStringField("SELECT `real_name` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public List<Group> getGroups(String username) {
        return null;
    }

    public void setGroups(List<Group> groups) {
    }

    public String getEmail(String username) {
        return this.dataManager.getStringField("SELECT `email_address` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public void setEmail(String username, String email) {
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `email_address` = '" + email + "' WHERE `member_name` = '" + username + "'");
    }

    public void setPassword(String username, String password) {
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `passwd` = '" + hashPassword(username, password) + "' WHERE `member_name` = '" + username + "'");
    }

    public String getPasswordHash(String username) {
        return this.dataManager.getStringField("SELECT `passwd` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public Date getBirthday(String username) {
        return this.dataManager.getDateField("SELECT `birthdate` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public void setBirthday(String username, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = format.format(date);
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `birthdate` = '" + birthday + "' WHERE `member_name` = '" + username + "'");
    }

    public ScriptAPI.Gender getGender(String username) {
        int gender = this.dataManager.getIntegerField("SELECT `gender` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
        if (gender == 0) {
            return ScriptAPI.Gender.MALE;
        } else if (gender == 1) {
            return ScriptAPI.Gender.FEMALE;
        } else {
            return ScriptAPI.Gender.UNKNOWN;
        }
    }

    public void setGender(String username, ScriptAPI.Gender gender) {
        int genderID;
        if (gender == ScriptAPI.Gender.MALE) {
            genderID = 0;
        } else if (gender == ScriptAPI.Gender.FEMALE) {
            genderID = 1;
        } else {
            genderID = 0;
        }
        this.dataManager.executeSQLquery("UPDATE `" + this.dataManager.getPrefix() + "members`" + "SET `gender` = '" + genderID + "' WHERE `member_name` = '" + username + "'");
    }

    public int getTotalPostCount() {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() + "messages`");
    }

    public int getPostCount(String username) {
        return this.dataManager.getIntegerField("SELECT `posts` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public Post getLastUserPost(String username) {
        int userid = getUserId(username);
        JTable postTable = new JTable(this.dataManager.resultSetToTableModel("SELECT * FROM `" + this.dataManager.getPrefix() + "messages` WHERE `id_member` = '" + userid + "' ORDER BY `id_msg` ASC LIMIT 1"));
        int postid = 0, threadid = 0, boardid = 0, authorid = 0;
        Date postdate = null;
        String author = null, authoremail = null, authorip = null, subject = null, body = null;
        if (postTable.getRowCount() == 1) {
            postid = Integer.parseInt(postTable.getModel().getValueAt(0, 0).toString());
            threadid = Integer.parseInt(postTable.getModel().getValueAt(0, 1).toString());
            boardid = Integer.parseInt(postTable.getModel().getValueAt(0, 2).toString());
            postdate = new Date(Long.parseLong(postTable.getModel().getValueAt(0, 3).toString()) * 1000);
            authorid = Integer.parseInt(postTable.getModel().getValueAt(0, 4).toString());
            author = postTable.getModel().getValueAt(0, 7).toString();
            authoremail = postTable.getModel().getValueAt(0, 8).toString();
            authorip = postTable.getModel().getValueAt(0, 9).toString();
            subject = postTable.getModel().getValueAt(0, 6).toString();
            body = postTable.getModel().getValueAt(0, 13).toString();
        }
        return new Post(postid, threadid, boardid, postdate, authorid, author, authoremail, authorip, subject, body);

    }

    public Post getLastPost() {
        JTable postTable = new JTable(this.dataManager.resultSetToTableModel("SELECT * FROM `" + this.dataManager.getPrefix() + "messages` ORDER BY `id_msg` ASC LIMIT 1"));
        int postid = 0, threadid = 0, boardid = 0, authorid = 0;
        Date postdate = null;
        String author = null, authoremail = null, authorip = null, subject = null, body = null;
        if (postTable.getRowCount() == 1) {
            postid = Integer.parseInt(postTable.getModel().getValueAt(0, 0).toString());
            threadid = Integer.parseInt(postTable.getModel().getValueAt(0, 1).toString());
            boardid = Integer.parseInt(postTable.getModel().getValueAt(0, 2).toString());
            postdate = new Date(Long.parseLong(postTable.getModel().getValueAt(0, 3).toString()) * 1000);
            authorid = Integer.parseInt(postTable.getModel().getValueAt(0, 4).toString());
            author = postTable.getModel().getValueAt(0, 7).toString();
            authoremail = postTable.getModel().getValueAt(0, 8).toString();
            authorip = postTable.getModel().getValueAt(0, 9).toString();
            subject = postTable.getModel().getValueAt(0, 6).toString();
            body = postTable.getModel().getValueAt(0, 13).toString();
        }
        return new Post(postid, threadid, boardid, postdate, authorid, author, authoremail, authorip, subject, body);
    }

    public List<Post> getPosts(int amount) {
        JTable postTable = new JTable(this.dataManager.resultSetToTableModel("SELECT * FROM `" + this.dataManager.getPrefix() + "messages` ORDER BY `id_msg` ASC LIMIT 0 , " + (amount - 1)));
        List<Post> posts = new ArrayList<Post>();
        for (int i=0; postTable.getRowCount() > i; i++) {
            int postid = 0, threadid = 0, boardid = 0, authorid = 0;
            Date postdate = null;
            String author = null, authoremail = null, authorip = null, subject = null, body = null;
            if (postTable.getRowCount() == 1) {
                postid = Integer.parseInt(postTable.getModel().getValueAt(0, 0).toString());
                threadid = Integer.parseInt(postTable.getModel().getValueAt(0, 1).toString());
                boardid = Integer.parseInt(postTable.getModel().getValueAt(0, 2).toString());
                postdate = new Date(Long.parseLong(postTable.getModel().getValueAt(0, 3).toString()) * 1000);
                authorid = Integer.parseInt(postTable.getModel().getValueAt(0, 4).toString());
                author = postTable.getModel().getValueAt(0, 7).toString();
                authoremail = postTable.getModel().getValueAt(0, 8).toString();
                authorip = postTable.getModel().getValueAt(0, 9).toString();
                subject = postTable.getModel().getValueAt(0, 6).toString();
                body = postTable.getModel().getValueAt(0, 13).toString();
            }
            posts.add(new Post(postid, threadid, boardid, postdate, authorid, author, authoremail, authorip, subject, body));
        }
        return posts;
    }

    public int getTotalThreadCount() {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() + "topics`");
    }

    public int getThreadCount(String username) {
        int userid = getUserId(username);
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() + "ban_groups` WHERE `id_member_started` = '" + userid + "'");
    }

    public Thread getLastThread() {
        return null;
    }

    public Thread getLastUserThread(String username) {
        return null;
    }

    public List<Thread> getThreads() {
        return null;
    }

    public int getUserCount() {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() + "members`");
    }

    public int getGroupCount() {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() + "membersgroups`");
    }

    public Image getAvatar(String username) {
        String avatar = getAvatarURL(username);
        try {
            URL url = new URL(avatar);
            return Toolkit.getDefaultToolkit().getDefaultToolkit().createImage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAvatarURL(String username) {
        return this.dataManager.getStringField("SELECT `avatar` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
    }

    public List<String> getIPs(String username) {
        return null;
    }

    public String getRegIP(String username) {
        return null;
    }

    public String getLastIP(String username) {
        return null;
    }

    public List<Ban> getBans() {
        JTable banTable = new JTable(this.dataManager.resultSetToTableModel("SELECT * FROM `" + this.dataManager.getPrefix() + "ban_groups`"));
        List<Ban> bans = new ArrayList<Ban>();
        for(int i=0; banTable.getRowCount() > i; i++) {
            String id = banTable.getModel().getValueAt(i, 0).toString();
            String username = banTable.getModel().getValueAt(i, 1).toString();
            String email = this.dataManager.getStringField("SELECT `email_address` FROM `" + this.dataManager.getPrefix() + "ban_items` WHERE `id_ban_group` = '" + id + "' AND `email_address` != '' ORDER BY `id_ban` ASC LIMIT 1");
            JTable banIPtable = new JTable(this.dataManager.resultSetToTableModel("SELECT `ip_low1`, `ip_high1`, `ip_low2`, `ip_high2`, `ip_low3`, `ip_high3`, `ip_low4`, `ip_high4` FROM `" + this.dataManager.getPrefix() + "ban_items` WHERE `id_ban_group` = '" + id + "' AND `ip_low1` != '0' ORDER BY `id_ban` ASC LIMIT 1"));
            String ip = null;
            if (banIPtable.getRowCount() == 1) {
                String ip1 = banIPtable.getModel().getValueAt(0, 0) + "." + banIPtable.getModel().getValueAt(0, 2) + "." + banIPtable.getModel().getValueAt(0, 4) + "." + banIPtable.getModel().getValueAt(0, 6);
                String ip2 = banIPtable.getModel().getValueAt(0, 1) + "." + banIPtable.getModel().getValueAt(0, 3) + "." + banIPtable.getModel().getValueAt(0, 5) + "." + banIPtable.getModel().getValueAt(0, 7);
                if (ip1.equalsIgnoreCase(ip2)) {
                    ip = ip1;
                }   else {
                    ip = valuesToIP(ip1, ip2);
                }
            }
            String reason = banTable.getModel().getValueAt(i, 8).toString();
            String notes = banTable.getModel().getValueAt(i, 9).toString();
            int userid = this.dataManager.getIntegerField("SELECT `id_member` FROM `" + this.dataManager.getPrefix() + "ban_items` WHERE `id_ban_group` = '" + id + "' AND `id_member` != '0' ORDER BY `id_ban` ASC LIMIT 1");
            Date startdate = new Date(Long.parseLong(banTable.getModel().getValueAt(i, 2).toString()) * 1000);
            Date enddate;
            if (banTable.getModel().getValueAt(i, 3) == null) {
                enddate = null;
            } else {
                enddate = new Date(Long.parseLong(banTable.getModel().getValueAt(i, 3).toString()) * 1000);
            }
            bans.add(new Ban(username, email, ip, reason, notes, userid, startdate, enddate));
        }
        return bans;
    }

    public int getBanCount() {
        return this.dataManager.getIntegerField("SELECT COUNT(*) FROM `" + this.dataManager.getPrefix() + "ban_groups`");
    }

    public boolean isBanned(String string) {
        if (CraftCommons.isEmail(string)) {
            if (this.dataManager.getStringField("SELECT `id_ban` FROM `" + this.dataManager.getPrefix() + "ban_items` WHERE `email_address` = '" + string + "'") != null) {
                return true;
            }
        } else if (CraftCommons.isIP(string)) {
            String[] values = ipValues(string);
            if (this.dataManager.getStringField("SELECT `id_ban` FROM `" + this.dataManager.getPrefix() + "ban_items` WHERE `ip_low1` = '" + values[0] + "' AND `ip_high1` = '" + values[1]
                                                + "' AND `ip_low2` = '" + values[2] + "' AND `ip_high2` = '" + values[3]
                                                + "' AND `ip_low3` = '" + values[4] + "' AND `ip_high3` = '" + values[5]
                                                + "' AND `ip_low4` = '" + values[6] + "' AND `ip_high4` = '" + values[7] + "'") != null) {
                return true;
            }
        } else {
            if (this.dataManager.getStringField("SELECT `id_ban_group` FROM `" + this.dataManager.getPrefix() + "ban_groups` WHERE `name` = '" + string + "'") != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isActivated(String username) {
        int activated = this.dataManager.getIntegerField("SELECT `is_activated` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'");
        if (activated == 1) {
            return true;
        }
        return false;
    }

    public boolean isRegistered(String username) {
        if (this.dataManager.getStringField("SELECT `member_name` FROM `" + this.dataManager.getPrefix() + "members` WHERE `member_name` = '" + username + "'") == null) {
            return true;
        }
        return false;
    }

    private String[] ipValues(String string) {
        String[] split = string.split("\\.");
        String[] values = new String[8];
        for (int i=0; split.length>i; i++) {
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
        for (int i=0; split1.length>i; i++) {
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