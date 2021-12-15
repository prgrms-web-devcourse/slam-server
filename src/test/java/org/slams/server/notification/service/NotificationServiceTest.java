package org.slams.server.notification.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.notification.dto.CursorRequest;
import org.slams.server.notification.dto.NotificationResponse;
import org.slams.server.notification.dto.request.FollowNotificationRequest;
import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.entity.NotificationType;
import org.slams.server.notification.repository.NotificationRepository;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by yunyun on 2021/12/09.
 */

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp(){
        /** following 알림 메시지 **/
        User receiver = User.of(
                "receiver-socialId",
                "receiver@test.com",
                "receiver",
                "http://test-image-location-receiver",
                "소개-receiver",
                Role.USER,
                Proficiency.INTERMEDIATE,
                List.of(Position.PG)
        );
        User user = User.of(
                "socialId-user",
                "user@test.com",
                "user",
                "http://test-image-location-user",
                "소개-user",
                Role.USER,
                Proficiency.INTERMEDIATE,
                List.of(Position.TBD)
        );

        userRepository.save(receiver);
        userRepository.save(user);

        Court court = new Court(
                "잠실 농구장",
                1203.20302,
                2038.2939,
                "https://court-image",
                1,
                Texture.CONCRETE
        );
        courtRepository.save(court);

        FollowNotification.of(1L, receiver, )



        /** 농구장 확성기 알림 메시지 **/
        Notification alarmForLoudSpeaker = Notification.createNotificationForLoudSpeaker(
                10L,
                11L,
                13,
                "잠실농구장");

        /** following 알림 메시지 **/
        Notification alarmForFollowing2 = Notification.createNotificationForFollowing(
                null,
                11L,
                "flora"
        );
        alarmRepository.save(alarmForFollowing1);
        alarmRepository.save(alarmForLoudSpeaker);
        alarmRepository.save(alarmForFollowing2);
    }

    @AfterEach
    void tearDown(){
    alarmRepository.deleteAll();
    courtRepository.deleteAll();
    }


    @Test
    @DisplayName("사용자 구별키를 이용하여, 공지의 최초 정보부터 특정 개수의 정보를 추출할 수 있다.")
    void findAllByUserIdIsFirstTrue(){
        //Given
        CursorRequest cursorRequest = new CursorRequest(5, 0L,true);

        //When
        List<NotificationResponse> notificationList = alarmService.findAllByUserId(11L, cursorRequest);

        //Then
        assertThat(notificationList.size(), is(3));
        assertThat(notificationList.get(0).getNotificationType(), is(NotificationType.FOLLOWING_ALARM));
        assertThat(notificationList.get(0).getMessage(), containsString("flora"));
        assertThat(notificationList.get(1).getNotificationType(), is(NotificationType.LOUDSPEAKER));
        assertThat(notificationList.get(1).getMessage(), containsString("잠실농구장"));

    }

    @Test
    @DisplayName("사용자 구별키를 이용하여, 공지의 최초 정보부터 특정 개수의 정보를 추출할 수 있다.")
    void findAllByUserIdIsFirstFalse(){
        //Given
        Long lastId = alarmRepository.findAllByUserId(11L).get(1).getId();
        CursorRequest cursorRequest = new CursorRequest(5, lastId,false);

        //When
        List<NotificationResponse> notificationList = alarmService.findAllByUserId(11L, cursorRequest);

        //Then
        assertThat(notificationList.size(), is(2));
        assertThat(notificationList.get(0).getNotificationType(), is(NotificationType.LOUDSPEAKER));
        assertThat(notificationList.get(0).getMessage(), containsString("잠실농구장"));

    }

}