/*
 * This file is part of Bifrost.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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

import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.ScriptException;

/**
 * This class should only be used with a forum post.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@link #update()}.
 * <p>
 * When creating a new ForumPost make sure you use the correct constructor:
 * {@link #ForumPost(ForumHandle, int)}.
 * <p>
 * Remember to run {@link #create()} after creating a post to insert it into the script.
 */
public class ForumPost extends Message {
    private int threadid;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script    the script
     * @param postid    the ID of the post
     * @param threadid  the ID of the thread which the post is posted in
     */
    public ForumPost(ForumScript script, int postid, int threadid) {
        super(script.getHandle(), postid);
        this.threadid = threadid;
    }

    /**
     * This constructor should be used when creating a new post for the script.
     * <p>
     * Remember to run {@link #create()} after creating a post to insert it into the script.
     *
     * @param handle    the handle
     * @param threadid  the ID of the thread which the post is going to be posted in
     */
    public ForumPost(ForumHandle handle, int threadid) {
        super(handle);
        this.threadid = threadid;
    }

    @Override
    public ForumHandle getHandle() {
        return (ForumHandle) super.getHandle();
    }

    /**
     * Returns the ID of the thread that the post is posted in.
     *
     * @return the ID of the thread
     */
    public int getThreadID() {
        return this.threadid;
    }

    /**
     * Sets the thread the post is posted in.
     * 
     * @param threadid  the ID of the thread
     */
    public void setThreadID(int threadid) {
        this.threadid = threadid;
    }

    /**
     * Returns the ID of the board that the thread is posted in.
     * 
     * @return                    the ID of the board
     * @throws ScriptException  if the method is not supported by script
     */
    public int getBoardID() throws ScriptException {
        try {
            return getThread().getBoardID();
        } catch (SQLException e) {
            return 0;
        }
    }
    
    /**
     * Returns a ForumBoard object for the board that the thread is posted in.
     * 
     * @return                    a ForumBoard object
     * @throws ScriptException  if the method is not supported by script
     * @throws SQLException       if a SQL exception occurred
     */
    public ForumBoard getBoard() throws ScriptException, SQLException {
        return getThread().getBoard();
    }

    /**
     * Returns the {@link ForumThread} of the post.
     *
     * @return                    the thread Object
     * @throws ScriptException  if the method is not supported by the script
     * @throws SQLException       if a SQL exception occurred
     */
    public ForumThread getThread() throws ScriptException, SQLException {
        return getHandle().getThread(this.threadid);
    }

    /**
     * Returns the date when the post was posted.
     *
     * @return date of the post
     */
    public Date getPostDate() {
        return getDate();
    }

    /**
     * Sets the date of the post.
     *
     * @param postdate  date of the post
     */
    public void setPostDate(Date postdate) {
        setDate(postdate);
    }

    /**
     * Returns the subject of the post.
     *
     * @return subject of the post
     */
    public String getSubject() {
        return super.getTitle();
    }

    /**
     * Sets the subject of the post.
     *
     * @param subject  subject of the post
     */
    public void setSubject(String subject) {
        super.setTitle(subject);
    }

    /**
     * This method should be run after changing any post values.
     * <p>
     * It should <b>not</b> be run when creating a new post, only when editing an already existing post.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     */
    @Override
    public void update() throws SQLException, ScriptException {
        getHandle().updatePost(this);
    }

    /**
     * This method should be run after creating a new post.
     * <p>
     * It should <b>not</b> be run when updating a post, only when creating a new post.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     */
    @Override
    public void create() throws SQLException, ScriptException {
        getHandle().createPost(this);
    }

    /**
     * Returns {@code true} if the handle contains a post cache with the given id parameter,
     * {@code false} if not.
     *
     * @param  handle  the script handle
     * @param  id      the id of the object to look for
     * @return         {@code true} if contains, {@code false} if not
     */
    public static boolean hasCache(ScriptHandle handle, int id) {
        return handle.getCache().contains(CacheGroup.POST, id);
    }

