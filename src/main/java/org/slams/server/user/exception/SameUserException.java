package org.slams.server.user.exception;

import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.error.exception.InvalidValueException;

public class SameUserException extends InvalidValueException {

	public SameUserException(String value) {
		super(value, ErrorCode.SAME_USER_VALUE);
	}

}
