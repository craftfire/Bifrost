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

import com.craftfire.bifrost.classes.general.Cache;
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.classes.general.ViewsCounter;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class should only be used with a forum thread/topic.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the thread, run {@link #update()}.
 * <p>
 * When creating a new ForumThread make sure you use the correct constructor:
 * {@link #ForumThread(ForumHandle, int)}.
 * <p>
 * Remember to run {@link #create()} after creating a thread to insert it into the script.
 */
public class ForumThread extends Message implements ViewsCounter {
    private int firstpostid, lastpostid;
    private int threadviews, threadreplies;
    private boolean locked, poll, sticky;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script       the script
     * @param firstpostid  the ID of the first post in the thread
     * @param lastpostid   the ID of the last post in the thread
     * @param threadid     the ID of the thread which the post is posted in
     * @param boardid      the ID of the board which the thread is posted in
     */
    public ForumThread(ForumScript script, int firstpostid, int lastpostid, int threadid, int boardid) {
        super(script.getHandle(), threadid, boardid);
        this.firstpostid = firstpostid;
        this.lastpostid = lastpostid;
    }

    /**
     * This constructor may be used when creating a new thread for the script.
     * <p>
     * Remember to run {@link #create()} after creating a thread to insert it into the script.
     *
     * @param handle   the script the thread is created for
     * @param boardid  the ID of the board that the thread should be in
     */
    public ForumThread(ForumHandle handle, int boardid) {
        super(handle);
        setCategoryID(boardid);
    }

    @Override
    public ForumHandle getHandle() {
        return (ForumHandle) super.getHandle();
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
     * @throws SQLException       if a MySQL exception occurred
     */
    public ForumBoard getBoard() throws UnsupportedMethod, SQLException {
        return getHandle().getBoard(getCategoryID());
    }

    /**
     * Returns a List of {@link ForumPost}'s depending on the limit,
     * <code>limit = 0</code> returns all the posts.
     * <p>
     * List will be empty if there were no posts.
     *
     * @param  limit         how many ForumPosts that should be returned, 0 = returns all.
     * @return               a List of ForumPost's
     * @throws SQLException  if a MySQL exception occurred
     * @see                  ForumPost
     */
    public List<ForumPost> getPosts(int limit) throws UnsupportedMethod, SQLException {
        return getHandle().getPostsFromThread(getID(), limit);
    }

    /**
     * Returns the first Post of the thread.
     *
     * @return                     first ForumPost of the thread
     * @throws  UnsupportedMethod  if the method is not supported by the script
     * @throws  SQLException       if a MySQL exception occurred
     * @see                        ForumPost
     */
    public ForumPost getFirstPost() throws UnsupportedMethod, SQLException {
        return getHandle().getPost(this.firstpostid);
    }

    /**
     * Returns the last Post of the thread.
     *
     * @return                     last ForumPosts of the thread
     * @throws  UnsupportedMethod  if the method is not supported by the script
     * @throws  SQLException       if a MySQL exception occurred
     * @see                        ForumPost
     */
    public ForumPost getLastPost() throws UnsupportedMethod, SQLException {
        return getHandle().getPost(this.lastpostid);
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
    @Override
    public int getViewsCount() {
        return this.threadviews;
    }

    /**
     * Sets the number of views that the thread has.
     *
     * @param threadviews  number of views
     */
    @Override
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
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getHandle().updateThread(this);
    }

    /**
     * This method should be run after creating a new thread.
     * <p>
     * It should <b>not</b> be run when updating a thread, only when creating a new thread.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getHandle().createThread(this);
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
        handle.getCache().putMetadatable(CacheGroup.THREAD, thread.getID(), thread);
        handle.getCache().setMetadata(CacheGroup.THREAD, thread.getID(), "bifrost-cache.old-board", thread.getBoardID());
        if (thread.getAuthor() != null) {
            handle.getCache().setMetadata(CacheGroup.THREAD, thread.getID(), "bifrost-cache.old-author", thread.getAuthor().getUsername());
        } else {
            handle.getCache().removeMetadata(CacheGroup.THREAD, thread.getID(), "bifrost-cache.old-author");
        }
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
     * Removes outdated cache elements related to given {@param thread} from cache.
     * <p>
     * The method should be called when updating or creating a {@link ForumThread}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     * 
     * @param handle  the handle the method is called from
     * @param thread  the thread to cleanup related cache
     * @param reason  the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     * @see           Cache
     */
    public static void cleanupCache(ScriptHandle handle, ForumThread thread, CacheCleanupReason reason) {
        if (thread.getAuthor() != null) {
            handle.getCache().remove(CacheGroup.THREAD_COUNT, thread.getAuthor().getUsername());
            handle.getCache().remove(CacheGroup.THREAD_LIST_USER, thread.getAuthor().getUsername());
            handle.getCache().remove(CacheGroup.THREAD_LAST_USER, thread.getAuthor().getUsername());
        }
        handle.getCache().remove(CacheGroup.BOARD_THREADS, thread.getBoardID());
        switch (reason) {
        case CREATE:
            handle.getCache().clear(CacheGroup.THREAD_COUNT_TOTAL);
            handle.getCache().clear(CacheGroup.THREAD_LAST);
            handle.getCache().clear(CacheGroup.THREAD_LIST);
            break;
        case OTHER:
            handle.getCache().clear(CacheGroup.THREAD_COUNT_TOTAL);
            handle.getCache().clear(CacheGroup.THREAD_LAST);
            handle.getCache().clear(CacheGroup.THREAD_LIST);
            /* Passes through */
        case UPDATE:
            Object oldUsername = handle.getCache().getMetadata(CacheGroup.THREAD, thread.getID(), "bifrost-cache.old-author");
            Object oldBoardID = handle.getCache().getMetadata(CacheGroup.THREAD, thread.getID(), "bifrost-cache.old-board");
            handle.getCache().remove(CacheGroup.THREAD_COUNT, oldUsername);
            handle.getCache().remove(CacheGroup.THREAD_LIST_USER, oldUsername);
            handle.getCache().remove(CacheGroup.THREAD_LAST_USER, oldUsername);
            handle.getCache().remove(CacheGroup.BOARD_THREADS, oldBoardID);
            break;
        }
    }

    /**
     * Returns a Category object for the category of the message.
     * <p>
     * For ForumThread it has always the same result as {@see #getBoard()}.
     */
    @Override
    public ForumBoard getCategory() throws UnsupportedMethod, SQLException {
        return getBoard();
    }

    /**
     * Returns the list of messages whose parent is this object.
     * <p>
     * For ForumThread it has always the same result as {@see #getPosts(int)}.
     */
    @Override
    public List<ForumPost> getChildMessages(int limit) throws UnsupportedMethod, SQLException {
        return getPosts(limit);
    }

    /**
     * Returns the parent of the message.
     * <p>
     * For ForumThread it always has the same result as {@see #getBoard()}.
     */
    @Override
    public ForumBoard getParent() throws UnsupportedMethod, SQLException {
        return getBoard();
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.classes.general.Message#getAuthor()
     */
    @Override
    public ForumUser getAuthor() {
        return (ForumUser) super.getAuthor();
    }

    /* (non-Javadoc)
     * @see com.craftfire.bifrost.classes.general.Message#setAuthor(com.craftfire.bifrost.classes.general.ScriptUser)
     */
    @Override
    public void setAuthor(ScriptUser author) {
        if (author instanceof ForumUser) {
            super.setAuthor(author);
        }
    }
}
