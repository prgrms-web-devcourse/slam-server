//package org.slams.server.court.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.slams.server.user.entity.Position;
//import org.slams.server.user.entity.Skill;
//import org.slams.server.user.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureRestDocs
//class CourtServiceTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//
//    private User user;
//    LocalDateTime now = LocalDateTime.now();
//
//    @BeforeEach
//    public void setUp() {
//
//
//        user = User.builder()
//                .nickname("test")
//                .email("sds1zzang@naver.com")
//                .id(1L)
//                .description("my name is sds")
//                .profileImage("desktop Image")
//                .role("user")
//                .skill(Skill.BEGINNER)
//                .position(Position.forward)
//                .build();
//
//        user.setCreatedAt(now);
//        user.setUpdateAt(now);
//
//        userRepository.save(user);
//
////        this.mockMvc = MockMvcBuilders
////                .webAppContextSetup(context)
////                .apply(springSecurity())
////                .build();
//
//    }
//
//    @Test
//    void insert() {
//    }
//
//    @Test
//    void getUser() {
//    }
//
//
//
//}