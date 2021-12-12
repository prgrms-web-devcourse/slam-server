package org.slams.server.reservation.controller;

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
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.service.ReservationService;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Skill;
import org.slams.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class ReservationControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReservationService reservationService;

    private User user;

    private Court court;

    @Autowired
    private UserTempRepository userTempRepository;

    @Autowired
    private CourtService courtService;

    @Autowired
    private CourtRepository courtRepository;




    @BeforeEach
    void setUp() {


        // User 생성
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




        // Court 생성
        court=Court.builder()
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

        court=Court.builder()
                .name("광진구 농구장")
                .latitude(45.987654)
                .longitude(13.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(4)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);
        courtRepository.save(court);


    }


    // 생성한 코트에 예약 해보기
    @Test
    @DisplayName("[POST] '/api/v1/reservations'")
    void testInsertCall() throws Exception {
        // GIVEN
        ReservationInsertRequestDto givenRequest = ReservationInsertRequestDto.builder()
                .courtId(1L)
                .startTime("2021-01-01T12:20:10")
                .endTime("2021-01-01T12:20:10")
                .hasBall(false)
                .build();


        log.info(givenRequest.toString());

        log.info("user_ID:"+user.getId());


//        ReservationInsertResponseDto stubResponse = new ReservationInsertResponseDto();
//        given(reservationService.insert(any(), any()));

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/reservations/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON) // TODO: 사진 들어오면 multipart/form-data
                .content(objectMapper.writeValueAsString(givenRequest));

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("reservation-save",
                        requestFields(
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("코트 시작시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("농구 종료시간"),
                                fieldWithPath("hasBall").type(JsonFieldType.BOOLEAN).description("농구공 여부")
                        ),
                        responseFields(
                                fieldWithPath("reservationId").type(JsonFieldType.NUMBER).description("예약 아이디"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("코트 시작시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("농구 종료시간"),
                                fieldWithPath("hasBall").type(JsonFieldType.BOOLEAN).description("농구공 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }


}
