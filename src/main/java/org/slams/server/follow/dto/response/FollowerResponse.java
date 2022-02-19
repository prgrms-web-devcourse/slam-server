package org.slams.server.follow.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.follow.entity.Follow;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowerResponse {

	private Long id;
	private FollowCreatorDto creator;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private FollowerResponse(Long id, FollowCreatorDto creator, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.creator = creator;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static FollowerResponse toResponse(Follow follow) {
		return new FollowerResponse(follow.getId(), FollowCreatorDto.toDto(follow.getFollower()), follow.getCreatedAt(), follow.getUpdateAt());
	}

}
