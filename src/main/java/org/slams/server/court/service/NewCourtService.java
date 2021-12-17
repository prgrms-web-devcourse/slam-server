package org.slams.server.court.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.repository.NewCourtRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewCourtService {

	private final NewCourtRepository newCourtRepository;
	private final UserRepository userRepository;

	@Transactional
	public CourtInsertResponseDto insert(CourtInsertRequestDto request, Long id) {
		// user검색후 없으면 반환
		User user = getUser(id);

		NewCourt newCourt = request.insertRequestDtoToEntity(request);

		newCourtRepository.save(newCourt);
		return new CourtInsertResponseDto(newCourt);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));
	}

}
