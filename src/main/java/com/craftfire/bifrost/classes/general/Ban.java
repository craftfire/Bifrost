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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

import java.sql.SQLException;
import java.util.Date;

public class Ban {
    private String name, email, ip, reason, notes;
    private int banid, userid;
    private Date startdate, enddate;
    private final Script script;

    public Ban(Script script, int banid, String name, String email, String ip) {
        this.script = script;
        this.banid = banid;
        this.name = name;
        this.email = email;
        this.ip = ip;
    }

    public Ban(Script script, String name, String email, String ip) {
        this.script = script;
        this.name = name;
        this.email = email;
        this.ip = ip;
    }

    public int getID() {
        return this.banid;
    }

    public void setID(int id) {
        this.banid = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserID() {
        return this.userid;
    }

    public void setUserID(int userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIP() {
        return this.ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public long getTimeLength() {
        if (! isPermanent()) {
            return (this.enddate.getTime() - this.startdate.getTime());
        }
        return 0;
    }

    public long getTimeRemaining() {
        if (! isPermanent()) {
            Date now = new Date();
            return (this.enddate.getTime() - now.getTime());
        }
        return 0;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getStartDate() {
        return this.startdate;
    }

    public void setStartDate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEndDate() {
        return this.enddate;
    }

    public void setEndDate(Date enddate) {
        this.enddate = enddate;
    }

    public boolean isPermanent() {
        return getEndDate() == null;
    }

    public void updateBan() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).updateBan(this);
    }

    public void addBan() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).addBan(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.BAN, id);
    }

    public static void addCache(ScriptHandle handle, Ban ban) {
        handle.getCache().put(CacheGroup.BAN, ban.getID(), ban);
    }

    @SuppressWarnings("unchecked")
    public static Ban getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.BAN, id)) {
            return (Ban) handle.getCache().get(CacheGroup.BAN, id);
        }
        return null;
    }
}
