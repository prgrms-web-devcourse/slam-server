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




    // JWT ?????? ??????
    private String jwtToken;



    @BeforeEach
    void setUp() throws Exception{

        // JWT ?????? ??????
        // ???????????? ??????
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();

        // ???????????? ?????? ?????? ??????
        ConfigurableEnvironment env = ctx.getEnvironment();

        // ???????????? ?????? ?????? ??????
        MutablePropertySources prop = env.getPropertySources();

        // ???????????? ?????? ????????? ???????????? ?????? ??????
        prop.addLast(new ResourcePropertySource("classpath:test.properties"));

        // ???????????? ?????? ??????
        String token = env.getProperty("token");
        jwtToken = "Bearer "+token;

        // user ID + role + ?????? ???????????? + ???????????? ==> token
        LocalDateTime now = LocalDateTime.now();
        // User ??????
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



        // Court ??????
        newCourt=NewCourt.builder()
                .id(1L)
                .name("????????????????????? ?????????")
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
                .name("????????????????????? ?????????")
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
                .name("????????????????????? ?????????")
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
                .contentType(MediaType.APPLICATION_JSON) // TODO: ?????? ???????????? multipart/form-data
                .content(objectMapper.writeValueAsString(givenRequest));

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("user-court-save",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("texture").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("texture").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ????????? ????????????"),
                                fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ?????? ????????????"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("?????? ?????? ????????????"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                        )
                ));
    }



    @Test
    @DisplayName("[GET] '/api/v1/courts/detail/{courtId}/{date}/{time}")
    void testDetailCourt() throws Exception {

        // ???????????? ????????? ????????? ?????? ?????? ??????

        // GIVEN
        LocalDateTime now = LocalDateTime.now();
        court=Court.builder()
                .id(1L)
                .name("????????????????????? ?????????")
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
                .contentType(MediaType.APPLICATION_JSON); // TODO: ?????? ???????????? multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("AllCourt-select",
                        responseFields(
                                fieldWithPath("courtName").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("image").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("texture").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("courtReservation").type(JsonFieldType.NUMBER).description("????????? ?????? ????????? ???"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("?????? ????????????"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("?????? ????????????")
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
                .contentType(MediaType.APPLICATION_JSON); // TODO: ?????? ???????????? multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reservationsByDateByCourt-select",
                        responseFields(
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("date").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("reservations").type(JsonFieldType.ARRAY).description("data"),
                                fieldWithPath("reservations.[].reservationId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                fieldWithPath("reservations.[].courtId").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                                fieldWithPath("reservations.[].userId").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                                fieldWithPath("reservations.[].avatarImgSrc").type(JsonFieldType.STRING).description("????????? ????????? ????????? ?????????"),
                                fieldWithPath("reservations.[].courtId").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                                fieldWithPath("reservations.[].startTime").type(JsonFieldType.STRING).description("????????? ????????? ????????????"),
                                fieldWithPath("reservations.[].endTime").type(JsonFieldType.STRING).description("????????? ????????? ????????????"),
                                fieldWithPath("reservations.[].hasBall").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??? ??????")
                        )
                ));
    }





}
