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
	INVALID_INPUT_TOKEN(400, "Invalid Input Token"),
	NOT_EXIST_MEMBER(404,"존재하지 않는 회원입니다."),

	//Court
	NOT_EXIST_COURT(404,"존재하지 않는 코트입니다."),

	// New Court
	NEWCOURT_NOT_FOUND(400, "New Court Not Found"),

	// Reservation
	NOT_EXIST_RESERVATION(404,"존재하지 않는 예약입니다."),
	NOT_FORBIDDEN_RESERVATION(403,"권한이 없습니다"),

	// Favorite
	NOT_EXIST_FAVORITE(404,"존재하지 않는 즐겨찾기입니다."),


	// Follow
	FOLLOW_ALREADY_EXIST(400, "Follow Already Exists"),
	FOLLOW_NOT_FOUND(400, "Follow Not Found")

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
