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

    public CMSUser(CMSScript script, String username, String password) {
        super(script, username, password);
    }

    public int getCommentCount() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getUserCommentCount(getUsername());
    }

    public int getArticleCount() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getUserArticleCount(getUsername());
    }

    public CMSComment getLastComment() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getLastUserComment(getUsername());
    }

    public CMSArticle getLastArticle() throws UnsupportedMethod {
        return Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).getLastUserArticle(getUsername());
    }

    @Override
    public CMSScript getScript() {
        return (CMSScript) super.getScript();
    }

    @Override
    public void update() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).updateUser(this);
    }

    @Override
    public void create() throws SQLException, UnsupportedMethod {
        Bifrost.getInstance().getScriptAPI().getCMSHandle(getScript().getScript()).createUser(this);
    }
}
