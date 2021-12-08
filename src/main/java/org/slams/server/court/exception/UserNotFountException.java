package org.slams.server.court.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.common.error.exception.ErrorCode;

public class UserNotFountException extends EntityNotFoundException {
    public UserNotFountException(String message, ErrorCode notExistMember) {
        super(message);
    }
}