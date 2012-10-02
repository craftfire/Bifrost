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
package com.craftfire.bifrost;

import java.util.HashMap;
import java.util.Map;

import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

import com.craftfire.bifrost.classes.cms.CMSHandle;
import com.craftfire.bifrost.classes.forum.ForumHandle;
import com.craftfire.bifrost.classes.general.Script;
import com.craftfire.bifrost.classes.general.ScriptHandle;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedScript;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.bifrost.scripts.cms.WordPress;
import com.craftfire.bifrost.scripts.forum.SMF;
import com.craftfire.bifrost.scripts.forum.XenForo;

//TODO: Add example to the description
/**
 * ScriptAPI contains all the methods for creating and grabbing script handles.
 *
 * @see ScriptHandle
 */
public class ScriptAPI {
    private Map<Scripts, ScriptHandle> handles = new HashMap<Scripts, ScriptHandle>();
    private ScriptHandle lastHandle = null;

    /**
     * Returns the Bifrost instance.
     *
     * @return Bifrost instance
     * @see Bifrost
     */
    public Bifrost getBifrost() {
        return Bifrost.getInstance();
    }

    /**
     * Returns the LoggingManager of Bifrost.
     *
     * @return LoggingManager of Bifrost
     * @see LoggingManager
     */
    public LoggingManager getLoggingManager() {
        return Bifrost.getInstance().getLoggingManager();
    }

    /**
     * Converts a string into a script enum.
     *
     * @param string  the string which contains the script name
     * @return        the script for the string, if none are found it returns null.
     * @throws        UnsupportedScript if the input string is not found in the list of supported scripts.
     */
    public static Scripts stringToScript(String string) throws UnsupportedScript {
        for (Scripts script : Scripts.values()) {
            if (string.equalsIgnoreCase(script.toString()) || string.equalsIgnoreCase(script.getAlias())) {
                return script;
            } else if (script.getAlias().contains(",")) {
                String[] aliases = script.getAlias().split(",");
                for (String alias : aliases) {
                    if (string.equalsIgnoreCase(alias)) {
                        return script;
                    }
                }
            }
        }
        throw new UnsupportedScript();
    }

    /**
     * Returns a {@link Script} object depending on which <code>script</code> and <code>version</code> has been used.
     * <p>
     * Returns <code>null</code> if the <code>script</code> is not supported.
     *
     * @param script       the script
     * @param version      the version of the script
     * @param dataManager  the {@link DataManager} for the script
     * @return             a {@link Script} object, returns null if not supported
     */
    public static Script setScript(Scripts script, String version, DataManager dataManager) {
        switch (script) {
            case WP:
                return new WordPress(script, version, dataManager);
            case SMF:
                return new SMF(script, version, dataManager);
            case XF:
                return new XenForo(script, version, dataManager);
            default:
                return null;
        }
    }

    /**
     * Returns a Map with all the {@link ScriptHandle}s that have been created.
     *
     * @return Map with all the script handles
     */
    public Map<Scripts, ScriptHandle> getHandles() {
        this.getLoggingManager().debug("ScriptAPI: Getting all handles, size: " + this.handles.size());
        return this.handles;
    }

    /**
     * Returns the {@link ScriptHandle} for the specified <code>script</code>.
     *
     * @param script  the script you want to grab the script handle from
     * @return        the script handle for the specified <code>script</code>
     */
    public ScriptHandle getHandle(Scripts script) {
        if (handleExists(script)) {
            this.getLoggingManager().debug("ScriptAPI: Found handle for '" + script.getAlias() + "!");
            return this.handles.get(script);
        } else {
            this.getLoggingManager().debug("ScriptAPI: Handle for '" + script.getAlias() + "' does not exist, " +
                                            "returning null");
            return null;
        }
    }

    /**
     * Returns the {@link ForumHandle} for the specified <code>script</code>.
     *
     * @param script  the script you want to grab the forum handle from
     * @return        the forum handle for the specified <code>script</code>
     */
    public ForumHandle getForumHandle(Scripts script) {
        if (handleExists(script)) {
            this.getLoggingManager().debug("ScriptAPI: Found forum handle for '" + script.getAlias() + "!");
            return (ForumHandle) this.handles.get(script);
        } else {
            this.getLoggingManager().debug("ScriptAPI: Forum handle for '" + script.getAlias() + "' does not exist, " +
                                            "returning null");
            return null;
        }
    }

    /**
     * Returns the {@link CMSHandle} for the specified <code>script</code>.
     *
     * @param script  the script you want to grab the cms handle from
     * @return        the cms handle for the specified <code>script</code>
     */
    public CMSHandle getCMSHandle(Scripts script) {
        if (handleExists(script)) {
            this.getLoggingManager().debug("ScriptAPI: Found cms handle for '" + script.getAlias() + "!");
            return (CMSHandle) this.handles.get(script);
        } else {
            this.getLoggingManager().debug("ScriptAPI: CMS handle for '" + script.getAlias() + "' does not exist, " +
                                            "returning null");
            return null;
        }
    }

    /**
     * Returns the latest script handle, returns null if there are no handles.
     *
     * @return latest script handle, return null if there are no handles
     */
    public ScriptHandle getHandle() {
        this.getLoggingManager().debug("ScriptAPI: Returning last handle: " + this.lastHandle);
        return this.lastHandle;
    }

    /**
     * Returns the latest forum handle, returns null if there are no handles.
     *
     * @return latest forum handle, return null if there are no handles
     */
    public ForumHandle getForumHandle() {
        this.getLoggingManager().debug("ScriptAPI: Returning last forum handle: " + this.lastHandle);
        return (ForumHandle) this.lastHandle;
    }

    /**
     * Returns the latest cms handle, returns null if there are no handles.
     *
     * @return latest cms handle, return null if there are no handles
     */
    public CMSHandle getCMSHandle() {
        this.getLoggingManager().debug("ScriptAPI: Returning last cms handle: " + this.lastHandle);
        return (CMSHandle) this.lastHandle;
    }

    /**
     * Adds a handle to the list.
     *
     * @param script               the script
     * @param version              the version of the script
     * @param dataManager          the {@link DataManager} of the script
     * @throws UnsupportedScript   if the specified <code>script</code> is not supported by Bifrost
     * @throws UnsupportedVersion  if the specified <code>version</code> is not supported by the script
     */
    public void addHandle(Scripts script, String version, DataManager dataManager) throws UnsupportedScript, UnsupportedVersion {
        ScriptHandle handle;
        switch (script.getType()) {
        case CMS:
            handle = new CMSHandle(script, version, dataManager);
            break;
        case FORUM:
            handle = new ForumHandle(script, version, dataManager);
            break;
        default:
            handle = new ScriptHandle(script, version, dataManager);
        }
        this.getLoggingManager().debug("ScriptAPI: Adding handle with type: '" + script.getType() + "' for script: '" +
                                        script.getAlias() + "', version: '" + version + "'");
        this.handles.put(handle.getScript().getScript(), handle);
        this.lastHandle = handle;
    }

    /**
     * Checks if a script handle already exists, returns true if it does, false if not
     *
     * @param script  the script you want to check
     * @return        true if exists, false if not
     */
    protected boolean handleExists(Scripts script) {
        return this.handles.containsKey(script);
    }
}
