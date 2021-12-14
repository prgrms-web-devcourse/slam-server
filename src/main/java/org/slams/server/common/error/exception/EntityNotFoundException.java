package org.slams.server.common.error.exception;

public class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException(String message) {
		super(ErrorCode.ENTITY_NOT_FOUND);
	}

}
