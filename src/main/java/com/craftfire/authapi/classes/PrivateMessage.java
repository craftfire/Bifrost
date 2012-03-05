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

public class PrivateMessage implements PrivateMessageInterface {
	private final Script script;
	private int pmid;
	private String subject, body;
	private ScriptUser fromuser, touser;
	private Date date;
	private boolean deletedbysender, deleted, read, isnew;

	public PrivateMessage(Script script, int pmid) {
		this.script = script;
		this.pmid = pmid;
	}

	public PrivateMessage(Script script, ScriptUser fromuser, ScriptUser touser) {
		this.script = script;
		this.fromuser = fromuser;
		this.touser = touser;
	}
	
	@Override
	public int getID() {
		return this.pmid;
	}
	
	@Override
	public void setID(int id) {
		this.pmid = id;
	}

	@Override
	public ScriptUser getFromUser() {
		return this.fromuser;
	}

	@Override
	public void setFromUser(ScriptUser user) {
		this.fromuser = user;
	}

	@Override
	public ScriptUser getToUser() {
		return this.touser;
	}

	@Override
	public void setToUser(ScriptUser user) {
		this.touser = user;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String getSubject() {
		return this.subject;
	}

	@Override
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String getBody() {
		return this.body;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public boolean isDeletedBySender() {
		return this.deletedbysender;
	}

	@Override
	public void setDeletedBySender(boolean deleted) {
		this.deletedbysender = deleted;
	}

	@Override
	public boolean isRead() {
		return this.read;
	}

	@Override
	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public boolean isNew() {
		return this.isnew;
	}

	@Override
	public void setNew(boolean isnew) {
		this.isnew = isnew;
	}

	@Override
	public boolean isDeleted() {
		return this.deleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public void updatePrivateMessage() {
		this.script.updatePrivateMessage(this);
	}

	@Override
	public void createPrivateMessage() {
		this.script.createPrivateMessage(this);
	}
}
