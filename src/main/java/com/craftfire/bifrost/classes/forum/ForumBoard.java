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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Category;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

/**
 * This class should only be used with a forum board/category.
 * <p>
 * The second constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the board, run {@see #updateBoard()}.
 * <p>
 * When creating a new ForumBoard make sure you use the correct constructor:
 * {@see #ForumBoard(com.craftfire.bifrost.script.Script, String, int)}.
 * <p>
 * Remember to run {@see #createBoard()} after creating a board to insert it into the script.
 */
public class ForumBoard extends Category {

    /**
     * This constructor may be used when creating a new board for the script.
     * <p>
     * Remember to run {@see #createBoard()} after creating a board to insert it into the script.
     * 
     * @param script  the script the board is created for
     */
    public ForumBoard(Script script) {
        super(script);
    }

    /**
     * This constructor should only be used by the script and not by that library user.
     * 
     * @param script   the script the board comes from
     * @param boardid  the ID of the board
     */
    public ForumBoard(Script script, int boardid) {
        super(script, boardid);
    }

    /**
     * This constructor should be preferred when creating a new board for the script.
     * <p>
     * Remember to run {@see #createBoard()} after creating a board to insert it into the script.
     * 
     * @param script    the script the board is created for
     * @param name      the name of the board
     * @param parentid  the id of the parent board or 0 if none
     */
    public ForumBoard(Script script, String name, int parentid) {
        super(script, name, parentid);
    }

    /**
     * Returns the list of messages contained in this category.
     * <p>
     * For ForumBoard it has the same effect as {@see #getThreads(int)}
     */
    @Override
    public List<ForumThread> getMessages(int limit) throws UnsupportedMethod {
        return getThreads(limit);
    }
    
    /**
     * Returns the list of threads contained in the board.
     * <p>
     * Loads the threads from a database if not cached.
     * 
     * @param limit               how many threads should be returned, 0 = returns all
     * @return                    the list of threads
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public List<ForumThread> getThreads(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getThreadsFromBoard(getID(), limit);
    }

    /**
     * This method should be run after changing any board values.
     * <p>
     * It should <b>not</b> be run when creating a new board, only when editing an already existing board.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void updateBoard() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).updateBoard(this);
    }

    /**
     * This method should be run after creating a new board.
     * <p>
     * It should <b>not</b> be run when updating a board, only when creating a new board.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void createBoard() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).createBoard(this);
    }

    /**
     * Returns <code>true</code> if the handle contains a board cache with the given id parameter,
     * <code>false</code> if not.
     * 
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, int id) {
        return handle.getCache().contains(CacheGroup.BOARD, id);
    }

    /**
     * Adds a board to the cache with the given script handle
     *
     * @param handle  the script handle
     * @param board   the board object
     */
    public static void addCache(ScriptHandle handle, ForumBoard board) {
        handle.getCache().putMetadatable(CacheGroup.BOARD, board.getID(), board);
        handle.getCache().setMetadata(CacheGroup.BOARD, board.getID(), "bifrost-cache.old-parent", board.getParentID());
    }

    /**
     * Returns the board object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the board
     * @return        board object if cache was found, <code>null</code> if no cache was found
     */
    public static ForumBoard getCache(ScriptHandle handle, int id) {
        if (handle.getCache().contains(CacheGroup.BOARD, id)) {
            return (ForumBoard) handle.getCache().get(CacheGroup.BOARD, id);
        }
        return null;
    }

    public static void cleanupCache(ScriptHandle handle, ForumBoard board, CacheCleanupReason reason) {
        handle.getCache().remove(CacheGroup.BOARD_SUBS, board.getParentID());
        switch (reason) {
        case OTHER:
            handle.getCache().remove(CacheGroup.BOARD_SUBS, handle.getCache().getMetadata(CacheGroup.BOARD, board.getID(), "bifrost-cache.old-parent"));
            /* Passes through */
        case CREATE:
            handle.getCache().clear(CacheGroup.BOARD_COUNT);
            handle.getCache().clear(CacheGroup.BOARD_LIST);
            break;
        case UPDATE:
            handle.getCache().remove(CacheGroup.BOARD_SUBS, handle.getCache().getMetadata(CacheGroup.BOARD, board.getID(), "bifrost-cache.old-parent"));
            break;
        }
    }

    /* (non-javadoc)
     * @see Category#getParent()
     */
    @Override
    public ForumBoard getParent() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getBoard(getParentID());
    }

    /* (non-javadoc)
     * @see Category#getSubcategories(int)
     */
    @Override
    public List<ForumBoard> getSubcategories(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getSubBoards(getID(), limit);
    }

}
