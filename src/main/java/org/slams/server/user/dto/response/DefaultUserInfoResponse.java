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

	private UserResponse user;
	private List<NotificationResponse> notifications;

	private DefaultUserInfoResponse(UserResponse user, List<NotificationResponse> notifications) {
		this.user = user;
		this.notifications = notifications;
	}

	public static DefaultUserInfoResponse toResponse(User user, List<NotificationResponse> notifications) {
		return new DefaultUserInfoResponse(UserResponse.toResponse(user), notifications);
	}

}
