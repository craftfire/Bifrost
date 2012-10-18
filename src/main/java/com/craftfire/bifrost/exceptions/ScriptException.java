package com.craftfire.bifrost.exceptions;

import com.craftfire.bifrost.classes.general.Script;

/**
 * This exception should be thrown when the library user uses a method which is not supported by the script.
 */
@SuppressWarnings("serial")
public class ScriptException extends Exception {
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

    public ScriptException(Script script) {
        super("Version " + script.getVersion() + " of " + script.getScriptName() + " is not currently supported");
    }
}