    /**
     * Adds a ForumPost to the cache with the given script handle.
     *
     * @param handle  the script handle
     * @param post    the ForumPost object
     */
    public static void addCache(ScriptHandle handle, ForumPost post) {
        handle.getCache().putMetadatable(CacheGroup.POST, post.getID(), post);
        handle.getCache().setMetadata(CacheGroup.POST, post.getID(), "bifrost-cache.old-thread", post.getThreadID());
        if (post.getAuthor() != null) {
            handle.getCache().setMetadata(CacheGroup.POST, post.getID(), "bifrost-cache.old-author", post.getAuthor().getUsername());
        } else {
            handle.getCache().removeMetadata(CacheGroup.POST, post.getID(), "bifrost-cache.old-author");
        }
    }

    /**
     * Returns the ForumPost object by the given id if found, returns {@code null} if no cache was found.
     *
     * @param  handle  the script handle
     * @param  id      the id of the post
     * @return         ForumPost object if cache was found, {@code null} if no cache was found
     */
    public static ForumPost getCache(ScriptHandle handle, int id) {
        ForumPost temp = null;
        if (handle.getCache().contains(CacheGroup.POST, id)) {
            temp = (ForumPost) handle.getCache().get(CacheGroup.POST, id);
        }
        return temp;
    }

    /**
     * Removes outdated cache elements related to given {@code post} from cache.
     * <p>
     * The method should be called when updating or creating a {@link ForumPost}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     *
     * @param handle  the handle the method is called from
     * @param post    the post to cleanup related cache
     * @param reason  the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     */
    public static void cleanupCache(ScriptHandle handle, ForumPost post, CacheCleanupReason reason) {
        if (post.getAuthor() != null) {
            handle.getCache().remove(CacheGroup.POST_COUNT, post.getAuthor().getUsername());
            handle.getCache().remove(CacheGroup.POST_LAST_USER, post.getAuthor().getUsername());
            handle.getCache().remove(CacheGroup.POST_LIST_USER, post.getAuthor().getUsername());
        }
        handle.getCache().remove(CacheGroup.THREAD_POSTS, post.getThreadID());
        switch (reason) {
        case CREATE:
            handle.getCache().clear(CacheGroup.POST_COUNT_TOTAL);
            handle.getCache().clear(CacheGroup.POST_LAST);
            handle.getCache().clear(CacheGroup.POST_LIST);
            break;
        case OTHER:
            handle.getCache().clear(CacheGroup.POST_COUNT_TOTAL);
            handle.getCache().clear(CacheGroup.POST_LAST);
            handle.getCache().clear(CacheGroup.POST_LIST);
            /* Passes through */
        case UPDATE:
            Object oldUsername = handle.getCache().getMetadata(CacheGroup.POST, post.getID(), "bifrost-cache.old-author");
            Object oldThreadID = handle.getCache().getMetadata(CacheGroup.POST, post.getID(), "bifrost-cache.old-thread");
            handle.getCache().remove(CacheGroup.POST_COUNT, oldUsername);
            handle.getCache().remove(CacheGroup.POST_LAST_USER, oldUsername);
            handle.getCache().remove(CacheGroup.POST_LIST_USER, oldUsername);
            handle.getCache().remove(CacheGroup.THREAD_POSTS, oldThreadID);
            break;
        }
    }

    /**
     * @see Message#getCategory()
     */
    @Override
    public ForumBoard getCategory() throws ScriptException, SQLException {
        return getBoard();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.craftfire.bifrost.classes.general.Message#getCategoryID()
     */
    @Override
    public int getCategoryID() {
        try {
            return getBoardID();
        } catch (ScriptException e) {
            return 0;
        }
    }

    /**
     * Returns the list of messages whose parent is this object.
     * <p>
     * For ForumPost this always returns null.
     */
    @Override
    public List<? extends Message> getChildMessages(int limit) throws ScriptException {
        return null;
    }

    /**
     * Returns the parent of the message.
     * <p>
     * For ForumPost it always has the same result as {@link #getThread()}.
     */
    @Override
    public ForumThread getParent() throws ScriptException, SQLException {
        return getThread();
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
