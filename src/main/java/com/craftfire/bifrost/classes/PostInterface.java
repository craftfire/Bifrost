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
package com.craftfire.bifrost.classes;

import com.craftfire.bifrost.exceptions.UnsupportedFunction;

import java.sql.SQLException;
import java.util.Date;

public abstract interface PostInterface {

    /**
     * Returns the id of the Post.
     *
     * @return the id of the Post.
     */
    public int getID();

    /**
     * sets the id of the Post.
     *
     * @param id
     */
    public void setID(int id);

    public int getThreadID();

    public Thread getThread() throws UnsupportedFunction;

    public int getBoardID();

    public Date getPostDate();

    public void setPostDate(Date postdate);

    public ScriptUser getAuthor();

    public void setAuthor(ScriptUser author);

    public String getSubject();

    public void setSubject(String subject);

    public String getBody();

    public void setBody(String body);

    public void updatePost() throws SQLException, UnsupportedFunction;

    public void createPost() throws SQLException, UnsupportedFunction;
}
