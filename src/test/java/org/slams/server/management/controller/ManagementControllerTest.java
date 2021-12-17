package org.slams.server.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.court.dto.request.NewCourtRequest;
import org.slams.server.court.dto.response.NewCourtResponse;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;
import org.slams.server.court.service.NewCourtService;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class ManagementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private NewCourtService newCourtService;

	private String jwtToken;

	@BeforeEach
	void setUp() throws IOException {
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
		jwtToken = "Bearer " + token;
	}

	@Test
	void getNewCourtsInStatus() throws Exception {
		// given
		CursorPageRequest request = new CursorPageRequest(3, 5L, false);

		NewCourt acceptedCourt1 = NewCourt.builder()
			.id(1L)
			.name("관악구민운동장 농구장")
			.latitude(38.987654)
			.longitude(12.309472)
			.image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
			.texture(Texture.ASPHALT)
			.basketCount(2)
			.status(Status.ACCEPT)
			.createdAt(LocalDateTime.now())
			.updateAt(LocalDateTime.now())
			.build();
		NewCourt acceptedCourt2 = NewCourt.builder()
			.id(2L)
			.name("뚝섬 농구장")
			.latitude(127.139326)
			.longitude(27.485599)
			.image("농구장 이미지")
			.texture(Texture.CONCRETE)
			.basketCount(2)
			.status(Status.ACCEPT)
			.createdAt(LocalDateTime.now())
			.updateAt(LocalDateTime.now())
			.build();
		NewCourt deniedCourt = NewCourt.builder()
			.id(3L)
			.name("우리집 앞 농구장")
			.latitude(28.987344)
			.longitude(47.301472)
			.image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
			.texture(Texture.ETC)
			.basketCount(4)
			.status(Status.DENY)
			.createdAt(LocalDateTime.now())
			.updateAt(LocalDateTime.now())
			.build();

		List<NewCourtResponse> newCourts = List.of(NewCourtResponse.toResponse(deniedCourt),
			NewCourtResponse.toResponse(acceptedCourt2), NewCourtResponse.toResponse(acceptedCourt1)
		);

		CursorPageResponse<List<NewCourtResponse>> response = new CursorPageResponse<>(newCourts, 5L);

		given(newCourtService.getNewCourtsInStatus(anyString(), any())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/management/newCourts")
				.header("Authorization", jwtToken)
				.param("status", "DONE")
				.param("size", String.valueOf(request.getSize()))
				.param("lastId", String.valueOf(request.getLastId()))
				.param("isFirst", request.getIsFirst().toString())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andDo(document("management/newCourt-getNewCourtsInStatus", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("status").description("사용자가 추가한 농구장목록을 불러오는 기준"),
					parameterWithName("size").description("요청할 데이터의 수"),
					parameterWithName("lastId").description("화면에 보여준 마지막 데이터의 구별키"),
					parameterWithName("isFirst").description("처음으로 요청했는지 여부")
				),
				responseFields(
					fieldWithPath("contents").type(JsonFieldType.ARRAY).description("사용자가 추가한 농구장 목록"),
					fieldWithPath("contents[].newCourtId").type(JsonFieldType.NUMBER).description("팔로우 구별키"),
					fieldWithPath("contents[].courtName").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 닉네임"),
					fieldWithPath("contents[].latitude").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 위도"),
					fieldWithPath("contents[].longitude").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 경도"),
					fieldWithPath("contents[].image").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 이미지"),
					fieldWithPath("contents[].texture").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 바닥 재질"),
					fieldWithPath("contents[].basketCount").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 골대 수"),
					fieldWithPath("contents[].status").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 승인여부"),
					fieldWithPath("contents[].createdAt").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 정보 최초 생성시간"),
					fieldWithPath("contents[].updatedAt").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 정보 최근 수정시간"),
					fieldWithPath("lastId").type(JsonFieldType.NUMBER).description("서버에서 제공한 마지막 데이터의 구별키")
				)
			));
	}

	@Test
	void accept() throws Exception {
		// given
		NewCourt acceptedCourt = NewCourt.builder()
			.id(1L)
			.name("관악구민운동장 농구장")
			.latitude(38.987654)
			.longitude(12.309472)
			.image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
			.texture(Texture.ASPHALT)
			.basketCount(2)
			.status(Status.ACCEPT)
			.createdAt(LocalDateTime.now())
			.updateAt(LocalDateTime.now())
			.build();

		NewCourtRequest request = new NewCourtRequest(acceptedCourt.getId());

		NewCourtResponse response = NewCourtResponse.toResponse(acceptedCourt);

		given(newCourtService.acceptNewCourt(acceptedCourt.getId())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/management/newCourt/accept")
				.header("Authorization", jwtToken)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isAccepted())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("newCourtId").value(acceptedCourt.getId()))
			.andExpect(jsonPath("courtName").value(acceptedCourt.getName()))
			.andExpect(jsonPath("latitude").value(acceptedCourt.getLatitude()))
			.andExpect(jsonPath("longitude").value(acceptedCourt.getLongitude()))
			.andExpect(jsonPath("image").value(acceptedCourt.getImage()))
			.andExpect(jsonPath("texture").value(acceptedCourt.getTexture().toString()))
			.andExpect(jsonPath("basketCount").value(acceptedCourt.getBasketCount()))
			.andExpect(jsonPath("status").value(acceptedCourt.getStatus().toString()))
			.andDo(document("management/newCourt-accept", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 구별키")
				),
				responseFields(
					fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 구별키"),
					fieldWithPath("courtName").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 닉네임"),
					fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 위도"),
					fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 경도"),
					fieldWithPath("image").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 이미지"),
					fieldWithPath("texture").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 바닥 재질"),
					fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 골대 수"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 승인여부"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 정보 최초 생성시간"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 정보 최근 수정시간")
				)
			));
	}

	@Test
	void deny() throws Exception {
		// given
		NewCourt acceptedCourt = NewCourt.builder()
			.id(1L)
			.name("관악구민운동장 농구장")
			.latitude(38.987654)
			.longitude(12.309472)
			.image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
			.texture(Texture.ASPHALT)
			.basketCount(2)
			.status(Status.DENY)
			.createdAt(LocalDateTime.now())
			.updateAt(LocalDateTime.now())
			.build();

		NewCourtRequest request = new NewCourtRequest(acceptedCourt.getId());

		NewCourtResponse response = NewCourtResponse.toResponse(acceptedCourt);

		given(newCourtService.acceptNewCourt(acceptedCourt.getId())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/management/newCourt/accept")
				.header("Authorization", jwtToken)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isAccepted())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("newCourtId").value(acceptedCourt.getId()))
			.andExpect(jsonPath("courtName").value(acceptedCourt.getName()))
			.andExpect(jsonPath("latitude").value(acceptedCourt.getLatitude()))
			.andExpect(jsonPath("longitude").value(acceptedCourt.getLongitude()))
			.andExpect(jsonPath("image").value(acceptedCourt.getImage()))
			.andExpect(jsonPath("texture").value(acceptedCourt.getTexture().toString()))
			.andExpect(jsonPath("basketCount").value(acceptedCourt.getBasketCount()))
			.andExpect(jsonPath("status").value(acceptedCourt.getStatus().toString()))
			.andDo(document("management/newCourt-accept", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 구별키")
				),
				responseFields(
					fieldWithPath("newCourtId").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 구별키"),
					fieldWithPath("courtName").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 닉네임"),
					fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 위도"),
					fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 경도"),
					fieldWithPath("image").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 이미지"),
					fieldWithPath("texture").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 바닥 재질"),
					fieldWithPath("basketCount").type(JsonFieldType.NUMBER).description("사용자가 추가한 농구장 골대 수"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 승인여부"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 정보 최초 생성시간"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("사용자가 추가한 농구장 정보 최근 수정시간")
				)
			));
	}
	
}