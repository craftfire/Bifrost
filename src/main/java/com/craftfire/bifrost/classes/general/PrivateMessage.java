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
import java.util.*;

import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.ScriptException;

/**
 * This class should only be used with a private/conversation message.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@link #update()}.
 * <p>
 * When creating a new PrivateMessage make sure you use the correct constructor:
 * {@link #PrivateMessage(ScriptHandle, ScriptUser, List, int)}.
 * <p>
 * Remember to run {@link #create()} after creating a private/conversation message
 * to insert it into the script.
 */
public class PrivateMessage extends Message {
    private int parentid;
    private List<ScriptUser> recipients;
    private Map<ScriptUser, Boolean> isnew = new HashMap<ScriptUser, Boolean>();
    private Map<ScriptUser, Boolean> read = new HashMap<ScriptUser, Boolean>();
    private Map<ScriptUser, Boolean> deleted = new HashMap<ScriptUser, Boolean>();
    private boolean deletedbysender;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script  the script
     * @param pmid    the ID of the private message
     */
    public PrivateMessage(Script script, int pmid) {
        super(script.getHandle(), pmid);
    }

    /**
     * This constructor should be used when creating a new private message for the script.
     * <p>
     * Remember to run {@link #create()} after creating a private message to insert it into the script.
     *
     * @param handle      the handle
     * @param sender      the sender of the private message
     * @param recipients  a list with {@link ScriptUser}s, these are the recipients of the private message
     */
    public PrivateMessage(ScriptHandle handle, ScriptUser sender, List<ScriptUser> recipients) {
        super(handle);
        setAuthor(sender);
        this.recipients = recipients;
    }

    public PrivateMessage(ScriptHandle handle, ScriptUser sender, List<ScriptUser> recipients, int parentid) {
        super(handle);
        setAuthor(sender);
        this.recipients = recipients;
        this.parentid = parentid;
    }

    /**
     * Returns a ScriptUser object of the sender, null if error.
     *
     * @return  private message sender, null if error
     * @see     ScriptUser
     */
    public ScriptUser getSender() {
        return getAuthor();
    }

    /**
     * Sets the private message sender.
     *
     * @param sender  a ScriptUser object containing the sender
     * @see           ScriptUser
     */
    public void setSender(ScriptUser sender) {
        setAuthor(sender);
    }

    /**
     * Returns a List of recipients for the private message.
     *
     * @return the list of recipients
     */
    public List<ScriptUser> getRecipients() {
        return this.recipients;
    }

    /**
     * Sets the List of recipients for the private message.
     *
     * @param users  the list of recipients
     */
    public void setRecipients(List<ScriptUser> users) {
        this.recipients = users;
    }

    /**
     * Returns the subject/title of the private message.
     *
     * @return subject/title of the private message
     */
    public String getSubject() {
        return getTitle();
    }

    /**
     * Sets the subject/title of the private message.
     *
     * @param subject  subject/title of the private message
     */
    public void setSubject(String subject) {
        setTitle(subject);
    }

    /**
     * Returns {@code true} if the private message has been deleted by the sender, {@code false} if not.
     *
     * @return {@code true} if deleted by sender, {@code false} if not.
     */
    public boolean isDeletedBySender() {
        return this.deletedbysender;
    }

    /**
     * Set to {@code true} if the private message is deleted by the sender, {@code false} if not.
     *
     * @param deleted  {@code true} if deleted by the sender, {@code false} if not
     */
    public void setDeletedBySender(boolean deleted) {
        this.deletedbysender = deleted;
    }

    /**
     * Returns {@code true} if the private message has been read by the {@code recipient},
     * {@code false} if not.
     *
     * @param  recipient  the recipient
     * @return            {@code true} if read by the {@code recipient}, {@code false} if not.
     */
    public boolean isRead(ScriptUser recipient) {
        if (this.read.containsKey(recipient)) {
            return this.read.get(recipient);
        }
        return false;
    }

    /**
     * Set to {@code true} if the private message is read by the {@code recipient}, {@code false} if not.
     *
     * @param recipient  the recipient
     * @param read       {@code true} if read by the {@code recipient}, {@code false} if not
     */
    public void setRead(ScriptUser recipient, boolean read) {
        this.read.put(recipient, read);
    }

    /**
     * Returns {@code true} if the private message is new for the {@code recipient},
     * {@code false} if not.
     *
     * @param  recipient  the recipient
     * @return            {@code true} if new for the {@code recipient}, {@code false} if not.
     */
    public boolean isNew(ScriptUser recipient) {
        if (this.isnew.containsKey(recipient)) {
            return this.isnew.get(recipient);
        }
        return false;
    }

    /**
     * Set to {@code true} if the private message is new for the {@code recipient}, {@code false} if not.
     *
     * @param recipient  the recipient
     * @param isnew      {@code true} if new for the {@code recipient}, {@code false} if not
     */
    public void setNew(ScriptUser recipient, boolean isnew) {
        this.isnew.put(recipient, isnew);
    }

