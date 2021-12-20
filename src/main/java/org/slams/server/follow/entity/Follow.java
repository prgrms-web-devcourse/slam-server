package org.slams.server.follow.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.user.entity.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "follow", uniqueConstraints = { @UniqueConstraint(columnNames = {"follower_id", "following_id"} ) } )
public class Follow extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "follower_id", nullable = false, referencedColumnName = "id")
	private User follower;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "following_id", nullable = false, referencedColumnName = "id")
	private User following;

	private Follow(User follower, User following) {
		super();
		this.follower = follower;
		this.following = following;
	}

	public static Follow of(User follower, User following) {
		return new Follow(follower, following);
	}

}