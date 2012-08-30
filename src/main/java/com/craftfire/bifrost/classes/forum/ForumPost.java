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
import com.craftfire.bifrost.classes.general.Message;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

import java.sql.SQLException;
import java.util.Date;

/**
 * This class should only be used with a forum post.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@see #updatePost()}.
 * <p>
 * When creating a new ForumPost make sure you use the correct constructor:
 * {@see #ForumPost(com.craftfire.bifrost.script.Script, int, int)}.
 * <p>
 * Remember to run {@see #createPost()} after creating a post to insert it into the script.
 */
public class ForumPost extends Message {
    private final int threadid, boardid;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script    the script
     * @param postid    the ID of the post
     * @param threadid  the ID of the thread which the post is posted in
     * @param boardid   the ID of the board which the thread is posted in
     */
    public ForumPost(Script script, int postid, int threadid, int boardid) {
        super(script, postid);
        this.threadid = threadid;
        this.boardid = boardid;
    }

    /**
     * This constructor should be used when creating a new post for the script.
     * <p>
     * Remember to run {@see #createPost()} after creating a post to insert it into the script.
     *
     * @param script    the script
     * @param threadid  the ID of the thread which the post is going to be posted in
     * @param boardid   the ID of the board which the thread is posted in
     */
    public ForumPost(Script script, int threadid, int boardid) {
        super(script);
        this.threadid = threadid;
        this.boardid = boardid;
    }

    @Override
    public int getID() {
        return super.getID();
    }

    @Override
    public void setID(int id) {
        super.setID(id);
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
     * Returns the ID of the board that the thread is posted in.
     *
     * @return the ID of the board
     */
    public int getBoardID() {
        return this.boardid;
    }

    /**
     * Returns the {@link ForumThread} of the post.
     *
     * @return the thread Object
     * @throws UnsupportedFunction if the method is not supported by the script
     */
    public ForumThread getThread() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getThread(this.threadid);
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

    @Override
    public ScriptUser getAuthor() {
        return super.getAuthor();
    }

    @Override
    public void setAuthor(ScriptUser author) {
        super.setAuthor(author);
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

    @Override
    public String getBody() {
        return super.getBody();
    }

    @Override
    public void setBody(String body) {
        super.setBody(body);
    }

    /**
     * This method should be run after changing any post values.
     * <p>
     * It should <b>not</b> be run when creating a new post, only when editing an already existing post.
     *
     * @throws SQLException         if a SQL error concurs
     * @throws UnsupportedFunction  if the method is not supported by the script
     */
    public void updatePost() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).updatePost(this);
    }

    /**
     * This method should be run after creating a new post.
     * <p>
     * It should <b>not</b> be run when updating a post, only when creating a new post.
     *
     * @throws SQLException         if a SQL error concurs
     * @throws UnsupportedFunction  if the method is not supported by the script
     */
    public void createPost() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).createPost(this);
    }

    /**
     * Returns <code>true</code> if the handle contains a post cache with the given id parameter,
     * <code>false</code> if not.
     *
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.POST, id);
    }

    /**
     * Adds a post to the cache with the given script handle
     *
     * @param handle  the script handle
     * @param post    the post object
     */
    public static void addCache(ScriptHandle handle, ForumPost post) {
        handle.getCache().put(CacheGroup.POST, post.getID(), post);
    }

    /**
     * Returns the post object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the post
     * @return        post object if cache was found, <code>null</code> if no cache was found
     */
    public static ForumPost getCache(ScriptHandle handle, Object id) {
        ForumPost temp = null;
        if (handle.getCache().contains(CacheGroup.POST, id)) {
            temp = (ForumPost) handle.getCache().get(CacheGroup.POST, id);
        }
        return temp;
    }
}
