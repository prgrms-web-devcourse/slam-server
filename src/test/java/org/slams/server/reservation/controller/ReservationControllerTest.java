package org.slams.server.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.service.CourtService;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.request.ReservationUpdateRequestDto;
import org.slams.server.reservation.dto.response.ReservationDeleteResponseDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.dto.response.ReservationUpcomingResponseDto;
import org.slams.server.reservation.dto.response.ReservationUpdateResponseDto;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.reservation.service.ReservationService;
import org.slams.server.user.dto.response.ProfileImageResponse;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
public class ReservationControllerTest {


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;

    private User user;
    private Court court;
    private Reservation reservation;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtService courtService;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    LocalDateTime now = LocalDateTime.now();

    // JWT 추가 코드
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception{


        // JWT 추가 코드
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
        jwtToken = "Bearer "+token;


        // User 생성
        user = User.builder()
                .nickname("test")
                .email("sds1zzang@naver.com")
                .id(1L)
                .socialId("1L")
                .description("my name is sds")
                .profileImage("desktop Image")
                .role(Role.USER)
                .proficiency(Proficiency.INTERMEDIATE.BEGINNER)
                .positions(Arrays.asList(Position.PF))
                .build();

        user.setCreatedAt(now);
        user.setUpdateAt(now);



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

        court=Court.builder()
                .id(127L)
                .name("광진구 농구장")
                .latitude(45.987654)
                .longitude(13.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(4)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);

        reservation=Reservation.builder()
                .id(1L)
                .court(court)
                .user(user)
                .hasBall(false)
                .startTime(now)
                .endTime(now)
                .build();

        reservation.setCreatedAt(now);
        reservation.setUpdateAt(now);

    }


