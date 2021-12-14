package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImageResponse {

	private String profileImage;

	public ProfileImageResponse(String profileImage){
		this.profileImage = profileImage;
	}

}
