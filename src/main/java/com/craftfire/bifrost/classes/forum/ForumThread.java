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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * This class contains functions and data that are related to a thread/topic.
 */
public class ForumThread {
    private ScriptUser author;
    private String subject, body;
    private int firstpostid, lastpostid, threadid;
    private final int boardid;
    private final Script script;
    private int threadviews, threadreplies;
    private Date threaddate;
    private boolean locked, poll, sticky;

    public ForumThread(Script script, int firstpostid, int lastpostid, int threadid, int boardid) {
        this.script = script;
        this.firstpostid = firstpostid;
        this.lastpostid = lastpostid;
        this.threadid = threadid;
        this.boardid = boardid;
    }

    public ForumThread(Script script, int boardid) {
        this.script = script;
        this.boardid = boardid;
    }

    /**
     * Returns the ID of the thread.
     *
     * @return ID of the thread
     */
    public int getID() {
        return this.threadid;
    }

    /**
     * Sets the ID of the thread, should only be used when creating a new Thread.
     *
     * @param id the ID of the thread.
     */
    public void setID(int id) {
        this.threadid = id;
    }

    /**
     * Gets the board/category ID of the thread.
     *
     * @return board/category ID of the thread, 0 if error.
     */
    public int getBoardID() {
        return this.boardid;
    }

    /**
     * Returns a List of Post's depending on the limit, limit = 0 returns all posts.
     * List will be empty if there were no posts.
     *
     * @param limit how many posts that should be returned, 0 = returns all.
     * @return a List of Post's
     * @see List
     * @see ForumPost
     */
    public List<ForumPost> getPosts(int limit) throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript())
                                                                            .getPostsFromThread(this.threadid, limit);
    }

    /**
     * Returns the first Post of the thread, null if error.
     *
     * @return first Post of the thread, null if error.
     * @see ForumPost
     */
    public ForumPost getFirstPost() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getPost(this.firstpostid);
    }

    /**
     * Returns the last Post of the thread, null if error.
     *
     * @return last Post of the thread, null if error.
     * @see ForumPost
     */
    public ForumPost getLastPost() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getPost(this.lastpostid);
    }

    /**
     * Returns the Date of when the thread was created.
     *
     * @return Date of thread creation date.
     * @see Date
     */
    public Date getThreadDate() {
        return this.threaddate;
    }

    /**
     * Sets the Date of the thread.
     *
     * @param threaddate Date of thread creation date.
     * @see Date
     */
    public void setThreadDate(Date threaddate) {
        this.threaddate = threaddate;
    }

    /**
     * Returns a ScriptUser object of the author, null if error.
     *
     * @return ScriptUser of the author, null if error.
     * @see com.craftfire.bifrost.classes.general.ScriptUser
     */
    public ScriptUser getAuthor() {
        return this.author;
    }

    /**
     * Sets the author of the thread.
     *
     * @param author a ScriptUser object containing the author.
     */
    public void setAuthor(ScriptUser author) {
        this.author = author;
    }

    /**
     * Returns the subject/title of the thread, null if error.
     *
     * @return subject/title of the thread, null if error.
     */
    public String getSubject() {
        return this.subject;
    }

    /**
     * Sets the subject/title of the thread.
     *
     * @param subject subject/title of the thread.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the body text of the thread, null if error.
     *
     * @return body text of the thread, null if error.
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Sets the body text of the thread.
     *
     * @param body body text of the thread.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns number of views the thread has, 0 if error.
     *
     * @return number of views, 0 if error.
     */
    public int getViewsCount() {
        return this.threadviews;
    }

    /**
     * Sets the number of views that the thread has.
     *
     * @param threadviews number of views.
     */
    public void setViewsCount(int threadviews) {
        this.threadviews = threadviews;
    }

    /**
     * Returns number of replies for the thread, 0 if error or none.
     *
     * @return number of replies, 0 if error or none.
     */
    public int getRepliesCount() {
        return this.threadreplies;
    }

    /**
     * Sets the number of replies that the thread has.
     *
     * @param threadreplies number of replies.
     */
    public void setRepliesCount(int threadreplies) {
        this.threadreplies = threadreplies;
    }

    /**
     * Returns true if thread is locked, false if error or not locked.
     *
     * @return true if locked, false if error or not locked.
     */
    public boolean isLocked() {
        return this.locked;
    }

    /**
     * Sets the topic's lock to whatever Boolean the parameter has, true = locked and false = unlocked.
     *
     * @param isLocked true for locked, false for not locked.
     */
    public void setLocked(boolean isLocked) {
        this.locked = isLocked;
    }

    /**
     * Returns true if thread is a poll, false if error not a poll.
     *
     * @return true if poll, false if error nr not a poll.
     */
    public boolean isPoll() {
        return this.poll;
    }

    /**
     * Sets the thread to a poll if parameter is true.
     *
     * @param isPoll true for poll, false for not a poll.
     */
    public void setPoll(boolean isPoll) {
        this.poll = isPoll;
    }

    /**
     * Returns true if the thread is sticky, false if error or not sticky.
     *
     * @return true if sticky, false if error not sticky.
     */
    public boolean isSticky() {
        return this.sticky;
    }

    /**
     * Sets the thread to be a sticky or not.
     *
     * @param isSticky true if sticky, false if not sticky.
     */
    public void setSticky(boolean isSticky) {
        this.sticky = isSticky;
    }

    /**
     * Run this function if you want to update any changed values for the thread.
     * NB! This has to be run if you want to "write" changed to the database.
     *
     * @throws SQLException if an error occurred.
     */
    public void updateThread() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).updateThread(this);
    }

    /**
     * Run this function if you create a thread with current values, make sure you know what you are doing!
     *
     * @throws SQLException if an error occurred.
     */
    public void createThread() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).createThread(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.THREAD, id);
    }

    public static void addCache(ScriptHandle handle, ForumThread thread) {
        handle.getCache().put(CacheGroup.THREAD, thread.getID(), thread);
    }

    @SuppressWarnings("unchecked")
    public static ForumThread getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.THREAD, id)) {
            return (ForumThread) handle.getCache().get(CacheGroup.THREAD, id);
        }
        return null;
    }
}
