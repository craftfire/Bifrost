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
package com.craftfire.bifrost;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.craftfire.commons.enums.DataType;
import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.cms.CMSArticle;
import com.craftfire.bifrost.classes.cms.CMSCategory;
import com.craftfire.bifrost.classes.cms.CMSComment;
import com.craftfire.bifrost.classes.cms.CMSHandle;
import com.craftfire.bifrost.classes.cms.CMSUser;
import com.craftfire.bifrost.classes.forum.ForumBoard;
import com.craftfire.bifrost.classes.forum.ForumHandle;
import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.forum.ForumUser;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.exceptions.UnsupportedScript;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;

public class BifrostScriptTest {
    static final String newline = System.getProperty("line.separator");
    static final String seperate = newline + "|------------------------------------------------------------------|" + newline;
    static Bifrost bifrost;
    static Scripts script;
    static String version;
    static DataManager dataManager;
    static HashMap<String, String> data = new HashMap<String, String>();
    static String username;
    static ScriptHandle handle;
    Random randomGenerator = new Random();
    int randomInt = this.randomGenerator.nextInt(1000000);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        int count = 1;
        HashMap<Integer, Scripts> scriptsh = new HashMap<Integer, Scripts>();
        for (Scripts s : Scripts.values()) {
            System.out.println("#" + count + " - " + s.toString() + newline);
            scriptsh.put(count, s);
            count++;
        }
        System.out.println(newline + "Please select a number for which script you wish to use." + newline);
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader buf_reader = new BufferedReader(reader);
        int tmp;
        String s = buf_reader.readLine();
        tmp = Integer.parseInt(s.trim());
        Scripts ss = scriptsh.get(tmp);
        script = ss;
        System.out.println(newline + "Selected " + ss.toString() + " as script." + newline);
        System.out.println(seperate);
        System.out.println("Please continue by typing the script version number (e.g. 1.0.1)" + newline);
        String line = null;
        boolean valid = false;
        do {
            if (line != null) {
                System.out.println(newline + "That was not a valid version number, please try again." + newline);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            line = br.readLine();
            if (line.contains(".")) {
                String[] split = line.split("\\.");
                if (split.length > 1) {
                    version = line;
                    valid = true;
                }
            } else {
                version = "1.0.0";
                System.out.println(version);
                valid = true;
            }
        } while (!valid);
        System.out.println(newline + script.toString() + " version set to " + version + "." + newline);
        System.out.println(seperate);
        ask("MySQL keepalive", "mysql_keepalive", "true");
        ask("MySQL timeout", "mysql_timeout", "0");
        ask("MySQL host", "mysql_host", "localhost");
        ask("MySQL port", "mysql_port", "3306");
        ask("MySQL database", "mysql_database", "craftfire");
        ask("MySQL username", "mysql_username", "craftfire");
        ask("MySQL password", "mysql_password", "craftfire");
        ask("MySQL prefix", "mysql_prefix", "smf__202__");
        ask("Script user username", "script_username", "Contex");

        boolean keepalive = false;
        if (data.get("mysql_keepalive").equalsIgnoreCase("true") || data.get("mysql_keepalive").equalsIgnoreCase("1")) {
            keepalive = true;
        }
        int port = 3306;
        int tempport = Integer.parseInt(data.get("mysql_port"));
        if (tempport > 0) {
            port = tempport;
        }
        int timeout = 0;
        int temptimeout = Integer.parseInt(data.get("mysql_timeout"));
        if (temptimeout > 0) {
            timeout = temptimeout;
        }
        dataManager = new DataManager(DataType.MYSQL, data.get("mysql_username"), data.get("mysql_password"));
        dataManager.setHost(data.get("mysql_host"));
        dataManager.setPort(port);
        dataManager.setDatabase(data.get("mysql_database"));
        dataManager.setPrefix(data.get("mysql_prefix"));
        dataManager.setTimeout(timeout);
        dataManager.setKeepAlive(keepalive);
        username = data.get("script_username");
        try {
            bifrost = new Bifrost();
            int handleID = bifrost.getScriptAPI().addHandle(script, version, dataManager);
            handle = bifrost.getScriptAPI().getHandle(handleID);
        } catch (UnsupportedVersion unsupportedVersion) {
            throw new RuntimeException(unsupportedVersion);
        } catch (UnsupportedScript unsupportedScript) {
            throw new RuntimeException(unsupportedScript);
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        print(seperate);
    }

    @Test
    public void printDataManagerSettings() {
        print(seperate);
        print("DATAMANAGER");
        print("getDatabase = " + dataManager.getDatabase());
        print("getHost = " + dataManager.getHost());
        print("getPassword = " + dataManager.getPassword());
        print("getPrefix = " + dataManager.getPrefix());
        print("getUsername = " + dataManager.getUsername());
        print("getConnection = " + dataManager.getConnection());
        print("getDataType = " + dataManager.getDataType());
        print("getPort = " + dataManager.getPort());
        print("getTimeout = " + dataManager.getTimeout());
        print("isConnected = " + dataManager.isConnected());
        print("isKeepAlive = " + dataManager.isKeepAlive());
    }

    // ----------------------------------------------------------- GENERAL TESTS

    @Test
    public void testScriptClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - SCRIPT CLASS");
        try {
            printResult("getScriptName", handle.getScriptName(), true);
            printResult("getScriptShortname", handle.getScriptShortname(), true);
            printResult("getLatestVersion", handle.getLatestVersion().toString(), true);
            printResult("getVersion", handle.getVersion().toString(), true);
            printResult("getHomeURL", handle.getHomeURL(), true);
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
        try {
            printResult("getBanCount", "" + handle.getBanCount(), true);
            printResult("getBans", handle.getBans(0));
            printResult("isBanned", "" + handle.isBanned("test"), true);
        } catch (UnsupportedMethod e) {
            e.printStackTrace();
        }
        try {
            printResult("getGroupCount", "" + handle.getGroupCount(), true);
            printResult("getGroups", handle.getGroups(0));
        } catch (UnsupportedMethod e) {
            e.printStackTrace();
        }
        try {
            printResult("getLastRegUser", "" + handle.getLastRegUser(), true);
            printResult("getUserCount", "" + handle.getUserCount(), true);
        } catch (UnsupportedMethod e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUserClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - USER CLASS - " + username);
        try {
            ScriptUser user = handle.getUser(username);
            printResult("getAvatarURL", user.getAvatarURL());
            printResult("getBirthday", "" + user.getBirthday());
            printResult("getEmail", user.getEmail());
            printResult("getFirstName", user.getFirstName());
            printResult("getGender", "" + user.getGender());
            printResult("getPassword", user.getPassword());
            printResult("getPasswordSalt", user.getPasswordSalt());
            printResult("getNickname", user.getNickname());
            printResult("getProfileURL", user.getProfileURL());
            printResult("getLastIP", user.getLastIP());
            printResult("getLastLogin", "" + user.getLastLogin());
            printResult("getLastName", user.getLastName());
            try {
                printResult("getIPs", "" + user.getIPs());
            } catch (UnsupportedMethod e) {
                e.printStackTrace();
            }
            try {
                printResult("getPMReceivedCount", "" + user.getPMReceivedCount());
                printResult("getPMSentCount", "" + user.getPMSentCount());
                printResult("getPMsSent", "" + user.getPMsSent(0));
                printResult("getPMsReceived", "" + user.getPMsReceived(0));
            } catch (UnsupportedMethod e) {
                e.printStackTrace();
            }
            printResult("getRealName", user.getRealName());
            printResult("getRegIP", user.getRegIP());
            printResult("getRegDate", "" + user.getRegDate());
            printResult("getStatusMessage", user.getStatusMessage());
            printResult("getUsername", user.getUsername());
            printResult("getUserTitle", user.getUserTitle());
            printResult("getUserID", "" + user.getID());
            printResult("getUserGroups", "" + user.getGroups());
            printResult("isActivated", "" + user.isActivated());
            printResult("isAnonymous", "" + user.isAnonymous());
            try {
                printResult("isBanned", "" + user.isBanned());
            } catch (UnsupportedMethod e) {
                e.printStackTrace();
            }
            printResult("isRegistered", "" + user.isRegistered());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testUserUpdate() throws UnsupportedMethod, SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - USER UPDATING");
        try {
            ScriptUser user = handle.getUser(username);
            String temp = user.getUsername();
            user.setUsername("Debug");
            user.setPassword("craftfire");
            user.update();
            user.setUsername(temp);
            user.update();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testUserCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - USER CREATE");
        ScriptUser newUser = handle.newScriptUser("craftfire" + this.randomInt, "craftfire");
        newUser.setNickname("testing" + this.randomInt);
        newUser.setUserTitle("title");
        newUser.setRegIP("127.0.0.1");
        newUser.setLastIP("127.0.0.1");
        newUser.setEmail("dev@craftfire.com");
        try {
            newUser.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testBanClass() {
        print(seperate);
        print(script.toString() + " - " + version + " - BAN CLASS");
        try {
            Ban ban = handle.getBans(1).get(0);
            printResult("getEmail", ban.getEmail());
            printResult("getIP", ban.getIP());
            printResult("getID", "" + ban.getID());
            printResult("getNotes", ban.getNotes());
            printResult("getReason", ban.getReason());
            printResult("getUsername", ban.getName());
            printResult("getEndDate", "" + ban.getEndDate());
            printResult("getStartDate", "" + ban.getStartDate());
            printResult("getTimeLength", "" + ban.getTimeLength());
            printResult("getTimeRemaining", "" + ban.getTimeRemaining());
            printResult("getUserID", "" + ban.getUserID());
            printResult("isPermanent", "" + ban.isPermanent());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testBanUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - BAN UPDATING");
        try {
            Ban ban = handle.getBans(1).get(0);
            String temp = ban.getReason();
            ban.setReason("Debug");
            ban.update();
            ban.setReason(temp);
            ban.update();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testBanCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - BAN CREATE");
        try {
            Ban newBan = handle.newBan("craftfire-ban-" + this.randomInt, "dev@craftfire.com", "127.0.0.1");
            newBan.setNotes("Staff notes");
            newBan.setReason("Hello world!");
            newBan.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGroupClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - GROUP CLASS");
        try {
            ScriptUser user = handle.getUser(username);
            Group group = user.getGroups().get(0);
            printResult("getName", group.getName());
            printResult("getID", "" + group.getID());
            printResult("getDescription", group.getDescription());
            printResult("getUserCount", "" + group.getUserCount());
            printResult("getUsers", "" + group.getUsers());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGroupUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - GROUP UPDATING");
        try {
            ScriptUser user = handle.getUser(username);
            Group group = user.getGroups().get(0);
            String temp = group.getName();
            group.setName("Debug");
            group.update();
            group.setName(temp);
            group.update();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGroupCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - GROUP CREATE");
        try {
            Group newGroup = handle.newGroup("craftfire_group_" + this.randomInt);
            newGroup.setDescription("Description is not needed!");
            newGroup.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testPMClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - PRIVATEMESSAGE CLASS");
        try {
            ScriptUser user = handle.getUser(username);
            PrivateMessage pm = user.getPMsSent(1).get(0);
            printResult("getBody", pm.getBody());
            printResult("getSubject", pm.getSubject());
            printResult("getRecipients", "" + pm.getRecipients());
            printResult("getDate", "" + pm.getDate());
            printResult("getID", "" + pm.getID());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testPMUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - PRIVATEMESSAGE UPDATING");
        try {
            ScriptUser user = handle.getUser(username);
            PrivateMessage pm = user.getPMsSent(1).get(0);
            String temp = pm.getBody();
            pm.setBody("Debug");
            pm.update();
            pm.setBody(temp);
            pm.update();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testPMCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - PRIVATEMESSAGE CREATE");
        try {
            ScriptUser from = handle.getUser("Contex");
            List<ScriptUser> recipients = new ArrayList<ScriptUser>();
            recipients.add(handle.getUser("Craftfire"));
            recipients.add(handle.getUser("craftfire" + this.randomInt));
            PrivateMessage newPM = handle.newPrivateMessage(from, recipients);
            newPM.setBody("This is an example body: " + this.randomInt);
            newPM.setSubject("This is an example subject: " + this.randomInt);
            newPM.setNew(handle.getUser("Craftfire"), true);
            newPM.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    // ----------------------------------------------------------- FORUM TESTS

    @Test
    public void testForumUserClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMUSER CLASS - " + username);
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumUser user = fhandle.getUser(username);
            printResult("getLastPost", "" + user.getLastPost());
            printResult("getLastThread", "" + user.getLastThread());
            printResult("getPostCount", "" + user.getPostCount());
            printResult("getThreadCount", "" + user.getThreadCount());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumPostClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMPOST CLASS");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumUser user = fhandle.getUser(username);
            ForumPost post = user.getLastPost();
            printResult("getAuthor", "" + post.getAuthor());
            printResult("getBody", post.getBody());
            printResult("getSubject", post.getSubject());
            printResult("getBoardID", "" + post.getBoardID());
            printResult("getPostDate", "" + post.getPostDate());
            printResult("getPostID", "" + post.getID());
            printResult("getThreadID", "" + post.getThreadID());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumPostUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMPOST UPDATING");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumUser user = fhandle.getUser(username);
            ForumPost post = user.getLastPost();
            String temp = post.getSubject();
            post.setSubject("Debug");
            post.update();
            post.setSubject(temp);
            post.update();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumPostCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMPOST CREATE");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumPost newPost = fhandle.newPost(1);
            newPost.setBody("Test: This is the body of the post?!");
            newPost.setAuthor(handle.getUser("craftfire" + this.randomInt));
            newPost.setSubject("Test " + this.randomInt + ": This is the subject of the post!");
            newPost.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumThreadClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMTHREAD CLASS");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumThread thread = fhandle.getLastThread();
            printResult("getAuthor", "" + thread.getAuthor());
            printResult("getBody", thread.getBody());
            printResult("getSubject", thread.getSubject());
            printResult("getBoardID", "" + thread.getBoardID());
            printResult("getFirstPost", "" + thread.getFirstPost());
            printResult("getLastPost", "" + thread.getLastPost());
            printResult("getPosts", "" + thread.getPosts(0));
            printResult("getReplies", "" + thread.getRepliesCount());
            printResult("getThreadDate", "" + thread.getThreadDate());
            printResult("getThreadID", "" + thread.getID());
            printResult("getViews", "" + thread.getViewsCount());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumThreadUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMTHREAD UPDATING");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumThread thread = fhandle.getLastThread();
            String temp = thread.getSubject();
            thread.setSubject("Debug");
            thread.update();
            thread.setSubject(temp);
            thread.update();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumThreadCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMTHREAD CREATE");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumThread newThread = fhandle.newThread(2);
            newThread.setBody("Test: " + this.randomInt + " This it the body of the thread?!");
            newThread.setAuthor(handle.getUser("craftfire" + this.randomInt));
            newThread.setSubject("Test: " + this.randomInt + " This is the subject of the thread!");
            newThread.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumBoardClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMBOARD CLASS");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumBoard board = fhandle.getLastThread().getBoard();
            printResult("getID", "" + board.getID());
            printResult("getName", board.getName());
            printResult("getDescription", board.getDescription());
            printResult("getParentID", "" + board.getParentID());
            printResult("getParent", "" + board.getParent());
            printResult("getSubcategories", board.getSubcategories(0));
            printResult("getThreads", board.getThreads(0));
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumBoardUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMBOARD UPDATING");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumBoard board = fhandle.getLastThread().getBoard();
            String temp = board.getName();
            String changed = null;
            board.setName("Debug");
            board.update();
            changed = board.getName();
            board.setName(temp);
            board.update();
            assertEquals("Debug", changed);
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testForumBoardCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - FORUMBOARD CREATE");
        ForumHandle fhandle = getForumHandle();
        if (fhandle == null) {
            fail("Not a forum script.");
        }
        try {
            ForumBoard newBoard = fhandle.newBoard(2);
            newBoard.setDescription("Test: " + this.randomInt + " This it the description of the board?!");
            newBoard.setName("Test: " + this.randomInt + " This is the name of the board!");
            newBoard.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    // ----------------------------------------------------------- CMS TESTS

    @Test
    public void testCMSUserClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSUSER CLASS - " + username);
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            printResult("getLastComment", "" + user.getLastComment());
            printResult("getLastArticle", "" + user.getLastArticle());
            printResult("getCommentCount", "" + user.getCommentCount());
            printResult("getArticleCount", "" + user.getArticleCount());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSCommentClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSCOMMENT CLASS");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            CMSComment comment = user.getLastComment();
            printResult("getID", "" + comment.getID());
            printResult("getAuthor", "" + comment.getAuthor());
            printResult("getBody", comment.getBody());
            printResult("getTitle", comment.getTitle());
            printResult("getCategorzID", "" + comment.getCategoryID());
            printResult("getDate", "" + comment.getDate());
            printResult("getArticleID", "" + comment.getArticleID());
            printResult("isDeleted", "" + comment.isDeleted());
            printResult("getParentID", "" + comment.getParentID());
            printResult("getChildMessages", "" + comment.getChildMessages(0));
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSCommentUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSCOMMENT UPDATING");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            CMSComment comment = user.getLastComment();
            String temp = comment.getBody();
            String changed = null;
            comment.setBody("Debug");
            comment.update();
            handle.getCache().remove(CacheGroup.COMMENT, comment.getID());
            comment = chandle.getComment(comment.getID());
            changed = comment.getBody();
            comment.setBody(temp);
            comment.update();
            assertEquals("Debug", changed);
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSCommentCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSCOMMENT CREATE");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSComment newComment = chandle.newComment(1);
            newComment.setBody("Test: This is the body of the comment?!");
            newComment.setAuthor(chandle.getUser("craftfire" + this.randomInt));
            newComment.setTitle("Test " + this.randomInt + ": This is the title of the comment!");
            newComment.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSArticleClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSARTICLE CLASS");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            CMSArticle article = user.getLastArticle();
            printResult("getID", "" + article.getID());
            printResult("getAuthor", "" + article.getAuthor());
            printResult("getTitle", article.getTitle());
            printResult("getIntro", article.getIntro());
            printResult("getBody", article.getBody());
            printResult("getCategoryID", "" + article.getCategoryID());
            printResult("getDate", "" + article.getDate());
            printResult("isDeleted", "" + article.isDeleted());
            printResult("isPublic", "" + article.isPublic());
            printResult("isFeatured", "" + article.isFeatured());
            printResult("isAllowingComments", "" + article.isAllowingComments());
            printResult("getUrl", article.getUrl());
            printResult("getViewsCount", "" + article.getViewsCount());
            printResult("getComments", article.getComments(0));
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSArticleUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSARTICLE UPDATING");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            CMSArticle article = user.getLastArticle();
            String temp = article.getTitle();
            String changed = null;
            article.setTitle("Debug");
            article.update();
            handle.getCache().remove(CacheGroup.ARTICLE, article.getID());
            article = chandle.getArticle(article.getID());
            changed = article.getTitle();
            article.setTitle(temp);
            article.update();
            assertEquals("Debug", changed);
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSArticleCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSARTICLE CREATE");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSArticle newArticle = chandle.newArticle(2);
            newArticle.setBody("Test: This is the body of the article?!");
            newArticle.setAuthor(chandle.getUser("craftfire" + this.randomInt));
            newArticle.setTitle("Test " + this.randomInt + ": This is the title of the article!");
            newArticle.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSCategoryClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSCATEGORY CLASS");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            CMSCategory category = user.getLastArticle().getCategory();
            printResult("getID", "" + category.getID());
            printResult("getName", category.getName());
            printResult("getDescription", category.getDescription());
            printResult("getParentID", "" + category.getParentID());
            printResult("getArticles", category.getArticles(0));
            printResult("getSubcategories", category.getSubcategories(0));
            printResult("isPublic", "" + category.isPublic());
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSCategoryUpdate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSCATEGORY UPDATING");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSUser user = chandle.getUser(username);
            CMSCategory category = user.getLastArticle().getCategory();
            String temp = category.getName();
            String changed = null;
            category.setName("Debug");
            category.update();
            handle.getCache().remove(CacheGroup.CMSCAT, category.getID());
            category = chandle.getCategory(category.getID());
            changed = category.getName();
            category.setName(temp);
            category.update();
            assertEquals("Debug", changed);
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testCMSCategoryCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - CMSCATEGORY CREATE");
        CMSHandle chandle = getCMSHandle();
        if (chandle == null) {
            fail("Not a CMS script.");
        }
        try {
            CMSCategory newCategory = chandle.newCategory("", 0);
            newCategory.setDescription("Test: This is the description of the category?!");
            newCategory.setName("Test " + this.randomInt + ": This is the name of the category!");
            newCategory.create();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    // ----------------------------------------------------------- UTIL METHODS

    public static void ask(String name, String key, String defaultvalue) {
        String line = null;
        boolean valid = false;
        try {
            do {
                System.out.println(newline + name + ": ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    data.put(key, line);
                    valid = true;
                } else {
                    data.put(key, defaultvalue);
                    System.out.println(defaultvalue);
                    valid = true;
                }
            } while (!valid);
        } catch (IOException ioe) {
            System.out.println("IO exception = " + ioe);
        }
    }


    public static void print(String string) {
        System.out.println(string);
    }

    public static void printResult(String function, String data) {
        printResult(function, data, false);
    }

    public static void printResult(String function, String data, boolean assumeValid) {
        if (assumeValid) {
            print(function + "() = " + data);
            return;
        }
        String line = "NOT SUPPORTED";
        String prefix = "-";
        if (data != null && !data.equalsIgnoreCase("0") && !data.equalsIgnoreCase("null") && !data.isEmpty()) {
            line = data;
            prefix = "+";
        }
        print(prefix + function + "() = " + line);
    }

    public static void printResult(String function, Collection<?> data) {
        String line = "[";
        if (data == null) {
            printResult(function, "");
            return;
        }
        Iterator<?> i = data.iterator();
        while (i.hasNext()) {
            line += i.next();
        }
        printResult(function, line, true);
    }

    public static ForumHandle getForumHandle() {
        try {
            return (ForumHandle) handle;
        } catch (ClassCastException ignore) {
        }
        return null;
    }

    public static CMSHandle getCMSHandle() {
        try {
            return (CMSHandle) handle;
        } catch (ClassCastException ignore) {
        }
        return null;
    }
}
