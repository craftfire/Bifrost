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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

//TODO: Fix Javadoc
/**
 * This class contains methods relevant to methods to use for a forum user.
 *
 * @see ScriptUser Documentation of all the methods
 */
public class ForumUser extends ScriptUser {

    /**
     * @see ForumUser#ForumUser(ForumScript, int, String, String) Documentation for this constructor
     */
    public ForumUser(ForumScript script, int userid, String username, String password) {
        super(script, userid, username, password);
    }

    /**
     * @see ForumUser#ForumUser(ForumHandle, String, String) Documentation for this constructor
     */
    public ForumUser(ForumHandle handle, String username, String password) {
        super(handle, username, password);
    }

    @Override
    public ForumHandle getHandle() {
        return (ForumHandle) super.getHandle();
    }

    /**
     * Returns the last forum post of this user.
     *
     * @see ForumScript#getPostCount(String) Documentation for this method
     */
    public int getPostCount() throws UnsupportedMethod {
        return getHandle().getPostCount(getUsername());
    }

    /**
     * Returns the last forum post of this user.
     *
     * @see ForumScript#getThreadCount(String) Documentation for this method
     */
    public int getThreadCount() throws UnsupportedMethod {
        return getHandle().getThreadCount(getUsername());
    }

    /**
     * Returns the last forum post of this user.
     *
     * @see ForumScript#getLastUserThread(String) Documentation for this method
     */
    public ForumThread getLastThread() throws UnsupportedMethod, SQLException {
        return getHandle().getLastUserThread(getUsername());
    }

    /**
     * Returns the last forum post of this user.
     *
     * @see ForumScript#getLastUserPost(String) Documentation for this method
     */
    public ForumPost getLastPost() throws UnsupportedMethod, SQLException {
        return getHandle().getLastUserPost(getUsername());
    }

    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getHandle().updateUser(this);
    }

    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getHandle().createUser(this);
    }
}
