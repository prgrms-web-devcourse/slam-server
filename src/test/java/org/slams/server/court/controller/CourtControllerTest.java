package org.slams.server.court.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.NewCourtRepository;
import org.slams.server.court.repository.UserTempRepository;
import org.slams.server.court.service.CourtService;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.Skill;
import org.slams.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
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


@ActiveProfiles("dev")
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
    private NewCourt newCourt;
    private Reservation reservation;

    @Autowired
    private UserTempRepository userTempRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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

        // user ID + role + 토큰 발행일자 + 만료일자 ==> token
        LocalDateTime now = LocalDateTime.now();
        user = User.builder()
                .nickname("test")
                .email("sds1zzang@naver.com")
                .id(1L)
                .socialId("1L")
                .description("my name is sds")
                .profileImage("desktop Image")
                .role(Role.USER)
                .skill(Skill.BEGINNER)
                .position(Position.PF)
                .build();


        user.setCreatedAt(now);
        user.setUpdateAt(now);

        userTempRepository.save(user);


    }


    @Test
    @DisplayName("[POST] '/api/v1/courts/new'")
    @Order(1)
    void testInsertCall() throws Exception {
        // GIVEN
        CourtInsertRequestDto givenRequest = CourtInsertRequestDto.builder()
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg") // image ->
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .status(Status.READY)
                .build();

        newCourt=NewCourt.builder()
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .status(Status.READY)
                .build();



//        CourtInsertResponseDto response = new CourtInsertResponseDto(newCourt);
//        given(courtService.insert(any(), any())).willReturn(response);


        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/courts/new")
                .header("Authorization",jwtToken)
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
    @Order(2)
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
                .header("Authorization",jwtToken)
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


    @Test
    @DisplayName("[GET] '/api/v1/courts/detail/{courtId}")
    @Order(3)
    void testDetailCourt() throws Exception {

//         시나리오
//         이미 만들어져있는 유저 1, 코트 3개
//         코트 1에 예약 2개하기 (총 예약 3개)
//         코트 아이디 1을 넣어 예약 2개 확인하기

        // GIVEN
        LocalDateTime now = LocalDateTime.now();

        court=courtService.getCourt(1L);
        Court tempCourt=courtService.getCourt(2L);

        reservation = Reservation.builder()
                .user(user)
                .court(court)
                .startTime(now)
                .endTime(now)
                .hasBall(false)
                .build();

        reservation.setCreatedAt(now);
        reservation.setUpdateAt(now);
        reservationRepository.save(reservation);


        reservation = Reservation.builder()
                .user(user)
                .court(tempCourt)
                .startTime(now)
                .endTime(now)
                .hasBall(false)
                .build();

        reservation.setCreatedAt(now);
        reservation.setUpdateAt(now);
        reservationRepository.save(reservation);


        reservation = Reservation.builder()
                .user(user)
                .court(court)
                .startTime(now)
                .endTime(now)
                .hasBall(false)
                .build();

        reservation.setCreatedAt(now);
        reservation.setUpdateAt(now);
        reservationRepository.save(reservation);





        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/courts/detail/"+1)
                .header("Authorization",jwtToken)
                .contentType(MediaType.APPLICATION_JSON); // TODO: 사진 들어오면 multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("AllCourt-select",
                        responseFields(

                fieldWithPath("courtName").type(JsonFieldType.STRING).description("코트 이름"),
                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                fieldWithPath("image").type(JsonFieldType.STRING).description("코트 이미지"),
                fieldWithPath("texture").type(JsonFieldType.STRING).description("코트 재질"),
                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("골대 갯수"),
                fieldWithPath("courtReservation").type(JsonFieldType.NUMBER).description("농구장 준비상태"),
                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }





}
