package org.slams.server.follow.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.common.error.exception.ErrorCode;

public class FollowNotFoundException extends EntityNotFoundException {

	public FollowNotFoundException(String message) {
		super(message, ErrorCode.FOLLOW_NOT_FOUND);
	}

}
