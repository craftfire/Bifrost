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
import java.util.HashMap;
import java.util.List;

public class PrivateMessage {
    private int pmid;
    private String subject, body;
    private ScriptUser sender;
    private List<ScriptUser> recipients;
    private HashMap<ScriptUser, Boolean> isnew = new HashMap<ScriptUser, Boolean>();
    private HashMap<ScriptUser, Boolean> read = new HashMap<ScriptUser, Boolean>();
    private HashMap<ScriptUser, Boolean> deleted = new HashMap<ScriptUser, Boolean>();
    private Date date;
    private boolean deletedbysender;
    private final Script script;

    public PrivateMessage(Script script, int pmid) {
        this.script = script;
        this.pmid = pmid;
    }

    public PrivateMessage(Script script, ScriptUser sender, List<ScriptUser> recipients) {
        this.script = script;
        this.sender = sender;
        this.recipients = recipients;
    }

    /**
     * Returns the ID of the PrivateMessage, this is unique per PrivateMessage.
     *
     * @return ID of the PrivateMessage
     */
    public int getID() {
        return this.pmid;
    }

    public void setID(int id) {
        this.pmid = id;
    }

    public ScriptUser getSender() {
        return this.sender;
    }

    public void setSender(ScriptUser user) {
        this.sender = user;
    }

    public List<ScriptUser> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(List<ScriptUser> users) {
        this.recipients = users;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isDeletedBySender() {
        return this.deletedbysender;
    }

    public void setDeletedBySender(boolean deleted) {
        this.deletedbysender = deleted;
    }

    public boolean isRead(ScriptUser recipient) {
        if (this.read.containsKey(recipient)) {
            return this.read.get(recipient);
        }
        return false;
    }

    public void setRead(ScriptUser recipient, boolean read) {
        this.read.put(recipient, read);
    }

    public boolean isNew(ScriptUser recipient) {
        if (this.isnew.containsKey(recipient)) {
            return this.isnew.get(recipient);
        }
        return false;
    }

    public void setNew(ScriptUser recipient, boolean isnew) {
        this.isnew.put(recipient, isnew);
    }

    public boolean isDeleted(ScriptUser recipient) {
        if (this.deleted.containsKey(recipient)) {
            return this.deleted.get(recipient);
        }
        return false;
    }

    public void setDeleted(ScriptUser recipient, boolean deleted) {
        this.deleted.put(recipient, deleted);
    }

    public void updatePrivateMessage() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).updatePrivateMessage(this);
    }

    public void createPrivateMessage() throws SQLException, UnsupportedFunction {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).createPrivateMessage(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.PM, id);
    }

    public static void addCache(ScriptHandle handle, PrivateMessage privateMessage) {
        handle.getCache().put(CacheGroup.PM, privateMessage.getID(), privateMessage);
    }

    @SuppressWarnings("unchecked")
    public static PrivateMessage getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.PM, id)) {
            return (PrivateMessage) handle.getCache().get(CacheGroup.PM, id);
        }
        return null;
    }
}
