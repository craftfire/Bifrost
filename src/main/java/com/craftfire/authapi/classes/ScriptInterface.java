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
package com.craftfire.authapi.classes;

import java.util.List;

public abstract interface ScriptInterface {
    /**
     * Returns the latest version which is supported by the script.
     *
     * @return Latest supported version of the script.
     */
    public String getLatestVersion();

    /**
     * Returns the version which the script has been set to, this is usually the user's version.
     *
     * @return the version.
     */
    public String getVersion();

    /**
     * Returns which encryption method is used by the script to hash the passwords.
     *
     * This can be sha-1, sha-256, sha-512, whirpool, md5 and so on.
     *
     * @return Encryption method.
     */
    public String getEncryption();

    /**
     * Returns the script's full name, for example SimpleMachines.
     *
     * @return The script's full name.
     */
    public String getScriptName();

    /**
     * Returns the script's short name, for example SMF
     *
     * @return The script's short name.
     */
    public String getScriptShortname();

    /**
     * Returns true if {@param username} and {@param password} matches the username and password for the user in the
     * script.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the username and password matches.
     */
    public boolean authenticate(String username, String password);

    /**
     * Hashes the password of the user with whatever encryption the script uses and returns the hashed string.
     *
     * @param salt     The salt of the user to hash.
     * @param password The password of the user to hash.
     * @return A hashed string.
     */
    public String hashPassword(String salt, String password);

    /**
     * Returns the username of {@param userid}, if none is found it returns null.
     *
     * @param userid The userid to get the username of.
     * @return Username of {@param userid} or null if nothing was found.
     */
    public String getUsername(int userid);

    /**
     * Returns the user ID of {@param username}, if none is found it returns 0.
     *
     * @param username The user ID to get the username of.
     * @return User ID of {@param username} or 0 if nothing was found.
     */
    public int getUserID(String username);

    /**
     * Return the ScriptUser object of the given username, returns null if nothing was found.
     *
     * @param username The username to get the ScriptUser object from.
     * @return A ScriptUser object of the given username, returns null if nothing was found.
     * @see ScriptUser
     */
    public ScriptUser getUser(String username);

    /**
     * Return the ScriptUser object of the given user ID, returns null if nothing was found.
     *
     * @param userid The user ID to get the ScriptUser object from.
     * @return A ScriptUser object of the given user ID, returns null if nothing was found.
     * @see ScriptUser
     */
    public ScriptUser getUser(int userid);

    /**
     * Return the ScriptUser object of the latest registered user.
     *
     * @return A ScriptUser object of the latest registered user.
     * @see ScriptUser
     */
    public ScriptUser getLastRegUser();

    /**
     * Updates the ScriptUser object with whatever values set by the user.
     *
     * @param user The ScriptUser object.
     * @see ScriptUser
     */
    public void updateUser(ScriptUser user);

    /**
     * Creates a ScriptUser object with whatever values set by the user.
     *
     * @param user The ScriptUser object.
     * @see ScriptUser
     */
    public void createUser(ScriptUser user);

    /**
     * Returns a List with Group objects.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return List with Group objects, if none are found it returns an empty List.
     * @see Group
     * @see List
     */
    public List<Group> getGroups(int limit);

    /**
     * Returns a Group object of the given group id, if nothing is found it returns null.
     *
     * @param groupid The group ID.
     * @return Group object, null if nothing was found.
     * @see Group
     */
    public Group getGroup(int groupid);

    /**
     * Returns a List with the Group objects that the user is a part of.
     * If none are found, the List will be empty.
     *
     * @param username The username to grab the groups of.
     * @return List with Group objects, if none are found it returns an empty List.
     * @see Group
     * @see List
     */
    public List<Group> getUserGroups(String username);

    /**
     * Updates the Group object with whatever values set by the user.
     *
     * @param group The Group object.
     * @see Group
     */
    public void updateGroup(Group group);

    /**
     * Creates the Group object with whatever values set by the user.
     *
     * @param group The Group object.
     * @see Group
     */
    public void createGroup(Group group);

    /**
     * Returns a PrivateMessage object of the given private message id, if nothing is found it returns null.
     *
     * @param pmid The private message ID.
     * @return PrivateMessage object, null if nothing was found.
     * @see PrivateMessage
     */
    public PrivateMessage getPM(int pmid);

    /**
     * Returns a List with PrivateMessage objects that the user has sent.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username The username to get the PrivateMessage objects from.
     * @param limit    The limit. Set to 0 if you want to return all.
     * @return List with Group objects, if none are found it returns an empty List.
     * @see PrivateMessage
     * @see List
     */
    public List<PrivateMessage> getPMsSent(String username, int limit);

    /**
     * Returns a List with PrivateMessage objects that the user has received.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username The username to get the PrivateMessage objects from.
     * @param limit    The limit. Set to 0 if you want to return all.
     * @return List with Group objects, if none are found it returns an empty List.
     * @see PrivateMessage
     * @see List
     */
    public List<PrivateMessage> getPMsReceived(String username, int limit);

    /**
     * Returns an amount of how many private messages {@param username} has sent.
     *
     * @param username The username to get the count from.
     * @return The amount of how many private messages the username has sent, returns 0 if none.
     */
    public int getPMSentCount(String username);

    /**
     * Returns an amount of how many private messages {@param username} has received.
     *
     * @param username The username to get the count from.
     * @return The amount of how many private messages the username has received, returns 0 if none.
     */
    public int getPMReceivedCount(String username);

