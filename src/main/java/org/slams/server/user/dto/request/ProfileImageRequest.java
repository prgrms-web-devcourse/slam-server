package org.slams.server.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImageRequest {

	private String profileImage;

	public ProfileImageRequest(String profileImage) {
		this.profileImage = profileImage;
	}

}
