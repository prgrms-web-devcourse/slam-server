package org.slams.server.follow.repository;

import org.slams.server.follow.entity.Follow;
import org.slams.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	// 해당 유저의 팔로워 숫자 (누군가 나를 팔로우 하고 있는 수를 셈)
	Long countByFollower(User user);

	// 해당 유저의 팔로잉 숫자
	Long countByFollowing(User user);

}
