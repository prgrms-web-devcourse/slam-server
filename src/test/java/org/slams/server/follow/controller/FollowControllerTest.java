package org.slams.server.follow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.follow.dto.FollowResponse;
import org.slams.server.follow.entity.Follow;
import org.slams.server.follow.service.FollowService;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class FollowControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private FollowService followService;

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
	void followerPage() throws Exception {
		// given
		CursorPageRequest request = new CursorPageRequest(2, 5L, false);

		User user1 = User.builder()
			.id(1L)
			.email("jelly@gmail.com")
			.nickname("젤리")
			.description("나는 젤리가 정말 좋아")
			.profileImage("s3에 저장된 프로필 이미지 url")
			.role(Role.USER)
			.positions(Arrays.asList(Position.SG, Position.PG))
			.proficiency(Proficiency.INTERMEDIATE)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		User user2 = User.builder()
			.id(2L)
			.email("chocolate@gmail.com")
			.nickname("초코")
			.description("나는 초코가 정말 좋아")
			.profileImage("s3에 저장된 프로필 이미지 url")
			.role(Role.USER)
			.positions(Arrays.asList(Position.C))
			.proficiency(Proficiency.BEGINNER)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		User user3 = User.builder()
			.id(3L)
			.email("candy@gmail.com")
			.nickname("캔디")
			.description("나는 캔디가 정말 좋아")
			.profileImage("s3에 저장된 프로필 이미지 url")
			.role(Role.USER)
			.positions(Arrays.asList(Position.PF, Position.C))
			.proficiency(Proficiency.MASTER)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		Follow follow1 = Follow.of(user1, user2);
		Follow follow2 = Follow.of(user3, user2);

		List<FollowResponse> followerList = List.of(
			FollowResponse.toResponse(2L, user3, LocalDateTime.now(), LocalDateTime.now()),
			FollowResponse.toResponse(1L, user1, LocalDateTime.now(), LocalDateTime.now())
		);

		CursorPageResponse<List<FollowResponse>> response = new CursorPageResponse<>(followerList, 1L);

		given(followService.followerPage(anyLong(), any())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/follow/{userId}/followers", user2.getId())
				.header("Authorization", jwtToken)
				.param("size", String.valueOf(request.getSize()))
				.param("lastId", String.valueOf(request.getLastId()))
				.param("isFirst", request.getIsFirst().toString())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andDo(document("follow/follow-followerPage", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("userId").description("사용자 구별키")
				),
				responseFields(
					fieldWithPath("contents").type(JsonFieldType.ARRAY).description("팔로워 목록"),
					fieldWithPath("contents[].followId").type(JsonFieldType.NUMBER).description("팔로우 구별키"),
					fieldWithPath("contents[].creatorId").type(JsonFieldType.NUMBER).description("사용자(팔로워) 구별키"),
					fieldWithPath("contents[].nickname").type(JsonFieldType.STRING).description("사용자(팔로워) 닉네임"),
					fieldWithPath("contents[].profileImage").type(JsonFieldType.STRING).description("사용자(팔로워) 프로필 이미지"),
					fieldWithPath("contents[].createdAt").type(JsonFieldType.STRING).description("팔로우 최초 생성시간"),
					fieldWithPath("contents[].updatedAt").type(JsonFieldType.STRING).description("팔로우 최근 수정시간"),
					fieldWithPath("lastId").type(JsonFieldType.NUMBER).description("서버에서 제공한 마지막 데이터의 구별키")
				)
			));

	}

}