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
