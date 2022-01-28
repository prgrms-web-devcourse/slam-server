package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;
import org.springframework.security.access.method.P;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImageResponse {

	private UserWithoutEmailDto profileImage;

	private ProfileImageResponse(UserWithoutEmailDto profileImage) {
		this.profileImage = profileImage;
	}

	public static ProfileImageResponse toResponse(User user){
		return new ProfileImageResponse(UserWithoutEmailDto.toDto(user));
	}

}
