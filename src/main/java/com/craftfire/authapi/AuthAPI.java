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
import com.craftfire.authapi.classes.Script;
import com.craftfire.authapi.classes.ScriptUser;
import com.craftfire.commons.DataManager;

public class AuthAPI {
	private final ScriptAPI scriptAPI;
	private final Script script;
	private final DataManager dataManager;

	public AuthAPI(Scripts script, String version, String host, int port, String database, String username,
				   String password, String prefix) {
		this.dataManager = new DataManager(host, port, database, username, password, prefix);
		this.scriptAPI = new ScriptAPI(script, version, this.dataManager);
		this.script = this.scriptAPI.getScript();
	}

	public AuthAPI(Scripts script, String version, DataManager dataManager) {
		this.scriptAPI = new ScriptAPI(script, version, dataManager);
		this.script = this.scriptAPI.getScript();
		this.dataManager = dataManager;
	}

	public Script getScript() {
		return this.script;
	}

	public ScriptUser getUser(String username) {
		return this.script.getUser(username);
	}
	
	public boolean authenticate(String username, String password) {
		return this.script.authenticate(username, password);
	}
}
