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

public class Post implements PostInterface {
	private final String author, authorip, authoremail, subject, body;
	private final int postid, threadid, boardid, authorid;
	private final Date postdate;

	public Post(int postid, int threadid, int boardid, Date postdate, int authorid, String author, String authoremail, String authorip, String subject, String body) {
		this.postid = postid;
		this.threadid = threadid;
		this.boardid = boardid;
		this.postdate = postdate;
		this.authorid  = authorid;
		this.author = author;
        this.authoremail = authoremail;
		this.authorip = authorip;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public int getPostID() {
		return this.postid;
	}

	@Override
	public int getThreadID() {
		return this.threadid;
	}

	@Override
	public int getBoardID() {
		return this.boardid;
	}

	@Override
	public Date getPostDate() {
		return this.postdate;
	}

	@Override
	public int getAuthorID() {
		return this.authorid;
	}

	@Override
	public String getAuthor() {
		return this.author;
	}

    @Override
    public String getAuthorEmail() {
        return this.authoremail;
    }

	@Override
	public String getAuthorIP() {
		return this.authorip;
	}

	@Override
	public String getSubject() {
		return this.subject;
	}

	@Override
	public String getBody() {
		return this.body;
	}
}
