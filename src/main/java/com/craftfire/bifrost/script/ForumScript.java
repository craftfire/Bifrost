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
package com.craftfire.bifrost.script;

import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.commons.managers.DataManager;

import java.sql.SQLException;
import java.util.List;

/**
 * This class contains functions relevant to direct functions for each script.
 */
public class ForumScript extends Script {
    protected ForumScript(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    /**
     * Returns an amount of how many posts {@param username} has made.
     *
     * @param username The username to get the count from.
     * @return         The amount of how many posts the username have made, returns 0 if none.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
	public int getPostCount(String username) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the complete count of how many posts have been made.
     *
     * @return The amount of how many posts have been made, returns 0 if none.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public int getTotalPostCount() throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the Post object of the last post that has been made.
     *
     * @return Post object of the last post.
     * @see    ForumPost
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public ForumPost getLastPost() throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the Post object of the last post that has been made by {@param username}.
     *
     * @param username The username to grab the last post from.
     * @return         Post object of the last post made by the user.
     * @see            ForumPost
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
	public ForumPost getLastUserPost(String username) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the complete count of how many threads have been made.
     *
     * @return The amount of how many threads have been made, returns 0 if none.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public List<ForumPost> getPosts(int limit) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns a List with Post objects from the given thread/topic ID.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param threadid The thread ID to grab the posts from.
     * @param limit    The limit. Set to 0 if you want to return all.
     * @return         List with Post objects, if none are found it returns an empty List.
     * @see            ForumPost
     * @see            List
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
	public List<ForumPost> getPostsFromThread(int threadid, int limit) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns a ForumPost object of the given post id, if nothing is found it returns null.
     *
     * @param postid The post ID.
     * @return       ForumPost object, null if nothing was found.
     * @see          ForumPost
     * @throws       UnsupportedFunction if the function is not supported by the script.
     */
	public ForumPost getPost(int postid) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Updates the Post object with whatever values set by the user.
     *
     * @param post The Post object.
     * @see        ForumPost
     * @throws     SQLException if a MySQL exception occurred.
     * @throws     UnsupportedFunction if the function is not supported by the script.
     */
	public void updatePost(ForumPost post) throws SQLException, UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Creates the Post object with whatever values set by the user.
     *
     * @param post The Post object.
     * @see        ForumPost
     * @throws     SQLException if a MySQL exception occurred.
     * @throws     UnsupportedFunction if the function is not supported by the script.
     */
	public void createPost(ForumPost post) throws SQLException, UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns an amount of how many threads {@param username} has made.
     *
     * @param username The username to get the count from.
     * @return         The amount of how many threads the username have made, returns 0 if none.
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
	public int getThreadCount(String username) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the complete count of how many threads have been made.
     *
     * @return The amount of how many threads have been made, returns 0 if none.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public int getTotalThreadCount() throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the Thread object of the last thread that has been made.
     *
     * @return Thread object of the last thread.
     * @see    ForumThread
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public ForumThread getLastThread() throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the Thread object of the last thread that has been made by {@param username}.
     *
     * @param username The username to grab the last thread from.
     * @return         Thread object of the last post made by the user.
     * @see            ForumThread
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
	public ForumThread getLastUserThread(String username) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns a Thread object of the given thread ID, if nothing is found it returns null.
     *
     * @param threadid The post ID.
     * @return         Thread object, null if nothing was found.
     * @see            Thread
     * @throws         UnsupportedFunction if the function is not supported by the script.
     */
	public ForumThread getThread(int threadid) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns a List with Thread objects.
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit The limit. Set to 0 if you want to return all.
     * @return      List with Thread objects, if none are found it returns an empty List.
     * @see         Thread
     * @see         List
     * @throws      UnsupportedFunction if the function is not supported by the script.
     */
	public List<ForumThread> getThreads(int limit) throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Updated the Thread object with whatever values set by the user.
     *
     * @param thread The Thread object.
     * @see          Thread
     * @throws       SQLException if a MySQL exception occurred.
     * @throws       UnsupportedFunction if the function is not supported by the script.
     */
	public void updateThread(ForumThread thread) throws SQLException, UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Creates the Thread object with whatever values set by the user.
     *
     * @param thread The Thread object.
     * @see          Thread
     * @throws       SQLException if a MySQL exception occurred.
     * @throws       UnsupportedFunction if the function is not supported by the script.
     */
	public void createThread(ForumThread thread) throws SQLException, UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the script's home URL.
     *
     * @return Home URL of the script.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public String getHomeURL() throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}

    /**
     * Returns the script's forum URL.
     *
     * @return Forum URL of the script.
     * @throws UnsupportedFunction if the function is not supported by the script.
     */
	public String getForumURL() throws UnsupportedFunction {
		throw new UnsupportedFunction();
	}
}