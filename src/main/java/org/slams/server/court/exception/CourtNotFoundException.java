package org.slams.server.court.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.common.error.exception.ErrorCode;

public class CourtNotFoundException extends EntityNotFoundException {
    public CourtNotFoundException(String message) {
        super(message);
    }
}
