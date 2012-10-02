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
package com.craftfire.bifrost.classes.general;

import com.craftfire.bifrost.classes.cms.CMSHandle;
import com.craftfire.bifrost.classes.forum.ForumHandle;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

import java.sql.SQLException;

/**
 * Generic methods that are being used by most of the script classes.
 */
public class GenericMethods {
    public int id;
    public final ScriptHandle handle;

    /**
     * Default constructor.
     *
     * @param handle  the script handle
     */
    public GenericMethods(ScriptHandle handle) {
        this.handle = handle;
    }

    /**
     * Returns the ID.
     *
     * @return the ID
     */
    public int getID() {
        return this.id;
    }

    /**
     * Sets the ID.
     *
     * @param id  the ID
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Returns the handle.
     *
     * @return the handle
     */
    public ScriptHandle getHandle() {
        return this.handle;
    }

    /**
     * Returns the forum handle.
     *
     * @return the forum handle
     */
    public ForumHandle getForumHandle() {
        return (ForumHandle) this.handle;
    }

    /**
     * Returns the CMS handle.
     *
     * @return the CMS handle
     */
    public CMSHandle getCMSHandle() {
        return (CMSHandle) this.handle;
    }

    /**
     * This method applies the changed values of the object into the database. Should be run after changing any values in the object.
     * <p>
     * It should <b>not</b> be run when creating a new object, only when editing an already existing object.
     *
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void update() throws SQLException,UnsupportedMethod {
        throw new UnsupportedMethod();
    }

    /**
     * This method puts the object into the database. Should be run after creating a new object.
     * <p>
     * It should <b>not</b> be run when updating an object, only when creating a new object.
     *
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    public void create() throws SQLException, UnsupportedMethod {
        throw new UnsupportedMethod();
    }
}
