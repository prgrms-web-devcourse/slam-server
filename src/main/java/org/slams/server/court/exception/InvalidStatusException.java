package org.slams.server.court.exception;

import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.error.exception.InvalidValueException;

public class InvalidStatusException extends InvalidValueException {

	public InvalidStatusException(String value) {
		super(value, ErrorCode.INVALID_STATUS_VALUE);
	}

}
