package org.slams.server.favorite.service;

import lombok.AllArgsConstructor;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.AllCourtResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.exception.UserNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.favorite.dto.request.FavoriteInsertRequestDto;
import org.slams.server.favorite.dto.response.FavoriteInsertResponseDto;
import org.slams.server.favorite.entity.Favorite;
import org.slams.server.favorite.repository.FavoriteRepository;
import org.slams.server.reservation.dto.response.ReservationDeleteResponseDto;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;



    @Transactional
    public FavoriteInsertResponseDto insert(Long courtId, Long id) {
        // User 검색 후 없으면 반환
        User user = getUser(id);

        // 코트 검색 후 없으면 반환
        Court court=getCourt(courtId);

        Favorite favorite = Favorite.of(court,user);
        Favorite save = favoriteRepository.save(favorite);

        return new FavoriteInsertResponseDto(save);
    }


    // 내가 즐겨찾기 한 코트 검색
    public List<FavoriteInsertResponseDto> getAll(Long userId) {
        User user =getUser(userId);

        return favoriteRepository.findAllByUser(user).stream()
                .map(FavoriteInsertResponseDto::new)
                .collect(Collectors.toList());
    }

    public Long delete(Long userId, Long favoriteId) {
        User user =getUser(userId);

        Favorite reservation= favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_FAVORITE.getMessage()));

        favoriteRepository.delete(reservation);
        return favoriteId;

    }


    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));
    }


    @Transactional
    public Court getCourt(Long CourtId) {
        return courtRepository.findById(CourtId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));

    }



}
