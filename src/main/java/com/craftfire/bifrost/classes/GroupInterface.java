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
package com.craftfire.bifrost.classes;

import com.craftfire.bifrost.exceptions.UnsupportedFunction;

import java.sql.SQLException;
import java.util.List;

public abstract interface GroupInterface {
    public int getID();

    public void setID(int id);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public List<ScriptUser> getUsers();

    public void setUsers(List<ScriptUser> users);

    public int getUserCount();

    public void setUserCount(int usercount);

    public void updateGroup() throws SQLException, UnsupportedFunction;

    public void createGroup() throws SQLException, UnsupportedFunction;
}
