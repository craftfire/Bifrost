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
package com.craftfire.bifrost.enums;

import com.craftfire.bifrost.classes.general.Script;
import com.craftfire.bifrost.scripts.cms.WordPress;
import com.craftfire.bifrost.scripts.forum.PhpBB;
import com.craftfire.bifrost.scripts.forum.SMF;
import com.craftfire.bifrost.scripts.forum.XenForo;
import com.craftfire.commons.managers.DataManager;

/**
 * This enum holds all the different supported scripts.
 */
public enum Scripts {
    WP("wordpress", ScriptType.CMS),
    PHPBB("phpbb", ScriptType.FORUM),
    SMF("simplemachines", ScriptType.FORUM),
    XF("xenforo", ScriptType.FORUM);

    private final String alias;
    private final ScriptType type;

    /**
     * Construct the script, we have to define the alias and {@link ScriptType}  .
     *
     * @param alias the alias of the script
     * @param type the {@link ScriptType} of the script
     */
    Scripts(String alias, ScriptType type) {
        this.alias = alias;
        this.type = type;
    }

    /**
     * Returns the alias of the script, the alias is the full length name of the script.
     *
     * @return alias of the script
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * Returns the {@link ScriptType} of the script.
     *
     * @return {@link ScriptType}
     */
    public ScriptType getType() {
        return this.type;
    }

    /**
     * Returns a {@link Script} object depending on which <code>script</code> and <code>version</code> has been used.
     * <p>
     * Returns <code>null</code> if the <code>script</code> is not supported.
     *
     * @param script       the script
     * @param version      the version of the script
     * @param dataManager  the {@link com.craftfire.commons.managers.DataManager} for the script
     * @return             a {@link Script} object, returns null if not supported
     */
    public static Script getScript(Scripts script, String version, DataManager dataManager) {
        switch (script) {
            case WP:
                return new WordPress(script, version, dataManager);
            case PHPBB:
                return new PhpBB(script, version, dataManager);
            case SMF:
                return new SMF(script, version, dataManager);
            case XF:
                return new XenForo(script, version, dataManager);
            default:
                return null;
        }
    }
}
