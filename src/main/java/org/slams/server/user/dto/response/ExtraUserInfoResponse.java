package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtraUserInfoResponse {

	private DefaultUserDto user;

	public ExtraUserInfoResponse(DefaultUserDto user) {
		this.user = user;
	}

	public static ExtraUserInfoResponse toResponse(User user) {
		return new ExtraUserInfoResponse(DefaultUserDto.toDto(user));
	}

}
