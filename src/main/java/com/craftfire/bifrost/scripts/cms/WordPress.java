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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.Results;
import com.craftfire.commons.enums.Encryption;
import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.cms.CMSArticle;
import com.craftfire.bifrost.classes.cms.CMSCategory;
import com.craftfire.bifrost.classes.cms.CMSComment;
import com.craftfire.bifrost.classes.cms.CMSUser;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.Gender;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.CMSHandle;
import com.craftfire.bifrost.script.CMSScript;

/**
 * This class contains all the methods for WordPress.
 */
public class WordPress extends CMSScript {
    private final String scriptName = "wordpress";
    private final String shortName = "wp";
    private final Encryption encryption = Encryption.PHPASS;
    private final String[] versionRanges = { "3.4.0", "3.4.1" }; // TODO: Does it work with other versions?
    private DataManager dataManager;
    private CMSHandle handle;
    private boolean init;

    public WordPress(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
        this.dataManager = dataManager;
    }

    //Start Generic Methods

    // FIXME: Workaround to get references to some objects that are not
    // obtainable in constructor.
    public void init() {
        if (this.init) {
            return;
        }
        this.handle = Bifrost.getInstance().getScriptAPI().getCMSHandle(Scripts.WP);
        this.init = true;
    }

    @Override
    public String[] getVersionRanges() {
        return this.versionRanges;
    }

    @Override
    public String getLatestVersion() {
        /* TODO: Delete this method or implement it */
        return null;
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
        init();
        String hash = this.dataManager.getStringField("users", "user_pass", "`user_login` = '" + username + "'");
        if (hash == null) {
            return false;
        }
        return hashPassword(hash, password).equals(hash);
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
        init();
        return this.dataManager.getStringField("users", "user_login", "`ID` = '" + userid + "'");
    }

    @Override
    public int getUserID(String username) {
        init();
        return this.dataManager.getIntegerField("users", "ID", "`user_login` = '" + username + "'");
    }

    @Override
    public CMSUser getLastRegUser() throws UnsupportedMethod, SQLException {
        init();
        return this.handle.getUser(this.dataManager.getIntegerField("SELEC `ID` FROM `" + this.dataManager.getPrefix() + "users` ORDER BY `user_registered` LIMIT 1"));
    }

    @Override
    public CMSUser getUser(String username) throws UnsupportedMethod, SQLException {
        init();
        return this.handle.getUser(getUserID(username));
    }

