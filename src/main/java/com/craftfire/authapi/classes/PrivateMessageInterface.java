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
import java.util.List;

import com.craftfire.authapi.exceptions.UnsupportedFunction;

public abstract interface PrivateMessageInterface {
    public int getID();

    public void setID(int id);

    public ScriptUser getSender();

    public void setSender(ScriptUser user);

    public List<ScriptUser> getRecipients();

    public void setRecipients(List<ScriptUser> recipients);

    public Date getDate();

    public void setDate(Date date);

    public String getSubject();

    public void setSubject(String subject);

    public String getBody();

    public void setBody(String body);

    public boolean isDeletedBySender();

    public void setDeletedBySender(boolean deleted);

    public boolean isRead(ScriptUser recipient);

    public void setRead(ScriptUser recipient, boolean read);

    public boolean isNew(ScriptUser recipient);

    public void setNew(ScriptUser recipient, boolean isnew);

    public boolean isDeleted(ScriptUser recipient);

    public void setDeleted(ScriptUser recipient, boolean deleted);

    public void updatePrivateMessage() throws SQLException, UnsupportedFunction;

    public void createPrivateMessage() throws SQLException, UnsupportedFunction;
}
