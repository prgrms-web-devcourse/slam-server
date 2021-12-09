package org.slams.server.user.exception;

import org.slams.server.common.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

	public UserNotFoundException(String message) {
		super(message);
	}

}
