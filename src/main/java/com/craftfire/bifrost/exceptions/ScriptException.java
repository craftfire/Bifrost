/*
 * This file is part of Bifrost.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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
package com.craftfire.bifrost.exceptions;

import com.craftfire.bifrost.classes.general.Script;

/**
 * This exception should be thrown when the library user uses a method which is not supported by the script.
 */
@SuppressWarnings("serial")
public class ScriptException extends Exception {
    private Type type = Type.UNSUPPORTED_METHOD;

    public enum Type {
        UNSUPPORTED_METHOD, UNSUPPORTED_VERSION, UNSUPPORTED_SCRIPT
    }

    /**
     * Default constructor with a default message.
     */
    public ScriptException() {
        super("This method is not supported by this script.");
    }

    /**
     * Constructs the exception with the specified {@code message}.
     *
     * @param message  the message for the exception
     */
    public ScriptException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with the specified {@code message}.
     *
     * @param message  the message for the exception
     * @param type     exception type
     */
    public ScriptException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public ScriptException(Script script) {
        super("Version " + script.getVersion() + " of " + script.getScriptName() + " is not currently supported");
    }

    public Type getType() {
        return this.type;
    }
}
