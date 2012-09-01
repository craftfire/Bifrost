package com.craftfire.bifrost.classes.general;

import java.util.List;

import com.craftfire.bifrost.exceptions.UnsupportedMethod;

/**
 * Everything that can be parent of {@see Message}.
 * <p>
 * Used for complex structures of message replies.
 */
public interface MessageParent {
    /**
     * Returns the list of messages whose parent is this object.
     * <p>
     * If the MessageParent is a message, these are usually replies to the message. If the MessageParent is a Category, these are contained messages.
     * 
     * @param  limit              how many messages should be returned, 0 = returns all
     * @return                    the list of messages
     * @throws UnsupportedMethod  if the method is not supported by script
     */
    public List<? extends Message> getChildMessages(int limit) throws UnsupportedMethod;
}
