package org.slams.server.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by yunyun on 2021/12/19.
 */

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


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



    }

    @Test
    void findUserChatRoomByUserId() throws Exception {
        //Given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("size", "5");
        params.add("isFirst", "true");
        params.add("lastId", "0");

        //When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/chat/{courtId}", 1)
                        .header("Authorization", jwtToken)
                        .params(params)
//                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @Test
    void findChatContentByChatRoomId() {
    }

    @Test
    void createUserChatRoom() {
    }

    @Test
    void deleteUserChatRoom() {
    }
}