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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.handles.ScriptHandle;
import com.craftfire.bifrost.script.Script;

public class PrivateMessage extends Message {
    private int parentid;
    private List<ScriptUser> recipients;
    private HashMap<ScriptUser, Boolean> isnew = new HashMap<ScriptUser, Boolean>();
    private HashMap<ScriptUser, Boolean> read = new HashMap<ScriptUser, Boolean>();
    private HashMap<ScriptUser, Boolean> deleted = new HashMap<ScriptUser, Boolean>();
    private boolean deletedbysender;

    public PrivateMessage(Script script, int pmid) {
        super(script, pmid);
    }

    public PrivateMessage(Script script, ScriptUser sender, List<ScriptUser> recipients) {
        super(script);
        setAuthor(sender);
        this.recipients = recipients;
    }

    public PrivateMessage(Script script, ScriptUser sender, List<ScriptUser> recipients, int parentid) {
        super(script);
        setAuthor(sender);
        this.recipients = recipients;
        this.parentid = parentid;
    }

    /**
     * Returns the ID of the PrivateMessage, this is unique per PrivateMessage.
     *
     * @return ID of the PrivateMessage
     */
    @Override
    public int getID() {
        return super.getID();
    }

    @Override
    public void setID(int id) {
        super.setID(id);
    }

    public ScriptUser getSender() {
        return getAuthor();
    }

    public void setSender(ScriptUser user) {
        setAuthor(user);
    }

    public List<ScriptUser> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(List<ScriptUser> users) {
        this.recipients = users;
    }

    public String getSubject() {
        return getTitle();
    }

    public void setSubject(String subject) {
        setTitle(subject);
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

    public void updatePrivateMessage() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getHandle(getScript().getScript()).updatePrivateMessage(this);
    }

    public void createPrivateMessage() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getHandle(getScript().getScript()).createPrivateMessage(this);
    }

    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.PM, id);
    }

    public static void addCache(ScriptHandle handle, PrivateMessage privateMessage) {
        handle.getCache().putMetadatable(CacheGroup.PM, privateMessage.getID(), privateMessage);
        handle.getCache().setMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-parent", privateMessage.getParentID());
        if (privateMessage.getAuthor() != null) {
            handle.getCache().setMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-author", privateMessage.getAuthor().getUsername());
        } else {
            handle.getCache().removeMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-author");
        }
        if (privateMessage.getRecipients() != null) {
            List<String> recipientNames = new ArrayList<String>();
            Iterator<ScriptUser> I = privateMessage.getRecipients().iterator();
            while (I.hasNext()) {
                ScriptUser user = I.next();
                if (user != null) {
                    recipientNames.add(user.getUsername());
                }
            }
            handle.getCache().setMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-recipients", privateMessage.getRecipients());
        } else {
            handle.getCache().removeMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-recipients");
        }
    }

    public static PrivateMessage getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.PM, id)) {
            return (PrivateMessage) handle.getCache().get(CacheGroup.PM, id);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void cleanupCache(ScriptHandle handle, PrivateMessage privateMessage, CacheCleanupReason reason) {
        handle.getCache().remove(CacheGroup.PM_REPLIES, privateMessage.getParentID());
        if (privateMessage.getAuthor() != null) {
            handle.getCache().remove(CacheGroup.PM_SENT, privateMessage.getAuthor().getUsername());
            handle.getCache().remove(CacheGroup.PM_SENT_COUNT, privateMessage.getAuthor().getUsername());
        }
        if (privateMessage.getRecipients() != null) {
            Iterator<ScriptUser> I = privateMessage.getRecipients().iterator();
            while (I.hasNext()) {
                ScriptUser user = I.next();
                if (user != null) {
                    handle.getCache().remove(CacheGroup.PM_RECEIVED, user.getUsername());
                    handle.getCache().remove(CacheGroup.PM_RECEIVED_COUNT, user.getUsername());
                }
            }
        }
        switch (reason) {
        case CREATE:
            handle.getCache().clear(CacheGroup.PM_LIST);
            break;
        case OTHER:
            handle.getCache().clear(CacheGroup.PM_LIST);
            /* Passes through */
        case UPDATE:
            Object old_parentid = handle.getCache().getMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-parent");
            Object old_username = handle.getCache().getMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-author");
            List<String> old_recipients = (List<String>) handle.getCache().getMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-recipients");
            handle.getCache().remove(CacheGroup.PM_REPLIES, old_parentid);
            handle.getCache().remove(CacheGroup.PM_SENT, old_username);
            handle.getCache().remove(CacheGroup.PM_SENT_COUNT, old_username);
            if (old_recipients != null) {
                Iterator<String> I = old_recipients.iterator();
                while (I.hasNext()) {
                    String username = I.next();
                    handle.getCache().remove(CacheGroup.PM_RECEIVED, username);
                    handle.getCache().remove(CacheGroup.PM_RECEIVED_COUNT, username);
                }
            }
            break;
        }

    }

    /**
     * Private Message categories are not supported. Always returns null.
     * 
     * @return always null
     * @see    Message#getCategory()
     */
    @Override
    public Category getCategory() {
        return null;
    }

    /**
     * Returns the ID of the private message this message replies to.
     * 
     * @return the ID of the parent message.
     */
    public int getParentID() {
        return this.parentid;
    }

    /**
     * Sets the private message this message replies to.
     * 
     * @param parentid  the ID of the parent message
     */
    public void setParentID(int parentid) {
        this.parentid = parentid;
    }

    /**
     * Returns a PrivateMessage object for the private message this message replies to.
     * <p>
     * Loads it from database if not cached.
     * 
     * @see Message#getParent()
     */
    @Override
    public PrivateMessage getParent() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getHandle(getScript().getScript()).getPM(this.parentid);
    }

    /**
     * Returns the list of PrivateMessages replying to this message.
     * 
     * @see com.craftfire.bifrost.classes.general.MessageParent#getChildMessages(int)
     */
    @Override
    public List<PrivateMessage> getChildMessages(int limit) throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getHandle(getScript().getScript()).getPMReplies(getID(), limit);
    }
}
