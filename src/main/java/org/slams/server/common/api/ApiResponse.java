package org.slams.server.common.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {


	private T data;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime serverDateTime;
	private Boolean success;

	public ApiResponse(T data, Boolean success) {
		this.data = data;
		this.serverDateTime = LocalDateTime.now();
		this.success=success;
	}

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(data,true);
	}

	public static <T> ApiResponse<T> fail(T data) {
		return new ApiResponse<>(data,false);
	}



}
