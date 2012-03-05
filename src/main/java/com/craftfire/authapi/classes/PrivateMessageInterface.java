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

public interface PrivateMessageInterface {
	public int getID();

	public void setID(int id);
	
	public ScriptUser getFromUser();
	
	public void setFromUser(ScriptUser user);
	
	public ScriptUser getToUser();
	
	public void setToUser(ScriptUser user);
	
	public Date getDate();
	
	public void setDate(Date date);
	
	public String getSubject();
	
	public void setSubject(String subject);
	
	public String getBody();
	
	public void setBody(String body);

	public boolean isDeletedBySender();
	
	public void setDeletedBySender(boolean deleted);

	public boolean isRead();
	
	public void setRead(boolean read);
	
	public boolean isNew();
	
	public void setNew(boolean isnew);
	
	public boolean isDeleted();
	
	public void setDeleted(boolean deleted);
	
	public void updatePrivateMessage();

	public void createPrivateMessage();
}
