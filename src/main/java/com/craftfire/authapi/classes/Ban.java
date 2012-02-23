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

public class Ban implements BanInterface {
	private final String username, email, ip, reason, notes;
	private final int userid;
	private final Date startdate, enddate;

	public Ban(String username, String email, String ip, String reason, String notes, int userid, Date startdate, Date enddate) {
		this.username = username;
		this.email = email;
		this.ip = ip;
		this.reason = reason;
		this.notes = notes;
		this.userid = userid;
		this.startdate = startdate;
		this.enddate = enddate;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public int getUserID() {
		return this.userid;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public String getIP() {
		return this.ip;
	}

	@Override
	public long getTimeLength() {
		if (! isPermanent()) {
			return (this.enddate.getTime() - this.startdate.getTime());
		}
		return 0;
	}

	@Override
	public long getTimeRemaining() {
		if (! isPermanent()) {
			Date now = new Date();
			return (this.enddate.getTime() - now.getTime());
		}
		return 0;
	}

	@Override
	public String getReason() {
		return this.reason;
	}

	@Override
	public String getNotes() {
		return this.notes;
	}

	@Override
	public Date getStartDate() {
		return this.startdate;
	}

	@Override
	public Date getEndDate() {
		return this.enddate;
	}

	@Override
	public boolean isPermanent() {
		if (getEndDate() == null) {
			return true;
		}
		return false;
	}
}
