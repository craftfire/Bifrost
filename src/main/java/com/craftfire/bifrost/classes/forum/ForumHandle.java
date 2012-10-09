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
import java.util.List;

import com.craftfire.commons.managers.DataManager;

import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;

/**
 * This class contains methods relevant to methods to use for a forum script.
 *
 * @see ForumScript Documentation of all the methods
 */
public class ForumHandle extends ScriptHandle {

    /**
     * Creates a ForumScriptHandle.
     *
     * @see ScriptHandle#ScriptHandle(int, Scripts, String, DataManager) Documentation for this constructor
     */
    public ForumHandle(int handleID, Scripts script, String version, DataManager dataManager) throws UnsupportedVersion {
        super(handleID, script, version, dataManager);
    }

    /**
     * Returns the ForumScript object
     *
     * @return the ForumScript object
     */
    public ForumScript getForumScript() {
        return (ForumScript) this.getScript();
    }

    /**
     * Creates a new forum post in the specified {@code threadid} and {@code boardid}.
     *
     * @see ForumPost#ForumPost(ForumHandle, int) Documentation for this method
     */
    public ForumPost newPost(int threadid) {
        return new ForumPost(this, threadid);
    }

    /**
     * Creates a new forum thread in the specified {@code boardid}.
     *
     * @see ForumThread#ForumThread(ForumHandle, int) Documentation for this method
     */
    public ForumThread newThread(int boardid) {
        return new ForumThread(this, boardid);
    }

    /**
     * Creates a new board in the forum.
     *
     * @see ForumBoard#ForumBoard(ForumHandle, String, int) Documentation for this method
     */
    public ForumBoard newBoard(int parentid) {
        return new ForumBoard(this, "", parentid);
    }

    /**
     * @see ForumScript#getPost(int) Documentation for this method
     */
    public ForumPost getPost(int postID) throws UnsupportedMethod, SQLException {
        if (ForumPost.hasCache(this, postID)) {
            return ForumPost.getCache(this, postID);
        }
        ForumPost post = this.getForumScript().getPost(postID);
        ForumPost.addCache(this, post);
        return post;
    }

    /**
     * @see ForumScript#getPosts(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumPost> getPosts(int limit) throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.POST_LIST)) {
            List<ForumPost> posts = (List<ForumPost>) getCache().get(CacheGroup.POST_LIST);
            if (posts.size() == ((limit == 0) ? getTotalPostCount() : limit)) {
                return posts;
            } else if ((posts.size() > limit) && (limit != 0)) {
                return posts.subList(0, limit);
            }
        }
        List<ForumPost> posts = this.getForumScript().getPosts(limit);
        getCache().put(CacheGroup.POST_LIST, posts);
        return posts;
    }

    /**
     * @see ForumScript#getPostsFromThread(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumPost> getPostsFromThread(int threadid, int limit) throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.THREAD_POSTS, threadid)) {
            List<ForumPost> posts = (List<ForumPost>) getCache().get(CacheGroup.THREAD_POSTS, threadid);
            if (posts.size() == ((limit == 0) ? getPostCountInThread(threadid) : limit)) {
                return posts;
            } else if ((posts.size() > limit) && (limit !=0)) {
                return posts.subList(0, limit);
            }
        }
        List<ForumPost> posts = this.getForumScript().getPostsFromThread(threadid, limit);
        getCache().put(CacheGroup.THREAD_POSTS, threadid, posts);
        return posts;
    }

    /**
     * @see ForumScript#getUserPosts(String, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumPost> getUserPosts(String username, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.POST_LIST_USER, username)) {
            List<ForumPost> posts = (List<ForumPost>) getCache().get(CacheGroup.POST_LIST_USER, username);
            if (posts.size() == ((limit == 0) ? getPostCount(username) : limit)) {
                return posts;
            } else if ((posts.size() > limit) && (limit != 0)) {
                return posts.subList(0, limit);
            }
        }
        List<ForumPost> posts = this.getForumScript().getUserPosts(username, limit);
        getCache().put(CacheGroup.POST_LIST_USER, username, posts);
        return posts;
    }

    /**
     * @see ForumScript#updatePost(ForumPost) Documentation for this method
     */
    public void updatePost(ForumPost post) throws SQLException, UnsupportedMethod {
        this.getForumScript().updatePost(post);
        ForumPost.cleanupCache(this, post, CacheCleanupReason.UPDATE);
        ForumPost.addCache(this, post);
    }

