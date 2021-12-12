package org.slams.server.notification.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.entity.NotificationType;
import org.slams.server.notification.repository.NotificationRepository;
import org.slams.server.court.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by yunyun on 2021/12/09.
 */


@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationRepository alarmRepository;

    @Autowired
    private NotificationService alarmService;

    @Autowired
    private CourtRepository courtRepository;

    @BeforeEach
    void setUp(){
        /** following 알람 메시지 **/
        Notification alarmForFollowing = Notification.createNotificationForFollowing(
                null,
                11L,
                "flora"
        );
        /** 농구장 확성기 알람 메시지 **/

        Notification alarmForLoudSpeaker = Notification.createNotificationForLoudSpeaker(
                10L,
                11L,
                13,
                "잠실농구장");

        alarmRepository.save(alarmForFollowing);
        alarmRepository.save(alarmForLoudSpeaker);
    }

    @AfterEach
    void tearDown(){
    alarmRepository.deleteAll();
    courtRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 구별키를 이용하여, 알람 정보를 추출할 수 있다.")
    void findByUserId(){
        //When
        List<Notification> alarmList = alarmRepository.findAllByUserId(11L);

        //Then
        assertThat(alarmList.size(), is(2));
        assertThat(alarmList.get(0).getNotificationType(), is(NotificationType.FOLLOWING_ALARM));
        assertThat(alarmList.get(0).getUserId(), is(11L));
        assertThat(alarmList.get(0).getContent(), containsString("flora"));
        assertThat(alarmList.get(1).getNotificationType(), is(NotificationType.LOUDSPEAKER));
        assertThat(alarmList.get(1).getContent(), containsString("잠실농구장"));
    }

}