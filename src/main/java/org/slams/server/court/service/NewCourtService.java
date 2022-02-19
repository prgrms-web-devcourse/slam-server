package org.slams.server.court.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.service.ChatroomMappingService;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.dto.response.NewCourtResponse;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.exception.InvalidStatusException;
import org.slams.server.court.exception.NewCourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.NewCourtRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewCourtService {

	private final ChatroomMappingService chatroomMappingService;

	private final NewCourtRepository newCourtRepository;
	private final CourtRepository courtRepository;
	private final UserRepository userRepository;

	public CursorPageResponse<List<NewCourtResponse>> getNewCourtsInStatus(String status, CursorPageRequest cursorPageRequest) {
		PageRequest pageable = PageRequest.of(0, cursorPageRequest.getSize());

		List<NewCourt> newCourts;
		switch (status) {
			case "READY":
				newCourts = cursorPageRequest.getIsFirst() ?
					newCourtRepository.findByStatusOrderByIdDesc(List.of(Status.READY), pageable) :
					newCourtRepository.findByStatusLessThanIdOrderByIdDesc(List.of(Status.READY), cursorPageRequest.getLastId(), pageable);
				break;
			case "DONE":
				newCourts = cursorPageRequest.getIsFirst() ?
					newCourtRepository.findByStatusOrderByIdDesc(List.of(Status.ACCEPT, Status.DENY), pageable) :
					newCourtRepository.findByStatusLessThanIdOrderByIdDesc(List.of(Status.ACCEPT, Status.DENY), cursorPageRequest.getLastId(), pageable);
				break;
			default:
				throw new InvalidStatusException(MessageFormat.format("잘못된 상태값입니다. status : {0}", status));
		}

		List<NewCourtResponse> newCourtList = new ArrayList<>();
		for (NewCourt newCourt : newCourts) {
			newCourtList.add(
				NewCourtResponse.toResponse(newCourt)
			);
		}

		Long lastId = newCourtList.size() < cursorPageRequest.getSize() ? null : newCourts.get(newCourts.size() - 1).getId();

		return new CursorPageResponse<>(newCourtList, lastId);
	}

	@Transactional
	public CourtInsertResponseDto insert(CourtInsertRequestDto request, Long id) {
		// user검색후 없으면 반환
		User user = userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", id)));

		NewCourt newCourt = request.insertRequestDtoToEntity(request);

		newCourtRepository.save(newCourt);
		return new CourtInsertResponseDto(newCourt);
	}


	@Transactional
	public NewCourtResponse acceptNewCourt(Long newCourtId, Long supervisorId) {
		NewCourt newCourt = newCourtRepository.findById(newCourtId)
			.orElseThrow(() -> new NewCourtNotFoundException(
				MessageFormat.format("사용자가 추가한 농구장을 찾을 수 없습니다. id : {0}", newCourtId)
			));

		User supervisor = userRepository.findById(supervisorId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", supervisorId)
			));

		newCourt.acceptNewCourt(supervisor);

		Court court = courtRepository.save(Court.builder()
			.name(newCourt.getName())
			.latitude(newCourt.getLatitude())
			.longitude(newCourt.getLongitude())
			.image(newCourt.getImage())
			.basketCount(newCourt.getBasketCount())
			.texture(newCourt.getTexture())
			.reservations(Collections.emptyList())
			.build());

		// 채팅방 생성
		chatroomMappingService.saveChatRoom(court.getId());

		return NewCourtResponse.toResponse(newCourt);
	}

	@Transactional
	public NewCourtResponse denyNewCourt(Long newCourtId, Long supervisorId) {
		NewCourt newCourt = newCourtRepository.findById(newCourtId)
			.orElseThrow(() -> new NewCourtNotFoundException(
				MessageFormat.format("사용자가 추가한 농구장을 찾을 수 없습니다. id : {0}", newCourtId)
			));

		User supervisor = userRepository.findById(supervisorId)
			.orElseThrow(() -> new UserNotFoundException(
				MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", supervisorId)
			));

		newCourt.denyNewCourt(supervisor);

		return NewCourtResponse.toResponse(newCourt);
	}

}
