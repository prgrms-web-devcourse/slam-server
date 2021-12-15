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
public class UserProfileResponse {

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
	private List<FavoriteCourtResponse> favoriteCourts;

	private UserProfileResponse(Long userId, String nickname, String profileImage, String description,
								Proficiency proficiency, List<Position> positions,
								LocalDateTime createdAt, LocalDateTime updatedAt,
								Long followerCount, Long followingCount, List<FavoriteCourtResponse> favoriteCourts) {
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
		this.favoriteCourts = favoriteCourts;
	}

	public static UserProfileResponse toResponse(User user, Long followerCount, Long followingCount, List<FavoriteCourtResponse> favoriteCourts) {
		return new UserProfileResponse(user.getId(), user.getNickname(), user.getProfileImage(),
			user.getDescription(), user.getProficiency(), user.getPositions(),
			user.getCreatedAt(), user.getUpdateAt(), followerCount, followingCount, favoriteCourts
		);
	}

}
