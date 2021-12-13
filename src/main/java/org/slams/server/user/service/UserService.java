package org.slams.server.user.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.error.exception.EntityNotFoundException;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

//	public String findUserNickname(Long id){
//		User user = userRepository.findById(id)
//			.orElseThrow(()-> new EntityNotFoundException("X"));
//
//		return user.getNickname();
//	}

}
