//package org.slams.server.favorite.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.slams.server.court.entity.Court;
//import org.slams.server.court.service.CourtService;
//import org.slams.server.favorite.dto.response.FavoriteInsertResponseDto;
//import org.slams.server.favorite.entity.Favorite;
//import org.slams.server.favorite.service.FavoriteService;
//import org.slams.server.follow.dto.response.FollowerResponse;
//import org.slams.server.reservation.service.ReservationService;
//import org.slams.server.user.entity.Position;
//import org.slams.server.user.entity.Proficiency;
//import org.slams.server.user.entity.Role;
//import org.slams.server.user.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.support.GenericXmlApplicationContext;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MutablePropertySources;
//import org.springframework.core.io.support.ResourcePropertySource;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureRestDocs
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Slf4j
//public class FavoriteControllerTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @MockBean
//    private CourtService courtService;
//
//    private User user;
//    private Court court;
//    private Favorite favorite;
//
//
//
//    @MockBean
//    private FavoriteService favoriteService;
//
//
//
//    LocalDateTime now = LocalDateTime.now();
//
//    // JWT 추가 코드
//    private String jwtToken;
//
//
//    private Long COURT_ID=1L;
//    private Long FAVORITE_ID;
//
//    @BeforeEach
//    void setUp() throws Exception{
//
//
//        // JWT 추가 코드
//        // 컨테이너 생성
//        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
//
//        // 환경변수 관리 객체 생성
//        ConfigurableEnvironment env = ctx.getEnvironment();
//
//        // 프로퍼티 관리 객체 생성
//        MutablePropertySources prop = env.getPropertySources();
//
//        // 프로퍼티 관리 객체에 프로퍼티 파일 추가
//        prop.addLast(new ResourcePropertySource("classpath:test.properties"));
//
//        // 프로퍼티 정보 얻기
//        String token = env.getProperty("token");
//        jwtToken = "Bearer "+token;
//
//
//    }
//
//
//    // 즐겨찾기 추가
//    @Test
//    @DisplayName("[POST] '/api/v1/favorite'")
//    void testInsertCall() throws Exception {
//        // GIVEN
//
//        user = User.builder()
//                .id(1L)
//                .nickname("test")
//                .email("sds1zzang@naver.com")
//                .socialId("1L")
//                .description("my name is sds")
//                .profileImage("desktop Image")
//                .role(Role.USER)
//                .proficiency(Proficiency.INTERMEDIATE.BEGINNER)
//                .positions(Arrays.asList(Position.PF))
//                .build();
//
//        user.setCreatedAt(now);
//        user.setUpdateAt(now);
//
//
//        court=Court.builder()
//                .id(1L)
//                .name("testCourt")
//                .basketCount(2)
//                .latitude(40.0)
//                .longitude(50.0)
//                .build();
//
//        court.setCreatedAt(now);
//        court.setUpdateAt(now);
//
//
//       FavoriteInsertResponseDto favoriteInsertResponseDto=new FavoriteInsertResponseDto(favorite);
//       favoriteInsertResponseDto.FavoriteInsertResponseDto(1L,1L,1L,"hey");
//
//
//
//        given(favoriteService.insert(any(),any())).willReturn(favoriteInsertResponseDto);
//
//
//        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/favorites")
//                .header("Authorization",jwtToken)
//                .contentType(MediaType.APPLICATION_JSON) // TODO: 사진 들어오면 multipart/form-data
//                .content(objectMapper.writeValueAsString(favorite));
//
//        // WHEN // THEN
//        mockMvc.perform(request)
//                .andExpect(status().isCreated())
//                .andDo(print())
//                .andDo(document("favorite",
//                        requestFields(
//                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디")
//                        ),
//                        responseFields(
//                                fieldWithPath("favoriteId").type(JsonFieldType.NUMBER).description("예약 아이디"),
//                                fieldWithPath("courtName").type(JsonFieldType.NUMBER).description("농구장 이름"),
//                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
//                                fieldWithPath("courtId").type(JsonFieldType.NUMBER).description("코트 아이디"),
//                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
//                                fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
//                        )
//                ));
//    }
//
//
//
//
//    // 즐겨찾기 조회
//    @Test
//    @Order(2)
//    @DisplayName("[GET] '/api/v1/favorites")
//    @Transactional
//    void testAllCourt() throws Exception {
//        // GIVEN
//
//
//        user = User.builder()
//                .nickname("test")
//                .email("sds1zzang@naver.com")
//                .socialId("1L")
//                .description("my name is sds")
//                .profileImage("desktop Image")
//                .role(Role.USER)
//                .proficiency(Proficiency.INTERMEDIATE.BEGINNER)
//                .positions(Arrays.asList(Position.PF))
//                .build();
//
//        user.setCreatedAt(now);
//        user.setUpdateAt(now);
//
//
//        court=Court.builder()
//                .name("testCourt")
//                .basketCount(2)
//                .latitude(40.0)
//                .longitude(50.0)
//                .build();
//
//        court.setCreatedAt(now);
//        court.setUpdateAt(now);
//
//        LocalDateTime start=now.plusDays(1);
//        LocalDateTime end=now.plusDays(1);
//
//
////        Favorite favorite=new Favorite();
////
////        Favorite favorite1= Favorite.of(court,user);
////        Favorite favorite2 = Favorite.of(court,user);
//        FavoriteInsertResponseDto favoriteInsertResponseDto=new FavoriteInsertResponseDto(favorite1);
//        given(favoriteService.insert(any(),any())).willReturn(favoriteInsertResponseDto);
//
//
//
//        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/favorites")
//                .header("Authorization",jwtToken)
//                .contentType(MediaType.APPLICATION_JSON); // TODO: 사진 들어오면 multipart/form-data
//
//        // WHEN // THEN
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andDo(document("favorites-select",
//                        responseFields(
//                                fieldWithPath("favorites").type(JsonFieldType.ARRAY).description("즐겨찾기"),
//                                fieldWithPath("favorites.[].favoriteId").type(JsonFieldType.NUMBER).description("즐겨찾기 아이디"),
//                                fieldWithPath("favorites.[].courtId").type(JsonFieldType.NUMBER).description("즐겨찾기 한 코트 아이디"),
//                                fieldWithPath("favorites.[].courtName").type(JsonFieldType.STRING).description("즐겨찾기 한 코트 이름"),
//                                fieldWithPath("favorites.[].latitude").type(JsonFieldType.NUMBER).description("즐겨찾기 한 코트 위도"),
//                                fieldWithPath("favorites.[].longitude").type(JsonFieldType.NUMBER).description("즐겨찾기 한 코트 경도"),
//                                fieldWithPath("favorites.[].createdAt").type(JsonFieldType.STRING).description("코트 생성일자"),
//                                fieldWithPath("favorites.[].updatedAt").type(JsonFieldType.STRING).description("코트 수정일자")
//                        )
//                ));
//    }
//
//
//
//    // 즐겨찾기 삭제
//    @Test
//    @DisplayName("[DELETE] '/api/v1/favorites/{favoriteId}'")
//    @Order(3)
//    void testDelete() throws Exception {
//
//
//
//        mockMvc.perform(delete("/api/v1/favorites/{favoriteId}", FAVORITE_ID)
//                        .header("Authorization",jwtToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isAccepted())
////                .andExpect(jsonPath("id").value(reservation.getId()))
//                .andDo(print())
//                .andDo(
//                        document("reservation/delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
//                                pathParameters(
//                                        parameterWithName("favoriteId").description("삭제 요청 즐겨찾기 아이디")
//                                ),
//                                responseFields(
//                                        fieldWithPath("favoriteId").type(JsonFieldType.NUMBER).description("즐겨찾기 아이디")
//                                )
//                        )
//                );
//    }
//
//
//
//}
