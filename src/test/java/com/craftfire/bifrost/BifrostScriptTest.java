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

import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.classes.general.PrivateMessage;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.exceptions.UnsupportedScript;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.bifrost.handles.ForumHandle;
import com.craftfire.bifrost.handles.ScriptHandle;

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
            bifrost.getScriptAPI().addHandle(script, version, dataManager);
            handle = bifrost.getScriptAPI().getHandle(script);
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

    @Test
    public void testScriptClass() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - SCRIPT CLASS");
        try {
            printResult("getScriptName", handle.getScriptName(), true);
            printResult("getScriptShortname", handle.getScriptShortname(), true);
            printResult("getLatestVersion", handle.getLatestVersion(), true);
            printResult("getVersion", handle.getVersion(), true);
            printResult("getEncryption", "" + handle.getEncryption(), true);
            printResult("getHomeURL", handle.getHomeURL(), true);
            printResult("getBanCount", "" + handle.getBanCount(), true);
            printResult("getBans", handle.getBans(0));
            printResult("getGroupCount", "" + handle.getGroupCount(), true);
            printResult("getGroups", handle.getGroups(0));
            printResult("getLastRegUser", "" + handle.getLastRegUser(), true);
            printResult("getUserCount", "" + handle.getUserCount(), true);
            printResult("isBanned", "" + handle.isBanned("test"), true);
        } catch (UnsupportedMethod e) {
            fail(e.toString());
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
            printResult("getIPs", "" + user.getIPs());
            printResult("getLastIP", user.getLastIP());
            printResult("getLastLogin", "" + user.getLastLogin());
            printResult("getLastPost", "" + user.getLastPost());
            printResult("getLastThread", "" + user.getLastThread());
            printResult("getLastName", user.getLastName());
            printResult("getNickname", user.getNickname());
            printResult("getProfileURL", user.getProfileURL());
            printResult("getPassword", user.getPassword());
            printResult("getPasswordSalt", user.getPasswordSalt());
            printResult("getPMReceivedCount", "" + user.getPMReceivedCount());
            printResult("getPMSentCount", "" + user.getPMSentCount());
            printResult("getPMsSent", "" + user.getPMsSent(0));
            printResult("getPMsReceived", "" + user.getPMsReceived(0));
            printResult("getPostCount", "" + user.getPostCount());
            printResult("getRealName", user.getRealName());
            printResult("getRegIP", user.getRegIP());
            printResult("getRegDate", "" + user.getRegDate());
            printResult("getStatusMessage", user.getStatusMessage());
            printResult("getThreadCount", "" + user.getThreadCount());
            printResult("getUsername", user.getUsername());
            printResult("getUserTitle", user.getUserTitle());
            printResult("getUserID", "" + user.getID());
            printResult("getUserGroups", "" + user.getGroups());
            printResult("isActivated", "" + user.isActivated());
            printResult("isAnonymous", "" + user.isAnonymous());
            printResult("isBanned", "" + user.isBanned());
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
            user.updateUser();
            user.setUsername(temp);
            user.updateUser();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testUserCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - USER CREATE");
        ScriptUser newUser = bifrost.getScriptAPI().getHandle(script).newScriptUser("craftfire" + this.randomInt, "craftfire");
        newUser.setNickname("testing" + this.randomInt);
        newUser.setUserTitle("title");
        newUser.setRegIP("127.0.0.1");
        newUser.setLastIP("127.0.0.1");
        newUser.setEmail("dev@craftfire.com");
        try {
            newUser.createUser();
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
            ban.updateBan();
            ban.setReason(temp);
            ban.updateBan();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testBanCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - BAN CREATE");
        try {
            Ban newBan = bifrost.getScriptAPI().getForumHandle(script).newBan("craftfire-ban-" + this.randomInt, "dev@craftfire.com", "127.0.0.1");
            newBan.setNotes("Staff notes");
            newBan.setReason("Hello world!");
            newBan.addBan();
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
            group.updateGroup();
            group.setName(temp);
            group.updateGroup();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

    @Test
    public void testGroupCreate() throws SQLException {
        print(seperate);
        print(script.toString() + " - " + version + " - GROUP CREATE");
        try {
            Group newGroup = bifrost.getScriptAPI().getForumHandle(script).newGroup("craftfire_group_" + this.randomInt);
            newGroup.setDescription("Description is not needed!");
            newGroup.createGroup();
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
            pm.updatePrivateMessage();
            pm.setBody(temp);
            pm.updatePrivateMessage();
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
            newPM.createPrivateMessage();
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
            ScriptUser user = handle.getUser(username);
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
            ScriptUser user = handle.getUser(username);
            ForumPost post = user.getLastPost();
            String temp = post.getSubject();
            post.setSubject("Debug");
            post.updatePost();
            post.setSubject(temp);
            post.updatePost();
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
            ForumPost newPost = fhandle.newPost(1, 2);
            newPost.setBody("Test: This is the body of the post?!");
            newPost.setAuthor(handle.getUser("craftfire" + this.randomInt));
            newPost.setSubject("Test " + this.randomInt + ": This is the subject of the post!");
            newPost.createPost();
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
            thread.updateThread();
            thread.setSubject(temp);
            thread.updateThread();
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
            newThread.createThread();
        } catch (UnsupportedMethod e) {
            fail(e.toString());
        }
    }

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
        }
        Iterator<?> i = data.iterator();
        while (i.hasNext()) {
            line += i.next();
        }
        printResult(function, line, true);
    }

    public static ForumHandle getForumHandle() {
        try {
            return bifrost.getScriptAPI().getForumHandle(script);
        } catch (ClassCastException ignore) {
        }
        return null;
    }
}
