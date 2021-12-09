package org.slams.server.user.exception;

import org.slams.server.common.error.exception.BusinessException;
import org.slams.server.common.error.exception.ErrorCode;

public class UserNotAuthenticatedException extends BusinessException {

	public UserNotAuthenticatedException() {
		super(ErrorCode.USER_NOT_AUTHENTICATION);
	}

}
