package org.slams.server.notification.Exception;

import org.slams.server.common.error.exception.InvalidValueException;

import static org.slams.server.common.error.exception.ErrorCode.INVALID_INPUT_VALUE;

/**
 * Created by yunyun on 2021/12/16.
 */
public class InvalidCourtStartTime extends InvalidValueException {
    public InvalidCourtStartTime(String value) {
        super(value, INVALID_INPUT_VALUE);
    }
}