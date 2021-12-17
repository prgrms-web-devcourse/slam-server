package org.slams.server.court.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.service.CourtService;
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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
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
    private NewCourt newCourt;
    private Reservation reservation;

    @Autowired
    private UserRepository userRepository;

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
                .proficiency(Proficiency.BEGINNER)
                .positions(Arrays.asList(Position.PF))
                .build();


        user.setCreatedAt(now);
        user.setUpdateAt(now);

        userRepository.save(user);


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
                .image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAArIAAAEOCAYAAAB1mfQTAAAAAXNSR0IArs4c6QACtMd0RVh0bXhmaWxlACUzQ214ZmlsZSUyMGhvc3QlM0QlMjJhcHAuZGlhZ3JhbXMubmV0JTIyJTIwbW9kaWZpZWQlM0QlMjIyMDIxLTEwLTE3VDIzJTNBMjMlM0EzNi4zMDFaJTIyJTIwYWdlbnQlM0QlMjI1LjAlMjAoV2luZG93cyUyME5UJTIwMTAuMCUzQiUyMFdpbjY0JTNCJTIweDY0KSUyMEFwcGxlV2ViS2l0JTJGNTM3LjM2JTIwKEtIVE1MJTJDJTIwbGlrZSUyMEdlY2tvKSUyMENocm9tZSUyRjk0LjAuNDYwNi44MSUyMFNhZmFyaSUyRjUzNy4zNiUyMiUyMGV0YWclM0QlMjJvRUg1c0FjdEZSdjFxcGZub2R1UCUyMiUyMHZlcnNpb24lM0QlMjIxNS41LjQlMjIlMjB0eXBlJTNEJTIyZ29vZ2xlJTIyJTNFJTNDZGlhZ3JhbSUyMGlkJTNEJTIyRGV5dmg3UG5jZ1R3djJIcXhyc0UlMjIlMjBuYW1lJTNEJTIyUGFnZS0xJTIyJTNFN0x6WGx1UklraVQ2TmYzWWRjREpJemh4TUFmZ1lDOTd3RGx4Y09EckZ4WloxZDIxTmJOMzdwNlpTemNpTXdOaERoaGdxbXFpSW1xRyUyRkJ2SzlhYzB4MU9sajFuZSUyRlEyQnN2TnZLUDgzNVBuQzZPY0hhTGwlMkJ0WkEwOGF1aG5PdnNWeFA4endhbnZ2UGZHNkhmVzdjNnk1YyUyRm5iaU9ZN2ZXMDU4YjAzRVk4blQ5VTFzOHolMkJQeDU5T0tzZnZ6WGFlNHpQJTJGUzRLUng5OWRXdjg3VzZsY3JoWkQlMkZiSmZ6dXF6JTJCdUROTSUyRkQ3Z1B2N2o1TjlIc2xSeE5oNyUyRjBvUUtmME81ZVJ6WFgwZjl5ZVVkTU40ZmR2bDFuZmp2ZlBxUEI1dnpZZjJQWE9CY1U1RXB4SDhyNXZYV0NVaVFYQ1Q2TyUyRktybHozdXR0OEh6STd4blAzJTJCeE92MWh4bWVoNSUyRkE0ZFozVExxTzg5OVFkcyUyRm50WDRNcGNWSjNsbmpVcSUyRjFPRHluSk9PNmp2MiUyRm5NQjBkUWslMkJXTWZwYWEzV3ZudCUyQmdaJTJGRGNWdTdlc2k1ZjdnT2VociUyRk9xemZSd3E2eTg5JTJGYWZwOW1GSSUyQjl2azZYODhwdjMlMkY2ZCUyRmlQNlBrOTZQQSUyRm5ITDgwNFhvNzZkVSUyRiUyQkk5NHZlMiUyQlBlZ0tmJTJGUjlUJTJGdCUyQmh6OGJ0ciUyRkUyWkdzYiUyRlltZkdkcDhGQiUyRjExVDElMkYxUGFQNGYyN2tESDdCeDJwYnp1QTBaTjNiQVAwOVhhUEh6OWUlMkI2SWw2bVgzWXY2ak4lMkZIcCUyRjl1U1h6Unl2MFI4dHpYSzBybUhBTU1BTWklMkZyUXV2JTJCMTVONWElMkYxZU0lMkZXcDZEUE1WUllLeHBYTmJuQjRWQ09ZbFMlMkJkJTJCUnBDRCUyRmpzRUk5bmNLUSUyQkMlMkZFekJHWmpHTW94bWFQcWZGeCUyRkxmRnZTM1pTJTJGJTJGY3lLQXBQNEglMkY2TiUyRjhUOU8lMkY0WmpmdzBCRXZtdkNnSGkzd2tCcmh1M3JKakhaN0QlMkZMd3lGNHpoJTJCbTRieUNZUnglMkJTMTlIdUpmWW1GY3EzeiUyQjVkMiUyRnAlMkY4WTVYJTJCZWwlMkYlMkJPJTJGbyUyRlRIUG1MbXduNE40ejZOOXlNJTJGVmU1R2Y2TG13VU8lMkJhdG5qN3J2NGdFNGRWbmplZjA5OFNIb2Y0NWxrTDhBSVBRWHk4QTAlMkZsZTd3TlIlMkZGUVQlMkJRUVQlMkJ4UWg1OXFUYTMzOGQ1N1Y2WW1pSU8lMkJHZnJleFBGSU40JTJGSW5BZjU2ampTQnFmNUpJazYlMkZyOWJ2NTRtMGQlMkY1eGklMkZsMWJnbnYlMkZUeTA1NTEyODF2dWZhY0MlMkZaWlhmTDdYR0dzemdQenlBNGslMkZjUWYlMkY4UXY3c0Q1cjZqU0wlMkIzT2N5Ym5PYSUyRjk3TnYlMkJieCUyRjZIblAwTDNIMTNSZiUyQjduQ2FjeVglMkYlMkZTejQlMkZUJTJGakclMkIlMkYzVSUyRjR0RCUyRnIlMkZ6NGR4TDlxJTJCY2VtJTJGJTJGajYzJTJGTmlROGolMkJBMGolMkY5a0xTZiUyRmY2bFR5cjhucDMwMUc2ZldRdGl5ZkFWUWRWYjNtemhTbjRJUGpVUUIlMkY5bHJ5eSUyQjFhOG8lMkJHZjZRbTh4ZjMlMkI3MTklMkJlVjNHUHFOSkVucUlkRVVBZE1rZ2Y5UFhmOGZoME1NJTJCek1jRXVpJTJGQVlmUXYwRUk2ZjhxTkNUSiUyRjdqQiUyRnglMkJWJTJGYk40alolMkZrJTJGJTJCdlhoJTJCWU41ZDhRcnZaWTB6NmdsMVNPelBObE9KOUslMkJKVFBrWmclMkIlMkY3QSUyQng0VFBUNDdvbUZsOURqNmEwQWx2ejhhWUhNNDR6JTJGNTQlMkJQUTNoR1h0cnh2ejhSM2JhaE5PY1RSODdTYWpWWFdDMEpySTZzejl4aTR0TzcxNjY3UkttJTJCclZhYUk0NVVvYXRuTVN2MVN1c3IzVHcwcWw1c1pTYWNteVhCTmVzQVdVWSUyQnF5dnVEbnhwJTJCUHRhQTRTWHVWYzNxeiUyQkJGeHViSHl6SGxZeWhObkxFRmVjUHFMMXp4JTJGOXVCcHVwdjAxZUo4eWE3cVRlUTdxdk5CaFgwUG80R3M3UmgzREN0ZWVWWHI1eVElMkJvY0ltSnBIQlRabHRNJTJGaU5jd25CdVBGdzREaE81cmhVNXZPd3ZROU9UNWIxUFlTOUtsbnhjeCUyQnRIMDd2ZmNDRHZ1aU1jZFVIYzklMkZwYXhEVGtLekNaM1RaTXdBem9NN1hhWTdZZnJ5dHAydGh1UEs4bk9BN242dmJYSWYwZVd6MmZlVG9tOWhsREdxcjQlMkJsWVppbFolMkZyekZSTzBIN2ZtOGVrdXVhU1kzRHZ1NFRta01ROXJ5QzFsdVRHTm5XNlZJV3RPWm1LaGZFaSUyRmhmSDJPRnF1VVVjcDMzVE83eEslMkJlQVglMkIlMkZlSmQ0bVJJajlVNFRLYWU5TWVXQXBaJTJGQ2hyJTJGRCUyQmd4YUpJZUFGOXZVV0NDenVTaUhVVTJCR2FiYWU1RUlhMzlnVTdNT2V5anpjR2MxeCUyRjZLbTlSbDclMkJGdzA0YkRPalRpaEFPdk00OTBhMnMlMkJoaUNOcGUlMkJOVURTMk4yeWJTdjFaNEJuRlVkak8xalMxdkEzaSUyRmN0VnpydmdLMlc5ZTlWMTcwaDZXb2lQb3owVWxmM1lpT3dUJTJCWmhVbXFETTZMc3hUeVhSR1JINWV0ZzVXUEtnRiUyQkpjaWZBNWwlMkI1OGVvSWlHRVRRMHVic3d2aW5tdFNNak5lUzJwTlBJenk5QmxqdGl0ZkI1NWkyamhLYThpdm1HcTkzYVpsJTJCcWVkQnFLcm95MHhrZVpGeGFmVFhlWWs4Tk9HWnlmWTNoMGRabFlsUTdSS3VlbG5rMWElMkJ6STZ5SXp2NTZTeCUyQmwzeDRXa3FGZDN5UG1QeG1lUmRZY2xVanhoaGcxWjZoYUg3dWVPNVNEbkNIclZjSVlubG44dWFiTmg3QnliMFdlV1NpYTZybWpJRGE2YTByTzhYTllZVHpFbTYlMkZjUEt5eUpHVW9MJTJGYUMlMkJQNFJIU0xQOWVVN3RlVENRM3U0MlFZeXhJelhONjZtUkMlMkZNTzVZZlZHT2hvc29nRGg4ekpiUTFNaVlXMjUlMkJobG16azV2bnd0SVBRUEk3M3J0aGhGNDBVR2Z1R2dMRXZ4N1R6V2M4RXN1R1IwdnZHdWRSSUdGd1N5a00yUlh6d3VTVGRQMEZwS1VGSnBkWkpvTDdxNmo2eVZzTThYTHJVblFsMlV3ZkgxZ1JibU5GM3lDRDRndWM0VGI0MSUyRncyOGRGNVhVR0VTUXk5Z1ZPak01S3lUVCUyRm10MktxZWFYbVRKaTFOWlV4TnJLVGxHVTdPVWlFWXJROFRIU0xDc3cwcGppeENYbG4yVkd1MSUyQjZ0TXUzWlp0d2Q1V2V6enJkNWpVVnR4d2hTaHdxWjB3b0twbHAxSCUyQm1ISVolMkJyYjg4Q1hKa3E5c3RobFh3cVdZMW0xcVRNZkpFSGJaV2RKQnMxUHhGVUJMWmNUYzg3UnRUb3VJdFZ2JTJGWUtzQ1N1WmdWTHZENnR0VkVUazZVbWI1Wm42YXo5THB3WGdKdzFxaUhJYkVNODRSR2FraTdISmFrdjhxdzdJQkVtJTJCNFdjbzFjOWg5MVo3SXc4MDdrNW9YbXRxJTJCQkFzN2VsVlk2cWFPN2pGUWlGekt0UHNCVkxucmozYmptY3UzNFVQdWZSR1RzRkUzNFE3NW43MWZzRE4xR3Nmdm8ydnh5QnVnakJ5T0c1YUY4UU56JTJGcEhpU2EzZEdrd3VUZHRYbEMwJTJCa1ZpRms4QVZDZU1iWHZ3SnR1VEolMkJwdHpTYlVkMlVkWHpkREplZmltNHNwZmZmUzhDUzZLJTJCb1FTaFBHNFBNTXkydThFcWtVN2JCT2dFJTJCV1BCc3FlJTJCbHclMkZIcFRVbWZIc0hVam5qVGttZTk0NkdYbzFEeFRQbDE4MzBoRnNZYkFpSSUyQlZSRU02eXhwSyUyQlkwUXBXUHFyeXVvZHBLcjRmcWp1UTcxUFElMkZIVW")
                .texture(Texture.ASPHALT)
                .basketCount(2)
                .status(Status.READY)
                .build();

//        newCourt=NewCourt.builder()
//                .name("관악구민운동장 농구장")
//                .latitude(38.987654)
//                .longitude(12.309472)
//                .image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
//                .texture(Texture.ASPHALT)
//                .basketCount(2)
//                .status(Status.READY)
//                .build();



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


        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/courts/all")
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


    @Test
    @Order(2)
    @DisplayName("[GET] '/api/v1/detail/{courdId}/reservations/{date}")
    @Transactional
    void testSelectCall() throws Exception {
        // GIVEN

//        http://localhost:8080/api/v1/courts/detail/1/reservations/2021-12-18

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/courts/detail/"+1+"/reservations/"+"2021-12-18")
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