    /**
     * @see ForumScript#createPost(ForumPost) Documentation for this method
     */
    public void createPost(ForumPost post) throws SQLException, UnsupportedMethod {
        this.getForumScript().createPost(post);
        ForumPost.cleanupCache(this, post, CacheCleanupReason.CREATE);
        ForumPost.addCache(this, post);
    }

    /**
     * @see ForumScript#getPostCount(String) Documentation for this method
     */
    public int getPostCount(String username) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.POST_COUNT, username)) {
            return (Integer) getCache().get(CacheGroup.POST_COUNT, username);
        }
        int count = this.getForumScript().getPostCount(username);
        getCache().put(CacheGroup.POST_COUNT, username, count);
        return count;
    }

    /**
     * @see ForumScript#getPostCountInThread(int) Documentation for this method
     */
    public int getPostCountInThread(int threadid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.POST_COUNT_THREAD, threadid)) {
            return (Integer) getCache().get(CacheGroup.POST_COUNT_THREAD, threadid);
        }
        int count = this.getForumScript().getPostCountInThread(threadid);
        getCache().put(CacheGroup.POST_COUNT_THREAD, threadid, count);
        return count;
    }

    /**
     * @see ForumScript#getTotalPostCount() Documentation for this method
     */
    public int getTotalPostCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.POST_COUNT_TOTAL)) {
            return (Integer) getCache().get(CacheGroup.POST_COUNT_TOTAL);
        }
        int count = this.getForumScript().getTotalPostCount();
        getCache().put(CacheGroup.POST_COUNT_TOTAL, count);
        return count;
    }

    /**
     * @see ForumScript#getLastPost() Documentation for this method
     */
    public ForumPost getLastPost() throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.POST_LAST)) {
            return (ForumPost) getCache().get(CacheGroup.POST_LAST);
        }
        ForumPost post = this.getForumScript().getLastPost();
        getCache().put(CacheGroup.POST_LAST, post);
        return post;
    }

    /**
     * @see ForumScript#getLastUserPost(String) Documentation for this method
     */
    public ForumPost getLastUserPost(String username) throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.POST_LAST_USER, username)) {
            return (ForumPost) getCache().get(CacheGroup.POST_LAST_USER, username);
        }
        ForumPost post = this.getForumScript().getLastUserPost(username);
        getCache().put(CacheGroup.POST_LAST_USER, username, post);
        return post;
    }

    /**
     * @see ForumScript#getTotalThreadCount() Documentation for this method
     */
    public int getTotalThreadCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.THREAD_COUNT_TOTAL)) {
            return (Integer) getCache().get(CacheGroup.THREAD_COUNT_TOTAL);
        }
        int count = this.getForumScript().getTotalThreadCount();
        getCache().put(CacheGroup.THREAD_COUNT_TOTAL, count);
        return count;
    }

    /**
     * @see ForumScript#getThreadCount(String) Documentation for this method
     */
    public int getThreadCount(String username) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.THREAD_COUNT, username)) {
            return (Integer) getCache().get(CacheGroup.THREAD_COUNT, username);
        }
        int count = this.getForumScript().getThreadCount(username);
        getCache().put(CacheGroup.THREAD_COUNT, username, count);
        return count;
    }

    /**
     * @see ForumScript#getLastThread() Documentation for this method
     */
    public ForumThread getLastThread() throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.THREAD_LAST)) {
            return (ForumThread) getCache().get(CacheGroup.THREAD_LAST);
        }
        ForumThread thread = this.getForumScript().getLastThread();
        getCache().put(CacheGroup.THREAD_LAST, thread);
        return thread;
    }

    /**
     * @see ForumScript#getLastUserThread(String) Documentation for this method
     */
    public ForumThread getLastUserThread(String username) throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.THREAD_LAST_USER, username)) {
            return (ForumThread) getCache().get(CacheGroup.THREAD_LAST_USER, username);
        }
        ForumThread thread = this.getForumScript().getLastUserThread(username);
        getCache().put(CacheGroup.THREAD_LAST_USER, username, thread);
        return thread;
    }

    /**
     * @see ForumScript#getThread(int) Documentation for this method
     */
    public ForumThread getThread(int threadID) throws UnsupportedMethod, SQLException {
        if (ForumThread.hasCache(this, threadID)) {
            return ForumThread.getCache(this, threadID);
        }
        ForumThread thread = this.getForumScript().getThread(threadID);
        ForumThread.addCache(this, thread);
        return thread;
    }

    /**
     * @see ForumScript#getThreads(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumThread> getThreads(int limit) throws UnsupportedMethod, SQLException {
        if (getCache().contains(CacheGroup.THREAD_LIST)) {
            List<ForumThread> threads = (List<ForumThread>) getCache().get(CacheGroup.THREAD_LIST);
            if (threads.size() == ((limit == 0) ? getTotalThreadCount() : limit)) {
                return threads;
            } else if ((threads.size() > limit) && (limit != 0)) {
                return threads.subList(0, limit);
            }
        }
        List<ForumThread> threads = this.getForumScript().getThreads(limit);
        getCache().put(CacheGroup.THREAD_LIST, threads);
        return threads;
    }

    /**
     * @see ForumScript#getThreadsFromBoard(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumThread> getThreadsFromBoard(int boardid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_THREADS, boardid) && (limit != 0)) {
            List<ForumThread> threads = (List<ForumThread>) getCache().get(CacheGroup.BOARD_THREADS, boardid);
            if (threads.size() == limit) {
                return threads;
            } else if ((threads.size() > limit) && (limit != 0)) {
                return threads.subList(0, limit);
            }
        }
        List<ForumThread> threads = getForumScript().getThreadsFromBoard(boardid, limit);
        getCache().put(CacheGroup.BOARD_THREADS, boardid, threads);
        return threads;
    }

    /**
     * @see ForumScript#getUserThreads(String, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumThread> getUserThreads(String username, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.THREAD_LIST_USER, username)) {
            List<ForumThread> threads = (List<ForumThread>) getCache().get(CacheGroup.THREAD_LIST_USER, username);
            if (threads.size() == ((limit == 0) ? getThreadCount(username) : limit)) {
                return threads;
            } else if ((threads.size() > limit) && (limit != 0)) {
                return threads.subList(0, limit);
            }
        }
        List<ForumThread> threads = getForumScript().getUserThreads(username, limit);
        getCache().put(CacheGroup.THREAD_LIST_USER, username, threads);
        return threads;
    }

    /**
     * @see ForumScript#updateThread(ForumThread) Documentation for this method
     */
    public void updateThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        this.getForumScript().updateThread(thread);
        ForumThread.cleanupCache(this, thread, CacheCleanupReason.UPDATE);
        ForumThread.addCache(this, thread);
    }

    /**
     * @see ForumScript#createThread(ForumThread) Documentation for this method
     */
    public void createThread(ForumThread thread) throws SQLException, UnsupportedMethod {
        this.getForumScript().createThread(thread);
        ForumThread.cleanupCache(this, thread, CacheCleanupReason.CREATE);
        ForumThread.addCache(this, thread);
    }

    /**
     * @see ForumScript#getBoards(int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumBoard> getBoards(int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_LIST)) {
            List<ForumBoard> boards = (List<ForumBoard>) getCache().get(CacheGroup.BOARD_LIST);
            if (boards.size() == ((limit == 0) ? getBoardCount() : limit)) {
                return boards;
            } else if ((boards.size() > limit) && (limit != 0)) {
                return boards.subList(0, limit);
            }
        }
        List<ForumBoard> boards = getForumScript().getBoards(limit);
        getCache().put(CacheGroup.BOARD_LIST, boards);
        return boards;
    }

    /**
     * @see ForumScript#getBoardCount() Documentation for this method
     */
    public int getBoardCount() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_COUNT)) {
            return (Integer) getCache().get(CacheGroup.BOARD_COUNT);
        }
        int count = getForumScript().getBoardCount();
        getCache().put(CacheGroup.BOARD_COUNT, count);
        return count;
    }

    /**
     * @see ForumScript#getSubBoardCount(int) Documentation for this method
     */
    public int getSubBoardCount(int boardid) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_SUB_COUNT, boardid)) {
            return (Integer) getCache().get(CacheGroup.BOARD_SUB_COUNT, boardid);
        }
        int count = getForumScript().getSubBoardCount(boardid);
        getCache().put(CacheGroup.BOARD_SUB_COUNT, boardid, count);
        return count;
    }

    /**
     * @see ForumScript#getSubBoards(int, int) Documentation for this method
     */
    @SuppressWarnings("unchecked")
    public List<ForumBoard> getSubBoards(int boardid, int limit) throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.BOARD_SUBS, boardid)) {
            List<ForumBoard> boards = (List<ForumBoard>) getCache().get(CacheGroup.BOARD_SUBS, boardid);
            if (boards.size() == ((limit == 0) ? getSubBoardCount(boardid) : limit)) {
                return boards;
            } else if ((boards.size() > limit) && (limit != 0)) {
                return boards.subList(0, limit);
            }
        }
        List<ForumBoard> boards = getForumScript().getSubBoards(boardid, limit);
        getCache().put(CacheGroup.BOARD_SUBS, boardid, boards);
        return boards;
    }

    /**
     * @see ForumScript#getBoard(int) Documentation for this method
     */
    public ForumBoard getBoard(int boardID) throws UnsupportedMethod {
        if (ForumBoard.hasCache(this, boardID)) {
            return ForumBoard.getCache(this, boardID);
        }
        ForumBoard board = getForumScript().getBoard(boardID);
        ForumBoard.addCache(this, board);
        return board;
    }

    /**
     * @see ForumScript#updateBoard(ForumBoard) Documentation for this method
     */
    public void updateBoard(ForumBoard board) throws UnsupportedMethod, SQLException {
        getForumScript().updateBoard(board);
        ForumBoard.cleanupCache(this, board, CacheCleanupReason.UPDATE);
        ForumBoard.addCache(this, board);
    }

    /**
     * @see ForumScript#createBoard(ForumBoard) Documentation for this method
     */
    public void createBoard(ForumBoard board) throws UnsupportedMethod, SQLException {
        getForumScript().createBoard(board);
        ForumBoard.cleanupCache(this, board, CacheCleanupReason.CREATE);
        ForumBoard.addCache(this, board);
    }

    /**
     * @see ForumScript#getForumURL() Documentation for this method
     */
    public String getForumURL() throws UnsupportedMethod {
        if (getCache().contains(CacheGroup.URL_FORUM)) {
            return (String) getCache().get(CacheGroup.URL_FORUM);
        }
        String url = this.getForumScript().getForumURL();
        getCache().put(CacheGroup.URL_FORUM, url);
        return url;
    }

    /**
     * @see ForumScript#getUser(String) Documentation for this method
     */
    @Override
    public ForumUser getUser(String username) throws UnsupportedMethod, SQLException {
        return (ForumUser) super.getUser(username);
    }

    /**
     * @see ForumScript#getUser(int) Documentation for this method
     */
    @Override
    public ForumUser getUser(int userid) throws UnsupportedMethod, SQLException {
        return (ForumUser) super.getUser(userid);
    }

    /**
     * @see ForumScript#getLastRegUser() Documentation for this method
     */
    @Override
    public ForumUser getLastRegUser() throws UnsupportedMethod, SQLException {
        return (ForumUser) super.getLastRegUser();
    }

    /**
     * @see ForumScript#updateUser(ForumUser) Documentation for this method
     */
    public void updateUser(ForumUser user) throws SQLException, UnsupportedMethod {
        getForumScript().updateUser(user);
        ScriptUser.cleanupCache(this, user, CacheCleanupReason.UPDATE);
        ScriptUser.addCache(this, user);
    }

    /**
     * @see ForumScript#createUser(ForumUser) Documentation for this method
     */
    public void createUser(ForumUser user) throws SQLException, UnsupportedMethod {
        getForumScript().createUser(user);
        ScriptUser.cleanupCache(this, user, CacheCleanupReason.CREATE);
        ScriptUser.addCache(this, user);
    }
}
