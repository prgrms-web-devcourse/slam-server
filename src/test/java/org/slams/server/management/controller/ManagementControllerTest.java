package org.slams.server.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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