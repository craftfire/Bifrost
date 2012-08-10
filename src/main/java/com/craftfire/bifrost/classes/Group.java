/*
 * This file is part of AuthAPI.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * AuthAPI is licensed under the GNU Lesser General Public License.
 *
 * AuthAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.bifrost.classes;

import com.craftfire.bifrost.AuthAPI;
import com.craftfire.bifrost.ScriptHandle;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;

import java.sql.SQLException;
import java.util.List;

public class Group implements GroupInterface {
    private int groupid, usercount;
    private String groupname, groupdescription;
    private List<ScriptUser> users;
    private final Script script;

    public Group(Script script, int groupid, String groupname) {
        this.script = script;
        this.groupid = groupid;
        this.groupname = groupname;
    }

    public Group(Script script, String groupname) {
        this.script = script;
        this.groupname = groupname;
    }

    @Override
    public int getID() {
        return this.groupid;
    }

    @Override
    public void setID(int id) {
        this.groupid = id;
    }

    @Override
    public String getName() {
        return this.groupname;
    }

    @Override
    public void setName(String name) {
        this.groupname = name;
    }

    @Override
    public String getDescription() {
        return this.groupdescription;
    }

    @Override
    public void setDescription(String description) {
        this.groupdescription = description;
    }

    @Override
    public List<ScriptUser> getUsers() {
        return this.users;
    }

    @Override
    public void setUsers(List<ScriptUser> users) {
        this.users = users;
    }

    @Override
    public int getUserCount() {
        return this.usercount;
    }

    @Override
    public void setUserCount(int usercount) {
        this.usercount = usercount;
    }

    @Override
    public void updateGroup() throws SQLException, UnsupportedFunction {
        AuthAPI.getInstance().getScriptAPI().getHandle(this.script.getScript()).updateGroup(this);
    }

    @Override
    public void createGroup() throws SQLException, UnsupportedFunction {
        AuthAPI.getInstance().getScriptAPI().getHandle(this.script.getScript()).createGroup(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.GROUP, id);
    }

    public static void addCache(ScriptHandle handle, Group group) {
        handle.getCache().put(CacheGroup.GROUP, group.getID(), group);
    }

    @SuppressWarnings("unchecked")
    public static Group getCache(ScriptHandle handle, Object id) {
        Group temp = null;
        if (handle.getCache().contains(CacheGroup.GROUP, id)) {
            temp = (Group) handle.getCache().get(CacheGroup.GROUP, id);
        }
        return temp;
    }
}
