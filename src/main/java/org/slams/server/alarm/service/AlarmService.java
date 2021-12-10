package org.slams.server.alarm.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.alarm.dto.AlarmRequest;
import org.slams.server.alarm.entity.Alarm;
import org.slams.server.alarm.repository.AlarmRepository;
import org.slams.server.court.entity.Court;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;


    public void saveForLoudSpeaker(AlarmRequest alarmDto){
        alarmRepository.save(
                Alarm.createAlarmForLoudSpeaker(
                        alarmDto.getCourtId(),
                        alarmDto.getUserId(),
                        alarmDto.getStartTime(),
                        alarmDto.getCourtName()
                )
        );
    }

    public void saveForFollowing(Long userId, Long followingUserId){
        User userFollowed = userRepository.findById(followingUserId).orElseThrow(()-> new RuntimeException("데이터 없음"));
        alarmRepository.save(
                Alarm.createAlarmForFollowing(
                        null,
                        userId,
                        userFollowed.getNickname()
                )
        );
    }

    /** 무한스크롤 적용해야함. **/
    public List<Alarm> findAllByUserId(Long userId){
        return alarmRepository.findAllByUserId(userId);
    }

}
