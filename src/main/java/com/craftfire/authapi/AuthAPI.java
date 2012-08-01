/*
 * This file is part of AuthAPI <http://www.craftfire.com/>.
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
package com.craftfire.authapi;

import com.craftfire.authapi.ScriptAPI.Scripts;
import com.craftfire.authapi.classes.Cache;
import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.exceptions.UnsupportedScript;
import com.craftfire.authapi.exceptions.UnsupportedVersion;
import com.craftfire.commons.managers.CacheManager;
import com.craftfire.commons.managers.DataManager;
import com.craftfire.commons.managers.LoggingManager;

//TODO: Javadoc, analytics and logging.
public class AuthAPI {
    private static AuthAPI instance;
    private final ScriptAPI scriptAPI;
    private final Script script;
    private final DataManager dataManager;
    private final CacheManager cacheManager;
    private final Cache cache;
	private final LoggingManager loggingManager = new LoggingManager("CraftFire.AuthAPI", "[AuthAPI]");

    public AuthAPI(Scripts script, String version, DataManager dataManager) throws UnsupportedVersion {
        this.scriptAPI = new ScriptAPI(script, version);
        this.script = this.scriptAPI.getScript();
        this.dataManager = dataManager;
        this.cacheManager = new CacheManager();
        this.cache = new Cache();
        instance = this;
        this.loggingManager.debug("Initialized AuthAPI");
    }

    public AuthAPI(String script, String version, DataManager dataManager) throws UnsupportedScript,
                                                                                  UnsupportedVersion {
        this.scriptAPI = new ScriptAPI(script, version);
        this.script = this.scriptAPI.getScript();
        this.dataManager = dataManager;
        this.cacheManager = new CacheManager();
        this.cache = new Cache();
        instance = this;
        this.loggingManager.debug("Initialized AuthAPI");
    }

    public static AuthAPI getInstance() {
        return instance;
    }

    public Script getScript() {
        return this.script;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }

	public LoggingManager getLoggingManager() {
		return this.loggingManager;
	}

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public Cache getCache() {
        return this.cache;
    }
}