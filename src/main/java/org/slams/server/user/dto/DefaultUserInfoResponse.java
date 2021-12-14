package org.slams.server.user.dto;

import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.Skill;

public class DefaultUserInfoResponse {

	private Long id;
	private String email;
	private String nickname;
	private String profileImage;
	private String description;
	private Role role;
	private Skill skill;
	private Position position;

}
