package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserWithoutEmailDto {

	private Long id;
	private String nickname;
	private String profileImage;
	private String description;
	private Role role;
	private Proficiency proficiency;
	private List<Position> positions = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private UserWithoutEmailDto(Long id, String nickname, String profileImage, String description,
								Role role, Proficiency proficiency, List<Position> positions,
								LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.description = description;
		this.role = role;
		this.proficiency = proficiency;
		this.positions = positions;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static UserWithoutEmailDto toDto(User user){
		return new UserWithoutEmailDto(user.getId(), user.getNickname(), user.getProfileImage(), user.getDescription(),
			user.getRole(), user.getProficiency(), user.getPositions(),
			user.getCreatedAt(), user.getUpdateAt());
	}

}
