package org.slams.server.reservation.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;

public class ForbiddenException extends EntityNotFoundException {

    public ForbiddenException(String message) {
        super(message);
    }
}