    // 생성한 코트에 예약 해보기
    @Test
    @DisplayName("[POST] '/api/v1/reservations'")
    void testInsertCall() throws Exception {
        // GIVEN

        LocalDateTime start=now.plusDays(1);
        LocalDateTime end=now.plusDays(1);

        ReservationInsertRequestDto givenRequest = ReservationInsertRequestDto.builder()
                .courtId(1L)
                .startTime(start)
                .endTime(end)
                .hasBall(false)
                .build();



        ReservationInsertResponseDto stubResponse = new ReservationInsertResponseDto(reservation);
        given(reservationService.insert(any(), any())).willReturn(stubResponse);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/reservations")
                .header("Authorization",jwtToken)
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


    //  변경하기
    @Test
    @DisplayName("[PATCH] '/api/v1/reservations/{reservationId}'")
    void testUpdateCall() throws Exception {
        // 위에서 만든 예약을 변경하기
        // 예약정보 조회
        // GIVEN
        LocalDateTime start=now.plusDays(5);
        LocalDateTime end=now.plusDays(5);
        ReservationUpdateRequestDto givenRequest = ReservationUpdateRequestDto.builder()
                .reservationId(1L)
                .startTime(start)
                .endTime(end)
                .hasBall(false)
                .build();

        reservation.update(givenRequest);

        ReservationUpdateResponseDto stubResponse = new ReservationUpdateResponseDto(reservation);
        given(reservationService.update(any(), any(), any())).willReturn(stubResponse);


        RequestBuilder request = MockMvcRequestBuilders.patch("/api/v1/reservations/"+1L)
                .header("Authorization",jwtToken)
                .contentType(MediaType.APPLICATION_JSON) // TODO: 사진 들어오면 multipart/formdata
                .content(objectMapper.writeValueAsString(givenRequest));

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("reservation-update",
                        requestFields(
                                fieldWithPath("reservationId").type(JsonFieldType.NUMBER).description("예약 아이디"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("코트 시작시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("농구 종료시간"),
                                fieldWithPath("hasBall").type(JsonFieldType.BOOLEAN).description("농구공 여부")
                        ),
                        responseFields(
                                fieldWithPath("reservationId").type(JsonFieldType.NUMBER).description("예약 아이디"),
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("코트 시작시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("농구 종료시간"),
                                fieldWithPath("hasBall").type(JsonFieldType.BOOLEAN).description("농구공 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }

    // 삭제하기
    @Test
    @DisplayName("[DELETE] '/api/v1/reservations/{reservationId}'")
    void testDelete() throws Exception {

        ReservationDeleteResponseDto response = new ReservationDeleteResponseDto(reservation);
        given(reservationService.delete(any(),anyLong())).willReturn(response);

        mockMvc.perform(delete("/api/v1/reservations/{reservationId}", reservation.getId())
                        .header("Authorization",jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andDo(
                        document("reservation/delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("reservationId").description("삭제 요청 reservation 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("reservationId").description("삭제된 reservation 아이디")
                                )
                        )
                );
    }


    // 코트 1개당 3번 예약하기
    // 4,5,6번 코트 사용
    @Test
    @DisplayName("실제 데이터 쿼리 [POST] '/api/v1/reservations'")
    @Disabled
    void InsertReservation() throws Exception {
        // GIVEN

        // User 생성
        user = User.builder()
                .nickname("test")
                .email("sds1zzang@naver.com")
                .id(1L)
                .socialId("1L")
                .description("my name is sds")
                .profileImage("desktop Image")
                .role(Role.USER)
                .proficiency(Proficiency.INTERMEDIATE.BEGINNER)
                .positions(Arrays.asList(Position.PF))
                .build();

        user.setCreatedAt(now);
        user.setUpdateAt(now);
        userRepository.save(user);

        LocalDateTime start=now.plusHours(1);
        LocalDateTime end=now.plusMonths(1);

        Court court1 = courtRepository.getById(3L);
        Court court2 = courtRepository.getById(4L);
        Court court3 = courtRepository.getById(5L);

        Reservation reservation1=Reservation.builder()
                .court(court1)
                .startTime(start)
                .endTime(now.plusHours(1))
                .hasBall(false)
                .user(user)
                .build();

        reservationRepository.save(reservation1);

        Reservation reservation2=Reservation.builder()
                .court(court1)
                .startTime(start)
                .endTime(now.plusHours(1))
                .hasBall(false)
                .user(user)
                .build();


        reservationRepository.save(reservation2);

        Reservation reservation3=Reservation.builder()
                .court(court1)
                .startTime(start)
                .endTime(now.plusHours(1))
                .hasBall(false)
                .user(user)
                .build();

        reservationRepository.save(reservation3);


        Reservation reservation4=Reservation.builder()
                .court(court2)
                .startTime(start)
                .endTime(now.plusHours(2))
                .hasBall(false)
                .user(user)
                .build();

        reservationRepository.save(reservation4);

        Reservation reservation5=Reservation.builder()
                .court(court2)
                .startTime(start)
                .endTime(now.plusHours(2))
                .hasBall(false)
                .user(user)
                .build();


        reservationRepository.save(reservation5);


        Reservation reservation6=Reservation.builder()
                .court(court3)
                .startTime(start)
                .endTime(now.plusHours(3))
                .hasBall(false)
                .user(user)
                .build();

        reservationRepository.save(reservation6);

    }


    // 다가올 예약 목록 조회
    // /api/v1/reservations/upcoming
    @Test
    @Order(2)
    @DisplayName("[GET] '/api/v1/reservations/upcoming")
    @Transactional
    void testSelectCall() throws Exception {
        LocalDateTime start=now.plusHours(1);
        LocalDateTime end=now.plusMonths(1);
        // GIVEN
        List<ReservationUpcomingResponseDto> stubResponses = new ArrayList<>();
        ReservationUpcomingResponseDto reservationUpcomingResponseDto=new ReservationUpcomingResponseDto(
                reservation,3L);

        ReservationUpcomingResponseDto reservationUpcomingResponseDto2=new ReservationUpcomingResponseDto(
                reservation,3L);

        stubResponses.add(reservationUpcomingResponseDto);


        given(reservationService.findUpcoming(any())).willReturn(stubResponses);



        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/reservations/upcoming")
                .header("Authorization",jwtToken)
                .contentType(MediaType.APPLICATION_JSON); // TODO: 사진 들어오면 multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reservationsByUserByNow-select",
                        responseFields(
                                fieldWithPath("reservations").type(JsonFieldType.ARRAY).description("data"),
                                fieldWithPath("reservations.[].reservationId").type(JsonFieldType.NUMBER).description("예약 아이디"),
                                fieldWithPath("reservations.[].courtId").type(JsonFieldType.NUMBER).description("예약한 코트 아이디"),
                                fieldWithPath("reservations.[].courtName").type(JsonFieldType.STRING).description("예약한 코트 이름"),
                                fieldWithPath("reservations.[].latitude").type(JsonFieldType.NUMBER).description("예약한 코트 위도"),
                                fieldWithPath("reservations.[].longitude").type(JsonFieldType.NUMBER).description("예약한 코트 경도"),
                                fieldWithPath("reservations.[].basketCount").type(JsonFieldType.NUMBER).description("예약한 코트 골대개수"),
                                fieldWithPath("reservations.[].numberOfReservations").type(JsonFieldType.NUMBER).description("예약한 코트 수"),
                                fieldWithPath("reservations.[].startTime").type(JsonFieldType.STRING).description("예약한 코트의 시작시간"),
                                fieldWithPath("reservations.[].endTime").type(JsonFieldType.STRING).description("예약한 코트의 종료시간"),
                                fieldWithPath("reservations.[].createdAt").type(JsonFieldType.STRING).description("예약 생성일자"),
                                fieldWithPath("reservations.[].updatedAt").type(JsonFieldType.STRING).description("예약 수정일자")

                        )
                ));
    }







}
