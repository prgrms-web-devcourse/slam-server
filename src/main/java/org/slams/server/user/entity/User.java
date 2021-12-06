package org.slams.server.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.text.MessageFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User extends BaseEntity {

	private static final int MIN_NICKNAME_LENGTH = 1;
	private static final int MAX_NICKNAME_LENGTH = 30;
	private static final int MAX_DESCRIPTION_LENGTH = 100;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "social_id", nullable = false, unique = true)
	private Long socialId;

	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;

	@Column(name = "nickname", nullable = false, length = MAX_NICKNAME_LENGTH)
	private String nickname;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "description", length = MAX_DESCRIPTION_LENGTH)
	private String description;

	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "skill")
	private Skill skill;

	@Column(name = "position")
	private Position position;

	private User(Long socialId, String email, String nickname, String profileImage,
				 String description, Role role, Skill skill, Position position) {
		Assert.notNull(socialId, "socialId는 null이 될 수 없습니다.");
		Assert.notNull(email, "email은 null이 될 수 없습니다.");
		Assert.notNull(role, "role은 null이 될 수 없습니다.");

		validateNickname(nickname);
		validateDescription(description);

		this.socialId = socialId;
		this.email = email;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.description = description;
		this.role = role;
		this.skill = skill;
		this.position = position;
	}

	public static User of(Long socialId, String email, String nickname, String profileImage,
						  String description, Role role, Skill skill, Position position) {
		return new User(socialId, email, nickname, profileImage, description, role, skill, position);
	}

	//== Business Method ==//
	public void updateDetails(String nickname, String description, Skill skill, Position position) {
		validateNickname(nickname);
		validateDescription(description);

		this.nickname = nickname;
		this.description = description;
		this.skill = skill;
		this.position = position;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void deleteProfileImage() {
		this.profileImage = null;
	}

	//== Validation Method ==//
	private void validateNickname(String nickname) {
		Assert.notNull(nickname, "nickname은 null이 될 수 없습니다.");

		if (nickname.length() < MIN_NICKNAME_LENGTH || nickname.length() > MAX_NICKNAME_LENGTH) {
			throw new IllegalArgumentException(
				MessageFormat.format("nickname의 길이는 최소 {0}이상 {1}자 이하입니다. nickname = {2}",
					MIN_NICKNAME_LENGTH, MAX_NICKNAME_LENGTH, nickname));
		}
	}

	private void validateDescription(String description) {
		if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
			throw new IllegalArgumentException(
				MessageFormat.format("description의 길이는 최대 {0}자 이하입니다. description = {1}",
					MAX_DESCRIPTION_LENGTH, description));
		}
	}

}