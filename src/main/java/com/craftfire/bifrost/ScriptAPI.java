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

import com.craftfire.bifrost.classes.Script;
import com.craftfire.bifrost.enums.Scripts;
import com.craftfire.bifrost.exceptions.UnsupportedFunction;
import com.craftfire.bifrost.exceptions.UnsupportedScript;
import com.craftfire.bifrost.exceptions.UnsupportedVersion;
import com.craftfire.bifrost.scripts.forum.SMF;
import com.craftfire.bifrost.scripts.forum.XenForo;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

import java.sql.SQLException;
import java.util.HashMap;

public class ScriptAPI {
    private HashMap<Scripts, ScriptHandle> handles = new HashMap<Scripts, ScriptHandle>();
    private ScriptHandle lastHandle;

    public Bifrost getBifrost() {
        return Bifrost.getInstance();
    }

    public LoggingManager getLoggingManager() {
        return Bifrost.getInstance().getLoggingManager();
    }

    /**
     * Converts a string into a script enum.
     *
     * @param string The string which contains the script name
     * @return The script for the string, if none are found it returns null.
     * @throws UnsupportedScript if the input string is not found in the list of supported scripts.
     */
    public static Scripts stringToScript(String string) throws UnsupportedScript {
        for (Scripts script : Scripts.values()) {
            if (string.equalsIgnoreCase(script.toString()) || string.equalsIgnoreCase(script.alias)) {
                return script;
            } else if (script.alias.contains(",")) {
                String[] aliases = script.alias.split(",");
                for (String alias : aliases) {
                    if (string.equalsIgnoreCase(alias)) {
                        return script;
                    }
                }
            }
        }
        throw new UnsupportedScript();
    }

    public static Script setScript(Scripts scriptName, String version, DataManager dataManager) {
        switch (scriptName) {
            case SMF:
                return new SMF(scriptName, version, dataManager);
            case XF:
                return new XenForo(scriptName, version, dataManager);
            default:
                return null;
        }
    }

    public HashMap<Scripts, ScriptHandle> getHandles() {
        return this.handles;
    }

    public ScriptHandle getHandle(Scripts script) {
        if (this.handles.containsKey(script)) {
            return this.handles.get(script);
        } else {
            return null;
        }
    }

    public ScriptHandle getHandle() {
        if (this.lastHandle != null) {
            return this.lastHandle;
        }
        return null;
    }

    public void addHandle(String script, String version, DataManager dataManager) throws UnsupportedScript,
                                                                                                    UnsupportedVersion {
        ScriptHandle handle = new ScriptHandle(script, version, dataManager);
        this.handles.put(handle.getScript().getScript(), handle);
        this.lastHandle = handle;
    }

    public void addHandle(Scripts script, String version, DataManager dataManager) throws UnsupportedScript,
            UnsupportedVersion {
        ScriptHandle handle = new ScriptHandle(script, version, dataManager);
        this.handles.put(handle.getScript().getScript(), handle);
        this.lastHandle = handle;
    }

    public boolean convert(ScriptHandle from, ScriptHandle to) throws SQLException, UnsupportedFunction {
        //TODO: Create script converter, need to add methods to scripts
        return false;
    }
}
