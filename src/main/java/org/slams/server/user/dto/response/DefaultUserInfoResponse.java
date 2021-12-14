package org.slams.server.user.dto.response;

import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.Proficiency;

public class DefaultUserInfoResponse {

	private Long id;
	private String email;
	private String nickname;
	private String profileImage;
	private String description;
	private Role role;
	private Proficiency proficiency;
	private Position position;

}
