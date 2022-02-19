package org.slams.server.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	private String socialId;

	@Pattern(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b")
	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;

	@Column(name = "nickname", nullable = false, length = MAX_NICKNAME_LENGTH)
	private String nickname;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "description", length = MAX_DESCRIPTION_LENGTH)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "proficiency")
	private Proficiency proficiency;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<Position> positions = new ArrayList<>();

	private User(String socialId, String email, String nickname, String profileImage,
				 String description, Role role, Proficiency proficiency, List<Position> positions) {
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
		this.proficiency = proficiency;
		this.positions = positions;
	}

	@Builder
	public User(LocalDateTime createdAt, LocalDateTime updatedAt, String nickname, String email, Long id, String description, String profileImage, Role role, Proficiency proficiency, List<Position> positions, String socialId) {
		super(createdAt, updatedAt);
		this.nickname = nickname;
		this.email = email;
		this.id = id;
		this.description = description;
		this.profileImage = profileImage;
		this.role = role;
		this.proficiency = proficiency;
		this.positions = positions;
		this.socialId = socialId;
	}

	public static User of(String socialId, String email, String nickname, String profileImage,
						  String description, Role role, Proficiency proficiency, List<Position> positions) {
		return new User(socialId, email, nickname, profileImage, description, role, proficiency, positions);
	}

	//== Business Method ==//
	public void clearPositions() {
		this.positions.clear();
	}

	public void updateInfo(String nickname, String description, Proficiency proficiency, List<Position> positions) {
		validateNickname(nickname);
		validateDescription(description);
		validatePositions(positions);

		clearPositions();

		this.nickname = nickname;
		this.description = description;
		this.proficiency = proficiency;
		this.positions = positions;
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

	private void validatePositions(List<Position> positions) {
		if (positions.contains(Position.TBD) && positions.size() > 1) {
			throw new IllegalArgumentException("TBD(미정) 선택 시 다른 position은 선택할 수 없습니다.");
		}
		if (positions.size() > 2) {
			throw new IllegalArgumentException("선호하는 포지션은 최대 2개까지 선택 가능합니다.");
		}
	}

}