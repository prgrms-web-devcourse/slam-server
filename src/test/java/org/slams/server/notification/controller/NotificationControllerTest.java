package org.slams.server.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.repository.LoudspeakerRepository;
import org.slams.server.notification.repository.NotificationRepository;
import org.slams.server.notification.service.NotificationService;
import org.slams.server.user.repository.UserRepository;
import org.slams.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by yunyun on 2021/12/16.
 */

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private NotificationRepository notificationIndexRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoudspeakerRepository loudSpeakerNotificationRepository;

    @Autowired
    private UserService userService;


    private String jwtToken;

    @BeforeAll
    void setUp() throws IOException {
        // 컨테이너 생성
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();

        // 환경변수 관리 객체 생성
        ConfigurableEnvironment env = ctx.getEnvironment();

        // 프로퍼티 관리 객체 생성
        MutablePropertySources prop = env.getPropertySources();

        // 프로퍼티 관리 객체에 프로퍼티 파일 추가
        prop.addLast(new ResourcePropertySource("classpath:test.properties"));

        // 프로퍼티 정보 얻기
        String token = env.getProperty("token");
        jwtToken = "Bearer " + token;


        /** 더미 데이터 준비 **/

//        User receiver = User.of(
//                "receiver1-socialId",
//                "receiver1@test.com",
//                "receiver1",
//                "http://test-image-location-receiver1",
//                "소개-receiver1",
//                Role.USER,
//                Proficiency.INTERMEDIATE,
//                List.of(Position.PG)
//        );
//        User user = User.of(
//                "socialId-user1",
//                "user1@test.com",
//                "user1",
//                "http://test-image-location-user1",
//                "소개-user1",
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
//        Court court = courtRepository.findAll().get(0);
//
//        CursorPageRequest cursorRequest = new CursorPageRequest();
//        cursorRequest.setSize(5);
//        cursorRequest.setIsFirst(true);
//        cursorRequest.setLastId(0L);
//
//
//        /** following 알림 메시지 **/
//        FollowNotification followNotification = FollowNotification.of(
//                receiver,
//                user.getId(),
//                NotificationType.FOLLOWING
//        );
//        notificationIndexRepository.save(NotificationIndex.of(followNotification.getId(), user.getId()));
//        followNotificationRepository.save(
//                followNotification
//        );


    }

    @Test
    void test(){

    }

    @Test
    @DisplayName("해당 사용자의 공지 정보를 전송할 수 있다.")
    void findByUserId() throws Exception {
        //Given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", "5");
        params.add("isFirst", "true");
        params.add("lastId", "0");

        //When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notification")
                        .header("Authorization", jwtToken)
                        .params(params)
//                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

    }

    @Test
    @DisplayName("공지를 확인한 데이터를 읽으로 표시할 수 있다.")
    void updateIsClicked() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notification/isClicked")
                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}