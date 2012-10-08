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
package com.craftfire.bifrost.classes.cms;

import java.sql.SQLException;

import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.classes.general.Script;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class should only be used with a cms user.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@link #update()}.
 * <p>
 * When creating a new cms user make sure you use the correct constructor:
 * {@link #CMSUser(CMSHandle, String, String)}.
 * <p>
 * Remember to run {@link #create()} after creating a user to insert it into the script.
 */
public class CMSUser extends ScriptUser {

    /**
     * @see ScriptUser#ScriptUser(Script, int, String, String) Documentation for this constructor
     */
    public CMSUser(CMSScript script, int userid, String username, String password) {
        super(script, userid, username, password);
    }

    /**
     * @see ScriptUser#ScriptUser(ScriptHandle, String, String) Documentation for this constructor
     */
    public CMSUser(CMSHandle handle, String username, String password) {
        super(handle, username, password);
    }

    @Override
    public CMSHandle getHandle() {
        return (CMSHandle) super.getHandle();
    }

    /**
     * Returns the comment count of this user.
     *
     * @return                    comment count
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getCommentCount() throws UnsupportedMethod {
        return getHandle().getUserCommentCount(getUsername());
    }

    /**
     * Returns the article count of this user.
     *
     * @return                    article count
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public int getArticleCount() throws UnsupportedMethod {
        return getHandle().getUserArticleCount(getUsername());
    }

    /**
     * Returns the last comment of this user, returns null if no comments were found.
     *
     * @return                    last comment, null if empty
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public CMSComment getLastComment() throws UnsupportedMethod {
        return getHandle().getLastUserComment(getUsername());
    }

    /**
     * Returns the last article of this user, returns null if no articles were found.
     *
     * @return                    last article, null if empty
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public CMSArticle getLastArticle() throws UnsupportedMethod {
        return getHandle().getLastUserArticle(getUsername());
    }

    /**
     * This method should be run after changing any cms user values.
     * <p>
     * It should <b>not</b> be run when creating a new cms user, only when editing an already existing cms user.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see #create()             for creating an user
     */
    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getHandle().updateUser(this);
    }

    /**
     * This method should be run after creating a new cms user.
     * <p>
     * It should <b>not</b> be run when updating a cms user, only when creating a cms user.
     *
     * @throws SQLException       if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     * @see #update()             for updating an user
     */
    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getHandle().createUser(this);
    }
}
