package org.slams.server.favorite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.service.CourtService;
import org.slams.server.favorite.dto.request.FavoriteInsertRequestDto;
import org.slams.server.favorite.entity.Favorite;
import org.slams.server.favorite.repository.FavoriteRepository;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.reservation.service.ReservationService;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
public class FavoriteControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReservationService reservationService;

    private User user;
    private Court court;
    private Favorite favorite;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtService courtService;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;




    LocalDateTime now = LocalDateTime.now();

    // JWT 추가 코드
    private String jwtToken;


    private Long COURT_ID=1L;
    private Long FAVORITE_ID;

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
        userRepository.save(user);




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
        courtRepository.save(court);


        favorite=Favorite.of(court,user);
        FAVORITE_ID=favoriteRepository.save(favorite).getId();



    }


    // 즐겨찾기 추가
    // 생성한 코트에 예약 해보기
    @Test
    @DisplayName("[POST] '/api/v1/favorite'")
    @Order(1)
    void testInsertCall() throws Exception {
        // GIVEN

        LocalDateTime start=now.plusDays(1);
        LocalDateTime end=now.plusDays(1);

//        FavoriteInsertRequestDto givenRequest = FavoriteInsertRequestDto.builder()
//                .courtId(1L)
//                .build();
        FavoriteInsertRequestDto givenRequest=new FavoriteInsertRequestDto(COURT_ID);


        log.info(givenRequest.toString());

        log.info("user_ID:"+user.getId());


//        ReservationInsertResponseDto stubResponse = new ReservationInsertResponseDto();
//        given(reservationService.insert(any(), any()));

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/favorites")
                .header("Authorization",jwtToken)
                .contentType(MediaType.APPLICATION_JSON) // TODO: 사진 들어오면 multipart/form-data
                .content(objectMapper.writeValueAsString(givenRequest));

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("reservation-save",
                        requestFields(
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("favoriteId").type(JsonFieldType.NUMBER).description("예약 아이디"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }




    // 즐겨찾기 조회
    @Test
    @Order(2)
    @DisplayName("[GET] '/api/v1/favorites")
    @Transactional
    void testAllCourt() throws Exception {
        // GIVEN


        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/favorites")
                .header("Authorization",jwtToken)
                .contentType(MediaType.APPLICATION_JSON); // TODO: 사진 들어오면 multipart/form-data

        // WHEN // THEN
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("favorites-select",
                        responseFields(
                                fieldWithPath("favorites").type(JsonFieldType.ARRAY).description("즐겨찾기"),
                                fieldWithPath("favorites.[].favoriteId").type(JsonFieldType.NUMBER).description("즐겨찾기 아이디"),
                                fieldWithPath("favorites.[].courtId").type(JsonFieldType.NUMBER).description("즐겨찾기 한 코트 아이디"),
                                fieldWithPath("favorites.[].courtName").type(JsonFieldType.STRING).description("즐겨찾기 한 코트 이름"),
                                fieldWithPath("favorites.[].latitude").type(JsonFieldType.NUMBER).description("즐겨찾기 한 코트 위도"),
                                fieldWithPath("favorites.[].longitude").type(JsonFieldType.NUMBER).description("즐겨찾기 한 코트 경도"),
                                fieldWithPath("favorites.[].createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
                                fieldWithPath("favorites.[].updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
                        )
                ));
    }



    // 즐겨찾기 삭제
    @Test
    @DisplayName("[DELETE] '/api/v1/favorites/{favoriteId}'")
    @Order(3)
    void testDelete() throws Exception {



        mockMvc.perform(delete("/api/v1/favorites/{favoriteId}", FAVORITE_ID)
                        .header("Authorization",jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
//                .andExpect(jsonPath("id").value(reservation.getId()))
                .andDo(print())
                .andDo(
                        document("reservation/delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("favoriteId").description("삭제 요청 즐겨찾기 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("favoriteId").type(JsonFieldType.NUMBER).description("즐겨찾기 아이디")
                                )
                        )
                );
    }



}
