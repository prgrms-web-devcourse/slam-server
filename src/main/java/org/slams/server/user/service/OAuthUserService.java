package org.slams.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.utils.JSONObjectMapper;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OAuthUserService {

	@Value("${slam.admin.emails}")
	private final List<String> adminEmails;

	private final UserRepository userRepository;

	public Optional<User> findBySocialId(String socialId) {
		checkArgument(isNotEmpty(socialId), "socialId must be provided.");

		return userRepository.findBySocialId(socialId);
	}

	@Transactional
	public User join(OAuth2User oauth2User, String authorizedClientRegistrationId) {
		checkArgument(oauth2User != null, "oauth2User must be provided.");
		checkArgument(isNotEmpty(authorizedClientRegistrationId), "authorizedClientRegistrationId must be provided.");

		String socialId = oauth2User.getName();
		return findBySocialId(socialId)
			// 이미 가입한(존재하는) 사용자
			.map(user -> {
				log.warn("Already exists: {} for socialId : {})", user, socialId);
				return user;
			})
			// 새로 가입하려는 사용자
			.orElseGet(() -> {
				Map<String, Object> attributes = oauth2User.getAttributes();
				log.info(JSONObjectMapper.toJson(attributes));
				@SuppressWarnings("unchecked")
				Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
				checkArgument(properties != null, "OAuth2User properties is empty");

				Map<String, Object> accounts = (Map<String, Object>) attributes.get("kakao_account");
				Map<String, Object> accountsDetail = (Map<String, Object>) accounts.get("profile");

				String nickname = (String) properties.get("nickname");
				String email = (String) accounts.get("email");
				String profileImage = (String) accountsDetail.get("profile_image_url");
				Boolean isDefaultImage = (Boolean) accountsDetail.get("is_default_image");
				if (isDefaultImage) {
					profileImage = null;
				}

		        if(adminEmails.contains(email)){
					return userRepository.save(
							User.of(socialId, email, nickname, profileImage, null, Role.ADMIN, null, null)
					);
				}
				return userRepository.save(
					User.of(socialId, email, nickname, profileImage, null, Role.USER, null, null)
				);
			});
	}

}