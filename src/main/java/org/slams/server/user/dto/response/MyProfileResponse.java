package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyProfileResponse {

	private Long userId;
	private String nickname;
	private String profileImage;
	private String description;
	private Proficiency proficiency;
	private List<Position> positions;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long followerCount;
	private Long followingCount;

	private MyProfileResponse(Long userId, String nickname, String profileImage, String description,
							  Proficiency proficiency, List<Position> positions,
							  LocalDateTime createdAt, LocalDateTime updatedAt,
							  Long followerCount, Long followingCount) {
		this.userId = userId;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.description = description;
		this.proficiency = proficiency;
		this.positions = positions;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.followerCount = followerCount;
		this.followingCount = followingCount;
	}

	public static MyProfileResponse toResponse(User user, Long followerCount, Long followingCount) {
		return new MyProfileResponse(user.getId(), user.getNickname(), user.getProfileImage(),
			user.getDescription(), user.getProficiency(), user.getPositions(),
			user.getCreatedAt(), user.getUpdateAt(), followerCount, followingCount
		);
	}

}
