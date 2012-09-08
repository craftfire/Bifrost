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
import java.util.Date;
import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

/**
 * This class should only be used with a forum thread/topic.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the thread, run {@see #updateThread()}.
 * <p>
 * When creating a new ForumThread make sure you use the correct constructor:
 * {@see #ForumThread(Script, int)}.
 * <p>
 * Remember to run {@see #createThread()} after creating a thread to insert it into the script.
 */
public class ForumThread extends Message {
    private int firstpostid, lastpostid;
    private int threadviews, threadreplies;
    private boolean locked, poll, sticky;

    public ForumThread(Script script, int firstpostid, int lastpostid, int threadid, int boardid) {
        super(script, threadid, boardid);
        this.firstpostid = firstpostid;
        this.lastpostid = lastpostid;
    }

    public ForumThread(Script script, int boardid) {
        super(script);
        setCategoryID(boardid);
    }

    /**
     * Gets the board/category ID of the thread.
     *
     * @return board/category ID of the thread, 0 if error.
     */
    public int getBoardID() {
        return getCategoryID();
    }

    /**
     * Sets the board/category of the thread.
     * 
     * @param boardid  the ID of the board
     */
    public void setBoardID(int boardid) {
        setCategoryID(boardid);
    }

    /**
     * Returns a ForumBoard object for the board/category of the thread.
     * 
     * @return                    a ForumBoard object
     * @throws UnsupportedMethod  if the method is not supported by script
     */
    public ForumBoard getBoard() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getBoard(getCategoryID());
    }

    /**
     * Returns a List of {@link ForumPost}'s depending on the limit,
     * <code>limit = 0</code> returns all the posts.
     * <p>
     * List will be empty if there were no posts.
     *
     * @param limit  how many ForumPosts that should be returned, 0 = returns all.
     * @return       a List of ForumPost's
     * @see          ForumPost
     */
    public List<ForumPost> getPosts(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI()
                .getForumHandle(getScript().getScript())
                .getPostsFromThread(getID(), limit);
    }

    /**
     * Returns the first Post of the thread.
     *
     * @return  first ForumPost of the thread
     * @throws  UnsupportedMethod if the method is not supported by the script
     * @see     ForumPost
     */
    public ForumPost getFirstPost() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI()
                .getForumHandle(getScript().getScript())
                .getPost(this.firstpostid);
    }

    /**
     * Returns the last Post of the thread.
     *
     * @return  last ForumPosts of the thread
     * @throws  UnsupportedMethod if the method is not supported by the script
     * @see     ForumPost
     */
    public ForumPost getLastPost() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI()
                .getForumHandle(getScript().getScript())
                .getPost(this.lastpostid);
    }

    /**
     * Returns the date when the thread was posted.
     *
     * @return  date of the thread
     */
    public Date getThreadDate() {
        return getDate();
    }

    /**
     * Sets the date of the thread.
     *
     * @param threaddate date of the thread
     */
    public void setThreadDate(Date threaddate) {
        setDate(threaddate);
    }

    /**
     * Returns the subject of the thread.
     *
     * @return subject of the thread
     */
    public String getSubject() {
        return super.getTitle();
    }

    /**
     * Sets the subject of the thread.
     *
     * @param subject subject of the thread
     */
    public void setSubject(String subject) {
        super.setTitle(subject);
    }

    /**
     * Returns number of views the thread has.
     *
     * @return number of views
     */
    public int getViewsCount() {
        return this.threadviews;
    }

    /**
     * Sets the number of views that the thread has.
     *
     * @param threadviews  number of views
     */
    public void setViewsCount(int threadviews) {
        this.threadviews = threadviews;
    }

    /**
     * Returns number of replies for the thread.
     *
     * @return number of replies
     */
    public int getRepliesCount() {
        return this.threadreplies;
    }

    /**
     * Sets the number of replies that the thread has.
     *
     * @param threadreplies  number of replies
     */
    public void setRepliesCount(int threadreplies) {
        this.threadreplies = threadreplies;
    }

    /**
     * Returns <code>true</code> if thread is locked, <code>false</code> if not locked.
     *
     * @return <code>true</code> if locked, <code>false</code> if locked
     */
    public boolean isLocked() {
        return this.locked;
    }

    /**
     * Sets the topic's lock to whatever <code>Boolean</code> the <code>isLocked</code> parameter is.
     * <p>
     * <code>true</code> = locked and <code>false</code> = unlocked.
     *
     * @param isLocked  <code>true</code> for locked, <code>false</code> for not locked
     */
    public void setLocked(boolean isLocked) {
        this.locked = isLocked;
    }

    /**
     * Returns <code>true</code> if thread is a poll, false if error not a poll.
     *
     * @return <code>true</code> if poll, false if error nr not a poll
     */
    public boolean isPoll() {
        return this.poll;
    }

    /**
     * Sets the thread to a poll if parameter <code>isPoll</code> is <code>true</code>.
     *
     * @param isPoll  <code>true</code> for poll, <code>false</code> for not a poll
     */
    public void setPoll(boolean isPoll) {
        this.poll = isPoll;
    }

    /**
     * Returns <code>true</code> if the thread is sticky, <code>false</code> if not sticky.
     *
     * @return <code>true</code> if sticky, <code>false</code> if not sticky
     */
    public boolean isSticky() {
        return this.sticky;
    }

    /**
     * Sets the thread to be a sticky or not.
     *
     * @param isSticky  <code>true</code> if sticky, <code>false</code> if not sticky
     */
    public void setSticky(boolean isSticky) {
        this.sticky = isSticky;
    }

    /**
     * This method should be run after changing any thread values.
     * <p>
     * It should <b>not</b> be run when creating a new thread, only when editing an already existing thread.
     *
     * @throws SQLException         if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void updateThread() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI()
                .getForumHandle(getScript().getScript()).updateThread(this);
    }

    /**
     * This method should be run after creating a new thread.
     * <p>
     * It should <b>not</b> be run when updating a thread, only when creating a new thread.
     *
     * @throws SQLException         if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void createThread() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI()
                .getForumHandle(getScript().getScript()).createThread(this);
    }

    /**
     * Returns <code>true</code> if the handle contains a thread cache with the given id parameter,
     * <code>false</code> if not.
     *
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, int id) {
        return handle.getCache().contains(CacheGroup.THREAD, id);
    }

    /**
     * Adds a ForumThread to the cache with the given script handle
     *
     * @param handle  the script handle
     * @param thread  the ForumThread object
     */
    public static void addCache(ScriptHandle handle, ForumThread thread) {
        handle.getCache().put(CacheGroup.THREAD, thread.getID(), thread);
    }

    /**
     * Returns the ForumThread object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the thread
     * @return        ForumThread object if cache was found, <code>null</code> if no cache was found
     */
    public static ForumThread getCache(ScriptHandle handle, int id) {
        if (handle.getCache().contains(CacheGroup.THREAD, id)) {
            return (ForumThread) handle.getCache().get(CacheGroup.THREAD, id);
        }
        return null;
    }

    /**
     * @see Message#getCategory()
     */
    @Override
    public ForumBoard getCategory() throws UnsupportedMethod {
        return getBoard();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.craftfire.bifrost.classes.general.MessageParent#getChildMessages(int)
     */
    @Override
    public List<ForumPost> getChildMessages(int limit) throws UnsupportedMethod {
        return getPosts(limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Message#getParent()
     */
    @Override
    public ForumBoard getParent() throws UnsupportedMethod {
        return getBoard();
    }
}