    /**
     * Updates the PrivateMessage object with whatever values set by the user.
     *
     * @param privateMessage The PrivateMessage object.
     * @see PrivateMessage
     */
    public void updatePrivateMessage(PrivateMessage privateMessage);

    /**
     * Creates the PrivateMessage object with whatever values set by the user.
     *
     * @param privateMessage The PrivateMessage object.
     * @see PrivateMessage
     */
    public void createPrivateMessage(PrivateMessage privateMessage);

    /**
     * Returns a Post object of the given post id, if nothing is found it returns null.
     *
     * @param postid The post ID.
     * @return Post object, null if nothing was found.
     * @see Post
     */
    public Post getPost(int postid);

    /**
     * Returns a List with Post objects.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return List with Post objects, if none are found it returns an empty List.
     * @see Post
     * @see List
     */
    public List<Post> getPosts(int limit);

    /**
     * Returns a List with Post objects from the given thread/topic ID.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param threadid The thread ID to grab the posts from.
     * @param limit    The limit. Set to 0 if you want to return all.
     * @return List with Post objects, if none are found it returns an empty List.
     * @see Post
     * @see List
     */
    public List<Post> getPostsFromThread(int threadid, int limit);

    /**
     * Creates the Post object with whatever values set by the user.
     *
     * @param post The Post object.
     * @see Post
     */
    public void updatePost(Post post);

    /**
     * Creates the Post object with whatever values set by the user.
     *
     * @param post The Post object.
     * @see Post
     */
    public void createPost(Post post);

    /**
     * Returns an amount of how many posts {@param username} has made.
     *
     * @param username The username to get the count from.
     * @return The amount of how many posts the username have made, returns 0 if none.
     */
    public int getPostCount(String username);

    /**
     * Returns the complete count of how many posts have been made.
     *
     * @return The amount of how many posts have been made, returns 0 if none.
     */
    public int getTotalPostCount();

    /**
     * Returns the Post object of the last post that has been made.
     *
     * @return Post object of the last post.
     * @see Post
     */
    public Post getLastPost();

    /**
     * Returns the Post object of the last post that has been made by {@param username}.
     *
     * @param username The username to grab the last post from.
     * @return Post object of the last post made by the user.
     * @see Post
     */
    public Post getLastUserPost(String username);

    /**
     * Returns the complete count of how many threads have been made.
     *
     * @return The amount of how many threads have been made, returns 0 if none.
     */
    public int getTotalThreadCount();

    /**
     * Returns an amount of how many threads {@param username} has made.
     *
     * @param username The username to get the count from.
     * @return The amount of how many threads the username have made, returns 0 if none.
     */
    public int getThreadCount(String username);

    /**
     * Returns the Thread object of the last thread that has been made.
     *
     * @return Thread object of the last thread.
     * @see Thread
     */
    public Thread getLastThread();

    /**
     * Returns the Thread object of the last thread that has been made by {@param username}.
     *
     * @param username The username to grab the last thread from.
     * @return Thread object of the last post made by the user.
     * @see Thread
     */
    public Thread getLastUserThread(String username);

    /**
     * Returns a Thread object of the given thread ID, if nothing is found it returns null.
     *
     * @param threadid The post ID.
     * @return Thread object, null if nothing was found.
     * @see Thread
     */
    public Thread getThread(int threadid);

    /**
     * Returns a List with Thread objects.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return List with Thread objects, if none are found it returns an empty List.
     * @see Thread
     * @see List
     */
    public List<Thread> getThreads(int limit);

    /**
     * Updated the Thread object with whatever values set by the user.
     *
     * @param thread The Thread object.
     * @see Thread
     */
    public void updateThread(Thread thread);

    /**
     * Creates the Thread object with whatever values set by the user.
     *
     * @param thread The Thread object.
     * @see Thread
     */
    public void createThread(Thread thread);

    /**
     * Returns the complete count of how many users the script has registered.
     *
     * @return The amount of how many users the script has registered, returns 0 if none.
     */
    public int getUserCount();

    /**
     * Returns the complete count of how many groups the script has.
     *
     * @return The amount of how how many groups the script has, returns 0 if none.
     */
    public int getGroupCount();

    /**
     * Returns the script's home URL.
     *
     * @return Home URL of the script.
     */
    public String getHomeURL();

    /**
     * Returns the script's forum URL.
     *
     * @return Forum URL of the script.
     */
    public String getForumURL();

    /**
     * Returns a List with {@param username}'s IP addresses.
     *
     * @param username The username to get the IP addresses from.
     * @return List with {@param username}'s IP addresses.
     * @see List
     */
    public List<String> getIPs(String username);

    /**
     * Returns a List with Ban objects.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return List with Ban objects, if none are found it returns an empty List.
     * @see Ban
     * @see List
     */
    public List<Ban> getBans(int limit);

    /**
     * Creates the Ban object with whatever values set by the user.
     *
     * @param ban The Ban object.
     * @see Ban
     */
    public void updateBan(Ban ban);

    /**
     * Creates the Ban object with whatever values set by the user.
     *
     * @param ban The Ban object.
     * @see Ban
     */
    public void addBan(Ban ban);

    /**
     * Returns the complete count of how many bans that have been made.
     *
     * @return The amount of how many bans that have been made, returns 0 if none.
     */
    public int getBanCount();

    /**
     * Returns true if {@param string} matches a ban in the script.
     *
     * @param string String to search for.
     * @return True if {@param string} is banned.
     */
    public boolean isBanned(String string);

    /**
     * Returns true if {@param username} is already registered.
     *
     * @param username Username to check if is registered.
     * @return True if {@param username} is registered.
     */
    public boolean isRegistered(String username);
}
