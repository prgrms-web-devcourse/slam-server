package org.slams.server.court.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.service.CourtService;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Skill;
import org.slams.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
public class CourtControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourtService courtService;
    private User user;
    private Court court;



    @BeforeEach
    void setUp() {

        LocalDateTime now = LocalDateTime.now();
        user = User.builder()
                .nickname("test")
                .email("sds1zzang@naver.com")
                .id(1L)
                .description("my name is sds")
                .profileImage("desktop Image")
                .role("user")
                .skill(Skill.BEGINNER)
                .position(Position.forward)
                .build();


        user.setCreatedAt(now);
        user.setUpdateAt(now);



    }


    @Test
    @DisplayName("[POST] '/api/v1/courts/{id}/new'")
    void testInsertCall() throws Exception {
        // GIVEN
        CourtInsertRequestDto givenRequest = CourtInsertRequestDto.builder()
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .build();


        log.info(givenRequest.toString());

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/courts/"+user.getId()+"/new")
                .contentType(MediaType.APPLICATION_JSON) // TODO: 사진 들어오면 multipart/form-data
                .content(objectMapper.writeValueAsString(givenRequest));

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("user-court-save",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("코트 이름"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("코트 이미지"),
                                fieldWithPath("texture").type(JsonFieldType.STRING).description("코트 재질"),
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("골대 갯수")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답시간"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("응답시간"),
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부")
                        )
                ));
    }





}
