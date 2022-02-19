package org.slams.server.chat.service;

import org.junit.jupiter.api.*;
import org.slams.server.chat.dto.request.CreateChatContentsRequest;
import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.dto.response.subDto.ChatContentType;
import org.slams.server.chat.entity.ChatContents;
import org.slams.server.chat.entity.CourtChatroomMapping;
import org.slams.server.chat.repository.ChatContentsRepository;
import org.slams.server.chat.repository.ChatLoudSpeakerContentRepository;
import org.slams.server.chat.repository.CourtChatroomMappingRepository;
import org.slams.server.chat.repository.UserChatroomMappingRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
/**
 * Created by yunyun on 2021/12/18.
 */

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatContentsServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ChatContentsService chatContentsService;

    @Autowired
    private ChatContentsRepository chatContentsRepository;

    @Autowired
    private CourtChatroomMappingRepository courtChatroomMappingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChatLoudSpeakerContentRepository chatLoudSpeakerContentRepository;

    private User user = null;
    private Court court = null;
    private Reservation reservation;

    private Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");


    @BeforeAll
    void setUp(){
        userRepository.deleteAll();
        courtRepository.deleteAll();
        chatContentsRepository.deleteAll();
        courtChatroomMappingRepository.deleteAll();

        User userEntity = User.of(
                "socialId-user",
                "user@test.com",
                "user",
                "http://test-image-location-user",
                "소개-user",
                Role.USER,
                Proficiency.INTERMEDIATE,
                List.of(Position.TBD)
        );
        userRepository.save(userEntity);
        user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt")).get(0);

        Court courtEntity = new Court(
                "잠실 농구장",
                1203.20302,
                2038.2939,
                "https://court-image",
                1,
                Texture.CONCRETE
        );
        courtRepository.save(courtEntity);
        court = courtRepository.findAll(sort).get(0);

        reservation = reservationRepository.save(
          Reservation.builder()
                  .startTime(LocalDateTime.of(2021, 12, 18, 10, 00, 00))
                  .endTime(LocalDateTime.of(2021, 12, 18, 12, 00, 00))
                  .user(user)
                  .court(court)
                  .hasBall(true)
                  .build()
        );
        courtChatroomMappingRepository.save(
                CourtChatroomMapping.of(court)
        );
    }

    @Test
    @Order(2)
    void saveChatConversationContent(){
        //Given
        CreateChatContentsRequest request = CreateChatContentsRequest.builder()
                .courtId(court.getId())
                .content("대화 내용 테스트합니다.")
                .build();
        Long userId = user.getId();

        //When
        chatContentsService.saveChatConversationContent(request, userId);

        //Then
        ChatContents chatContents = chatContentsRepository.findAll(sort).get(0);
        assertThat(chatContents.getChatContentType(), is(ChatContentType.CONVERSATION));
        assertThat(chatContents.getChatConversationContent().getContent(), containsString("테스트"));

    }

    @Test
    @Order(1)
    void saveChatLoudSpeakerContent(){
        //Given
        LoudspeakerNotificationRequest request = new LoudspeakerNotificationRequest(
                court.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2L),
                reservation.getId()
                );
        Long userId = user.getId();

        //When
        chatContentsService.saveChatLoudSpeakerContent(request, userId);
        System.out.println(chatLoudSpeakerContentRepository.findAll().size());
        //Then
        ChatContents chatContents = chatContentsRepository.findAll(sort).get(0);
        assertThat(chatContents.getChatLoudSpeakerContent().getStartTime(), is(10));
        assertThat(chatContents.getCourt().getName(), is("잠실 농구장"));
    }

    @Test
    @Order(3)
    void findChatContentsListByCourtOrderByCreatedAt(){
        //Given
        Long courtId = court.getId();
        CursorPageRequest cursorRequest = new CursorPageRequest(5, 0L, Boolean.TRUE);

        //When
        List<ChatContentsResponse> chatContentsResponseList = chatContentsService.findChatContentsListByCourtOrderByCreatedAt(courtId, cursorRequest);

        //Then
        assertThat(chatContentsResponseList.get(0).getCourt().getName(), is("잠실 농구장"));
        assertThat(chatContentsResponseList.size(), is(2));
    }



}