package org.slams.server.court.service;

import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.AllCourtResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.NewCourtRepository;
import org.slams.server.court.repository.UserTempRepository;
import org.slams.server.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.slams.server.court.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtService {

    private final CourtRepository courtRepository;
    private final UserTempRepository userTempRepository;
    private final NewCourtRepository newCourtRepository;



    public CourtService(CourtRepository courtRepository, UserTempRepository userTempRepository, NewCourtRepository newCourtRepository) {
        this.courtRepository=courtRepository;
        this.userTempRepository=userTempRepository;
        this.newCourtRepository=newCourtRepository;
    }


    @Transactional
    public CourtInsertResponseDto insert(CourtInsertRequestDto request, Long id) {
        // user검색후 없으면 반환
        User user = getUser(id);
        NewCourt newCourt = request.insertRequestDtoToEntity(request);

        newCourtRepository.save(newCourt);
        return new CourtInsertResponseDto(newCourt);
    }


    @Transactional
    public User getUser(Long userId) {
        return userTempRepository.findById(userId)
                .orElseThrow(() -> new UserNotFountException("해당 유저 없습니다.",ErrorCode.NOT_EXIST_MEMBER));
    }


    @Transactional
    public List<AllCourtResponseDto> findAll() {
        return courtRepository.findAll().stream()
                .map(AllCourtResponseDto::new)
                .collect(Collectors.toList());
    }








}
