package org.slams.server.follow.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowCreatorDto {

	private Long id;
	private String nickname;
	private String profileImage;

	private FollowCreatorDto(Long id, String nickname, String profileImage) {
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	public static FollowCreatorDto toDto(User creator) {
		return new FollowCreatorDto(creator.getId(), creator.getNickname(), creator.getProfileImage());
	}

}
