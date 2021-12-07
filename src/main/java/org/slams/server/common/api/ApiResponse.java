package org.slams.server.common.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

	private T data;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime serverDateTime;

	public ApiResponse(T data) {
		this.data = data;
		this.serverDateTime = LocalDateTime.now();
	}

	public static <T> ApiResponse<T> ok(T data) {
		String message = "save";

		return new ApiResponse<>(data);
	}

	public static <T> ApiResponse<T> fail(T data) {
		return new ApiResponse<>(data);
	}

}
