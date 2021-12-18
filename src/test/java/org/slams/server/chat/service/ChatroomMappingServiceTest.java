package org.slams.server.chat.service;

import org.junit.jupiter.api.*;
import org.slams.server.chat.dto.request.CreateChatRoomRequest;
import org.slams.server.chat.dto.response.ChatroomResponse;
import org.slams.server.chat.entity.CourtChatroomMapping;
import org.slams.server.chat.entity.UserChatroomMapping;
import org.slams.server.chat.repository.CourtChatroomMappingRepository;
import org.slams.server.chat.repository.UserChatroomMappingRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by yunyun on 2021/12/18.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatroomMappingServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ChatroomMappingService chatroomMappingService;

    @Autowired
    private CourtChatroomMappingRepository courtChatroomMappingRepository;

    @Autowired
    private UserChatroomMappingRepository userChatroomMappingRepository;

    private User user = null;
    private Court court = null;
    private Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

    @BeforeAll
    void setUp(){
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
        userRepository.save(user);
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
        court = courtRepository.findAll().get(0);
    }

    @Test
    void saveChatRoom(){
        //Given
        Long courtId = court.getId();

        //When
        chatroomMappingService.saveChatRoom(courtId);

        //Then
        CourtChatroomMapping courtChatroomMapping = courtChatroomMappingRepository.findAll(sort).get(0);
        assertThat(courtChatroomMapping.getCourt().getName(), is("잠실 농구장"));
    }

    @Test
    void saveChatRoomForEachUser(){
        //Given
        Long userId = user.getId();
        CreateChatRoomRequest chatRoomRequest = CreateChatRoomRequest.builder()
                .courtId(court.getId())
                .build();

        //When
        chatroomMappingService.saveChatRoomForEachUser(userId, chatRoomRequest);

        //Then
        UserChatroomMapping userChatroomMapping = userChatroomMappingRepository.findAll(sort).get(0);
        assertThat(userChatroomMapping.getCourtChatroomMapping().getCourt().getName()
        ,is("잠실 농구장"));
        assertThat(userChatroomMapping.getUser().getNickname(), is("user"));
    }

    @Test
    void findChatRoomByCourt(){
        //Given
        Long userId = user.getId();

        CursorPageRequest cursorRequest = new CursorPageRequest();
        cursorRequest.setSize(5);
        cursorRequest.setIsFirst(true);
        cursorRequest.setLastId(0L);

        //When
        List<ChatroomResponse> chatroomResponseList = chatroomMappingService.findChatRoomByCourt(userId, cursorRequest);

        //Then
        assertThat(chatroomResponseList.get(0).getCourtName(), is("잠실 농구장"));
    }

    @Test
    void deleteEnteredChatRoomByChatRoomId(){
        //Given
        Long userChatRoomId = userChatroomMappingRepository.findAll(sort).get(0).getId();

        //When
        chatroomMappingService.deleteEnteredChatRoomByChatRoomId(userChatRoomId);

        //Then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userChatroomMappingRepository.findById(userChatRoomId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 정보 존재하지 않음"));
        });
    }
}