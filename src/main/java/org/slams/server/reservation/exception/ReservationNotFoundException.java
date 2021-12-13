package org.slams.server.reservation.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;

public class ReservationNotFoundException extends EntityNotFoundException {

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
