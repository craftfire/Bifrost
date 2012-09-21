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

import java.sql.SQLException;
import java.util.List;

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.forum.ForumBoard;
import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class contains functions relevant to direct functions for each script.
 */
public class ForumScript extends Script {
    protected ForumScript(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    /**
     * Returns an amount of how many posts are there in {@param threadid}.
     *
     * @param threadid            the ID of the thread to count posts from
     * @return                    the amount of how many posts the are there in the thread
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPostCountInThread(int threadid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns an amount of how many posts {@param username} has made.
     *
     * @param username  the username to get the count from.
     * @return          the amount of how many posts the username have made, returns 0 if none
     * @throws          UnsupportedMethod if the function is not supported by the script
     */
    public int getPostCount(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the complete count of how many posts have been made.
     *
     * @return the amount of how many posts have been made, returns 0 if none
     * @throws UnsupportedMethod if the function is not supported by the script
     */
	public int getTotalPostCount() throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the ForumPost object of the last post that has been made.
     *
     * @return ForumPost object of the last post
     * @see    ForumPost
     * @throws UnsupportedMethod if the function is not supported by the script
     * @throws SQLException if a MySQL exception occurred
     */
    public ForumPost getLastPost() throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the ForumPost object of the last post that has been made by {@param username}.
     *
     * @param username  The username to grab the last post from
     * @return          ForumPost object of the last post made by the user
     * @see             ForumPost
     * @throws          UnsupportedMethod if the function is not supported by the script
     * @throws          SQLException  if a MySQL exception occurred
     */
    public ForumPost getLastUserPost(String username) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the complete count of how many threads have been made.
     *
     * @return The amount of how many threads have been made, returns 0 if none
     * @throws UnsupportedMethod if the function is not supported by the script
     * @throws SQLException  if a MySQL exception occurred
     */
    public List<ForumPost> getPosts(int limit) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Returns a List with ForumPost objects from the given thread/topic ID.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param threadid  The thread ID to grab the posts from
     * @param limit     The limit. Set to 0 if you want to return all
     * @return          List with ForumPost objects, if none are found it returns an empty List
     * @see             ForumPost
     * @throws          UnsupportedMethod if the function is not supported by the script
     * @throws SQLException  if a MySQL exception occurred
     */
    public List<ForumPost> getPostsFromThread(int threadid, int limit) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Returns a List with ForumPost objects that have been made by the {@param username}.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     * 
     * @param username            the username to grab the posts from
     * @param limit               the limit, set to 0 if you want to return all
     * @return                    List with ForumPost objects, if none are found it returns an empty List
     * @see                       ForumPost
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<ForumPost> getUserPosts(String username, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a ForumPost object of the given post id, if nothing is found it returns null.
     *
     * @param postid The post ID.
     * @return       ForumPost object, null if nothing was found.
     * @see          ForumPost
     * @throws       UnsupportedMethod if the function is not supported by the script.
     * @throws SQLException  if a MySQL exception occurred
     */
    public ForumPost getPost(int postid) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Updates the ForumThread object with whatever values set by the user.
     *
     * @param post  the ForumThread object.
     * @see         ForumPost
     * @throws      SQLException if a SQL exception occurred.
     * @throws      UnsupportedMethod if the function is not supported by the script.
     */
	public void updatePost(ForumPost post) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Creates the Post object with whatever values set by the user.
     *
     * @param post the ForumThread object.
     * @see        ForumPost
     * @throws     SQLException if a MySQL exception occurred
     * @throws     UnsupportedMethod if the function is not supported by the script
     */
	public void createPost(ForumPost post) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns an amount of how many threads {@param username} has made.
     *
     * @param username  the username to get the count from
     * @return          the amount of how many threads the username have made, returns 0 if none
     * @throws          UnsupportedMethod if the function is not supported by the script
     */
	public int getThreadCount(String username) throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the complete count of how many threads have been made.
     *
     * @return the amount of how many threads have been made, returns 0 if none
     * @throws UnsupportedMethod if the function is not supported by the script
     */
	public int getTotalThreadCount() throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the Thread object of the last thread that has been made.
     *
     * @return ForumThread object of the last thread
     * @see    ForumThread
     * @throws UnsupportedMethod if the function is not supported by the script
     * @throws SQLException  if a MySQL exception occurred
     */
    public ForumThread getLastThread() throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the Thread object of the last thread that has been made by {@param username}.
     *
     * @param username  the username to grab the last thread from
     * @return          ForumThread object of the last post made by the user
     * @see             ForumThread
     * @throws          UnsupportedMethod if the function is not supported by the script
     * @throws SQLException  if a MySQL exception occurred
     */
    public ForumThread getLastUserThread(String username) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Returns a Thread object of the given thread ID, if nothing is found it returns null.
     *
     * @param threadid  the post ID
     * @return          ForumThread object, null if nothing was found
     * @see             ForumThread
     * @throws          UnsupportedMethod if the function is not supported by the script
     * @throws SQLException  if a MySQL exception occurred
     */
    public ForumThread getThread(int threadid) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

	/**
     * Returns a List with ForumThread objects from the given board/category ID.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param  boardid            the board ID to grab the threads from
     * @param  limit              the limit (set to 0 if you want to return all)
     * @return                    List with ForumThread objects (if none are found it returns an empty List)
     * @see                       ForumThread
     * @see                       List
     * @throws UnsupportedMethod  if the function is not supported by the script.
     */
	public List<ForumThread> getThreadsFormBoard(int boardid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with ForumThread objects that have been made by {@param username}.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param username            the username to grab the thread list from
     * @param limit               the limit, set to 0 if you want to return all
     * @return                    List with ForumThread objects, if none are found it returns an empty List
     * @see                       ForumThread
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<ForumThread> getUserThreads(String username, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with @Override objects.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param limit  the limit. Set to 0 if you want to return all.
     * @return       List with @Override objects, if none are found it returns an empty List.
     * @see          ForumThread
     * @throws       UnsupportedMethod if the function is not supported by the script.
     * @throws SQLException  if a MySQL exception occurred
     */
    public List<ForumThread> getThreads(int limit) throws UnsupportedMethod, SQLException {
		throw new UnsupportedMethod();
	}

    /**
     * Updated the @Override object with whatever values set by the user.
     *
     * @param thread the @Override object.
     * @see          @Override
     * @throws       SQLException if a MySQL exception occurred.
     * @throws       UnsupportedMethod if the function is not supported by the script.
     */
	public void updateThread(ForumThread thread) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Creates the @Override object with whatever values set by the user.
     *
     * @param thread The @Override object.
     * @see          @Override
     * @throws       SQLException if a MySQL exception occurred.
     * @throws       UnsupportedMethod if the function is not supported by the script.
     */
	public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

   /**
     * Returns the complete count of how many boards have been made.
     *
     * @return the amount of how many boards have been made, returns 0 if none.
     * @throws UnsupportedMethod if the function is not supported by the script.
     */
    public int getBoardCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the count of how many sub-boards the board has.
     *
     * @param boardid             the ID of the board
     * @return                    the number of sub-boards of the board
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getSubBoardCount(int boardid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a ForumBoard object of the given board ID, if nothing is found it returns null.
     *
     * @param  boardid            the board ID
     * @return                    ForumBoard object, null if nothing was found.
     * @see                       ForumBoard
     * @throws UnsupportedMethod  if the function is not supported by the script.
     */
    public ForumBoard getBoard(int boardid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }
    
    /**
     * Returns a List with ForumBoard objects that are subboards of the given board/category ID.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param  boardid            the board ID to grab the subboards from
     * @param  limit              the limit (set to 0 if you want to return all)
     * @return                    list with ForumBoard objects (if none are found it returns an empty List)
     * @see    ForumBoard
     * @see    List
     * @throws UnsupportedMethod  if the function is not supported by the script.
     */
    public List<ForumBoard> getSubBoards(int boardid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with ForumBoard objects.
     * <p>
     * Parameter {@param limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@param limit} to 0 to return all the objects.
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit (set to 0 if you want to return all)
     * @return                    list with ForumBoard objects (if none are found it returns an empty List)
     * @see    ForumBoard
     * @see    List
     * @throws UnsupportedMethod  if the function is not supported by the script
     */
    public List<ForumBoard> getBoards(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Update the ForumBoard object with whatever values set by the user.
     *
     * @param  board              the ForumBoard object.
     * @see    ForumBoard
     * @throws SQLException       if a MySQL exception occurred.
     * @throws UnsupportedMethod  if the function is not supported by the script
     */
    public void updateBoard(ForumBoard board) throws UnsupportedMethod, SQLException {
        throw new UnsupportedMethod();
    }

    /**
     * Creates the ForumBoard object with whatever values set by the user.
     *
     * @param  board              the ForumBoard object
     * @see    ForumBoard
     * @throws SQLException       if a MySQL exception occurred
     * @throws UnsupportedMethod  if the function is not supported by the script
     */
    public void createBoard(ForumBoard board) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the script's forum URL.
     *
     * @return forum URL of the script.
     * @throws UnsupportedMethod if the function is not supported by the script.
     */
	public String getForumURL() throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}
}