package org.slams.server.follow.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowingResponse {

	private Long followId;
	private Long receiverId;
	private String nickname;
	private String profileImage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private FollowingResponse(Long followId, Long receiverId, String nickname, String profileImage,
							  LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.followId = followId;
		this.receiverId = receiverId;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static FollowingResponse toResponse(Long followId, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
		return new FollowingResponse(followId, user.getId(), user.getNickname(), user.getProfileImage(),
			createdAt, updatedAt);
	}

}
