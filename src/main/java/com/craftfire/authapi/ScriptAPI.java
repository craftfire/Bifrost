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

import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.classes.ScriptUser;
import com.craftfire.authapi.scripts.forum.SMFScript;
import com.craftfire.commons.DataManager;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ScriptAPI {
	private Script script;
	private final DataManager dataManager;
	private final Scripts scriptName;
	private final String version;

	public enum Scripts {
		SMF
    }

	/**
	 * @param script  The script using the enum list, for example: Scripts.SMF.
	 * @param version The version that the user has set in his config.
	 */
	public ScriptAPI(Scripts script, String version, DataManager dataManager) {
		this.scriptName = script;
		this.version = version;
		this.dataManager = dataManager;
		setScript();
	}

	/**
	 * @param script  The script in a string, for example: smf.
	 * @param version The version that the user has set in his config.
	 */
	public ScriptAPI(String script, String version, DataManager dataManager) {
		this.scriptName = stringToScript(script);
		this.version = version;
		this.dataManager = dataManager;
		setScript();
	}

	/**
	 * @return The Script object.
	 */
	public Script getScript() {
		return this.script;
	}

	/**
	 * @param username The username of the player.
	 * @param password The password of the player.
	 * @return A hash encrypted with the script's encryption
	 */
	public String hashPassword(String username, String password) {
		return "";
		//return this.script.hashPassword(username, password);
	}

	/**
	 * Sets the script which is being used.
	 */
	private void setScript() {
		switch (this.scriptName) {
			case SMF:
				this.script = new SMFScript(this.scriptName, this.version, this.dataManager);
		}
	}

	/**
	 * Converts a string into a script.
	 *
	 * @param string The string which
	 * @return The script for the string, if none are found it returns null.
	 */
	private Scripts stringToScript(String string) {
		return Scripts.SMF;
	}
}
