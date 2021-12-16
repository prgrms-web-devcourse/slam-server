package org.slams.server.follow.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.follow.dto.FollowResponse;
import org.slams.server.follow.entity.Follow;
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
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	public CursorPageResponse<List<FollowResponse>> followerPage(Long userId, CursorPageRequest cursorPageRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		PageRequest pageable = PageRequest.of(0, cursorPageRequest.getSize());
		List<Follow> followers = cursorPageRequest.getIsFirst() ?
			followRepository.findByFollowingIdOrderByIdDesc(userId, pageable) :
			followRepository.findByFollowingIdAndIdLessThanOrderByIdDesc(userId, cursorPageRequest.getLastId(), pageable);

		List<FollowResponse> followerList = new ArrayList<>();
		for (Follow follow : followers) {
			followerList.add(
				FollowResponse.toResponse(
					follow.getId(), follow.getFollower(), follow.getCreatedAt(), follow.getUpdateAt())
			);
		}

		Long lastId = followers.size() < cursorPageRequest.getSize() ? null : followers.get(followers.size() - 1).getId();

		return new CursorPageResponse<>(followerList, lastId);
	}

}
