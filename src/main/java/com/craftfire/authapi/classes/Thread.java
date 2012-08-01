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

import com.craftfire.authapi.enums.CacheGroup;
import com.craftfire.authapi.exceptions.UnsupportedFunction;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Thread implements ThreadInterface {
    private final Script script;
    private ScriptUser author;
    private String subject, body;
    private int firstpostid, lastpostid, threadid;
    private final int boardid;
    private int threadviews, threadreplies;
    private Date threaddate;
    private boolean locked, poll, sticky;

    public Thread(Script script, int firstpostid, int lastpostid, int threadid, int boardid) {
        this.script = script;
        this.firstpostid = firstpostid;
        this.lastpostid = lastpostid;
        this.threadid = threadid;
        this.boardid = boardid;
    }

    public Thread(Script script, int boardid) {
        this.script = script;
        this.boardid = boardid;
    }

    @Override
    public int getID() {
        return this.threadid;
    }

    @Override
    public void setID(int id) {
        this.threadid = id;
    }

    @Override
    public int getBoardID() {
        return this.boardid;
    }

    @Override
    public List<Post> getPosts(int limit) throws UnsupportedFunction {
        return this.script.getPostsFromThread(this.threadid, limit);
    }

    @Override
    public Post getFirstPost() throws UnsupportedFunction {
        return this.script.getPost(this.firstpostid);
    }

    @Override
    public Post getLastPost() throws UnsupportedFunction {
        return this.script.getPost(this.lastpostid);
    }

    @Override
    public Date getThreadDate() {
        return this.threaddate;
    }

    @Override
    public void setThreadDate(Date threaddate) {
        this.threaddate = threaddate;
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
    public int getViews() {
        return this.threadviews;
    }

    @Override
    public void setViews(int threadviews) {
        this.threadviews = threadviews;
    }

    @Override
    public int getReplies() {
        return this.threadreplies;
    }

    @Override
    public void setReplies(int threadreplies) {
        this.threadreplies = threadreplies;
    }

    @Override
    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public void setLocked(boolean isLocked) {
        this.locked = isLocked;
    }

    @Override
    public boolean isPoll() {
        return this.poll;
    }

    @Override
    public void setPoll(boolean isPoll) {
        this.poll = isPoll;
    }

    @Override
    public boolean isSticky() {
        return this.sticky;
    }

    @Override
    public void setSticky(boolean isSticky) {
        this.sticky = isSticky;
    }

    @Override
    public void updateThread() throws SQLException, UnsupportedFunction {
        this.script.updateThread(this);
    }

    @Override
    public void createThread() throws SQLException, UnsupportedFunction {
        this.script.createThread(this);
    }

    public static void addCache(Thread thread) {
        Cache.put(CacheGroup.THREAD, thread.getID(), thread);
    }

    @SuppressWarnings("unchecked")
    public static Thread getCache(int id) {
        Thread temp = null;
        if (Cache.contains(CacheGroup.THREAD, id)) {
            temp = (Thread) Cache.get(CacheGroup.THREAD, id);
        }
        return temp;
    }
}