    /**
     * Returns {@code true} if the private message is deleted by the {@code recipient},
     * {@code false} if not.
     *
     * @param recipient  the recipient
     * @return           {@code true} if deleted by the {@code recipient}, {@code false} if not.
     */
    public boolean isDeleted(ScriptUser recipient) {
        if (this.deleted.containsKey(recipient)) {
            return this.deleted.get(recipient);
        }
        return false;
    }

    /**
     * Set to {@code true} if the private message is deleted by the {@code recipient},
     * {@code false} if not.
     *
     * @param recipient  the recipient
     * @param deleted    {@code true} if deleted by the {@code recipient}, {@code false} if not
     */
    public void setDeleted(ScriptUser recipient, boolean deleted) {
        this.deleted.put(recipient, deleted);
    }

    /**
     * This method should be run after changing any private message values.
     * <p>
     * It should <b>not</b> be run when creating a new private message,
     * only when editing an already existing private message.
     * <p>
     * Use {@link #create()} instead if you wish to update the private message.
     *
     * @throws SQLException     if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     */
    @Override
    public void update() throws SQLException, ScriptException {
        getHandle().updatePrivateMessage(this);
    }


    /**
     * This method should be run after creating a new private message.
     * <p>
     * It should <b>not</b> be run when updating a private message, only when creating a new private message.
     * <p>
     * Use {@link #update()} instead if you wish to update the private message.
     *
     * @throws SQLException     if a SQL error concurs
     * @throws ScriptException  if the method is not supported by the script
     */
    @Override
    public void create() throws SQLException, ScriptException {
       getHandle().createPrivateMessage(this);
    }

    /**
     * Returns {@code true} if the handle contains a private message cache with the given id parameter,
     * {@code false} if not.
     *
     * @param  handle  the script handle
     * @param  id      the id of the object to look for
     * @return         {@code true} if contains, {@code false} if not
     */
    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.PM, id);
    }

    /**
     * Adds a PrivateMessage to the cache with the given script handle
     *
     * @param handle          the script handle
     * @param privateMessage  the PrivateMessage object
     */
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
            Iterator<ScriptUser> iterator = privateMessage.getRecipients().iterator();
            while (iterator.hasNext()) {
                ScriptUser user = iterator.next();
                if (user != null) {
                    recipientNames.add(user.getUsername());
                }
            }
            handle.getCache().setMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-recipients", recipientNames);
        } else {
            handle.getCache().removeMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-recipients");
        }
    }

    /**
     * Returns the PrivateMessage object by the given id if found, returns {@code null} if no cache was found.
     *
     * @param  handle  the script handle
     * @param  id      the id of the private message
     * @return         PrivateMessage object if cache was found, {@code null} if no cache was found
     */
    public static PrivateMessage getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.PM, id)) {
            return (PrivateMessage) handle.getCache().get(CacheGroup.PM, id);
        }
        return null;
    }

    /**
     * Removes outdated cache elements related to given {@code privateMessage} from cache.
     * <p>
     * The method should be called when updating or creating a {@link PrivateMessage}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     * 
     * @param handle          the handle the method is called from
     * @param privateMessage  the private message to cleanup related cache
     * @param reason          the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     * @see                   Cache
     */
    @SuppressWarnings("unchecked")
    public static void cleanupCache(ScriptHandle handle, PrivateMessage privateMessage, CacheCleanupReason reason) {
        handle.getCache().remove(CacheGroup.PM_REPLIES, privateMessage.getParentID());
        if (privateMessage.getAuthor() != null) {
            handle.getCache().remove(CacheGroup.PM_SENT, privateMessage.getAuthor().getUsername());
            handle.getCache().remove(CacheGroup.PM_SENT_COUNT, privateMessage.getAuthor().getUsername());
        }
        if (privateMessage.getRecipients() != null) {
            Iterator<ScriptUser> iterator = privateMessage.getRecipients().iterator();
            while (iterator.hasNext()) {
                ScriptUser user = iterator.next();
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
            Object oldParentid = handle.getCache().getMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-parent");
            Object oldUsername = handle.getCache().getMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-author");
            List<String> oldRecipients = (List<String>) handle.getCache().getMetadata(CacheGroup.PM, privateMessage.getID(), "bifrost-cache.pm.old-recipients");
            handle.getCache().remove(CacheGroup.PM_REPLIES, oldParentid);
            handle.getCache().remove(CacheGroup.PM_SENT, oldUsername);
            handle.getCache().remove(CacheGroup.PM_SENT_COUNT, oldUsername);
            if (oldRecipients != null) {
                Iterator<String> iterator = oldRecipients.iterator();
                while (iterator.hasNext()) {
                    String username = iterator.next();
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
    public PrivateMessage getParent() throws ScriptException, SQLException {
        return getHandle().getPM(this.parentid);
    }

    /**
     * Returns the list of PrivateMessages replying to this message.
     * 
     * @see com.craftfire.bifrost.classes.general.MessageParent#getChildMessages(int)
     */
    @Override
    public List<PrivateMessage> getChildMessages(int limit) throws ScriptException {
        return getHandle().getPMReplies(getID(), limit);
    }
}
