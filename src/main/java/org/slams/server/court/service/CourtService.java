package org.slams.server.court.service;

import lombok.AllArgsConstructor;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.UserTempRepository;
import org.slams.server.user.entity.User;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slams.server.court.exception.*;

@Service
public class CourtService {

    private final CourtRepository courtRepository;
    private final UserTempRepository userTempRepository;



    public CourtService(CourtRepository courtRepository, UserTempRepository userTempRepository) {
        this.courtRepository=courtRepository;
        this.userTempRepository=userTempRepository;
    }


    @Transactional
    public Long insert(CourtInsertRequestDto request, Long id) {
        // user검색후 없으면 반환
        User user = getUser(id);
        Court court = request.insertRequestDtoToEntity(request, id);

//        court.addPost(user); // 양방향을 해야할까?!

        return courtRepository.save(court).getId();
//        return new PostDetailResponse(insertedPost);
    }


    @Transactional
    public User getUser(Long userId) {
        return userTempRepository.findById(userId)
                .orElseThrow(() -> new UserNotFountException("해당 유저 없어",ErrorCode.NOT_EXIST_MEMBER));
    }







}
