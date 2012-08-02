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

import com.craftfire.authapi.AuthAPI;
import com.craftfire.authapi.enums.CacheGroup;
import com.craftfire.authapi.exceptions.UnsupportedFunction;

import java.sql.SQLException;
import java.util.Date;

public class Post implements PostInterface {
    private ScriptUser author;
    private String subject, body;
    private int postid;
    private final int threadid, boardid;
    private Date postdate;

    public Post(int postid, int threadid, int boardid) {
        this.postid = postid;
        this.threadid = threadid;
        this.boardid = boardid;
    }

    public Post(int threadid, int boardid) {
        this.threadid = threadid;
        this.boardid = boardid;
    }

    @Override
    public int getID() {
        return this.postid;
    }

    @Override
    public void setID(int id) {
        this.postid = id;
    }

    @Override
    public int getThreadID() {
        return this.threadid;
    }

    @Override
    public int getBoardID() {
        return this.boardid;
    }

    @Override
    public Thread getThread() throws UnsupportedFunction {
        return AuthAPI.getInstance().getScriptAPI().getThread(this.threadid);
    }

    @Override
    public Date getPostDate() {
        return this.postdate;
    }

    @Override
    public void setPostDate(Date postdate) {
        this.postdate = postdate;
    }

    @Override
    public ScriptUser getAuthor() {
        return this.author;
    }

    @Override
    public void setAuthor(ScriptUser author) {
        this.author = author;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void updatePost() throws SQLException, UnsupportedFunction {
        AuthAPI.getInstance().getScriptAPI().updatePost(this);
    }

    @Override
    public void createPost() throws SQLException, UnsupportedFunction {
        AuthAPI.getInstance().getScriptAPI().createPost(this);
    }

    public static boolean hasCache(Object id) {
        return Cache.contains(CacheGroup.POST, id);
    }

    public static void addCache(Post post) {
        Cache.put(CacheGroup.POST, post.getID(), post);
    }

    @SuppressWarnings("unchecked")
    public static Post getCache(Object id) {
        Post temp = null;
        if (Cache.contains(CacheGroup.POST, id)) {
            temp = (Post) Cache.get(CacheGroup.POST, id);
        }
        return temp;
    }
}
