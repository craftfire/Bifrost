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
package com.craftfire.authapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.craftfire.authapi.classes.Ban;
import com.craftfire.authapi.classes.Group;
import com.craftfire.authapi.classes.Post;
import com.craftfire.authapi.classes.PrivateMessage;
import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.classes.ScriptUser;
import com.craftfire.authapi.classes.Thread;
import com.craftfire.commons.DataManager;

public class AuthDebug {
    static AuthAPI authAPI;
    static ScriptAPI.Scripts script;
    static String version;
    static DataManager dataManager;
    static HashMap<String, String> data = new HashMap<String, String>();
    static String newline = System.getProperty("line.separator");
    static String seperate = newline + "|------------------------------------------------------------------|" +
                             newline;

    public static void main(String[] args) {
        int count = 1;
        HashMap<Integer, ScriptAPI.Scripts> scriptsh = new HashMap<Integer, ScriptAPI.Scripts>();
        for (ScriptAPI.Scripts s : ScriptAPI.Scripts.values()) {
            System.out.println("#" + count + " - " + s.toString() + newline);
            scriptsh.put(count, s);
            count++;
        }
        System.out.println(newline + "Please select a number for which script you wish to use." + newline);
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader buf_reader = new BufferedReader(reader);
        int tmp;
        try {
            String s = buf_reader.readLine();
            tmp = Integer.parseInt(s.trim());
            ScriptAPI.Scripts ss = scriptsh.get(tmp);
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
            } while (! valid);
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
            if (data.get("mysql_keepalive").equalsIgnoreCase("true") ||
                data.get("mysql_keepalive").equalsIgnoreCase("1")) {
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
            dataManager =
                    new DataManager(keepalive, timeout, data.get("mysql_host"), port, data.get("mysql_database"),
                                    data.get("mysql_username"), data.get("mysql_password"), data.get("mysql_prefix"));
            authAPI = new AuthAPI(script, version, dataManager);
            runTests();
        } catch (IOException ioe) {
            System.out.println("IO exception = " + ioe);
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
                if (line != null && ! line.isEmpty()) {
                    data.put(key, line);
                    valid = true;
                } else {
                    data.put(key, defaultvalue);
                    System.out.println(defaultvalue);
                    valid = true;
                }
            } while (! valid);
        } catch (IOException ioe) {
            System.out.println("IO exception = " + ioe);
        }
    }

    public static void runTests() {
        String temp = "";
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

        String username = data.get("script_username");

        print(seperate);

        print(script.toString() + " - " + version + " - SCRIPT CLASS");
        Script tscript = authAPI.getScript();
        printResult("getEncryption", tscript.getEncryption());
        printResult("getLatestVersion", tscript.getLatestVersion());
        printResult("getScriptName", tscript.getScriptName());
        printResult("getScriptShortname", tscript.getScriptShortname());
        printResult("getVersion", tscript.getVersion());
        printResult("getForumURL", tscript.getForumURL());
        printResult("getHomeURL", tscript.getHomeURL());
        printResult("getLastThread", "" + tscript.getLastThread());
        printResult("getBanCount", "" + tscript.getBanCount());
        printResult("getBans", "" + tscript.getBans(1));
        printResult("getGroupCount", "" + tscript.getGroupCount());
        printResult("getGroups", "" + tscript.getGroups(1));
        printResult("getLastThread", "" + tscript.getLastThread());
        printResult("getLastPost", "" + tscript.getLastPost());
        printResult("getLastRegUser", "" + tscript.getLastRegUser());
        printResult("getTotalPostCount", "" + tscript.getTotalPostCount());
        printResult("getTotalThreadCount", "" + tscript.getTotalThreadCount());
        printResult("getThreads", "" + tscript.getThreads(1));
        printResult("getPosts", "" + tscript.getPosts(1));
        printResult("getUserCount", "" + tscript.getUserCount());
        printResult("isBanned", "" + tscript.isBanned("test"));

        print(seperate);

        print(script.toString() + " - " + version + " - USER CLASS - " + username);
        ScriptUser user = authAPI.getScript().getUser(username);
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
        printResult("getPMsFromUser", "" + user.getPMsFromUser(0));
        printResult("getPMsToUser", "" + user.getPMsToUser(0));
        printResult("getPostCount", "" + user.getPostCount());
        printResult("getRealName", user.getRealName());
        printResult("getRegIP", user.getRegIP());
        printResult("getRegDate", "" + user.getRegDate());
        printResult("getStatusMessage", user.getStatusMessage());
        printResult("getThreadCount", "" + user.getThreadCount());
        printResult("getUsername", user.getUsername());
        printResult("getUserTitle", user.getUserTitle());
        printResult("getUserID", "" + user.getID());
        printResult("getUserGroups", "" + user.getUserGroups());
        printResult("isActivated", "" + user.isActivated());
        printResult("isBanned", "" + user.isBanned());
        printResult("isRegistered", "" + user.isRegistered());

        print(seperate);

        print(script.toString() + " - " + version + " - USER UPDATING");
        temp = user.getUsername();
        user.setUsername("Debug");
        user.updateUser();
        user.setUsername(temp);
        user.updateUser();

        print(seperate);

        print(script.toString() + " - " + version + " - USER CREATE");
        ScriptUser newUser = new ScriptUser(authAPI.getScript(), "testing", "craftfire");
        newUser.setNickname("testing");
        newUser.setUserTitle("title");
        newUser.setRegIP("127.0.0.1");
        newUser.setLastIP("127.0.0.1");
        newUser.setEmail("dev@craftfire.com");
        newUser.createUser();

        print(seperate);

        print(script.toString() + " - " + version + " - BAN CLASS");
        Ban ban = authAPI.getScript().getBans(1).get(0);
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

        print(seperate);

        print(script.toString() + " - " + version + " - BAN UPDATING");
        temp = ban.getReason();
        ban.setReason("Debug");
        ban.updateBan();
        ban.setReason(temp);
        ban.updateBan();

        print(seperate);

        print(script.toString() + " - " + version + " - BAN CREATE");
        Ban newBan = new Ban(authAPI.getScript(), "Craftfire", "dev@craftfire.com", "127.0.0.1");
        newBan.setNotes("Staff notes");
        newBan.setReason("Hello world!");
        newBan.addBan();

        print(seperate);

        print(script.toString() + " - " + version + " - GROUP CLASS");
        Group group = user.getUserGroups().get(0);
        printResult("getName", group.getName());
        printResult("getID", "" + group.getID());
        printResult("getDescription", group.getDescription());
        printResult("getUserCount", "" + group.getUserCount());
        printResult("getUsers", "" + group.getUsers());

        print(seperate);

        print(script.toString() + " - " + version + " - GROUP UPDATING");
        temp = group.getName();
        group.setName("Debug");
        group.updateGroup();
        group.setName(temp);
        group.updateGroup();

        print(seperate);

        print(script.toString() + " - " + version + " - GROUP CREATE");
        Group newGroup = new Group(authAPI.getScript(), "Example group");
        newGroup.setDescription("Description is not needed!");
        //newGroup.createGroup();

        print(seperate);

        print(script.toString() + " - " + version + " - POST CLASS");
        Post post = user.getLastPost();
        printResult("getAuthor", "" + post.getAuthor());
        printResult("getBody", post.getBody());
        printResult("getSubject", post.getSubject());
        printResult("getBoardID", "" + post.getBoardID());
        printResult("getPostDate", "" + post.getPostDate());
        printResult("getPostID", "" + post.getID());
        printResult("getThreadID", "" + post.getThreadID());

        print(seperate);

        print(script.toString() + " - " + version + " - POST UPDATING");
        temp = post.getSubject();
        post.setSubject("Debug");
        post.updatePost();
        post.setSubject(temp);
        post.updatePost();

        print(seperate);

        print(script.toString() + " - " + version + " - POST CREATE");
        Post newPost = new Post(authAPI.getScript(), 1, 1);
        newPost.setBody("Test: This it the body of the post?!");
        newPost.setAuthor(authAPI.getUser("Craftfire"));
        newPost.setSubject("Test: This is the subject of the post!");
        //newPost.createPost();

        print(seperate);

        print(script.toString() + " - " + version + " - PRIVATEMESSAGE CLASS");
        PrivateMessage pm = user.getPMsFromUser(1).get(0);
        printResult("getBody", pm.getBody());
        printResult("getFromUser", "" + pm.getFromUser());
        printResult("getSubject", pm.getSubject());
        printResult("getToUser", "" + pm.getToUser());
        printResult("getDate", "" + pm.getDate());
        printResult("getID", "" + pm.getID());

        print(seperate);

        print(script.toString() + " - " + version + " - PRIVATEMESSAGE UPDATING");
        temp = pm.getBody();
        pm.setBody("Debug");
        pm.updatePrivateMessage();
        pm.setBody(temp);
        pm.updatePrivateMessage();

        print(seperate);

        print(script.toString() + " - " + version + " - PRIVATEMESSAGE CREATE");
        ScriptUser from = authAPI.getUser("Contex");
        ScriptUser to = authAPI.getUser("Craftfire");
        PrivateMessage newPM = new PrivateMessage(authAPI.getScript(), from, to);
        newPM.setBody("This is an example body");
        newPM.setSubject("This is an example subject");
        newPM.setNew(true);
        //newPM.createPrivateMessage();

        print(seperate);

        print(script.toString() + " - " + version + " - THREAD CLASS");
        Thread thread = authAPI.getScript().getLastThread();
        printResult("getAuthor", "" + thread.getAuthor());
        printResult("getBody", thread.getBody());
        printResult("getSubject", thread.getSubject());
        printResult("getBoardID", "" + thread.getBoardID());
        printResult("getFirstPost", "" + thread.getFirstPost());
        printResult("getLastPost", "" + thread.getLastPost());
        printResult("getPosts", "" + thread.getPosts(0));
        printResult("getReplies", "" + thread.getReplies());
        printResult("getThreadDate", "" + thread.getThreadDate());
        printResult("getThreadID", "" + thread.getID());
        printResult("getViews", "" + thread.getViews());

        print(seperate);

        print(script.toString() + " - " + version + " - THREAD UPDATING");
        temp = thread.getSubject();
        thread.setSubject("Debug");
        thread.updateThread();
        thread.setSubject(temp);
        thread.updateThread();

        print(seperate);

        print(script.toString() + " - " + version + " - THREAD CREATE");
        Thread newThread = new Thread(authAPI.getScript(), 1);
        newThread.setBody("Test: This it the body of the thread?!");
        newThread.setAuthor(authAPI.getUser("Craftfire"));
        newThread.setSubject("Test: This is the subject of the tgread!");
        //newThread.createThread();

        print(seperate);
    }

    public static void print(String string) {
        System.out.println(string);
    }

    public static void printResult(String function, String data) {
        String line = "NOT SUPPORTED";
        String prefix = "-";
        if (data != null && ! data.equalsIgnoreCase("0") && ! data.equalsIgnoreCase("null")) {
            line = data;
            prefix = "+";
        }
        print(prefix + function + "() = " + line);
    }
}