package org.slams.server.common.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

	// Common
	INVALID_INPUT_VALUE(400, "Invalid Input Value"),
	METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	ENTITY_NOT_FOUND(400, "Entity Not Found"),
	INTERNAL_SERVER_ERROR(500, "Server Error"),
	INVALID_TYPE_VALUE(400, "Invalid Type Value"),
	HANDLE_ACCESS_DENIED(403, "Access is Denied"),

	// User
	USER_NOT_AUTHENTICATION(401, "User has No Authentication"),
	USER_NOT_AUTHORIZED(403, "User is Denied to Access"),
	NOT_EXIST_MEMBER(404,"존재하지 않는 회원입니다.")
	;

	private final String message;
	private int status;

	ErrorCode(final int status, final String message) {
		this.status = status;
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public int getStatus() {
		return status;
	}

}
