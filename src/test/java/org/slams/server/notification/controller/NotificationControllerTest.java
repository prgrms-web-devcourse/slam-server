package org.slams.server.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.repository.FollowNotificationRepository;
import org.slams.server.notification.repository.LoudSpeakerNotificationRepository;
import org.slams.server.notification.repository.NotificationIndexRepository;
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
    private NotificationIndexRepository notificationIndexRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowNotificationRepository followNotificationRepository;

    @Autowired
    private LoudSpeakerNotificationRepository loudSpeakerNotificationRepository;

    @Autowired
    private UserService userService;


    private String jwtToken;

    @BeforeAll
    void setUp() throws IOException {
        // ???????????? ??????
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();

        // ???????????? ?????? ?????? ??????
        ConfigurableEnvironment env = ctx.getEnvironment();

        // ???????????? ?????? ?????? ??????
        MutablePropertySources prop = env.getPropertySources();

        // ???????????? ?????? ????????? ???????????? ?????? ??????
        prop.addLast(new ResourcePropertySource("classpath:test.properties"));

        // ???????????? ?????? ??????
        String token = env.getProperty("token");
        jwtToken = "Bearer " + token;


        /** ?????? ????????? ?????? **/

//        User receiver = User.of(
//                "receiver1-socialId",
//                "receiver1@test.com",
//                "receiver1",
//                "http://test-image-location-receiver1",
//                "??????-receiver1",
//                Role.USER,
//                Proficiency.INTERMEDIATE,
//                List.of(Position.PG)
//        );
//        User user = User.of(
//                "socialId-user1",
//                "user1@test.com",
//                "user1",
//                "http://test-image-location-user1",
//                "??????-user1",
//                Role.USER,
//                Proficiency.INTERMEDIATE,
//                List.of(Position.TBD)
//        );
//
//        userRepository.save(receiver);
//        userRepository.save(user);
//
//        Court courtEntity = new Court(
//                "?????? ?????????",
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
//        /** following ?????? ????????? **/
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
    @DisplayName("?????? ???????????? ?????? ????????? ????????? ??? ??????.")
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
    @DisplayName("????????? ????????? ???????????? ????????? ????????? ??? ??????.")
    void updateIsClicked() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notification/isClicked")
                        .header("Authorization", jwtToken)
//                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}