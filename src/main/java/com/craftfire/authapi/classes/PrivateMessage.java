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

import com.craftfire.authapi.enums.CacheGroup;
import com.craftfire.authapi.exceptions.UnsupportedFunction;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PrivateMessage implements PrivateMessageInterface {
    private final Script script;
    private int pmid;
    private String subject, body;
    private ScriptUser sender;
    private List<ScriptUser> recipients;
    private HashMap<ScriptUser, Boolean> isnew = new HashMap<ScriptUser, Boolean>();
    private HashMap<ScriptUser, Boolean> read = new HashMap<ScriptUser, Boolean>();
    private HashMap<ScriptUser, Boolean> deleted = new HashMap<ScriptUser, Boolean>();
    private Date date;
    private boolean deletedbysender;

    public PrivateMessage(Script script, int pmid) {
        this.script = script;
        this.pmid = pmid;
    }

    public PrivateMessage(Script script, ScriptUser sender, List<ScriptUser> recipients) {
        this.script = script;
        this.sender = sender;
        this.recipients = recipients;
    }

    @Override
    public int getID() {
        return this.pmid;
    }

    @Override
    public void setID(int id) {
        this.pmid = id;
    }

    @Override
    public ScriptUser getSender() {
        return this.sender;
    }

    @Override
    public void setSender(ScriptUser user) {
        this.sender = user;
    }

    @Override
    public List<ScriptUser> getRecipients() {
        return this.recipients;
    }

    @Override
    public void setRecipients(List<ScriptUser> users) {
        this.recipients = users;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean isDeletedBySender() {
        return this.deletedbysender;
    }

    @Override
    public void setDeletedBySender(boolean deleted) {
        this.deletedbysender = deleted;
    }

    @Override
    public boolean isRead(ScriptUser recipient) {
        if (this.read.containsKey(recipient)) {
            return this.read.get(recipient);
        }
        return false;
    }

    @Override
    public void setRead(ScriptUser recipient, boolean read) {
        this.read.put(recipient, read);
    }

    @Override
    public boolean isNew(ScriptUser recipient) {
        if (this.isnew.containsKey(recipient)) {
            return this.isnew.get(recipient);
        }
        return false;
    }

    @Override
    public void setNew(ScriptUser recipient, boolean isnew) {
        this.isnew.put(recipient, isnew);
    }

    @Override
    public boolean isDeleted(ScriptUser recipient) {
        if (this.deleted.containsKey(recipient)) {
            return this.deleted.get(recipient);
        }
        return false;
    }

    @Override
    public void setDeleted(ScriptUser recipient, boolean deleted) {
        this.deleted.put(recipient, deleted);
    }

    @Override
    public void updatePrivateMessage() throws SQLException, UnsupportedFunction {
        this.script.updatePrivateMessage(this);
    }

    @Override
    public void createPrivateMessage() throws SQLException, UnsupportedFunction {
        this.script.createPrivateMessage(this);
    }

    public static void addCache(PrivateMessage privateMessage) {
        Cache.put(CacheGroup.PRIVATEMESSAGE, privateMessage.getID(), privateMessage);
    }

    @SuppressWarnings("unchecked")
    public static PrivateMessage getCache(int id) throws UnsupportedFunction {
        PrivateMessage temp;
        if (Cache.contains(CacheGroup.PRIVATEMESSAGE, id)) {
            temp = (PrivateMessage) Cache.get(CacheGroup.PRIVATEMESSAGE, id);
        } else {
            temp = Cache.getScript().getPM(id);
            Cache.put(CacheGroup.PRIVATEMESSAGE, id, temp);
        }
        return temp;
    }
}
