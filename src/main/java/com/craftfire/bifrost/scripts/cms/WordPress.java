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
package com.craftfire.bifrost.scripts.cms;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.classes.Version;
import com.craftfire.commons.classes.VersionRange;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.Results;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.cms.CMSArticle;
import com.craftfire.bifrost.classes.cms.CMSCategory;
import com.craftfire.bifrost.classes.cms.CMSComment;
import com.craftfire.bifrost.classes.cms.CMSScript;
import com.craftfire.bifrost.classes.cms.CMSUser;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.Gender;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.ScriptException;

/**
 * This class contains all the methods for WordPress.
 */
public class WordPress extends CMSScript {

    /**
     * Default constructor for Wordpress.
     *
     * @param script       the {@link Scripts} enum
     * @param version      the version of the script
     * @param dataManager  the {@link DataManager}
     */
    public WordPress(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
        setScriptName("wordpress");
        setShortName("wp");
        setVersionRanges(new VersionRange[] { new VersionRange("3.4.0", "3.4.2") });
    }

    //Start Generic Methods

    @Override
    public Version getLatestVersion() {
        return getVersionRanges()[0].getMax();
    }

    @Override
    public boolean authenticate(String username, String password) {
        String hash = this.getDataManager().getStringField("users", "user_pass", "`user_login` = '" + username + "'");
        return hash != null && hash.equals(hashPassword(hash, password));
    }

    @Override
    public String hashPassword(String salt, String password) {
        String hash = CraftCommons.encrypt(Encryption.PHPASS, password, salt);
        if (hash.startsWith("*")) {
            hash = CraftCommons.encrypt(CraftCommons.unixHashIdentify(salt), password, salt);
        }
        return hash;
    }

