package org.slams.server.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CursorPageRequest {

	private int size;
	private Long lastId;
	private Boolean isFirst;

	public void setSize(int size) {
		this.size = size;
	}

	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}

	public void setIsFirst(Boolean first) {
		this.isFirst = first;
	}

}