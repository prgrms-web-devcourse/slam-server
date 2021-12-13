package org.slams.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.user.dto.request.ExtraUserInfoRequest;
import org.slams.server.user.dto.response.ExtraUserInfoResponse;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public ExtraUserInfoResponse addExtraUserInfo(Long userId, ExtraUserInfoRequest extraUserInfoRequest){
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

		user.updateInfo(extraUserInfoRequest.getNickname(), extraUserInfoRequest.getDescription(),
			extraUserInfoRequest.getProficiency(), extraUserInfoRequest.getPositions());
		userRepository.flush(); // updatedAt 반영

		return ExtraUserInfoResponse.entityToResponse(user);
	}

//	public String findUserNickname(Long id){
//		User user = userRepository.findById(id)
//			.orElseThrow(()-> new EntityNotFoundException("X"));
//
//		return user.getNickname();
//	}

}
