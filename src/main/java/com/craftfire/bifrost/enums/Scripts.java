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

/**
 * This enum holds all the different supported scripts.
 */
public enum Scripts {
    MYBB("mybb", ScriptType.FORUM),
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
}