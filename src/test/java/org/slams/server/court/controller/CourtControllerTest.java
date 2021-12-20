package org.slams.server.court.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtDetailResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.dto.response.CourtReservationResponseDetailDto;
import org.slams.server.court.dto.response.CourtReservationResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.service.CourtService;
import org.slams.server.court.service.NewCourtService;
import org.slams.server.follow.dto.FollowingResponse;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.dto.response.ReservationUpcomingResponseDto;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
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
    private NewCourtService newCourtService;

    @MockBean
    private CourtService courtService;

    private User user;
    private Court court;
    private NewCourt newCourt;
    private Reservation reservation;




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
        newCourt=NewCourt.builder()
                .id(1L)
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .status(Status.READY)
                .build();

        newCourt.setCreatedAt(now);
        newCourt.setUpdateAt(now);

        court=Court.builder()
                .id(1L)
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);

        reservation=Reservation.builder()
                .id(1L)
                .court(court)
                .hasBall(false)
                .startTime(now.plusHours(1))
                .endTime(now.plusHours(5))
                .user(user)
                .build();

        reservation.setCreatedAt(now);
        reservation.setUpdateAt(now);


    }


    @Test
    @DisplayName("[POST] '/api/v1/courts/new'")
    void testInsertCall() throws Exception {
        // GIVEN
        CourtInsertRequestDto givenRequest = CourtInsertRequestDto.builder()
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("data:image/png;base64,atasdfskdajfklsadjfkl")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .status(Status.READY)
                .build();



        CourtInsertResponseDto stubResponse = new CourtInsertResponseDto(newCourt);
        given(newCourtService.insert(any(), any())).willReturn(stubResponse);

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
                                fieldWithPath("name").type(JsonFieldType.STRING).description("신규 코트 이름"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("코트 이미지"),
                                fieldWithPath("texture").type(JsonFieldType.STRING).description("코트 재질"),
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("골대 갯수"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("신규 농구장 준비상태"),
                                fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("신규 농구장 코트 번호"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("신규 코트 생성일자"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("신규 코트 수정일자"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("신규 코트 상태")
                        )
                ));
    }



    @Test
    @DisplayName("[GET] '/api/v1/courts/detail/{courtId}/{date}/{time}")
    void testDetailCourt() throws Exception {

        // 사용자가 예약한 코트에 대한 정보 알기

        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        court=Court.builder()
                .id(1L)
                .name("관악구민운동장 농구장")
                .latitude(38.987654)
                .longitude(12.309472)
                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .build();

        court.setCreatedAt(now);
        court.setUpdateAt(now);


        CourtDetailResponseDto courtDetailResponseDto=new CourtDetailResponseDto(court, 3L);

        given(courtService.findDetail(any(),any(),any())).willReturn(courtDetailResponseDto);
        String date="2019-04-20";
        String time="dawn";


        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/courts/detail/1/"+date+"/"+time)
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
                                fieldWithPath("courtReservation").type(JsonFieldType.NUMBER).description("농구장 코트 예약한 수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }



    @Test
    @Disabled
    @DisplayName("[GET] '/api/v1/detail/{courdId}/reservations/{date}")
    void testSelectCall() throws Exception {
        // GIVEN

        CourtReservationResponseDto given=new CourtReservationResponseDto(reservation);
        List<CourtReservationResponseDto>courtReservationResponseDtoList=new ArrayList<>();
        courtReservationResponseDtoList.add(given);
        courtReservationResponseDtoList.add(given);

        given(courtService.findCourtReservations(any(),any(),any())).willReturn(courtReservationResponseDtoList);

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/courts/detail/"+1+"/reservations/"+"2021-12-20")
                .header("Authorization",jwtToken)
                .contentType(MediaType.APPLICATION_JSON); // TODO: 사진 들어오면 multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reservationsByDateByCourt-select",
                        responseFields(
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("예약 일자"),
                                fieldWithPath("reservations").type(JsonFieldType.ARRAY).description("data"),
                                fieldWithPath("reservations.[].reservationId").type(JsonFieldType.NUMBER).description("예약 아이디"),
                                fieldWithPath("reservations.[].courtId").type(JsonFieldType.NUMBER).description("예약한 코트 아이디"),
                                fieldWithPath("reservations.[].userId").type(JsonFieldType.NUMBER).description("예약한 유저 아이디"),
                                fieldWithPath("reservations.[].avatarImgSrc").type(JsonFieldType.STRING).description("예약한 유저의 아바타 이미지"),
                                fieldWithPath("reservations.[].courtId").type(JsonFieldType.NUMBER).description("예약한 코트 아이디"),
                                fieldWithPath("reservations.[].startTime").type(JsonFieldType.STRING).description("예약한 코트의 시작시간"),
                                fieldWithPath("reservations.[].endTime").type(JsonFieldType.STRING).description("예약한 코트의 종료시간"),
                                fieldWithPath("reservations.[].hasBall").type(JsonFieldType.BOOLEAN).description("예약한 코트의 공 유무")
                        )
                ));
    }





}
