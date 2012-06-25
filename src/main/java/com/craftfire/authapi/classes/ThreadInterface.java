/*
 * This file is part of AuthAPI.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * AuthAPI is licensed under the GNU Lesser General Public License.
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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.craftfire.authapi.exceptions.UnsupportedFunction;

/**
 * This interface contains functions that are related to a Thread.
 */
public abstract interface ThreadInterface {
	/**
	 * Returns the first Post of the thread, null if error.
	 *
	 * @return first Post of the thread, null if error.
	 * @see Post
	 */
    public Post getFirstPost() throws UnsupportedFunction;

	/**
	 * Returns the last Post of the thread, null if error.
	 *
	 * @return last Post of the thread, null if error.
	 * @see Post
	 */
    public Post getLastPost() throws UnsupportedFunction;

	/**
	 * Returns a List of Post's depending on the limit, limit = 0 returns all posts.
	 * List will be empty if there were no posts.
	 *
	 * @param limit how many posts that should be returned, 0 = returns all.
	 * @return a List of Post's
	 * @see List
	 * @see Post
	 */
    public List<Post> getPosts(int limit) throws UnsupportedFunction;

	/**
	 * Returns the ID of the thread, 0 if error.
	 *
	 * @return ID of the thread, 0 if error.
	 */
    public int getID();

	/**
	 * Sets the ID of the thread, should only be used when creating a new Thread.
	 *
	 * @param id the ID of the thread.
	 */
    public void setID(int id);

	/**
	 * Gets the board/category ID of the thread.
	 *
	 * @return board/category ID of the thread, 0 if error.
	 */
    public int getBoardID();

	/**
	 * Returns the Date of when the thread was created.
	 *
	 * @return Date of thread creation date.
	 * @see Date
	 */
    public Date getThreadDate();

	/**
	 * Sets the Date of the thread.
	 *
	 * @param threaddate Date of thread creation date.
	 * @see Date
	 */
    public void setThreadDate(Date threaddate);

	/**
	 * Returns a ScriptUser object of the author, null if error.
	 *
	 * @return ScriptUser of the author, null if error.
	 * @see ScriptUser
	 */
    public ScriptUser getAuthor();

	/**
	 * Sets the author of the thread.
	 *
	 * @param author a ScriptUser object containing the author.
	 */
    public void setAuthor(ScriptUser author);

	/**
	 * Returns the subject/title of the thread, null if error.
	 *
	 * @return subject/title of the thread, null if error.
	 */
    public String getSubject();

	/**
	 * Sets the subject/title of the thread.
	 *
	 * @param subject subject/title of the thread.
	 */
    public void setSubject(String subject);

	/**
	 * Returns the body text of the thread, null if error.
	 *
	 * @return body text of the thread, null if error.
	 */
    public String getBody();

	/**
	 * Sets the body text of the thread.
	 *
	 * @param body body text of the thread.
	 */
    public void setBody(String body);

	/**
	 * Returns number of views the thread has, 0 if error.
	 *
	 * @return number of views, 0 if error.
	 */
    public int getViews();

	/**
	 * Sets the number of views that the thread has.
	 *
	 * @param threadviews number of views.
	 */
    public void setViews(int threadviews);

	/**
	 * Returns number of replies for the thread, 0 if error or none.
	 *
	 * @return number of replies, 0 if error or none.
	 */
    public int getReplies();

	/**
	 * Sets the number of replies that the thread has.
	 *
	 * @param threadreplies number of replies.
	 */
    public void setReplies(int threadreplies);

	/**
	 * Returns true if thread is locked, false if error or not locked.
	 *
	 * @return true if locked, false if error or not locked.
	 */
    public boolean isLocked();

	/**
	 * Sets the topic's lock to whatever Boolean the parameter has, true = locked and false = unlocked.
	 *
	 * @param isLocked true for locked, false for not locked.
	 */
    public void setLocked(boolean isLocked);

	/**
	 * Returns true if thread is a poll, false if error not a poll.
	 *
	 * @return true if poll, false if error nr not a poll.
	 */
    public boolean isPoll();

	/**
	 * Sets the thread to a poll if parameter is true.
	 *
	 * @param isPoll true for poll, false for not a poll.
	 */
    public void setPoll(boolean isPoll);

	/**
	 * Returns true if the thread is sticky, false if error or not sticky.
	 *
	 * @return true if sticky, false if error not sticky.
	 */
    public boolean isSticky();

	/**
	 * Sets the thread to be a sticky or not.
	 *
	 * @param isSticky true if sticky, false if not sticky.
	 */
    public void setSticky(boolean isSticky);

	/**
	 * Run this function if you want to update any changed values for the thread.
	 * NB! This has to be run if you want to "write" changed to the database.
	 *
	 * @throws SQLException if an error occurred.
	 */
    public void updateThread() throws SQLException, UnsupportedFunction;

	/**
	 * Run this function if you create a thread with current values, make sure you know what you are doing!
	 *
	 * @throws SQLException if an error occurred.
	 */
    public void createThread() throws SQLException, UnsupportedFunction;
}
