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

import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.exceptions.ScriptException;
import com.craftfire.bifrost.classes.general.Script;
import com.craftfire.bifrost.classes.general.ScriptHandle;

/**
 * This class should only be used with a forum user.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@link #update()}.
 * <p>
 * When creating a new forum user make sure you use the correct constructor:
 * {@link #ForumUser(ForumHandle, String, String)}.
 * <p>
 * Remember to run {@link #create()} after creating a user to insert it into the script.
 */
public class ForumUser extends ScriptUser {

    /**
     * @see ScriptUser#ScriptUser(Script, int, String, String) Documentation for this constructor
     */
    public ForumUser(ForumScript script, int userid, String username, String password) {
        super(script, userid, username, password);
    }

    /**
     * @see ScriptUser#ScriptUser(ScriptHandle, String, String) Documentation for this constructor
     */
    public ForumUser(ForumHandle handle, String username, String password) {
        super(handle, username, password);
    }

    @Override
    public ForumHandle getHandle() {
        return (ForumHandle) super.getHandle();
    }

    /**
     * Returns the post count of this user.
     *
     * @return                    post count
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getPostCount() throws ScriptException {
        return getHandle().getPostCount(getUsername());
    }

    /**
     * Returns the thread count of this user.
     *
     * @return                    thread count
     * @throws ScriptException  if the method is not supported by the script
     */
    public int getThreadCount() throws ScriptException {
        return getHandle().getThreadCount(getUsername());
    }

    /**
     * Returns the last forum thread of this user, returns null if no threads were found.
     *
     * @return                    last forum thread, null if empty
     * @throws SQLException       if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     */
    public ForumThread getLastThread() throws ScriptException, SQLException {
        return getHandle().getLastUserThread(getUsername());
    }

    /**
     * Returns the last forum post of this user, return null if no posts were found.
     *
     * @return                    last forum post, null if empty
     * @throws SQLException       if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     */
    public ForumPost getLastPost() throws ScriptException, SQLException {
        return getHandle().getLastUserPost(getUsername());
    }

    /**
     * This method should be run after changing any forum user values.
     * <p>
     * It should <b>not</b> be run when creating a new forum user, only when editing an already existing forum user.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     * @see    #create()          for creating an user
     */
    @Override
    public void update() throws SQLException, ScriptException {
        getHandle().updateUser(this);
    }

    /**
     * This method should be run after creating a new forum user.
     * <p>
     * It should <b>not</b> be run when updating a forum user, only when creating a forum user.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     * @see    #update()          for updating an user
     */
    @Override
    public void create() throws SQLException, ScriptException {
        getHandle().createUser(this);
    }
}
