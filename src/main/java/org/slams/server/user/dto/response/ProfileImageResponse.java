package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileImageResponse {

	private UserWithoutEmailDto user;

	private ProfileImageResponse(UserWithoutEmailDto user) {
		this.user = user;
	}

	public static ProfileImageResponse toResponse(User user){
		return new ProfileImageResponse(UserWithoutEmailDto.toDto(user));
	}

}
