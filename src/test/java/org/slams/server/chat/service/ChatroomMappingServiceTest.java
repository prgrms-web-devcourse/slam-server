package org.slams.server.chat.service;

import org.junit.jupiter.api.*;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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


    private User user = null;
    private Court court = null;

    @BeforeAll
    void setUp(){
        user = User.of(
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

        //Then

    }

}