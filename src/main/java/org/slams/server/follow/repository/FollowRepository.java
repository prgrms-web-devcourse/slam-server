package org.slams.server.follow.repository;

import org.slams.server.follow.entity.Follow;
import org.slams.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	// 해당 유저의 팔로워 숫자 (누군가 나를 팔로우 하고 있는 수를 셈)
	Long countByFollower(User user);

	// 해당 유저의 팔로잉 숫자
	Long countByFollowing(User user);

	// 해당 유저의 팔로워 목록(무한 스크롤 - 최초)
	@Query("SELECT f FROM Follow f WHERE f.following.id = :followingId order by f.id desc")
	List<Follow> findByFollowingIdOrderByIdDesc(@Param("followingId") Long followingId, Pageable pageable);
	// 해당 유저의 팔로워 목록(무한 스크롤)
	@Query("SELECT f FROM Follow f WHERE f.following.id = :followingId and f.id < :lastId order by f.id desc")
	List<Follow> findByFollowingIdAndIdLessThanOrderByIdDesc(
		@Param("followingId") Long followingId,@Param("lastId") Long lastId, Pageable pageable);

	// 해당 유저의 팔로잉 목록(무한 스크롤 - 최초)
	@Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId order by f.id desc")
	List<Follow> findByFollowerIdOrderByIdDesc(@Param("followerId") Long followerId, Pageable pageable);
	// 해당 유저의 팔로잉 목록(무한 스크롤)
	@Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId and f.id < :lastId order by f.id desc")
	List<Follow> findByFollowerIdAndIdLessThanOrderByIdDesc(
		@Param("followerId") Long followerId,@Param("lastId") Long lastId, Pageable pageable);

}
