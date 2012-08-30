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
public class ForumPost {
    private ScriptUser author;
    private String subject, body;
    private int postid;
    private final int threadid, boardid;
    private Date postdate;
    private final Script script;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script   the script
     * @param postid   the ID of the post
     * @param threadid the ID of the thread which the post is posted in
     * @param boardid  the ID of the board which the thread is posted in
     */
    public ForumPost(Script script, int postid, int threadid, int boardid) {
        this.script = script;
        this.postid = postid;
        this.threadid = threadid;
        this.boardid = boardid;
    }

    /**
     * This constructor should be used when creating a new post for the script.
     * <p>
     * Remember to run {@see #createPost()} after creating a post to insert it into the script.
     *
     * @param script   the script
     * @param threadid the ID of the thread which the post is going to be posted in
     * @param boardid  the ID of the board which the thread is posted in
     */
    public ForumPost(Script script, int threadid, int boardid) {
        this.script = script;
        this.threadid = threadid;
        this.boardid = boardid;
    }

    public int getID() {
        return this.postid;
    }

    public void setID(int id) {
        this.postid = id;
    }

    public int getThreadID() {
        return this.threadid;
    }

    public int getBoardID() {
        return this.boardid;
    }

    public ForumThread getThread() throws UnsupportedFunction {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).getThread(this.threadid);
    }

    public Date getPostDate() {
        return this.postdate;
    }

    public void setPostDate(Date postdate) {
        this.postdate = postdate;
    }

    public ScriptUser getAuthor() {
        return this.author;
    }

    public void setAuthor(ScriptUser author) {
        this.author = author;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void updatePost() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).updatePost(this);
    }

    public void createPost() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getForumHandle(this.script.getScript()).createPost(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.POST, id);
    }

    public static void addCache(ScriptHandle handle, ForumPost post) {
        handle.getCache().put(CacheGroup.POST, post.getID(), post);
    }

    @SuppressWarnings("unchecked")
    public static ForumPost getCache(ScriptHandle handle, Object id) {
        ForumPost temp = null;
        if (handle.getCache().contains(CacheGroup.POST, id)) {
            temp = (ForumPost) handle.getCache().get(CacheGroup.POST, id);
        }
        return temp;
    }
}