    @Override
    public CMSUser getUser(int userid) throws SQLException {
        init();
        if (this.dataManager.exist("users", "ID", userid)) {
            CMSUser user = new CMSUser(this, userid, null, null);
            Results res = this.dataManager.getResults("SELECT * FROM `" + this.dataManager.getPrefix() + "users` WHERE `ID` = '" + userid + "' LIMIT 1");
            if (res != null && res.getFirstResult() != null) {
                DataRow record = res.getFirstResult();
                String lastlogin;
                String activation = this.dataManager.getStringField("usermeta", "meta_value", "`user_id` = '" + userid + "' AND `meta_key` = 'uae_user_activation_code'");
                if (activation == null || activation.equalsIgnoreCase("active")) {
                    user.setActivated(true);
                } else {
                    user.setActivated(false);
                }
                user.setEmail(record.getStringField("user_email"));
                user.setGender(Gender.UNKNOWN);
                user.setRegDate(record.getDateField("user_registered"));
                user.setPassword(record.getStringField("user_pass"));
                user.setUsername(record.getStringField("user_login"));
                user.setAvatarURL("http://www.gravatar.com/avatar/" + CraftCommons.encrypt(Encryption.MD5, record.getStringField("user_email").toLowerCase()));
                user.setFirstName(this.dataManager.getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'first_name'"));
                user.setLastName(this.dataManager.getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_name'"));
                user.setRealName(user.getFirstName() + user.getLastName());
                user.setNickname(this.dataManager.getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'nickname'"));
                lastlogin = this.dataManager.getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_user_login'");
                if (!CraftCommons.isLong(lastlogin)) {
                    lastlogin = this.dataManager.getStringField("usermeta", "meta_value", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'wp-last-login'");
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
    public void updateUser(ScriptUser user) throws SQLException {
        init();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user_login", user.getUsername());
        data.put("user_email", user.getEmail());
        if (user.getRegDate() != null) {
            // BUG: Resets the date to zero.
            data.put("user_registered", new java.sql.Date(user.getRegDate().getTime()));
        }
        if (CraftCommons.unixHashIdentify(user.getPassword()) == null) {
            user.setPassword(hashPassword(null, user.getPassword()));
            data.put("user_pass", user.getPassword());
        }
        this.dataManager.updateFields(data, "users", "`ID` = '" + user.getID() + "'");
        data.clear();

        data.put("meta_value", user.getNickname());
        this.dataManager.updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'nickname'");
        data.put("meta_value", user.getFirstName());
        this.dataManager.updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'first_name'");
        data.put("meta_value", user.getLastName());
        this.dataManager.updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_name'");
        if (user.getLastLogin() != null) {
            data.put("meta_value", String.valueOf(user.getLastLogin().getTime()));
            this.dataManager.updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'last_user_login'");
            this.dataManager.updateFields(data, "usermeta", "`user_id` = '" + user.getID() + "' AND `meta_key` = 'wp-last-login'");
        }
        data.clear();
        // TODO: Should we skip setting groups if no groups are cached?
        try {
            setUserGroups(user.getUsername(), user.getGroups());
        } catch (UnsupportedMethod e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(ScriptUser user) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public List<Group> getGroups(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getGroupID(String group) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public Group getGroup(int groupid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public Group getGroup(String group) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<Group> getUserGroups(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    public void setUserGroups(String username, List<Group> groups)
            throws SQLException {
        init();
        int userid = this.getUserID(username);
        Map<String, String> capmap = new HashMap<String, String>();
        List<String> adminlist = null;
        if (this.dataManager.exist("sitemeta", "meta_key", "site_admins")) {
            String admins = this.dataManager.getStringField("sitemeta", "meta_value", "`meta_key` = 'site_admins'");
            Object temp = CraftCommons.getUtil().phpUnserialize(admins);
            if (temp instanceof Map<?,?>) {
                @SuppressWarnings("unchecked")
                Map<Object, String> adminmap = (Map<Object, String>) temp;
                adminlist = new ArrayList<String>();
                Iterator<Object> I1 = adminmap.keySet().iterator();
                while (I1.hasNext()) {
                    adminlist.add(adminmap.get(I1.next()));
                }
                adminlist.remove(username);
            }
        }
        Iterator<Group> I = groups.iterator();
        while (I.hasNext()) {
            Group g = I.next();
            if (g.getID() == 6) {
                if (adminlist != null) {
                    adminlist.add(username);
                }
            } else {
                String gname = g.getName().toLowerCase();
                capmap.put(gname, "1");
            }
        }
        String capabilities = CraftCommons.getUtil().phpSerialize(capmap);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("meta_value", capabilities);
        this.dataManager.updateFields(data, "usermeta", "`meta_key` = 'wp_capabilities' AND `user_id` = '" + userid + "'");
        if (adminlist != null) {
            String admins = CraftCommons.getUtil().phpSerialize(adminlist);
            data.put("meta_value", admins);
            this.dataManager.updateFields(data, "sitemeta", "`meta_key` = 'site_admins'");
        }
        I = groups.iterator();
        while (I.hasNext()) {
            Group.cleanupCache(this.handle, I.next(), CacheCleanupReason.UPDATE);
            // TODO: Isn't it already done in all ScriptHandle methods that cause this method to be called?
        }
        // getCache().getCacheManager().remove(CacheGroup.USER_GROUP.toString(), username);
    }


    @Override
    public void updateGroup(Group group) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createGroup(Group group) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public PrivateMessage getPM(int pmid) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMs(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMReplies(int pmid, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMsSent(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<PrivateMessage> getPMsReceived(String username, int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public int getPMSentCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public int getPMReceivedCount(String username) {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public void updatePrivateMessage(PrivateMessage privateMessage) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void createPrivateMessage(PrivateMessage privateMessage) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public int getUserCount() {
        /*TODO*/
        return 0;
    }

    @Override
    public int getGroupCount() {
        /*TODO*/
        return 0;
    }

    @Override
    public String getHomeURL() {
        /*TODO*/
        return null;
    }

    @Override
    public List<String> getIPs(String username) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public List<Ban> getBans(int limit) {
        /* TODO: Delete this method or implement it */
        return null;
    }

    @Override
    public void updateBan(Ban ban) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public void addBan(Ban ban) {
        /* TODO: Delete this method or implement it */
    }

    @Override
    public int getBanCount() {
        /* TODO: Delete this method or implement it */
        return 0;
    }

    @Override
    public boolean isBanned(String string) {
        /* TODO: Delete this method or implement it */
        return false;
    }

    @Override
    public boolean isRegistered(String username) {
        /* TODO: Delete this method or implement it */
        return false;
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
