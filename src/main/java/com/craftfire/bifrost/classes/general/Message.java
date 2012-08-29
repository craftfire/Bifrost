package com.craftfire.bifrost.classes.general;

import java.util.Date;

import com.craftfire.bifrost.script.Script;

/**
 * Base class for all messages like Thread, Post, Article, Comment,
 * PrivateMessage, etc. Should not be instanced.
 */
public abstract class Message implements IDable {
    private int msgid;
    private ScriptUser author;
    private Date date;
    private String body;
    private final Script script;

    protected Message(Script script) {
        this.script = script;
    }

    protected Message(Script script, int id) {
        this.script = script;
        this.msgid = id;
    }

    /**
     * Sets the ID of the message, should only be used when creating a new
     * Message.
     * 
     * @param id the ID of the message
     */
    public void setID(int id) {
        this.msgid = id;
    }

    /**
     * Returns the ID of the message.
     * 
     * @return the ID of the message
     */
    @Override
    public int getID() {
        return this.msgid;
    }

    /**
     * Sets the message author.
     * 
     * @param author a ScriptUser object containing the author.
     * @see com.craftfire.bifrost.classes.general.ScriptUser
     */
    public void setAuthor(ScriptUser author) {
        this.author = author;
    }

    /**
     * Returns a ScriptUser object of the author, null if error.
     * 
     * @return message author
     * @see com.craftfire.bifrost.classes.general.ScriptUser
     */
    public ScriptUser getAuthor() {
        return this.author;
    }

    /**
     * Sets date when this message was posted.
     * 
     * @param date new message date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns date when this message was posted.
     * 
     * @return message date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Sets body text (a.k.a. content) of the message.
     * 
     * @param body body text (a.k.a. content) of the message.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns the body text (a.k.a. content) of the message, or null if error.
     * 
     * @return body text of the message, or null if error.
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Returns a Script object of the script this message comes from.
     * 
     * @return Script object of the script this message comes from.
     */
    public Script getScript() {
        return this.script;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Message " + this.msgid + " by " + this.author + " at " + this.date + " containing: " + this.body;
    }
}
