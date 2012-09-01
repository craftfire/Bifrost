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

import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.Category;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

public class ForumBoard extends Category {

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
     * This constructor should be used when creating a new board for the script.
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
     * @see Category#getMessages(int)
     * @see #getThreads(int)
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
        handle.getCache().put(CacheGroup.BOARD, board.getID(), board);
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

    /**
     * @see Category#getParent()
     */
    @Override
    public ForumBoard getParent() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getBoard(getParentID());
    }

    /**
     * @see Category#getSubcategories(int)
     */
    @Override
    public List<ForumBoard> getSubcategories(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getSubBoards(getID(), limit);
    }

}
