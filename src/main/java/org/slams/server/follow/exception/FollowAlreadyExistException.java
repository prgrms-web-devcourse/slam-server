package org.slams.server.follow.exception;

import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.error.exception.InvalidValueException;

public class FollowAlreadyExistException extends InvalidValueException {

	public FollowAlreadyExistException(String value) {
		super(value, ErrorCode.FOLLOW_ALREADY_EXIST);
	}

}
