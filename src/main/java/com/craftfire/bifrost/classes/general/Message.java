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
import java.util.Date;

import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * Base class for all messages like Thread, Post, Article, Comment, PrivateMessage, etc.
 * <p>
 * Should <b>NOT</b> be instanced.
 */
public abstract class Message extends GenericMethods implements MessageParent {
    private int categoryid;
    private ScriptUser author;
    private Date date;
    private String title, body;
    private boolean deleted;
    /**
     * This constructor should be used in extending class's constructor, which
     * is used to create new messages.
     * 
     * @param handle  a ScriptHandle Object of the script this message comes from
     */
    protected Message(ScriptHandle handle) {
        super(handle);
    }

    /**
     * This constructor should be used in extending class's constructor, which
     * is used only when loading the message from a database.
     * 
     * @param handle  a ScriptHandle Object of the script this message comes from.
     * @param id      the ID of the message.
     */
    protected Message(ScriptHandle handle, int id) {
        super(handle);
        this.setID(id);
    }

    /**
     * This constructor should be used in extending class's constructor, which
     * is used only when loading the message from a database.
     * 
     * @param handle      a ScriptHandle Object of the script this message comes from.
     * @param id          the ID of the message.
     * @param categoryid  the ID of the category of the message
     */
    protected Message(ScriptHandle handle, int id, int categoryid) {
        super(handle);
        this.setID(id);
        this.categoryid = categoryid;
    }

    /**
     * Sets the message author.
     * 
     * @param author  a ScriptUser object containing the author
     * @see           ScriptUser
     */
    public void setAuthor(ScriptUser author) {
        this.author = author;
    }

    /**
     * Returns a ScriptUser object of the author, null if error.
     * 
     * @return message author, null if error
     * @see    ScriptUser
     */
    public ScriptUser getAuthor() {
        return this.author;
    }

    /**
     * Sets date when this message was posted.
     * 
     * @param date  new message date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns date when this message was posted, null if error.
     * 
     * @return message date, null if error
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Sets body text (a.k.a. content) of the message.
     * 
     * @param body  body text (a.k.a. content) of the message
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the body text (a.k.a. content) of the message, or null if error.
     * 
     * @return body text of the message, or null if error
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Sets title (a.k.a. subject) of the message.
     *
     * @param title  title (a.k.a. subject) of the message
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the title (a.k.a. subject) of the message, or null if error.
     *
     * @return title of the message, or null if error
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the messages's deleted state to whatever {@code Boolean} the {@code isDeleted} parameter is.
     * <p>
     * {@code true} = deleted and {@code false} = not deleted.
     *
     * @param isDeleted  {@code true} for deleted, {@code false} for not deleted
     */
    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
    }

    /**
     * Returns {@code true} if message is deleted, {@code false} if not deleted.
     *
     * @return {@code true} if deleted, {@code false} if not deleted
     */
    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Returns the parent of the message.
     * 
     * @return                    the parent of the message
     * @throws UnsupportedMethod  if the method is not supported by script
     * @throws SQLException       if a SQL exception occurred
     */
    public abstract MessageParent getParent() throws UnsupportedMethod, SQLException;

    /**
     * Returns a Category object for the category of the message. Should be
     * implemented in classes of specific message types (such as ForumTopic).
     * 
     * @return                    a Category object
     * @throws UnsupportedMethod  if the method is not supported by script
     * @throws SQLException       if a SQL exception occurred
     */
    public abstract Category getCategory() throws UnsupportedMethod, SQLException;

    /**
     * Returns the ID of the category of the message.
     * 
     * @return the category ID
     */
    public int getCategoryID() {
        return this.categoryid;
    }

    /**
     * Sets the category of this message.
     * 
     * @param categoryid  the ID of the category
     */
    public void setCategoryID(int categoryid) {
        this.categoryid = categoryid;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Message " + this.getID() + " by " + this.author + " at " + this.date + " containing: " + this.body;
    }
}
