package org.slams.server.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CursorPageResponse<T> {

	private T data;
	private Long lastId;

}
