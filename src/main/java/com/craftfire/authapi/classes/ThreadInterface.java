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
import java.util.List;

public interface ThreadInterface {
	public Post getFirstPost();

	public Post getLastPost();

	public List<Post> getPosts(int limit);

	public int getID();

	public void setID(int id);

	public int getBoardID();

	public Date getThreadDate();

	public void setThreadDate(Date threaddate);

	public ScriptUser getAuthor();

	public void setAuthor(ScriptUser author);

	public String getSubject();

	public void setSubject(String subject);

	public String getBody();

	public void setBody(String body);

	public int getViews();

	public void setViews(int threadviews);

	public int getReplies();

	public void setReplies(int threadreplies);

	public boolean isLocked();

	public void setLocked(boolean isLocked);

	public boolean isPoll();

	public void setPoll(boolean isPoll);

	public boolean isSticky();

	public void setSticky(boolean isSticky);

	public void updateThread();

	public void createThread();
}