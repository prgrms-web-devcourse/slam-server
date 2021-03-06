package org.slams.server.follow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.follow.dto.response.FollowerResponse;
import org.slams.server.follow.dto.response.FollowingResponse;
import org.slams.server.follow.entity.Follow;
import org.slams.server.follow.service.FollowService;
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
import java.util.List;

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
		jwtToken = "Bearer " + token;
	}

	@Test
	void followerPage() throws Exception {
		// given
		CursorPageRequest request = new CursorPageRequest(2, 5L, false);

		User user1 = User.builder()
			.id(1L)
			.nickname("??????")
			.profileImage("s3??? ????????? ????????? ????????? url")
			.build();
		User user2 = User.builder()
			.id(2L)
			.nickname("??????")
			.profileImage("s3??? ????????? ????????? ????????? url")
			.build();
		User user3 = User.builder()
			.id(3L)
			.email("candy@gmail.com")
			.nickname("??????")
			.build();
		Follow follow1 = Follow.builder()
			.id(1L)
			.follower(user1)
			.following(user2)
			.build();
		follow1.setCreatedAt(LocalDateTime.now());
		follow1.setUpdateAt(LocalDateTime.now());
		Follow follow2 = Follow.builder()
			.id(2L)
			.follower(user3)
			.following(user2)
			.build();
		follow2.setCreatedAt(LocalDateTime.now());
		follow2.setUpdateAt(LocalDateTime.now());

		List<FollowerResponse> followerList = List.of(
			FollowerResponse.toResponse(follow2),
			FollowerResponse.toResponse(follow1)
		);

		CursorPageResponse<List<FollowerResponse>> response = new CursorPageResponse<>(followerList, 1L);

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
					parameterWithName("userId").description("????????? ?????????")
				),
				responseFields(
					fieldWithPath("contents").type(JsonFieldType.ARRAY).description("????????? ??????"),
					fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
					fieldWithPath("contents[].creator").type(JsonFieldType.OBJECT).description("?????????(?????????)"),
					fieldWithPath("contents[].creator.id").type(JsonFieldType.NUMBER).description("?????????(?????????) ?????????"),
					fieldWithPath("contents[].creator.nickname").type(JsonFieldType.STRING).description("?????????(?????????) ??????"),
					fieldWithPath("contents[].creator.profileImage").type(JsonFieldType.STRING).description("?????????(?????????) ????????? ?????????").optional(),
					fieldWithPath("contents[].createdAt").type(JsonFieldType.STRING).description("????????? ?????? ????????????"),
					fieldWithPath("contents[].updatedAt").type(JsonFieldType.STRING).description("????????? ?????? ????????????"),
					fieldWithPath("lastId").type(JsonFieldType.NUMBER).description("???????????? ????????? ????????? ???????????? ?????????").optional()
				)
			));
	}

	@Test
	void followingPage() throws Exception {
		// given
		CursorPageRequest request = new CursorPageRequest(2, 5L, false);

		User user1 = User.builder()
			.id(1L)
			.nickname("??????")
			.profileImage("s3??? ????????? ????????? ????????? url")
			.build();
		User user2 = User.builder()
			.id(2L)
			.nickname("??????")
			.profileImage("s3??? ????????? ????????? ????????? url")
			.build();
		User user3 = User.builder()
			.id(3L)
			.email("candy@gmail.com")
			.nickname("??????")
			.build();
		Follow follow1 = Follow.builder()
			.id(1L)
			.follower(user1)
			.following(user2)
			.build();
		follow1.setCreatedAt(LocalDateTime.now());
		follow1.setUpdateAt(LocalDateTime.now());
		Follow follow2 = Follow.builder()
			.id(2L)
			.follower(user1)
			.following(user3)
			.build();
		follow2.setCreatedAt(LocalDateTime.now());
		follow2.setUpdateAt(LocalDateTime.now());

		List<FollowingResponse> followingList = List.of(
			FollowingResponse.toResponse(follow2),
			FollowingResponse.toResponse(follow1)
		);

		CursorPageResponse<List<FollowingResponse>> response = new CursorPageResponse<>(followingList, 1L);

		given(followService.followingPage(anyLong(), any())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/follow/{userId}/followings", user1.getId())
				.header("Authorization", jwtToken)
				.param("size", String.valueOf(request.getSize()))
				.param("lastId", String.valueOf(request.getLastId()))
				.param("isFirst", request.getIsFirst().toString())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andDo(document("follow/follow-followingPage", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("userId").description("????????? ?????????")
				),
				responseFields(
					fieldWithPath("contents").type(JsonFieldType.ARRAY).description("????????? ??????"),
					fieldWithPath("contents[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
					fieldWithPath("contents[].receiver").type(JsonFieldType.OBJECT).description("?????????(?????????)"),
					fieldWithPath("contents[].receiver.id").type(JsonFieldType.NUMBER).description("?????????(?????????) ?????????"),
					fieldWithPath("contents[].receiver.nickname").type(JsonFieldType.STRING).description("?????????(?????????) ??????"),
					fieldWithPath("contents[].receiver.profileImage").type(JsonFieldType.STRING).description("?????????(?????????) ????????? ?????????").optional(),
					fieldWithPath("contents[].createdAt").type(JsonFieldType.STRING).description("????????? ?????? ????????????"),
					fieldWithPath("contents[].updatedAt").type(JsonFieldType.STRING).description("????????? ?????? ????????????"),
					fieldWithPath("lastId").type(JsonFieldType.NUMBER).description("???????????? ????????? ????????? ???????????? ?????????").optional()
				)
			));
	}

}