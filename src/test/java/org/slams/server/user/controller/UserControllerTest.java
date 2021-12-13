package org.slams.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slams.server.user.dto.request.ExtraUserInfoRequest;
import org.slams.server.user.dto.response.ExtraUserInfoResponse;
import org.slams.server.user.dto.response.ProfileImageResponse;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.service.UserService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
@ActiveProfiles("dev")
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

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
	void addExtraUserInfo() throws Exception {
		// given
		ExtraUserInfoRequest request = ExtraUserInfoRequest.builder()
			.nickname("젤리")
			.description("나는 젤리가 정말 좋아")
			.positions(Arrays.asList(Position.SG, Position.PG))
			.proficiency(Proficiency.INTERMEDIATE)
			.build();

		ExtraUserInfoResponse response = ExtraUserInfoResponse.builder()
			.userId(1L)
			.email("jelly@gmail.com")
			.nickname("젤리")
			.description("나는 젤리가 정말 좋아")
			.profileImage("www.s3_asdfjkl.com")
			.role(Role.USER)
			.positions(Arrays.asList(Position.SG, Position.PG))
			.proficiency(Proficiency.INTERMEDIATE)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		given(userService.addExtraUserInfo(anyLong(), any())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/me")
				.header("Authorization", jwtToken)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isCreated())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("userId").value(1L))
			.andExpect(jsonPath("email").value("jelly@gmail.com"))
			.andExpect(jsonPath("nickname").value("젤리"))
			.andExpect(jsonPath("description").value("나는 젤리가 정말 좋아"))
			.andExpect(jsonPath("profileImage").value("www.s3_asdfjkl.com"))
			.andExpect(jsonPath("proficiency").value("INTERMEDIATE"))
			.andDo(document("users/user-addExtraUserInfo", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("사용자 한줄 소개"),
					fieldWithPath("positions").type(JsonFieldType.ARRAY).description("선호하는 포지션들"),
					fieldWithPath("proficiency").type(JsonFieldType.STRING).description("숙련도")
				),
				responseFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 구별키"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("사용자 한줄 소개"),
					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
					fieldWithPath("role").type(JsonFieldType.STRING).description("사용자 권한"),
					fieldWithPath("positions").type(JsonFieldType.ARRAY).description("선호하는 포지션들"),
					fieldWithPath("proficiency").type(JsonFieldType.STRING).description("숙련도"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사용자 정보 최초 생성시간"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("사용자 정보 최근 수정시간")
				)
			));
	}

	@Test
	void deleteUserProfileImage() throws Exception {
		// given
		ProfileImageResponse response = new ProfileImageResponse(null);

		given(userService.deleteUserProfileImage(anyLong())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/myprofile/image")
				.header("Authorization", jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andDo(document("users/user-addExtraUserInfo", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("profileImage").type(JsonFieldType.NULL).description("사용자 프로필 이미지")
				)
			));
	}

}