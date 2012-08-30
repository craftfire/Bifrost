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
package com.craftfire.bifrost.handles;

import java.sql.SQLException;
import java.util.List;

import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.bifrost.script.ForumScript;
import com.craftfire.commons.managers.DataManager;

public class ForumHandle extends ScriptHandle {

    public ForumHandle(Scripts script, String version, DataManager dataManager) throws UnsupportedVersion {
        super(script, version, dataManager);
    }

    public ForumScript getForumScript() {
        return (ForumScript) this.script;
    }

    public Ban newBan(String name, String email, String ip) {
        return new Ban(this.script, name, email, ip);
    }

    public Group newGroup(String groupname) {
        return new Group(this.script, groupname);
    }

    public ForumPost newPost(int threadid, int boardid) {
        return new ForumPost(this.script, threadid, boardid);
    }

    public ForumThread newThread(int boardid) {
        return new ForumThread(this.script, boardid);
    }

    public ForumPost getPost(int postID) throws UnsupportedFunction {
        if (ForumPost.hasCache(this, postID)) {
            return ForumPost.getCache(this, postID);
        }
        ForumPost post = this.getForumScript().getPost(postID);
        ForumPost.addCache(this, post);
        return post;
    }

    @SuppressWarnings("unchecked")
    public List<ForumPost> getPosts(int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_LIST)) {
            return (List<ForumPost>) this.script.getCache().get(CacheGroup.POST_LIST);
        }
        List<ForumPost> posts = this.getForumScript().getPosts(limit);
        this.script.getCache().put(CacheGroup.POST_LIST, posts);
        return posts;
    }

    @SuppressWarnings("unchecked")
    public List<ForumPost> getPostsFromThread(int threadid, int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_POSTS, threadid)) {
            return (List<ForumPost>) this.script.getCache().get(CacheGroup.THREAD_POSTS, threadid);
        }
        List<ForumPost> posts = this.getForumScript().getPostsFromThread(threadid, limit);
        this.script.getCache().put(CacheGroup.THREAD_POSTS, posts);
        return posts;
    }

    public void updatePost(ForumPost post) throws SQLException, UnsupportedFunction {
        this.getForumScript().updatePost(post);
        ForumPost.addCache(this, post);
    }

    public void createPost(ForumPost post) throws SQLException, UnsupportedFunction {
        this.getForumScript().createPost(post);
        ForumPost.addCache(this, post);
    }

    public int getPostCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.POST_COUNT, username);
        }
        int count = this.getForumScript().getPostCount(username);
        this.script.getCache().put(CacheGroup.POST_COUNT, username, count);
        return count;
    }

    public int getTotalPostCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_COUNT_TOTAL)) {
            return (Integer) this.script.getCache().get(CacheGroup.POST_COUNT_TOTAL);
        }
        int count = this.getForumScript().getTotalPostCount();
        this.script.getCache().put(CacheGroup.POST_COUNT_TOTAL, count);
        return count;
    }

    public ForumPost getLastPost() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_LAST)) {
            return (ForumPost) this.script.getCache().get(CacheGroup.POST_LAST);
        }
        ForumPost post = this.getForumScript().getLastPost();
        this.script.getCache().put(CacheGroup.POST_LAST, post);
        return post;
    }

    public ForumPost getLastUserPost(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.POST_LAST_USER, username)) {
            return (ForumPost) this.script.getCache().get(CacheGroup.POST_LAST_USER, username);
        }
        ForumPost post = this.getForumScript().getLastUserPost(username);
        this.script.getCache().put(CacheGroup.POST_LAST_USER, username, post);
        return post;
    }

    public int getTotalThreadCount() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_COUNT_TOTAL)) {
            return (Integer) this.script.getCache().get(CacheGroup.THREAD_COUNT_TOTAL);
        }
        int count = this.getForumScript().getTotalThreadCount();
        this.script.getCache().put(CacheGroup.THREAD_COUNT_TOTAL, count);
        return count;
    }

    public int getThreadCount(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.THREAD_COUNT, username);
        }
        int count = this.getForumScript().getThreadCount(username);
        this.script.getCache().put(CacheGroup.THREAD_COUNT, username, count);
        return count;
    }

    public ForumThread getLastThread() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_LAST)) {
            return (ForumThread) this.script.getCache().get(CacheGroup.THREAD_LAST);
        }
        ForumThread thread = this.getForumScript().getLastThread();
        this.script.getCache().put(CacheGroup.THREAD_LAST, thread);
        return thread;
    }

    public ForumThread getLastUserThread(String username) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_LAST_USER, username)) {
            return (ForumThread) this.script.getCache().get(CacheGroup.THREAD_LAST_USER, username);
        }
        ForumThread thread = this.getForumScript().getLastUserThread(username);
        this.script.getCache().put(CacheGroup.THREAD_LAST_USER, username, thread);
        return thread;
    }

    public ForumThread getThread(int threadID) throws UnsupportedFunction {
        if (ForumThread.hasCache(this, threadID)) {
            return ForumThread.getCache(this, threadID);
        }
        ForumThread thread = this.getForumScript().getThread(threadID);
        ForumThread.addCache(this, thread);
        return thread;
    }

    @SuppressWarnings("unchecked")
    public List<ForumThread> getThreads(int limit) throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.THREAD_LIST)) {
            return (List<ForumThread>) this.script.getCache().get(CacheGroup.THREAD_LIST);
        }
        List<ForumThread> threads = this.getForumScript().getThreads(limit);
        this.script.getCache().put(CacheGroup.THREAD_LIST, threads);
        return threads;
    }

    public void updateThread(ForumThread thread) throws SQLException, UnsupportedFunction {
        this.getForumScript().updateThread(thread);
        ForumThread.addCache(this, thread);
    }

    public void createThread(ForumThread thread) throws SQLException, UnsupportedFunction {
        this.getForumScript().createThread(thread);
        ForumThread.addCache(this, thread);
    }

    public String getHomeURL() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.URL_HOME)) {
            return (String) this.script.getCache().get(CacheGroup.URL_HOME);
        }
        String url = this.getForumScript().getHomeURL();
        this.script.getCache().put(CacheGroup.URL_HOME, url);
        return url;
    }

    public String getForumURL() throws UnsupportedFunction {
        if (this.script.getCache().contains(CacheGroup.URL_FORUM)) {
            return (String) this.script.getCache().get(CacheGroup.URL_FORUM);
        }
        String url = this.getForumScript().getForumURL();
        this.script.getCache().put(CacheGroup.URL_FORUM, url);
        return url;
    }
}
