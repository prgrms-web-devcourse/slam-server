package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteCourtResponse {

	private Long courtId;
	private String courtName;

	public FavoriteCourtResponse(Long courtId, String courtName) {
		this.courtId = courtId;
		this.courtName = courtName;
	}

}
