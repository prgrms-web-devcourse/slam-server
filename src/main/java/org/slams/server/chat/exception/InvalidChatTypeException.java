package org.slams.server.chat.exception;

import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.error.exception.InvalidValueException;

/**
 * Created by yunyun on 2021/12/17.
 */
public class InvalidChatTypeException extends InvalidValueException {

    public InvalidChatTypeException(String value) {
        super(value);
    }

    public InvalidChatTypeException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
