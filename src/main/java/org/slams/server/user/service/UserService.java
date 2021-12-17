package org.slams.server.user.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.utils.AwsS3Uploader;

import org.slams.server.court.entity.Texture;
import org.slams.server.favorite.repository.FavoriteRepository;
import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.notification.dto.response.CourtInfo;
import org.slams.server.notification.dto.response.FollowerInfo;
import org.slams.server.notification.dto.response.LoudspeakerInfo;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.NotificationIndex;
import org.slams.server.notification.entity.NotificationType;
import org.slams.server.notification.service.NotificationService;
import org.slams.server.user.dto.request.ExtraUserInfoRequest;
import org.slams.server.user.dto.request.ProfileImageRequest;
import org.slams.server.user.dto.response.*;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Notification;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.slams.server.notification.entity.NotificationType.FOLLOWING;
import static org.slams.server.notification.entity.NotificationType.LOUDSPEAKER;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

	private final NotificationService notificationService;

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final FavoriteRepository favoriteRepository;

	private final AwsS3Uploader awsS3Uploader;

	public DefaultUserInfoResponse getDefaultInfo(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

//		NotificationResponse followNotification = NotificationResponse.createForFollowNotification(FOLLOWING, FollowerInfo.builder()
//			.userId(1L)
//			.userNickname("젤리")
//			.userImage("https://team14-slam.s3.ap-northeast-2.amazonaws.com/profile/%E1%84%82%E1%85%A1.png")
//			.build(), false, false, LocalDateTime.now(), LocalDateTime.now());
//		NotificationResponse loudspeakerNotification = NotificationResponse.createForLoudspeakerNotification(LOUDSPEAKER, LoudspeakerInfo.builder()
//			.courtInfo(
//				CourtInfo.builder()
//					.id(3L)
//					.name("용산구 농구장")
//					.latitude(123)
//					.longitude(456)
//					.image("https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court1.jpg")
//					.basketCount(4)
//					.texture(Texture.ASPHALT)
//					.build()
//			)
//			.startTime(13)
//			.build(), false, false, LocalDateTime.now(), LocalDateTime.now());
//
//		List<NotificationResponse> notifications = List.of(followNotification, loudspeakerNotification);

		List<NotificationResponse> top10Notifications = notificationService.getTop10Notification(user.getId());

		return DefaultUserInfoResponse.toResponse(user, top10Notifications);
	}

	@Transactional
	public ExtraUserInfoResponse addExtraUserInfo(Long userId, ExtraUserInfoRequest extraUserInfoRequest) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		user.updateInfo(extraUserInfoRequest.getNickname(), extraUserInfoRequest.getDescription(),
			extraUserInfoRequest.getProficiency(), extraUserInfoRequest.getPositions());
		userRepository.flush(); // updatedAt 반영

		return ExtraUserInfoResponse.toResponse(user);
	}

	public MyProfileResponse getMyInfo(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		Long followerCount = followRepository.countByFollower(user);
		Long followingCount = followRepository.countByFollowing(user);

		return MyProfileResponse.toResponse(user, followerCount, followingCount);
	}

	public UserProfileResponse getUserInfo(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		Long followerCount = followRepository.countByFollower(user);
		Long followingCount = followRepository.countByFollowing(user);

		List<FavoriteCourtResponse> favoriteCourts = favoriteRepository.findAllByUser(user)
			.stream().map(favorite -> new FavoriteCourtResponse(favorite.getCourt().getId(), favorite.getCourt().getName()))
			.collect(Collectors.toList());

		return UserProfileResponse.toResponse(user, followerCount, followingCount, favoriteCourts);
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
