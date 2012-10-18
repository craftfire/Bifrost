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
package com.craftfire.bifrost.classes.general;

import com.craftfire.bifrost.exceptions.ScriptException;

import java.sql.SQLException;
import java.util.List;

/**
 * Everything that can be parent of {@link Message}.
 * <p>
 * Used for complex structures of message replies.
 */
public interface MessageParent {
    /**
     * Returns the list of messages whose parent is this object.
     * <p>
     * If the MessageParent is a message, these are usually replies to the message.
     * If the MessageParent is a Category, these are contained messages.
     * 
     * @param  limit            how many messages should be returned, 0 = returns all
     * @return                  the list of messages
     * @throws ScriptException  if the method is not supported by script
     * @throws SQLException     if a SQL exception occurred
     */
    List<? extends Message> getChildMessages(int limit) throws ScriptException, SQLException;
}
