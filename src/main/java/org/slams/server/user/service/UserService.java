package org.slams.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.common.utils.AwsS3Uploader;

import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.user.dto.request.ExtraUserInfoRequest;
import org.slams.server.user.dto.request.ProfileImageRequest;
import org.slams.server.user.dto.response.ExtraUserInfoResponse;
import org.slams.server.user.dto.response.MyProfileResponse;
import org.slams.server.user.dto.response.ProfileImageResponse;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	private final AwsS3Uploader awsS3Uploader;

	private final AwsS3Uploader awsS3Uploader;

	@Transactional
	public ExtraUserInfoResponse addExtraUserInfo(Long userId, ExtraUserInfoRequest extraUserInfoRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		user.updateInfo(extraUserInfoRequest.getNickname(), extraUserInfoRequest.getDescription(),
			extraUserInfoRequest.getProficiency(), extraUserInfoRequest.getPositions());
		userRepository.flush(); // updatedAt 반영

		return ExtraUserInfoResponse.entityToResponse(user);
	}

	public MyProfileResponse getMyInfo(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		Long followerCount = followRepository.countByFollower(user);
		Long followingCount = followRepository.countByFollowing(user);

		return MyProfileResponse.toResponse(user, followerCount, followingCount);
	}

	@Transactional
	public ProfileImageResponse updateUserProfileImage(Long userId, ProfileImageRequest profileImageRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		String profileImageUrl = awsS3Uploader.upload(profileImageRequest.getProfileImage(), "profile");

		String profileImage = user.updateProfileImage(profileImageUrl);

		return new ProfileImageResponse(profileImage);
	}

	@Transactional
	public ProfileImageResponse deleteUserProfileImage(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		user.deleteProfileImage();

		return new ProfileImageResponse(null);
	}
//	public String findUserNickname(Long id){
//		User user = userRepository.findById(id)
//			.orElseThrow(()-> new EntityNotFoundException("X"));
//
//		return user.getNickname();
//	}

}