    @Override
    public String getUsername(int userid) {
        return this.getDataManager().getStringField("users", "user_login", "`ID` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        return this.getDataManager().getIntegerField("users", "ID", "`user_login` = '" + username + "'");
    }

    @Override
    public CMSUser getLastRegUser() throws ScriptException, SQLException {
        return this.getHandle().getUser(this.getDataManager().getIntegerField("SELECT `ID` FROM `" + this.getDataManager().getPrefix() + "users` ORDER BY `user_registered` DESC LIMIT 1"));
    }

    @Override
    public CMSUser getUser(String username) throws ScriptException, SQLException {
        return this.getHandle().getUser(getUserID(username));
    }

    @SuppressWarnings("unchecked")
    @Override
    public CMSUser getUser(int userid) throws SQLException, ScriptException {
        if (this.getDataManager().exist("users", "ID", userid)) {
            CMSUser user = new CMSUser(this, userid, null, null);
            Results res = this.getDataManager().getResults("SELECT * FROM `" + this.getDataManager().getPrefix() + "users` WHERE `ID` = '" + userid + "' LIMIT 1");
            if (res != null && res.getFirstResult() != null) {
                DataRow record = res.getFirstResult();
                String lastlogin;
                String activation = this.getDataManager().getStringField("usermeta", "meta_value", "`user_id` = '" + userid + "' AND `meta_key` = 'uae_user_activation_code'");
                user.setActivated(activation == null || activation.equalsIgnoreCase("active"));
                user.setEmail(record.getStringField("user_email"));
                user.setGender(Gender.UNKNOWN);
                user.setRegDate(record.getDateField("user_registered"));
                user.setPassword(record.getStringField("user_pass"));
                user.setUsername(record.getStringField("user_login"));
                user.setAvatarURL("http://www.gravatar.com/avatar/" + CraftCommons.encrypt(Encryption.MD5, record.getStringField("user_email").toLowerCase()));
                user.setFirstName(this.getDataManager().getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'first_name'"));
                user.setLastName(this.getDataManager().getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_name'"));
                user.setRealName(user.getFirstName() + user.getLastName());
                user.setNickname(this.getDataManager().getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'nickname'"));
                String capabilities = this.getDataManager().getStringField("usermeta", "meta_value", "`meta_key` = 'wp_capabilities' AND `user_id` = '" + userid + "'");
                Map<Object, Object> capmap = null;
                if (capabilities != null && !capabilities.isEmpty()) {
                    try {
                        Object temp = CraftCommons.getUtil().phpUnserialize(capabilities);
                        if (temp instanceof Map<?, ?>) {
                            capmap = (Map<Object, Object>) temp;
                        }
                    } catch (IllegalStateException ignore) {
                    }
                }
                if (capmap != null && !capmap.isEmpty()) {
                    user.setUserTitle(capmap.keySet().toArray()[0].toString());
                }
                lastlogin = this.getDataManager().getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_user_login'");
                if (!CraftCommons.isLong(lastlogin)) {
                    lastlogin = this.getDataManager().getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'wp-last-login'");
                }
                if (CraftCommons.isLong(lastlogin)) {
                    user.setLastLogin(new java.util.Date(Long.parseLong(lastlogin)));
                }
                return user;
            }
        }
        return null;
    }

    @Override
    public void updateUser(ScriptUser user) throws SQLException, ScriptException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user_login", user.getUsername());
        data.put("user_email", user.getEmail());
        if (user.getRegDate() != null) {
            data.put("user_registered", new java.sql.Date(user.getRegDate().getTime()));
        }
        if (CraftCommons.unixHashIdentify(user.getPassword()) == null) {
            user.setPassword(hashPassword(null, user.getPassword()));
            data.put("user_pass", user.getPassword());
        }
        this.getDataManager().updateFields(data, "users", "`ID` = '" + user.getID() + "'");
        data.clear();

        data.put("meta_value", user.getNickname());
        if (getDataManager().getCount("usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'nickname'") > 0) {
            this.getDataManager().updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'nickname'");
        } else {
            data.put("user_id", user.getID());
            data.put("meta_key", "nickname");
            getDataManager().insertFields(data, "usermeta");
        }
        data.put("meta_value", user.getFirstName());
        if (getDataManager().getCount("usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'first_name'") > 0) {
            this.getDataManager().updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'first_name'");
        } else {
            data.put("user_id", user.getID());
            data.put("meta_key", "first_name");
            getDataManager().insertFields(data, "usermeta");
        }
        data.put("meta_value", user.getLastName());
        if (getDataManager().getCount("usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_name'") > 0) {
            this.getDataManager().updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_name'");
        } else {
            data.put("user_id", user.getID());
            data.put("meta_key", "last_name");
            getDataManager().insertFields(data, "usermeta");
        }
        if (user.getLastLogin() != null) {
            data.put("meta_value", String.valueOf(user.getLastLogin().getTime()));
            this.getDataManager().updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_user_login'");
            this.getDataManager().updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'wp-last-login'");
        }
        data.clear();
        // TODO: Should we skip setting groups if no groups are cached?
        setUserGroups(user.getUsername(), user.getGroups());
    }

    @Override
    public void createUser(ScriptUser user) throws SQLException, ScriptException {
        HashMap<String, Object> data;
        if (CraftCommons.unixHashIdentify(user.getPassword()) == null) {
            user.setPassword(hashPassword(null, user.getPassword()));
        }
        if (user.getLastLogin() == null) {
            user.setLastLogin(new Date());
        }
        if (user.getRegDate() == null) {
            user.setRegDate(new Date());
        }
        data = new HashMap<String, Object>();
        data.put("user_login", user.getUsername());
        data.put("user_pass", user.getPassword());
        data.put("user_nicename", user.getUsername().toLowerCase());
        data.put("user_email", user.getEmail());
        data.put("user_registered", user.getRegDate());
        data.put("user_status", 0);
        data.put("display_name", user.getUsername());
        this.getDataManager().insertFields(data, "users");
        data.clear();
        user.setID(this.getDataManager().getLastID("ID", "users"));
        data.put("user_id", user.getID());
        data.put("meta_key", "nickname");
        data.put("meta_value", user.getNickname());
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "first_name");
        data.put("meta_value", user.getFirstName());
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "last_name");
        data.put("meta_value", user.getLastName());
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "rich_editing");
        data.put("meta_value", true);
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "comment_shortcuts");
        data.put("meta_value", false);
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "admin_color");
        data.put("meta_value", "fresh");
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "show_admin_bar_front");
        data.put("meta_value", true);
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "use_ssl");
        data.put("meta_value", 0);
        this.getDataManager().insertFields(data, "usermeta");
        data.put("meta_key", "default_password_nag");
        data.put("meta_value", 0);
        this.getDataManager().insertFields(data, "usermeta");
        setUserGroups(user.getUsername(), null);
    }

    @Override
    public List<Group> getGroups(int limit) throws ScriptException, SQLException {
        return getGroups(limit, false);
    }

    protected List<Group> getGroups(int limit, boolean namesonly) throws ScriptException, SQLException {
        List<Group> groups = new ArrayList<Group>();
        int newLimit = limit;
        if (newLimit > getGroupCount() | newLimit <= 0) {
            newLimit = getGroupCount();
        }

        // GroupID 0 might be used for none group if needed.
        for (int i = 1; i <= newLimit; ++i) {
            if (namesonly) {
                groups.add(getGroup(i, true));
            } else {
                groups.add(this.getHandle().getGroup(i));
            }
        }
        return groups;
    }

    @Override
    public int getGroupID(String group) throws ScriptException, SQLException {
        List<Group> groups = getGroups(0, true);
        for (Group grp : groups) {
            if (grp.getName().equalsIgnoreCase(group)) {
                return grp.getID();
            }
        }
        return 0;
    }

    @Override
    public Group getGroup(int groupid) throws ScriptException, SQLException {
        return getGroup(groupid, false);
    }

    @SuppressWarnings("unchecked")
    protected Group getGroup(int groupid, boolean namesonly) throws ScriptException, SQLException {
        String groupname = "";
        Group group;
        switch (groupid) {
        case 1:
            groupname = "Subscriber";
            break;
        case 2:
            groupname = "Contributor";
            break;
        case 3:
            groupname = "Author";
            break;
        case 4:
            groupname = "Editor";
            break;
        case 5:
            groupname = "Administrator";
            break;
        case 6:
            groupname = "Super Admin";
            break;
        default:
            return null;
        }
        group = new Group(this, groupid, groupname);
        if (namesonly) {
            return group;
        }
        List<ScriptUser> userlist = new ArrayList<ScriptUser>();
        if (groupid == 6) {
            if (this.getDataManager().exist("sitemeta", "meta_key", "site_admins")) {
                String admins = this.getDataManager().getStringField("sitemeta", "meta_value", "`meta_key` = 'site_admins'");
                Map<Object, String> adminmap = null;
                try {
                    adminmap = (Map<Object, String>) CraftCommons.getUtil().phpUnserialize(admins);
                } catch (IllegalStateException e) {
                    return null;
                } catch (ClassCastException e) {
                    return null;
                }
                Iterator<String> iterator = adminmap.values().iterator();
                while (iterator.hasNext()) {
                    userlist.add(this.getHandle().getUser(iterator.next()));
                }
            } else {
                return null;
            }
        } else {
            Results results = this.getDataManager().getResults("SELECT `meta_value`, `user_id` FROM `" + this.getDataManager().getPrefix() + "usermeta` WHERE `meta_key` = 'wp_capabilities'");
            List<DataRow> records = results.getArray();
            Iterator<DataRow> iterator = records.iterator();
            while (iterator.hasNext()) {
                DataRow d = iterator.next();
                String capabilities = d.getStringField("meta_value");
                int userid = d.getIntField("user_id");
                Map<String, String> capmap = null;
                try {
                    capmap = (Map<String, String>) CraftCommons.getUtil().phpUnserialize(capabilities);
                } catch (IllegalStateException e) {
                    continue;
                } catch (ClassCastException e) {
                    continue;
                }
                String gname = groupname.toLowerCase();
                if (capmap.containsKey(gname) && capmap.get(gname).equals("1")) {
                    userlist.add(this.getHandle().getUser(userid));
                }
            }
            group.setUsers(userlist);
            group.setUserCount(userlist.size());
        }
        return group;
    }

    @Override
    public Group getGroup(String group) throws ScriptException, SQLException {
        return getGroup(getGroupID(group));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> getUserGroups(String username) throws SQLException, ScriptException {
        int userid = this.getHandle().getUserID(username);
        String capabilities = this.getDataManager().getStringField("usermeta", "meta_value", "`meta_key` = 'wp_capabilities' AND `user_id` = '" + userid + "'");
        Map<Object, Object> capmap = null;
        if (capabilities != null && !capabilities.isEmpty()) {
            try {
                capmap = (Map<Object, Object>) CraftCommons.getUtil().phpUnserialize(capabilities);
            } catch (IllegalStateException ignore) {
            } catch (ClassCastException ignore) {
            }
        }
        List<Group> allGroups = this.getHandle().getGroups(0);
        List<Group> uGroups = new ArrayList<Group>();
        Iterator<Group> iterator = allGroups.iterator();
        while (iterator.hasNext()) {
            Group g = iterator.next();
            if (g.getID() == 6) {
                if (this.getDataManager().exist("sitemeta", "meta_key", "site_admins")) {
                    String admins = this.getDataManager().getStringField("sitemeta", "meta_value", "`meta_key` = 'site_admins'");
                    Map<Object, Object> adminmap = null;
                    Object temp = null;
                    try {
                        temp = CraftCommons.getUtil().phpUnserialize(admins);
                    } catch (IllegalStateException ignore) {
                    }
                    if (temp instanceof Map<?, ?>) {
                        adminmap = (Map<Object, Object>) temp;
                        if (adminmap.containsValue(username)) {
                            uGroups.add(g);
                        }
                    }
                }
            } else if (capmap != null) {
                String gname = g.getName().toLowerCase();
                if (capmap.containsKey(gname) && capmap.get(gname).equals("1")) {
                    uGroups.add(g);
                }
            }
        }
        return uGroups;
    }

    protected void setUserGroups(String username, List<Group> groups) throws SQLException, ScriptException {
        List<Group> newGroups = groups;
        if (newGroups == null) {
            newGroups = new ArrayList<Group>();
            Group defaultGroup = this.getHandle().getGroup(this.getDataManager().getStringField("options", "option_value", "`option_name` = 'default_role'"));
            newGroups.add(defaultGroup);
        }
        int userid = this.getUserID(username);
        Map<String, String> capmap = new HashMap<String, String>();
        int userLevel = 0;
        List<String> adminlist = null;
        if (this.getDataManager().exist("sitemeta", "meta_key", "site_admins")) {
            String admins = this.getDataManager().getStringField("sitemeta", "meta_value", "`meta_key` = 'site_admins'");
            Object temp = null;
            try {
                temp = CraftCommons.getUtil().phpUnserialize(admins);
            } catch (IllegalStateException ignore) {
            }
            if (temp instanceof Map<?, ?>) {
                @SuppressWarnings("unchecked")
                Map<Object, String> adminmap = (Map<Object, String>) temp;
                adminlist = new ArrayList<String>();
                Iterator<Object> iterator1 = adminmap.keySet().iterator();
                while (iterator1.hasNext()) {
                    adminlist.add(adminmap.get(iterator1.next()));
                }
                adminlist.remove(username);
            }
        }
        Iterator<Group> iterator = newGroups.iterator();
        while (iterator.hasNext()) {
            Group g = iterator.next();
            if (g.getID() == 6) {
                if (adminlist != null) {
                    adminlist.add(username);
                }
            } else {
                String gname = g.getName().toLowerCase();
                capmap.put(gname, "1");
                int level = -1;
                switch (g.getID()) {
                case 1:
                    level = 0;
                    break;
                case 2:
                    level = 1;
                    break;
                case 3:
                    level = 2;
                    break;
                case 4:
                    level = 7;
                    break;
                case 5:
                    level = 10;
                    break;
                }
                if (level > userLevel) {
                    userLevel = level;
                }
            }
        }
        String capabilities = CraftCommons.getUtil().phpSerialize(capmap);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("meta_value", capabilities);
        if (this.getDataManager().getCount("usermeta", "`meta_key` = 'wp_capabilities' AND `user_id` = '" + userid + "'") > 0) {
            this.getDataManager().updateFields(data, "usermeta", "`meta_key` = 'wp_capabilities' AND `user_id` = '" + userid + "'");
        } else {
            data.put("meta_key", "wp_capabilities");
            data.put("user_id", userid);
            this.getDataManager().insertFields(data, "usermeta");
        }
        data.clear();
        data.put("meta_value", userLevel);
        if (this.getDataManager().getCount("usermeta", "`meta_key` = 'wp_user_level' AND `user_id` = '" + userid + "'") > 0) {
            this.getDataManager().updateFields(data, "usermeta", "`meta_key` = 'wp_user_level' AND `user_id` = '" + userid + "'");
        } else {
            data.put("meta_key", "wp_user_level");
            data.put("user_id", userid);
            this.getDataManager().insertFields(data, "usermeta");
        }
        data.clear();
        if (adminlist != null) {
            String admins = CraftCommons.getUtil().phpSerialize(adminlist);
            data.put("meta_value", admins);
            this.getDataManager().updateFields(data, "sitemeta", "`meta_key` = 'site_admins'");
        }
        iterator = newGroups.iterator();
        while (iterator.hasNext()) {
            Group.cleanupCache(this.getHandle(), iterator.next(), CacheCleanupReason.UPDATE);
        }
    }


    @Override
    public void updateGroup(Group group) throws ScriptException, SQLException {
        if (!getGroup(group.getID(), true).getName().equalsIgnoreCase(group.getName())) {
            throw new ScriptException("The script doesn't support changing group names or IDs.");
        }
        if (group.getID() == 6) {
            if (this.getDataManager().exist("sitemeta", "meta_key", "site_admins")) {
                List<String> adminlist = new ArrayList<String>();
                Iterator<ScriptUser> iterator = group.getUsers().iterator();
                while (iterator.hasNext()) {
                    adminlist.add(iterator.next().getUsername());
                }
                String admins = CraftCommons.getUtil().phpSerialize(adminlist);
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("meta_value", admins);
                this.getDataManager().updateFields(data, "sitemeta", "`meta_key` = 'site_admins'");
            }
        } else {
            List<ScriptUser> oldUsers = new ArrayList<ScriptUser>(getGroup(group.getID()).getUsers());
            List<ScriptUser> newUsers = new ArrayList<ScriptUser>(group.getUsers());
            List<ScriptUser> unchangedUsers = new ArrayList<ScriptUser>(oldUsers);
            unchangedUsers.retainAll(newUsers);
            oldUsers.removeAll(unchangedUsers);
            newUsers.removeAll(unchangedUsers);
            Iterator<ScriptUser> iOld = oldUsers.iterator();
            while (iOld.hasNext()) {
                ScriptUser u = iOld.next();
                List<Group> groups = this.getHandle().getUserGroups(u.getUsername());
                groups.remove(group);
                setUserGroups(u.getUsername(), groups);
            }
            Iterator<ScriptUser> iNew = newUsers.iterator();
            while (iNew.hasNext()) {
                ScriptUser u = iNew.next();
                List<Group> groups = this.getHandle().getUserGroups(u.getUsername());
                groups.add(group);
                setUserGroups(u.getUsername(), groups);
            }
        }
    }

    @Override
    public int getUserCount() {
        return this.getDataManager().getCount("users");
    }

    @Override
    public int getGroupCount() {
        /*
         * 6 WordPress roles: Subscriber, Contributor, Author, Editor, Administrator, Super Admin
         */
        if (this.getDataManager().exist("sitemeta", "meta_key", "site_admins")) {
            // Super Admin doesn't always exist.
            return 6;
        } else {
            return 5;
        }
    }

    @Override
    public String getHomeURL() {
        return this.getDataManager().getStringField("options", "option_value", "`option_name` = 'siteurl'");
    }

    @Override
    public boolean isRegistered(String username) {
        return this.getDataManager().exist("users", "user_login", username);
    }

    //End Generic Script Methods

    //Start CMS Script Methods

    @Override
    public CMSComment getComment(int commentid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<CMSComment> getComments(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCommentTotalCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSComment> getCommentsOnArticle(int articleid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCommentCount(int articleid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSComment> getCommentReplies(int commentid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCommentReplyCount(int commentid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSComment> getUserComments(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getUserCommentCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public CMSComment getLastComment() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSComment getLastUserComment(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSComment getLastCommentOnArticle(int articleid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateComment(CMSComment comment) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createComment(CMSComment comment) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public CMSArticle getArticle(int articleid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<CMSArticle> getArticles(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getArticleTotalCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSArticle> getArticlesFromCategory(int catid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getArticleCount(int catid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSArticle> getUserArticles(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getUserArticleCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public CMSArticle getLastArticle() {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSArticle getLastUserArticle(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public CMSArticle getLastArticleFromCategory(int catid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateArticle(CMSArticle article) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createArticle(CMSArticle article) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public CMSCategory getCategory(int catid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<CMSCategory> getCategories(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getCategoryCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public List<CMSCategory> getSubCategories(int catid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getSubCategoryCount(int catid) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public void updateCategory(CMSCategory category) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createCategory(CMSCategory category) {
        /* TODO: Delete this method or implement it */
    }

    //End CMS Script Methods
}
