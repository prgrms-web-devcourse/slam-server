package org.slams.server.notification.Exception;

import org.slams.server.common.error.exception.InvalidValueException;

/**
 * Created by yunyun on 2021/12/20.
 */
public class InvalidNotificationTypeException extends InvalidValueException {
    public InvalidNotificationTypeException(String value) {
        super(value);
    }
}
