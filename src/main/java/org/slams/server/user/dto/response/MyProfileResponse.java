package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyProfileResponse {

	private DefaultUserDto user;
	private Long followerCount;
	private Long followingCount;

	private MyProfileResponse(DefaultUserDto user, Long followerCount, Long followingCount) {
		this.user = user;
		this.followerCount = followerCount;
		this.followingCount = followingCount;
	}

	public static MyProfileResponse toResponse(User user, Long followerCount, Long followingCount) {
		return new MyProfileResponse(DefaultUserDto.toDto(user), followerCount, followingCount);
	}

}
