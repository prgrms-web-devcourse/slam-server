package org.slams.server.common.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CursorPageResponse<T> {

	private T contents;
	private Long lastId;

}
