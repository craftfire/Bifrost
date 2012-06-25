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
package com.craftfire.authapi.classes;

import java.sql.SQLException;
import java.util.Date;

import com.craftfire.authapi.exceptions.UnsupportedFunction;

public class Ban implements BanInterface {
    private final Script script;
    private String name, email, ip, reason, notes;
    private int banid, userid;
    private Date startdate, enddate;

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

    @Override
    public int getID() {
        return this.banid;
    }
    @Override
    public void setID(int id) {
        this.banid = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getUserID() {
        return this.userid;
    }

    @Override
    public void setUserID(int userid) {
        this.userid = userid;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getIP() {
        return this.ip;
    }

    @Override public void setIP(String ip) {
        this.ip = ip;
    }

    @Override
    public long getTimeLength() {
        if (! isPermanent()) {
            return (this.enddate.getTime() - this.startdate.getTime());
        }
        return 0;
    }

    @Override
    public long getTimeRemaining() {
        if (! isPermanent()) {
            Date now = new Date();
            return (this.enddate.getTime() - now.getTime());
        }
        return 0;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String getNotes() {
        return this.notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Date getStartDate() {
        return this.startdate;
    }

    @Override
    public void setStartDate(Date startdate) {
        this.startdate = startdate;
    }

    @Override
    public Date getEndDate() {
        return this.enddate;
    }

    @Override
    public void setEndDate(Date enddate) {
        this.enddate = enddate;
    }

    @Override
    public boolean isPermanent() {
        return getEndDate() == null;
    }

    @Override
    public void updateBan() throws SQLException, UnsupportedFunction {
        this.script.updateBan(this);
    }

    @Override
    public void addBan() throws SQLException, UnsupportedFunction {
        this.script.addBan(this);
    }
}
