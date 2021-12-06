package org.slams.server.common.error;

import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.BusinessException;
import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.error.exception.InvalidValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// @RequestBody binding error
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("handleMethodArgumentNotValidException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// @ModelAttribute binding error
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.error("handleBindException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// enum type mismatch error
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error("handleMethodArgumentTypeMismatchException", e);
		ErrorResponse errorResponse = ErrorResponse.of(e);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// 지원하지 않는 HTTP Method error
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("handleAccessDeniedException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// Business error
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		log.error("handleBusinessException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// EntityNotFoundException
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
		log.error("handleEntityNotFoundException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// InvalidValueException
	@ExceptionHandler(InvalidValueException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidValueException(InvalidValueException e) {
		log.error("handleInvalidValueException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// IllegalArgumentException
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("handleIllegalArgumentException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);

		return ResponseEntity.badRequest().body(errorResponse);
	}

	// other Exception
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("handleException", e);
		ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

		return ResponseEntity.internalServerError().body(errorResponse);
	}

}