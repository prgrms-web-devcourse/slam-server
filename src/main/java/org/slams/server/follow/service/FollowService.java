package org.slams.server.follow.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.follow.dto.FollowerResponse;
import org.slams.server.follow.dto.FollowingResponse;
import org.slams.server.follow.entity.Follow;
import org.slams.server.follow.exception.FollowAlreadyExistException;
import org.slams.server.follow.exception.FollowNotFoundException;
import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	public CursorPageResponse<List<FollowerResponse>> followerPage(Long userId, CursorPageRequest cursorPageRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		PageRequest pageable = PageRequest.of(0, cursorPageRequest.getSize());
		List<Follow> followers = cursorPageRequest.getIsFirst() ?
			followRepository.findByFollowingIdOrderByIdDesc(userId, pageable) :
			followRepository.findByFollowingIdAndIdLessThanOrderByIdDesc(userId, cursorPageRequest.getLastId(), pageable);

		List<FollowerResponse> followerList = new ArrayList<>();
		for (Follow follow : followers) {
			followerList.add(
				FollowerResponse.toResponse(
					follow.getId(), follow.getFollower(), follow.getCreatedAt(), follow.getUpdateAt())
			);
		}

		Long lastId = followers.size() < cursorPageRequest.getSize() ? null : followers.get(followers.size() - 1).getId();

		return new CursorPageResponse<>(followerList, lastId);
	}

	public CursorPageResponse<List<FollowingResponse>> followingPage(Long userId, CursorPageRequest cursorPageRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		PageRequest pageable = PageRequest.of(0, cursorPageRequest.getSize());
		List<Follow> followings = cursorPageRequest.getIsFirst() ?
			followRepository.findByFollowerIdOrderByIdDesc(userId, pageable) :
			followRepository.findByFollowerIdAndIdLessThanOrderByIdDesc(userId, cursorPageRequest.getLastId(), pageable);

		List<FollowingResponse> followingList = new ArrayList<>();
		for (Follow follow : followings) {
			followingList.add(
				FollowingResponse.toResponse(
					follow.getId(), follow.getFollowing(), follow.getCreatedAt(), follow.getUpdateAt())
			);
		}

		Long lastId = followings.size() < cursorPageRequest.getSize() ? null : followings.get(followings.size() - 1).getId();

		return new CursorPageResponse<>(followingList, lastId);
	}

	@Transactional
	public void follow(Long myId, Long followingId){
		User follower = userRepository.findById(myId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", myId)));
		User following = userRepository.findById(followingId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", followingId)));

		if (followRepository.existsByFollowerAndFollowing(follower, following))
			throw new FollowAlreadyExistException(
				MessageFormat.format("이미 팔로우하고 있는 사용자입니다. id : {0}", followingId));

		followRepository.save(Follow.of(follower, following));
	}

	@Transactional
	public void unfollow(Long myId, Long followingId){
		User follower = userRepository.findById(myId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", myId)));
		User following = userRepository.findById(followingId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", followingId)));

		if (!followRepository.existsByFollowerAndFollowing(follower, following))
			throw new FollowNotFoundException(
				MessageFormat.format("원래 팔로우관계를 맺고 있지 않은 사용자입니다. id : {0}", followingId));

		followRepository.deleteByFollowerAndFollowing(follower, following);
	}

}
