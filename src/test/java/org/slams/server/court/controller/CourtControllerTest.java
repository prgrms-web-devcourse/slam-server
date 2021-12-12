package org.slams.server.court.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.UserTempRepository;
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
    @Autowired
    private CourtService courtService;
    private User user;
    private Court court;

    @Autowired
    private UserTempRepository userTempRepository;

    @Autowired
    private CourtRepository courtRepository;



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

        userTempRepository.save(user);


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
                .status(Status.READY)
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
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("골대 갯수"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("농구장 준비상태")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("코트 이름"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("코트 이미지"),
                                fieldWithPath("texture").type(JsonFieldType.STRING).description("코트 재질"),
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("골대 갯수"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("농구장 준비상태"),
                                fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("농구장 코트 번호"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }


    @Test
    @DisplayName("[GET] '/api/v1/courts/all")
    void testAllCourt() throws Exception {
        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        court = Court.builder()
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);
        courtRepository.save(court);


        court = Court.builder()
                .name("서울시 농구장")
                .latitude(209.987654)
                .longitude(1123.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(3)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);

        courtRepository.save(court);

        court = Court.builder()
                .name("한강 농구장")
                .latitude(7777.987654)
                .longitude(888.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.RUBBER)
                .basketCount(4)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);

        courtRepository.save(court);




        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/courts//all")
                .contentType(MediaType.APPLICATION_JSON); // TODO: 사진 들어오면 multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("AllCourt-select",
                        responseFields(
                                fieldWithPath("courts").type(JsonFieldType.ARRAY).description("data"),
                                fieldWithPath("courts.[].name").type(JsonFieldType.STRING).description("코트 이름"),
                                fieldWithPath("courts.[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("courts.[].longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("courts.[].image").type(JsonFieldType.STRING).description("코트 이미지"),
                                fieldWithPath("courts.[].texture").type(JsonFieldType.STRING).description("코트 재질"),
                                fieldWithPath("courts.[].basketCount").type(JsonFieldType.NUMBER).description("골대 갯수"),
                                fieldWithPath("courts.[].createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("courts.[].updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }





}
