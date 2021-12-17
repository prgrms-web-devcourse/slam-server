package org.slams.server.court.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.common.error.exception.ErrorCode;

public class NewCourtNotFoundException extends EntityNotFoundException {

	public NewCourtNotFoundException(String message) {
		super(message, ErrorCode.NEWCOURT_NOT_FOUND);
	}

}
