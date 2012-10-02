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
package com.craftfire.bifrost.classes.cms;

import java.sql.SQLException;

import com.craftfire.bifrost.Bifrost;
import com.craftfire.bifrost.classes.general.ScriptUser;
import com.craftfire.bifrost.exceptions.UnsupportedMethod;

public class CMSUser extends ScriptUser {

    public CMSUser(CMSScript script, int userid, String username, String password) {
        super(script, userid, username, password);
    }

    public CMSUser(CMSHandle handle, String username, String password) {
        super(handle, username, password);
    }

    public int getCommentCount() throws UnsupportedMethod {
        return getCMSHandle().getUserCommentCount(getUsername());
    }

    public int getArticleCount() throws UnsupportedMethod {
        return getCMSHandle().getUserArticleCount(getUsername());
    }

    public CMSComment getLastComment() throws UnsupportedMethod {
        return getCMSHandle().getLastUserComment(getUsername());
    }

    public CMSArticle getLastArticle() throws UnsupportedMethod {
        return getCMSHandle().getLastUserArticle(getUsername());
    }

    @Override
    public void update() throws SQLException, UnsupportedMethod {
        getCMSHandle().updateUser(this);
    }

    @Override
    public void create() throws SQLException, UnsupportedMethod {
        getCMSHandle().createUser(this);
    }
}
