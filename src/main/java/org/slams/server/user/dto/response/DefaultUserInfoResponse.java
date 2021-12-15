package org.slams.server.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultUserInfoResponse {

	private Long userId;
	private String email;
	private String nickname;
	private String profileImage;
	private String description;
	private Role role;
	private Proficiency proficiency;
	private List<Position> positions;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<NotificationResponse> notifications;

	public DefaultUserInfoResponse(Long userId, String email, String nickname,
								   String profileImage, String description, Role role,
								   Proficiency proficiency, List<Position> positions,
								   LocalDateTime createdAt, LocalDateTime updatedAt,
								   List<NotificationResponse> notifications) {
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.description = description;
		this.role = role;
		this.proficiency = proficiency;
		this.positions = positions;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.notifications = notifications;
	}

	public static DefaultUserInfoResponse toResponse(User user, List<NotificationResponse> notifications) {
		return new DefaultUserInfoResponse(user.getId(), user.getEmail(), user.getNickname(), user.getProfileImage(),
			user.getDescription(), user.getRole(), user.getProficiency(), user.getPositions(),
			user.getCreatedAt(), user.getUpdateAt(), notifications);
	}

}
