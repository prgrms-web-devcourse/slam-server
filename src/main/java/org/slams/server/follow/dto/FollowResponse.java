package org.slams.server.follow.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowResponse {

	private Long followId;
	private Long creatorId;
	private String nickname;
	private String profileImage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private FollowResponse(Long followId, Long creatorId, String nickname, String profileImage,
						   LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.followId = followId;
		this.creatorId = creatorId;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static FollowResponse toResponse(Long followId, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
		return new FollowResponse(followId, user.getId(), user.getNickname(), user.getProfileImage(),
			createdAt, updatedAt);
	}

}
