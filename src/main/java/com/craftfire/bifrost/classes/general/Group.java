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

import java.sql.SQLException;
import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

public class Group implements IDable {
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

    public void setID(int id) {
        this.groupid = id;
    }

    public String getName() {
        return this.groupname;
    }

    public void setName(String name) {
        this.groupname = name;
    }

    public String getDescription() {
        return this.groupdescription;
    }

    public void setDescription(String description) {
        this.groupdescription = description;
    }

    public List<ScriptUser> getUsers() {
        return this.users;
    }

    public void setUsers(List<ScriptUser> users) {
        this.users = users;
    }

    public int getUserCount() {
        return this.usercount;
    }

    public void setUserCount(int usercount) {
        this.usercount = usercount;
    }

    public void updateGroup() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).updateGroup(this);
    }

    public void createGroup() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).createGroup(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.GROUP, id);
    }

    public static void addCache(ScriptHandle handle, Group group) {
        handle.getCache().put(CacheGroup.GROUP, group.getID(), group);
    }

    @SuppressWarnings("unchecked")
    public static Group getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.GROUP, id)) {
            return (Group) handle.getCache().get(CacheGroup.GROUP, id);
        }
        return null;
    }
}
