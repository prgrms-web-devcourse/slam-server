package org.slams.server.court.service;

import lombok.AllArgsConstructor;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.utils.AwsS3Uploader;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.AllCourtResponseDto;
import org.slams.server.court.dto.response.CourtDetailResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.NewCourtRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slams.server.court.exception.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CourtService {

    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final NewCourtRepository newCourtRepository;
    private final AwsS3Uploader awsS3Uploader;




    @Transactional
    public CourtInsertResponseDto insert(CourtInsertRequestDto request, Long id) {
        // user검색후 없으면 반환
        User user = getUser(id);


        request.setImage(awsS3Uploader.upload(request.getImage(),"court"));

        NewCourt newCourt = request.insertRequestDtoToEntity(request);

        newCourtRepository.save(newCourt);
        return new CourtInsertResponseDto(newCourt);
    }


    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));
    }


    @Transactional
    public List<AllCourtResponseDto> findAll() {
        return courtRepository.findAll().stream()
                .map(AllCourtResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public CourtDetailResponseDto findDetail(Long courtId) {

        return courtRepository.findById(courtId)
                .map(CourtDetailResponseDto::new)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));
    }


    @Transactional
    public Court getCourt(Long CourtId) {
        return courtRepository.findById(CourtId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));

    }








}
