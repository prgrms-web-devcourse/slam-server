//package org.slams.server.notification.service;
//
//import org.junit.jupiter.api.*;
//import org.slams.server.common.api.CursorPageRequest;
//import org.slams.server.court.entity.Court;
//import org.slams.server.court.entity.Texture;
//import org.slams.server.notification.dto.request.FollowNotificationRequest;
//import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
//import org.slams.server.notification.dto.request.UpdateIsClickedStatusRequest;
//import org.slams.server.notification.dto.response.NotificationResponse;
//import org.slams.server.notification.entity.FollowNotification;
//import org.slams.server.notification.entity.LoudSpeakerNotification;
//import org.slams.server.notification.entity.NotificationIndex;
//import org.slams.server.notification.entity.NotificationType;
//import org.slams.server.notification.repository.FollowNotificationRepository;
//import org.slams.server.notification.repository.LoudSpeakerNotificationRepository;
//import org.slams.server.notification.repository.NotificationIndexRepository;
//import org.slams.server.court.repository.CourtRepository;
//import org.slams.server.user.entity.Position;
//import org.slams.server.user.entity.Proficiency;
//import org.slams.server.user.entity.Role;
//import org.slams.server.user.entity.User;
//import org.slams.server.user.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.CoreMatchers.*;
//
///**
// * Created by yunyun on 2021/12/09.
// */
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class NotificationServiceTest {
//
//    @Autowired
//    private NotificationIndexRepository notificationIndexRepository;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private CourtRepository courtRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private FollowNotificationRepository followNotificationRepository;
//
//    @Autowired
//    private LoudSpeakerNotificationRepository loudSpeakerNotificationRepository;
//
//    User receiver = null;
//    User user = null;
//    Court court =null;
//
//    @BeforeAll
//    void setUp(){
//
//        receiver = User.of(
//                "receiver-socialId",
//                "receiver@test.com",
//                "receiver",
//                "http://test-image-location-receiver",
//                "소개-receiver",
//                Role.USER,
//                Proficiency.INTERMEDIATE,
//                List.of(Position.PG)
//        );
//        user = User.of(
//                "socialId-user",
//                "user@test.com",
//                "user",
//                "http://test-image-location-user",
//                "소개-user",
//                Role.USER,
//                Proficiency.INTERMEDIATE,
//                List.of(Position.TBD)
//        );
//
//        userRepository.save(receiver);
//        userRepository.save(user);
//
//        Court courtEntity = new Court(
//                "잠실 농구장",
//                1203.20302,
//                2038.2939,
//                "https://court-image",
//                1,
//                Texture.CONCRETE
//        );
//        courtRepository.save(courtEntity);
//        court = courtRepository.findAll().get(0);
//    }
//
//    @AfterAll
//    void tearDown(){
////        loudSpeakerNotificationRepository.deleteAll();
////        followNotificationRepository.deleteAll();
////        notificationIndexRepository.deleteAll();
////        userRepository.deleteAll();
////        courtRepository.deleteAll();
//
//    }
//
//
//    @Test
//    @Order(1)
//    @DisplayName("사용자 구별키를 이용하여, 공지의 최초 정보부터 특정 개수의 정보를 추출할 수 있다.")
//    void findAllByUserIdIsFirstTrue(){
//        //Given
//        CursorPageRequest cursorRequest = new CursorPageRequest();
//        cursorRequest.setSize(5);
//        cursorRequest.setIsFirst(true);
//        cursorRequest.setLastId(0L);
//        플로라 혹시 위에 부분 setter말고 생성자로 생성해주실 수 있나요?? setter를 빼고싶어서요ㅎㅎ 제가 CursorPageReqeust에 생성자 만들어놨습니다~
//        - 젤리 -
//
//        /** following 알림 메시지 **/
//        FollowNotification followNotification = FollowNotification.of(
//                receiver,
//                1L,
//                NotificationType.FOLLOWING
//        );
//        notificationIndexRepository.save(NotificationIndex.of(followNotification.getId(), 1L));
//        followNotificationRepository.save(
//                followNotification
//        );
//
//        /** 농구장 확성기 알림 메시지 **/
//        LoudSpeakerNotification loudSpeakerNotification = LoudSpeakerNotification.of(
//                court,
//                12,
//                1L,
//                NotificationType.LOUDSPEAKER
//        );
//
//        notificationIndexRepository.save(NotificationIndex.of(loudSpeakerNotification.getId(), 1L));
//        loudSpeakerNotificationRepository.save(
//                loudSpeakerNotification
//        );
//
//
//        /** 농구장 확성기 알림 메시지 **/
//        LoudSpeakerNotification loudSpeakerNotification2 = LoudSpeakerNotification.of(
//                court,
//                15,
//                user.getId(),
//                NotificationType.LOUDSPEAKER
//        );
//        notificationIndexRepository.save(NotificationIndex.of(loudSpeakerNotification2.getId(), user.getId()));
//        loudSpeakerNotificationRepository.save(
//                loudSpeakerNotification2
//        );
//
//        //When
//        List<NotificationResponse> notificationResponseList = notificationService.findAllByUserId(
//                user.getId(),
//                cursorRequest
//        );
//
//        //Then
//        assertThat(notificationResponseList.size(), is(3));
//        assertThat(notificationResponseList.get(0).getType(), is(NotificationType.FOLLOWING));
//        assertThat((notificationResponseList.get(0).getFollowerInfo().getUserNickname()), is("receiver"));
//        assertThat(notificationResponseList.get(1).getType(), is(NotificationType.LOUDSPEAKER));
//        assertThat(notificationResponseList.get(1).getLoudspeakerInfo().getCourtInfo().getName(), containsString("잠실"));
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("사용자 구별키를 이용하여, 마지막으로 읽은 공지부터 특정 개수의 정보를 추출할 수 있다.")
//    void findAllByUserIdIsFirstFalse(){
//        //Given
//        CursorPageRequest cursorRequest = new CursorPageRequest();
//        cursorRequest.setSize(5);
//        cursorRequest.setIsFirst(false);
//        cursorRequest.setLastId(2L);
//
//
//        //When
//        List<NotificationResponse> notificationResponseList = notificationService.findAllByUserId(
//                user.getId(),
//                cursorRequest
//        );
//
//        //Then
//        assertThat(notificationResponseList.size(), is(2));
//        assertThat(notificationResponseList.get(0).getType(), is(NotificationType.LOUDSPEAKER));
//        assertThat((notificationResponseList.get(0).getLoudspeakerInfo().getCourtInfo().getName()), containsString("잠실"));
//        assertThat(notificationResponseList.get(1).getType(), is(NotificationType.LOUDSPEAKER));
//        assertThat(notificationResponseList.get(1).getLoudspeakerInfo().getCourtInfo().getName(), containsString("잠실"));
//
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("팔로우 공지를 저장할 수 있다.")
//    void saveForFollowNotification(){
//        //Given
//        Long userId = user.getId();
//        FollowNotificationRequest followNotificationRequest = new FollowNotificationRequest(receiver.getId());
//
//        //When
//        notificationService.saveForFollowNotification(
//                followNotificationRequest,
//                userId
//        );
//
//        //Then
//        List<NotificationIndex> notificationIndexList = notificationIndexRepository.findAll();
//        List<FollowNotification> followNotificationList = followNotificationRepository.findAll();
//        assertThat(notificationIndexList.size(), is(4));
//        assertThat(followNotificationList.size(), is(2));
//        assertThat(followNotificationList.get(1).getReceiver().getNickname(), is("receiver"));
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("확성기 공지를 저장할 수 있다.")
//    void saveForLoudspeakerNotification(){
//        //Given
//        Long userId = user.getId();
//
//        LoudspeakerNotificationRequest loudspeakerNotificationRequest = new LoudspeakerNotificationRequest(
//                court.getId(),
//                13,
//                1L
//                );
//
//        //When
//        notificationService.saveForLoudSpeakerNotification(loudspeakerNotificationRequest, userId);
//
//        //Then
//        List<NotificationIndex> notificationIndexList = notificationIndexRepository.findAll();
//        List<LoudSpeakerNotification> loudSpeakerNotificationList = loudSpeakerNotificationRepository.findAll();
//        assertThat(notificationIndexList.size(), is(5));
//        assertThat(loudSpeakerNotificationList.size(), is(3));
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("읽음 표기 기능을 할 수 있다.")
//    void updateIsClickedStatus(){
//        //Given
//        Long userId = user.getId();
//
//        CursorPageRequest cursorRequest = new CursorPageRequest();
//        cursorRequest.setSize(5);
//        cursorRequest.setIsFirst(true);
//        cursorRequest.setLastId(0L);
//
//
//        //When
//        notificationService.updateIsClickedStatus(
//                new UpdateIsClickedStatusRequest(true),
//                userId
//        );
//
//        assertThat(notificationIndexRepository.findAll().size(), is(5));
//        List<NotificationResponse> notificationResponseList = notificationService.findAllByUserId(userId, cursorRequest);
//        assertThat(notificationResponseList.get(0).isClicked(), is(true));
////        assertThat(notificationResponseList.get(1).isRead(), is(true));
////        assertThat(notificationResponseList.get(2).isRead(), is(true));
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("공지의 마지막 인데스 값을 알 수 있다.")
//    void findLastId(){
//        //Given
//        Long userId = user.getId();
//        CursorPageRequest cursorRequest = new CursorPageRequest();
//        cursorRequest.setSize(5);
//        cursorRequest.setIsFirst(true);
//        cursorRequest.setLastId(0L);
//
//
//        //When
//        Long lastId = notificationService.findNotificationLastId(userId, cursorRequest);
//
//        //Then
//        List<NotificationIndex> notificationIndexList = notificationIndexRepository.findAll();
//        assertThat(lastId, is(notificationIndexList.get(notificationIndexList.size()-1).getId()));
//    }
//
//}