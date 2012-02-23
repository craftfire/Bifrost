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
package com.craftfire.authapi.classes;

import java.util.Date;

public class ScriptUser implements ScriptUserInterface {
	private final Script script;
	private final String username;
	private String password;

	public ScriptUser(Script script, String username, String password) {
		this.script = script;
		this.username = username;
		this.password = password;
	}

	public ScriptUser(Script script, String username) {
		this.script = script;
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public Script getScript() {
		return this.script;
	}

	public String getPasswordHash() {
		return this.script.getPasswordHash(this.username);
	}

	public String getCurrentPasswordHash() {
		return this.script.hashPassword(this.username, this.password);
	}

	public Date getBirthday() {
		return this.script.getBirthday(this.username);
	}

	public boolean authenticate() {
		return this.script.authenticate(this.username, this.password);
	}
}
