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

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.forum.ForumBoard;
import com.craftfire.bifrost.classes.forum.ForumPost;
import com.craftfire.bifrost.classes.forum.ForumThread;
import com.craftfire.bifrost.classes.general.Ban;
import com.craftfire.bifrost.classes.general.Group;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.bifrost.script.ForumScript;

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

    public ForumPost getPost(int postID) throws UnsupportedMethod {
        if (ForumPost.hasCache(this, postID)) {
            return ForumPost.getCache(this, postID);
        }
        ForumPost post = this.getForumScript().getPost(postID);
        ForumPost.addCache(this, post);
        return post;
    }

    @SuppressWarnings("unchecked")
    public List<ForumPost> getPosts(int limit) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.POST_LIST)) {
            return (List<ForumPost>) this.script.getCache().get(CacheGroup.POST_LIST);
        }
        List<ForumPost> posts = this.getForumScript().getPosts(limit);
        this.script.getCache().put(CacheGroup.POST_LIST, posts);
        return posts;
    }

    @SuppressWarnings("unchecked")
    public List<ForumPost> getPostsFromThread(int threadid, int limit) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.THREAD_POSTS, threadid)) {
            return (List<ForumPost>) this.script.getCache().get(CacheGroup.THREAD_POSTS, threadid);
        }
        List<ForumPost> posts = this.getForumScript().getPostsFromThread(threadid, limit);
        this.script.getCache().put(CacheGroup.THREAD_POSTS, threadid, posts);
        return posts;
    }

    @SuppressWarnings("unchecked")
    public List<ForumPost> getUserPosts(String username, int limit) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.POST_LIST_USER, username)) {
            return (List<ForumPost>) this.script.getCache().get(CacheGroup.POST_LIST_USER, username);
        }
        List<ForumPost> posts = this.getForumScript().getUserPosts(username, limit);
        this.script.getCache().put(CacheGroup.POST_LIST_USER, username, posts);
        return posts;
    }

    public void updatePost(ForumPost post) throws SQLException, UnsupportedMethod {
        this.getForumScript().updatePost(post);
        ForumPost.cleanupCache(this, post, CacheCleanupReason.UPDATE);
        ForumPost.addCache(this, post);
    }

    public void createPost(ForumPost post) throws SQLException, UnsupportedMethod {
        this.getForumScript().createPost(post);
        ForumPost.cleanupCache(this, post, CacheCleanupReason.CREATE);
        ForumPost.addCache(this, post);
    }

    public int getPostCount(String username) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.POST_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.POST_COUNT, username);
        }
        int count = this.getForumScript().getPostCount(username);
        this.script.getCache().put(CacheGroup.POST_COUNT, username, count);
        return count;
    }

    public int getTotalPostCount() throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.POST_COUNT_TOTAL)) {
            return (Integer) this.script.getCache().get(CacheGroup.POST_COUNT_TOTAL);
        }
        int count = this.getForumScript().getTotalPostCount();
        this.script.getCache().put(CacheGroup.POST_COUNT_TOTAL, count);
        return count;
    }

    public ForumPost getLastPost() throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.POST_LAST)) {
            return (ForumPost) this.script.getCache().get(CacheGroup.POST_LAST);
        }
        ForumPost post = this.getForumScript().getLastPost();
        this.script.getCache().put(CacheGroup.POST_LAST, post);
        return post;
    }

    public ForumPost getLastUserPost(String username) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.POST_LAST_USER, username)) {
            return (ForumPost) this.script.getCache().get(CacheGroup.POST_LAST_USER, username);
        }
        ForumPost post = this.getForumScript().getLastUserPost(username);
        this.script.getCache().put(CacheGroup.POST_LAST_USER, username, post);
        return post;
    }

    public int getTotalThreadCount() throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.THREAD_COUNT_TOTAL)) {
            return (Integer) this.script.getCache().get(CacheGroup.THREAD_COUNT_TOTAL);
        }
        int count = this.getForumScript().getTotalThreadCount();
        this.script.getCache().put(CacheGroup.THREAD_COUNT_TOTAL, count);
        return count;
    }

    public int getThreadCount(String username) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.THREAD_COUNT, username)) {
            return (Integer) this.script.getCache().get(CacheGroup.THREAD_COUNT, username);
        }
        int count = this.getForumScript().getThreadCount(username);
        this.script.getCache().put(CacheGroup.THREAD_COUNT, username, count);
        return count;
    }

    public ForumThread getLastThread() throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.THREAD_LAST)) {
            return (ForumThread) this.script.getCache().get(CacheGroup.THREAD_LAST);
        }
        ForumThread thread = this.getForumScript().getLastThread();
        this.script.getCache().put(CacheGroup.THREAD_LAST, thread);
        return thread;
    }

    public ForumThread getLastUserThread(String username) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.THREAD_LAST_USER, username)) {
            return (ForumThread) this.script.getCache().get(CacheGroup.THREAD_LAST_USER, username);
        }
        ForumThread thread = this.getForumScript().getLastUserThread(username);
        this.script.getCache().put(CacheGroup.THREAD_LAST_USER, username, thread);
        return thread;
    }

    public ForumThread getThread(int threadID) throws UnsupportedMethod {
        if (ForumThread.hasCache(this, threadID)) {
            return ForumThread.getCache(this, threadID);
        }
        ForumThread thread = this.getForumScript().getThread(threadID);
        ForumThread.addCache(this, thread);
        return thread;
    }

    @SuppressWarnings("unchecked")
    public List<ForumThread> getThreads(int limit) throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.THREAD_LIST)) {
            return (List<ForumThread>) this.script.getCache().get(CacheGroup.THREAD_LIST);
        }
        List<ForumThread> threads = this.getForumScript().getThreads(limit);
        this.script.getCache().put(CacheGroup.THREAD_LIST, threads);
        return threads;
    }

    @SuppressWarnings("unchecked")
    public List<ForumThread> getThreadsFromBoard(int boardid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_THREADS, boardid)) {
            return (List<ForumThread>) getCache().get(CacheGroup.BOARD_THREADS, boardid);
        }
        List<ForumThread> threads = getForumScript().getThreadsFormBoard(boardid, limit);
        getCache().put(CacheGroup.BOARD_THREADS, boardid, threads);
        return threads;
    }

    @SuppressWarnings("unchecked")
    public List<ForumThread> getUserThreads(String username, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.THREAD_LIST_USER, username)) {
            return (List<ForumThread>) getCache().get(CacheGroup.THREAD_LIST_USER, username);
        }
        List<ForumThread> threads = getForumScript().getUserThreads(username, limit);
        getCache().put(CacheGroup.THREAD_LIST_USER, username, threads);
        return threads;
    }

    public void updateThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        this.getForumScript().updateThread(thread);
        ForumThread.cleanupCache(this, thread, CacheCleanupReason.UPDATE);
        ForumThread.addCache(this, thread);
    }

    public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        this.getForumScript().createThread(thread);
        ForumThread.cleanupCache(this, thread, CacheCleanupReason.CREATE);
        ForumThread.addCache(this, thread);
    }

    public int getBoardCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_COUNT)) {
            return (Integer) getCache().get(CacheGroup.BOARD_COUNT);
        }
        int count = getForumScript().getBoardCount();
        getCache().put(CacheGroup.BOARD_COUNT, count);
        return count;
    }

    @SuppressWarnings("unchecked")
    public List<ForumBoard> getSubBoards(int boardid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_SUBS, boardid)) {
            return (List<ForumBoard>) getCache().get(CacheGroup.BOARD_SUBS, boardid);
        }
        List<ForumBoard> boards = getForumScript().getSubBoards(boardid, limit);
        getCache().put(CacheGroup.BOARD_SUBS, boardid, boards);
        return boards;
    }

    public ForumBoard getBoard(int boardID) throws UnsupportedMethod {
        if (ForumBoard.hasCache(this, boardID)) {
            return ForumBoard.getCache(this, boardID);
        }
        ForumBoard board = getForumScript().getBoard(boardID);
        ForumBoard.addCache(this, board);
        return board;
    }

    public void updateBoard(ForumBoard board) throws UnsupportedMethod, SQLException {
        getForumScript().updateBoard(board);
        ForumBoard.cleanupCache(this, board, CacheCleanupReason.UPDATE);
        ForumBoard.addCache(this, board);
    }

    public void createBoard(ForumBoard board) throws UnsupportedMethod, SQLException {
        getForumScript().createBoard(board);
        ForumBoard.cleanupCache(this, board, CacheCleanupReason.CREATE);
        ForumBoard.addCache(this, board);
    }

    public String getForumURL() throws UnsupportedMethod {
        if (this.script.getCache().contains(CacheGroup.URL_FORUM)) {
            return (String) this.script.getCache().get(CacheGroup.URL_FORUM);
        }
        String url = this.getForumScript().getForumURL();
        this.script.getCache().put(CacheGroup.URL_FORUM, url);
        return url;
    }
}
