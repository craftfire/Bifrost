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

public abstract interface BanInterface {
    public int getID();

    public void setID(int id);

    public String getName();

    public void setName(String name);

    public int getUserID();

    public void setUserID(int userid);

    public String getEmail();

    public void setEmail(String email);

    public String getIP();

    public void setIP(String ip);

    public long getTimeLength();

    public long getTimeRemaining();

    public String getReason();

    public void setReason(String reason);

    public String getNotes();

    public void setNotes(String notes);

    public Date getStartDate();

    public void setStartDate(Date startdate);

    public Date getEndDate();

    public void setEndDate(Date enddate);

    public boolean isPermanent();

    public void updateBan() throws SQLException, UnsupportedFunction;

    public void addBan() throws SQLException, UnsupportedFunction;
}
