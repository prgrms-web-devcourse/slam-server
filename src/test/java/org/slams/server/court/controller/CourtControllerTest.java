package org.slams.server.court.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.service.CourtService;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Skill;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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
class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private CourtService courtService;
    private User user;
    private Court court;


    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    public void setUp() {


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

        userRepository.save(user);

//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();

    }

    @Test
    @DisplayName("[POST] '/api/v1/court'")
    void insert() throws Exception{

        // GIVEN
        CourtInsertRequestDto givenRequest = CourtInsertRequestDto.builder()
                .basketCount(10)
                .image("sds")
                .latitude(35.0)
                .longitude(36.0)
                .texture(Texture.ASPHALT)
                .name("서울한강공원")
                .build();
//        CourtInsertResponseDto stubResponse = new CourtInsertResponseDto(court);
//        given(courtService.insert(any(), any())).willReturn(stubResponse);


        Long userId=user.getId();



        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/courts/"+userId+"/new")
                .contentType(MediaType.APPLICATION_JSON) // TODO: 사진 들어오면 multipart/form-data
                .content(objectMapper.writeValueAsString(givenRequest));

        // WHEN // THEN
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andDo(document("post-save",
//                        requestFields(
//                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
//                                fieldWithPath("contents").type(JsonFieldType.STRING).description("contents")
//                        ),
//                        responseFields(
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("id"),
//                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("title"),
//                                fieldWithPath("data.contents").type(JsonFieldType.STRING).description("contents"),
//                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("status"),
//                                fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data.isHidden").type(JsonFieldType.BOOLEAN).description("판매자"),
//                                fieldWithPath("data.sellerName").type(JsonFieldType.STRING).description("판매자"),
//                                fieldWithPath("data.buyerName").type(JsonFieldType.STRING).description("구매자"),
//                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("생성 일자"),
//                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 일자"),
//                                fieldWithPath("data.deletedAt").type(JsonFieldType.NULL).description("삭제 일자"),
//                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("응답시간")
//                        )
//                ));


        // THEN
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("court-insert"
//                        ,requestFields(
//                                fieldWithPath("title").type(JsonFieldType.STRING).description("title"),
//                                fieldWithPath("contents").type(JsonFieldType.STRING).description("contents")
//                        )
//                        ,responseFields(
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("데이터"),
//                                fieldWithPath("data.serverDateTime").type(JsonFieldType.STRING).description("응답시간")
//                        )
                ));




    }



}