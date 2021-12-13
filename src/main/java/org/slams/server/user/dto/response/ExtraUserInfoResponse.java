package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExtraUserInfoResponse {

	private Long id;
	private String email;
	private String nickname;
	private String profileImage;
	private String description;
	private Role role;
	private Proficiency proficiency;
	private List<Position> positions;

	private ExtraUserInfoResponse(Long id, String email, String nickname, String profileImage, String description,
								 Role role, Proficiency proficiency, List<Position> positions) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.description = description;
		this.role = role;
		this.proficiency = proficiency;
		this.positions = positions;
	}

	public static ExtraUserInfoResponse entityToResponse(User user) {
		return new ExtraUserInfoResponse(user.getId(), user.getEmail(), user.getNickname(), user.getProfileImage(),
			user.getDescription(), user.getRole(), user.getProficiency(), user.getPositions());
	}

}
