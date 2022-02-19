package org.slams.server.follow.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowRecieverDto {

	private Long id;
	private String nickname;
	private String profileImage;

	private FollowRecieverDto(Long id, String nickname, String profileImage) {
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	public static FollowRecieverDto toDto(User reciever) {
		return new FollowRecieverDto(reciever.getId(), reciever.getNickname(), reciever.getProfileImage());
	}

}
