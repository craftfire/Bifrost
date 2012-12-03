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

import com.craftfire.commons.analytics.AnalyticsException;
import com.craftfire.commons.analytics.AnalyticsManager;
import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.util.LoggingManager;

import java.io.IOException;
import java.net.MalformedURLException;

//TODO: Javadoc, analytics and logging.

/**
 * This is the main class for Bifrost.
 * <p>
 * This contains all the required methods to use the scripts.
 * To use Bifrost in your projects, you have to use {@link DataManager} and
 * {@link ScriptAPI}, which is provided with Bifrost.
 * <p>
 * Using Bifrost is simple, here's an example on how to initialize Bifrost and ScriptAPI:
 * <pre>
 *      Bifrost bifrost = new Bifrost();
 *      bifrost.getLoggingManager().setDebug(true);
 *      DataManager dataManager = new DataManager(DataType.MYSQL, "username", "password");
 *      dataManager.setDatabase("craftfire");
 *      dataManager.setKeepAlive(true);
 *      dataManager.setPort(3306);
 *      ScriptAPI scriptAPI = bifrost.getScriptAPI();
 *      scriptAPI.addHandle(Scripts.SMF, "2.0.2", dataManager);
 *      ForumUser forumUser = scriptAPI.getForumHandle(Scripts.SMF).getLastRegUser();
 * </pre>
 *
 * @see LoggingManager
 * @see DataManager
 * @see ScriptAPI
 */
public class Bifrost {
    private String version = "1.0.0";
    private final ScriptAPI scriptAPI;
    private final LoggingManager loggingManager = new LoggingManager("CraftFire.Bifrost", "[Bifrost]");

    /**
     * Default constructor for Bifrost.
     */
    public Bifrost() {
        this.scriptAPI = new ScriptAPI(this);
        getLoggingManager().info("Initializing Bifrost version " + this.version);
        AnalyticsManager analytics = new AnalyticsManager("http://stats.craftfire.com", "Bifrost", this.version);
        analytics.setLoggingManager(getLoggingManager());
        analytics.submitVoid();
        getLoggingManager().info("Initialized Bifrost version " + this.version);
    }

    /**
     * Returns the version of Bifrost.
     *
     * @return version of Bifrost
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns the ScriptAPI.
     * <p>
     * ScriptAPI contains all the needed methods to add new script handles, use script methods and such.
     *
     * @return ScriptAPI
     * @see    ScriptAPI
     */
    public ScriptAPI getScriptAPI() {
        return this.scriptAPI;
    }

    /**
     * Returns the LoggingManager of Bifrost.
     *
     * @return LoggingManager
     * @see    LoggingManager
     */
    public LoggingManager getLoggingManager() {
        return this.loggingManager;
    }
}