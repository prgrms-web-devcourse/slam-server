package org.slams.server.follow.repository;

import org.slams.server.follow.entity.Follow;
import org.slams.server.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<Follow, Long> {

	// 해당 유저의 팔로잉 숫자
	Long countByFollower(User user);

	// 해당 유저의 팔로워 숫자
	Long countByFollowing(User user);

	// 해당 유저의 팔로워 목록(무한 스크롤 - 최초)
	@Query("SELECT f FROM Follow f WHERE f.following.id = :followingId order by f.id desc")
	List<Follow> findByFollowingIdOrderByIdDesc(@Param("followingId") Long followingId, Pageable pageable);
	// 해당 유저의 팔로워 목록(무한 스크롤)
	@Query("SELECT f FROM Follow f WHERE f.following.id = :followingId and f.id < :lastId order by f.id desc")
	List<Follow> findByFollowingIdAndIdLessThanOrderByIdDesc(
		@Param("followingId") Long followingId, @Param("lastId") Long lastId, Pageable pageable);

	// 해당 유저의 팔로잉 목록(무한 스크롤 - 최초)
	@Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId order by f.id desc")
	List<Follow> findByFollowerIdOrderByIdDesc(@Param("followerId") Long followerId, Pageable pageable);
	// 해당 유저의 팔로잉 목록(무한 스크롤)
	@Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId and f.id < :lastId order by f.id desc")
	List<Follow> findByFollowerIdAndIdLessThanOrderByIdDesc(
		@Param("followerId") Long followerId, @Param("lastId") Long lastId, Pageable pageable);

	// 팔로우 관계를 맺고있는지 확인
	Boolean existsByFollowerAndFollowing(User follower, User following);

	// 팔로우 관계 삭제
	@Modifying
	@Query("DELETE FROM Follow f WHERE f.follower.id = :followerId and f.following.id = :followingId")
	void deleteByFollowerAndFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

	// 팔로우 관계 follower 내가 팔로우 함 / following 누군가 나를 팔로우 함
	Optional<Follow> findByFollowerAndFollowing(User follower, User following);

}
