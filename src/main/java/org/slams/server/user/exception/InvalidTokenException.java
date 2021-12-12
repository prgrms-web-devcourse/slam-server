package org.slams.server.user.exception;

import org.slams.server.common.error.exception.InvalidValueException;

import static org.slams.server.common.error.exception.ErrorCode.INVALID_INPUT_TOKEN;

public class InvalidTokenException extends InvalidValueException {

	public InvalidTokenException(String value) {
		super(value, INVALID_INPUT_TOKEN);
	}

}
