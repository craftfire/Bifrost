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
package com.craftfire.bifrost.classes.forum;

import java.sql.SQLException;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;
import com.craftfire.bifrost.script.ForumScript;

public class ForumUser extends ScriptUser {

    public ForumUser(ForumScript script, int userid, String username, String password) {
        super(script, userid, username, password);
    }

    public ForumUser(ForumScript script, String username, String password) {
        super(script, username, password);
    }

    public int getPostCount() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getPostCount(getUsername());
    }

    public int getThreadCount() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getThreadCount(getUsername());
    }

    public ForumThread getLastThread() throws UnsupportedMethod, SQLException {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getLastUserThread(getUsername());
    }

    public ForumPost getLastPost() throws UnsupportedMethod, SQLException {
        return Bifrost.getInstance().getScriptAPI().getForumHandle(getScript().getScript()).getLastUserPost(getUsername());
    }

    @Override
    public ForumScript getScript() {
        return (ForumScript) super.getScript();
    }
}
