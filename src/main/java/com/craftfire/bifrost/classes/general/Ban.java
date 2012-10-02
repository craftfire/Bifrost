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

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.Cache;
import com.craftfire.bifrost.enums.CacheCleanupReason;
import com.craftfire.bifrost.enums.CacheGroup;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * This class should only be used with a ban.
 * <p>
 * The first constructor should only be used by the script itself and not by the library user.
 * To update any changed values in the post, run {@link #update()}.
 * <p>
 * When creating a new Ban make sure you use the correct constructor:
 * {@link #Ban(Script, String, String, String)}.
 * <p>
 * Remember to run {@link #create()} after creating a ban to insert it into the script.
 */
public class Ban implements IDable {
    private String name, email, ip, reason, notes;
    private int banid, userid;
    private Date startdate, enddate;
    private final Script script;

    /**
     * This constructor should only be used by the script and not by that library user.
     *
     * @param script  the script
     * @param banid   the ID of the ban
     * @param name    the name/username which is set in the ban entry
     * @param email   the email which is set in the ban entry
     * @param ip      the ip which is set in the ban entry
     */
    public Ban(Script script, int banid, String name, String email, String ip) {
        this.script = script;
        this.banid = banid;
        this.name = name;
        this.email = email;
        this.ip = ip;
    }

    /**
     * This constructor should be used when creating a new ban for the script.
     * <p>
     * Remember to run {@link #create()} after creating a ban to insert it into the script.
     *
     * @param script  the script
     * @param name    the name/username of the ban, set to null if none.
     * @param email   the email of the ban, set to null if none.
     * @param ip      the ip of the ban, set to null if none.
     */
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

    public void setID(int id) {
        this.banid = id;
    }

    /**
     * Returns the name/username.
     * <p>
     * Returns <code>null</code> if no name/username was found.
     *
     * @return name/username of the ban entry, <code>null</code> if no name/username was found
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name/username.
     *
     * @param name  the name/username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the ID of the name/username.
     * <p>
     * Returns <code>null</code> if no ID was found.
     *
     * @return ID of the name/username if any, <code>null</code> if no ID was found
     */
    public int getUserID() {
        return this.userid;
    }

    /**
     * Sets the ID of the name/username.
     *
     * @param userid  the ID of the name/username
     */
    public void setUserID(int userid) {
        this.userid = userid;
    }

    /**
     * Returns the email.
     * <p>
     * Returns <code>null</code> if no email was found.
     *
     * @return email of the ban entry, <code>null</code> if no email was found
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email.
     *
     * @param email  the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the IP-address.
     * <p>
     * Returns <code>null</code> if no IP-address was found.
     *
     * @return IP-address of the ban entry, <code>null</code> if no IP-address was found
     */
    public String getIP() {
        return this.ip;
    }

    /**
     * Sets the IP-address.
     *
     * @param ip  the IP-address
     */
    public void setIP(String ip) {
        this.ip = ip;
    }

    /**
     * Returns the length of the ban in seconds.
     * <p>
     * Returns <code>0</code> if the ban is permanent or not supported.
     * <p>
     * Use {@link #isPermanent()} to check if the ban is permanent.
     *
     * @return the length of the ban in seconds, <code>0</code> if permanent or not supported
     */
    public long getTimeLength() {
        if (!isPermanent()) {
            return (this.enddate.getTime() - this.startdate.getTime());
        }
        return 0;
    }

    /**
     * Returns amount of seconds remaining of the ban.
     * <p>
     * Returns <code>0</code> if the ban is permanent or not supported.
     * <p>
     * Use {@link #isPermanent()} to check if the ban is permanent.
     *
     * @return amount of second remaining of the ban, <code>0</code> if permanent or not supported
     */
    public long getTimeRemaining() {
        if (!isPermanent()) {
            return (this.enddate.getTime() - new Date().getTime());
        }
        return 0;
    }

    /**
     * Returns the reason for the ban.
     * <p>
     * Returns <code>null</code> if no reason given or not supported.
     * <p>
     * The reason is the public message that is being shown to the banned name/username/email/IP-address.
     *
     * @return reason of the ban, <code>null</code> if no reason given or not supported
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Sets the reason for the ban.
     * <p>
     * The reason is the public message that is being shown to the banned name/username/email/IP-address.
     *
     * @param reason  the sreason for the ban
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the notes for the ban.
     * <p>
     * Returns <code>null</code> if no notes were found or not supported.
     * <p>
     * The notes is the private message that is being shown to the staff of the script only.
     *
     * @return notes for the ban, <code>null</code> if no notes were found or not supported
     */
    public String getNotes() {
        return this.notes;
    }

    /**
     * Sets the notes for the ban.
     * <p>
     * The notes is the private message that is being shown to the staff of the script only..
     *
     * @param notes  the notes for the ban
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the date of when the ban is valid.
     * <p>
     * Returns <code>null</code> if no start date was set or not supported.
     *
     * @return the date of when the ban is valid, <code>null</code> if no date set or not supported
     */
    public Date getStartDate() {
        return this.startdate;
    }

    /**
     * Sets the date of when the ban is valid.
     *
     * @param startdate  the date of when the ban is valid
     */
    public void setStartDate(Date startdate) {
        this.startdate = startdate;
    }

    /**
     * Returns the date of when the ban is ends.
     * <p>
     * Returns <code>null</code> if no end date was set or not supported.
     *
     * @return the date of when the ban ends, <code>null</code> if no date set or not supported
     */
    public Date getEndDate() {
        return this.enddate;
    }

    /**
     * Sets the date of when the ban ends.
     *
     * @param enddate  the date of when the ban ends
     */
    public void setEndDate(Date enddate) {
        this.enddate = enddate;
    }

    /**
     * Returns <code>true</code> if the ban is permanent, <code>false</code> if not.
     *
     * @return <code>true</code> if ban is permanent, <code>false</code> if not
     */
    public boolean isPermanent() {
        return getEndDate() == null;
    }

    /**
     * This method should be run after changing any ban values.
     * <p>
     * It should <b>not</b> be run when creating a new ban, only when editing an already existing ban.
     *
     * @throws SQLException         if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void update() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).updateBan(this);
    }

    /**
     * This method should be run after creating a new ban.
     * <p>
     * It should <b>not</b> be run when updating a ban, only when creating a new ban.
     *
     * @throws SQLException         if a SQL error concurs
     * @throws UnsupportedMethod  if the method is not supported by the script
     */
    @Override
    public void create() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getHandle(this.script.getScript()).addBan(this);
    }

    /**
     * Returns <code>true</code> if the handle contains a ban cache with the given id parameter,
     * <code>false</code> if not.
     *
     * @param handle  the script handle
     * @param id      the id of the object to look for
     * @return        <code>true</code> if contains, <code>false</code> if not
     */
    public static boolean hasCache(ScriptHandle handle, Object id) {
        return handle.getCache().contains(CacheGroup.BAN, id);
    }

    /**
     * Adds a Ban to the cache with the given script handle
     *
     * @param handle  the script handle
     * @param ban     the Ban object
     */
    public static void addCache(ScriptHandle handle, Ban ban) {
        handle.getCache().put(CacheGroup.BAN, ban.getID(), ban);
    }

    /**
     * Returns the Ban object by the given id if found, returns <code>null</code> if no cache was found.
     *
     * @param handle  the script handle
     * @param id      the id of the ban
     * @return        Ban object if cache was found, <code>null</code> if no cache was found
     */
    public static Ban getCache(ScriptHandle handle, Object id) {
        if (handle.getCache().contains(CacheGroup.BAN, id)) {
            return (Ban) handle.getCache().get(CacheGroup.BAN, id);
        }
        return null;
    }

    /**
     * Removes outdated cache elements related to given {@param ban} from cache.
     * <p>
     * The method should be called when updating or creating a {@link Ban}, but before calling {@link #addCache}.
     * Only {@link ScriptHandle} and derived classes need to call this method.
     * 
     * @param handle  the handle the method is called from
     * @param ban     the ban to cleanup related cache
     * @param reason  the reason of cache cleanup, {@link CacheCleanupReason#OTHER} causes full cleanup
     * @see           Cache
     */
    public static void cleanupCache(ScriptHandle handle, Ban ban, CacheCleanupReason reason) {
        switch (reason) {
        case CREATE:
        case OTHER:
            handle.getCache().clear(CacheGroup.BAN_LIST);
            handle.getCache().clear(CacheGroup.BAN_COUNT);
            break;
        case UPDATE:
            break;
        }
    }
}
