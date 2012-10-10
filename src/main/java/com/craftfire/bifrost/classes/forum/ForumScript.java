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
package com.craftfire.bifrost.classes.forum;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.general.Script;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class contains methods relevant to direct methods for each forum script.
 */
public class ForumScript extends Script {
    protected ForumScript(Scripts script, String version, DataManager dataManager) {
        super(script, version, dataManager);
    }

    @Override
    public ForumHandle getHandle() {
        return (ForumHandle) super.getHandle();
    }

    /**
     * Returns an amount of how many posts are there in {@code threadid}.
     *
     * @param  threadid           the ID of the thread to count posts from
     * @return                    the amount of how many posts the are there in the thread
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPostCountInThread(int threadid) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns an amount of how many posts {@code username} has made.
     *
     * @param  username           the username to get the count from.
     * @return                    the amount of how many posts the username have made, returns 0 if none
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getPostCount(String username) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the complete count of how many posts have been made.
     *
     * @return                    the amount of how many posts have been made, returns 0 if none
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
	public int getTotalPostCount() throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the ForumPost object of the last post that has been made.
     *
     * @return                    ForumPost object of the last post
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumPost
     */
    public ForumPost getLastPost() throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the ForumPost object of the last post that has been made by {@code username}.
     *
     * @param  username           the username to grab the last post from
     * @return                    ForumPost object of the last post made by the user
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @throws SQLException       if a SQL exception occurred
     * @see                       ForumPost
     */
    public ForumPost getLastUserPost(String username) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * TODO: Javadoc
     * Returns the complete count of how many threads have been made.
     *
     * @return                    the amount of how many threads have been made, returns {@code 0} if none
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @throws SQLException       if a SQL exception occurred
     */
    public List<ForumPost> getPosts(int limit) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns a List with ForumPost objects from the given thread/topic ID.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  threadid           the thread ID to grab the posts from
     * @param  limit              the limit, set to {@code 0} if you want to return all
     * @return                    List with ForumPost objects, if none are found it returns an empty List
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumPost
     */
    public List<ForumPost> getPostsFromThread(int threadid, int limit) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns a List with ForumPost objects that have been made by the {@code username}.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to {@code 0} to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     * 
     * @param  username           the username to grab the posts from
     * @param  limit              the limit, set to {@code 0} if you want to return all
     * @return                    List with ForumPost objects, if none are found it returns an empty List
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumPost
     */
    public List<ForumPost> getUserPosts(String username, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a ForumPost object of the given post id, if nothing is found it returns null.
     *
     * @param  postid             the post ID
     * @return                    ForumPost object, null if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumPost
     */
    public ForumPost getPost(int postid) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Updates the ForumThread object with whatever values set by the user.
     *
     * @param  post               the ForumThread object
     * @throws SQLException       if a SQL exception occurred.
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumPost
     */
	public void updatePost(ForumPost post) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Creates the Post object with whatever values set by the user.
     *
     * @param  post               the ForumThread object
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumPost
     */
	public void createPost(ForumPost post) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns an amount of how many threads {@code username} has made.
     *
     * @param  username           the username to get the count from
     * @return                    the amount of how many threads the username have made, returns 0 if none
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
	public int getThreadCount(String username) throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the complete count of how many threads have been made.
     *
     * @return                    the amount of how many threads have been made, returns 0 if none
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
	public int getTotalThreadCount() throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the ForumThread object of the last thread that has been made.
     *
     * @return                    ForumThread object of the last thread
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see    ForumThread
     */
    public ForumThread getLastThread() throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the ForumThread object of the last thread that has been made by {@code username}.
     *
     * @param  username           the username to grab the last thread from
     * @return                    ForumThread object of the last post made by the user
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumThread
     */
    public ForumThread getLastUserThread(String username) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns a ForumThread object of the given thread ID, if nothing is found it returns null.
     *
     * @param  threadid           the thread ID
     * @return                    ForumThread object, {@code null} if nothing was found
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumThread
     */
    public ForumThread getThread(int threadid) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

	/**
     * Returns a List with ForumThread objects from the given board/category ID.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  boardid            the board ID to grab the threads from
     * @param  limit              the limit (set to 0 if you want to return all)
     * @return                    List with ForumThread objects (if none are found it returns an empty List)
     * @throws UnsupportedMethod  if the function is not supported by the script
     * @see                       ForumThread
     */
	public List<ForumThread> getThreadsFromBoard(int boardid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with ForumThread objects that have been made by {@code username}.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param username            the username to grab the thread list from
     * @param limit               the limit, set to 0 if you want to return all
     * @return                    List with ForumThread objects, if none are found it returns an empty List
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumThread
     */
    public List<ForumThread> getUserThreads(String username, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with ForumThread objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit, set to 0 if you want to return all
     * @return                    List with ForumThread objects, if none are found it returns an empty List
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumThread
     */
    public List<ForumThread> getThreads(int limit) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Updated the ForumThread object with whatever values set by the user.
     *
     * @param  thread             the ForumThread object
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumThread
     */
	public void updateThread(ForumThread thread) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Creates the ForumThread object with whatever values set by the user.
     *
     * @param  thread             the ForumThread object
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumThread
     */
	public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    /**
     * Returns the complete count of how many boards have been made.
     *
     * @return                    the amount of how many boards have been made, returns 0 if none.
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getBoardCount() throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the count of how many sub-boards the board has.
     *
     * @param  boardid            the board ID
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
     * Returns a List with ForumBoard objects that are sub boards of the given board/category ID.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  boardid            the board ID to grab the sub boards from
     * @param  limit              the limit (set to 0 if you want to return all)
     * @return                    list with ForumBoard objects (if none are found it returns an empty List)
     * @throws UnsupportedMethod  if the function is not supported by the script.
     * @see    ForumBoard
     */
    public List<ForumBoard> getSubBoards(int boardid, int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns a List with ForumBoard objects.
     * <p>
     * Parameter {@code limit} can be used as a limit of how many objects should be returned.
     * <p>
     * Set {@code limit} to 0 to return all the objects.
     * <p>
     * If none are found, the List will be empty.
     *
     * @param  limit              the limit (set to 0 if you want to return all)
     * @return                    list with ForumBoard objects (if none are found it returns an empty List)
     * @throws UnsupportedMethod  if the function is not supported by the script
     * @see                       ForumBoard
     */
    public List<ForumBoard> getBoards(int limit) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Update the ForumBoard object with whatever values set by the user.
     *
     * @param  board              the ForumBoard object
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the function is not supported by the script
     * @see                       ForumBoard
     */
    public void updateBoard(ForumBoard board) throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Creates the ForumBoard object with whatever values set by the user.
     *
     * @param  board              the ForumBoard object
     * @throws UnsupportedMethod  if the function is not supported by the script
     * @see                       ForumBoard
     */
    public void createBoard(ForumBoard board) throws UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * Returns the script's forum URL.
     *
     * @return                    forum URL of the script
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
	public String getForumURL() throws UnsupportedMethod {
		throw new UnsupportedMethod();
	}

    @Override
    public ForumUser getUser(String username) throws SQLException, UnsupportedMethod {
        return (ForumUser) super.getUser(username);
    }

    @Override
    public ForumUser getUser(int userid) throws SQLException, UnsupportedMethod {
        return (ForumUser) super.getUser(userid);
    }

    @Override
    public ForumUser getLastRegUser() throws SQLException, UnsupportedMethod {
        return (ForumUser) super.getLastRegUser();
    }

    /**
     * Updates the ForumUser object with whatever values set by the user.
     *
     * @param  user               the ForumUser object
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumUser
     */
    public void updateUser(ForumUser user) throws SQLException, UnsupportedMethod {
        updateUser((ScriptUser) user);
    }

    /**
     * Creates a ForumUser with whatever values set by the user.
     *
     * @param user                the ForumUser object
     * @throws SQLException       if a SQL exception occurred
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see                       ForumUser
     */
    public void createUser(ForumUser user) throws SQLException, UnsupportedMethod {
        createUser((ScriptUser) user);
    }
}