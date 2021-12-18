package org.slams.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.notification.dto.response.CourtInfo;
import org.slams.server.notification.dto.response.FollowerInfo;
import org.slams.server.notification.dto.response.LoudspeakerInfo;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.user.dto.request.ExtraUserInfoRequest;
import org.slams.server.user.dto.request.ProfileImageRequest;
import org.slams.server.user.dto.response.*;
import org.slams.server.user.entity.Position;
import org.slams.server.user.entity.Proficiency;
import org.slams.server.user.entity.Role;
import org.slams.server.user.entity.User;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.slams.server.notification.entity.NotificationType.FOLLOWING;
import static org.slams.server.notification.entity.NotificationType.LOUDSPEAKER;
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
	void getDefaultInfo() throws Exception {
		// given
		NotificationResponse followNotification = NotificationResponse.createForFollowNotification(FOLLOWING, FollowerInfo.builder()
			.userId(1L)
			.userNickname("젤리")
			.userImage("https://team14-slam.s3.ap-northeast-2.amazonaws.com/profile/%E1%84%82%E1%85%A1.png")
			.build(), false, false, LocalDateTime.now(), LocalDateTime.now());
		NotificationResponse loudspeakerNotification = NotificationResponse.createForLoudspeakerNotification(LOUDSPEAKER, LoudspeakerInfo.builder()
			.courtInfo(
				CourtInfo.builder()
					.id(3L)
					.name("용산구 농구장")
					.latitude(123)
					.longitude(456)
					.image("https://team14-slam.s3.ap-northeast-2.amazonaws.com/court_dummy/court1.jpg")
					.basketCount(4)
					.texture(Texture.ASPHALT)
					.build()
			)
			.startTime(13)
			.build(), false, false, LocalDateTime.now(), LocalDateTime.now());

		List<NotificationResponse> notifications = List.of(followNotification, loudspeakerNotification);

		DefaultUserInfoResponse response = DefaultUserInfoResponse.builder()
			.userId(1L)
			.email("jelly@gmail.com")
			.nickname("젤리")
			.description("나는 젤리가 정말 좋아")
			.profileImage("s3에 저장된 프로필 이미지 url")
			.role(Role.USER)
			.positions(Arrays.asList(Position.SG, Position.PG))
			.proficiency(Proficiency.INTERMEDIATE)
			.notifications(notifications)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		given(userService.getDefaultInfo(anyLong())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me")
				.header("Authorization", jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("userId").value(1L))
			.andExpect(jsonPath("email").value("jelly@gmail.com"))
			.andExpect(jsonPath("nickname").value("젤리"))
			.andExpect(jsonPath("description").value("나는 젤리가 정말 좋아"))
			.andExpect(jsonPath("profileImage").value("s3에 저장된 프로필 이미지 url"))
			.andExpect(jsonPath("proficiency").value("INTERMEDIATE"))
			.andDo(document("users/user-getMyInfo", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 구별키"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("사용자 한줄 소개"),
					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
					fieldWithPath("role").type(JsonFieldType.STRING).description("사용자 권한"),
					fieldWithPath("positions").type(JsonFieldType.ARRAY).description("선호하는 포지션들"),
					fieldWithPath("proficiency").type(JsonFieldType.STRING).description("숙련도"),
					fieldWithPath("notifications").type(JsonFieldType.ARRAY).description("최근 알람목록(최대 10개)"),
					fieldWithPath("notifications[].type").type(JsonFieldType.STRING).description("알람 종류"),
					fieldWithPath("notifications[].followerInfo").type(JsonFieldType.OBJECT).description("팔로우 알람정보").optional(),
					fieldWithPath("notifications[].followerInfo.userId").type(JsonFieldType.NUMBER).description("사용자 구별키"),
					fieldWithPath("notifications[].followerInfo.userNickname").type(JsonFieldType.STRING).description("사용자 이름"),
					fieldWithPath("notifications[].followerInfo.userImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
					fieldWithPath("notifications[].createdAt").type(JsonFieldType.STRING).description("알람 최초 생성시간"),
					fieldWithPath("notifications[].updatedAt").type(JsonFieldType.STRING).description("알람 최근 수정시간"),
					fieldWithPath("notifications[].isClicked").type(JsonFieldType.BOOLEAN).description("알람 읽음 여부"),
					fieldWithPath("notifications[].isRead").type(JsonFieldType.BOOLEAN).description("알람 읽음 여부"),
					fieldWithPath("notifications[].loudspeakerInfo").type(JsonFieldType.OBJECT).description("확성기 알람정보").optional(),
					fieldWithPath("notifications[].loudspeakerInfo.startTime").type(JsonFieldType.NUMBER).description("경기 시작시간"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo").type(JsonFieldType.OBJECT).description("농구장 상세정보"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.id").type(JsonFieldType.NUMBER).description("농구장 구별키"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.name").type(JsonFieldType.STRING).description("농구장 이름"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.latitude").type(JsonFieldType.NUMBER).description("농구장 위도"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.longitude").type(JsonFieldType.NUMBER).description("농구장 경도"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.image").type(JsonFieldType.STRING).description("농구장 이미지"),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.basketCount").type(JsonFieldType.NUMBER).description("농구장 골대 수 "),
					fieldWithPath("notifications[].loudspeakerInfo.courtInfo.texture").type(JsonFieldType.STRING).description("농구장 바닥 재질"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사용자 정보 최초 생성시간"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("사용자 정보 최근 수정시간")
				)
			));
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
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/myprofile")
				.header("Authorization", jwtToken)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isAccepted())
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
	void getMyInfo() throws Exception {
		// given
		MyProfileResponse response = MyProfileResponse.builder()
			.userId(1L)
			.nickname("젤리")
			.description("나는 젤리가 정말 좋아")
			.profileImage("s3에 저장된 프로필 이미지 url")
			.positions(Arrays.asList(Position.SG, Position.PG))
			.proficiency(Proficiency.INTERMEDIATE)
			.followerCount(325L)
			.followingCount(118L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		given(userService.getMyInfo(anyLong())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/myprofile")
				.header("Authorization", jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("userId").value(1L))
			.andExpect(jsonPath("nickname").value("젤리"))
			.andExpect(jsonPath("description").value("나는 젤리가 정말 좋아"))
			.andExpect(jsonPath("profileImage").value("s3에 저장된 프로필 이미지 url"))
			.andExpect(jsonPath("proficiency").value("INTERMEDIATE"))
			.andExpect(jsonPath("followerCount").value(325L))
			.andExpect(jsonPath("followingCount").value(118L))
			.andDo(document("users/user-getMyInfo", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 구별키"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("사용자 한줄 소개"),
					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
					fieldWithPath("positions").type(JsonFieldType.ARRAY).description("선호하는 포지션들"),
					fieldWithPath("proficiency").type(JsonFieldType.STRING).description("숙련도"),
					fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("사용자 팔로워 수"),
					fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("사용자 팔로잉 수"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사용자 정보 최초 생성시간"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("사용자 정보 최근 수정시간")
				)
			));
	}

	@Test
	void getUserInfo() throws Exception {
		// given
		User user = User.builder()
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
		Court court1 = Court.builder()
			.id(1L)
			.name("관악구민운동장 농구장")
			.latitude(38.987654)
			.longitude(124.309472)
			.image("aHR0cHM6Ly9pYmIuY28vcXMwSnZXYg")
			.texture(Texture.ASPHALT)
			.basketCount(2)
			.build();
		Court court2 = Court.builder()
			.id(2L)
			.name("용산구민운동장 농구장")
			.latitude(37.987654)
			.longitude(125.309472)
			.image("aHR0cHM6Ly9pYmIuY28v123g")
			.texture(Texture.ASPHALT)
			.basketCount(4)
			.build();

		List<FavoriteCourtResponse> favoriteCourts = List.of(
			new FavoriteCourtResponse(court1.getId(), court1.getName()),
			new FavoriteCourtResponse(court2.getId(), court2.getName()));

		Long followerCount = 325L;
		Long followingCount = 129L;

		UserProfileResponse response = UserProfileResponse.toResponse(user, true, followerCount, followingCount, favoriteCourts);

		given(userService.getUserInfo(anyLong(), anyLong())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}", user.getId())
				.header("Authorization", jwtToken)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("userId").value(user.getId()))
			.andExpect(jsonPath("nickname").value(user.getNickname()))
			.andExpect(jsonPath("description").value(user.getDescription()))
			.andExpect(jsonPath("profileImage").value(user.getProfileImage()))
			.andExpect(jsonPath("proficiency").value(user.getProficiency().toString()))
			.andExpect(jsonPath("followerCount").value(followerCount))
			.andExpect(jsonPath("followingCount").value(followingCount))
			.andDo(document("users/user-getUserInfo", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 구별키"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
					fieldWithPath("description").type(JsonFieldType.STRING).description("사용자 한줄 소개"),
					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
					fieldWithPath("positions").type(JsonFieldType.ARRAY).description("선호하는 포지션들"),
					fieldWithPath("proficiency").type(JsonFieldType.STRING).description("숙련도"),
					fieldWithPath("isFollowing").type(JsonFieldType.BOOLEAN).description("팔로우 여부"),
					fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("사용자 팔로워 수"),
					fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("사용자 팔로잉 수"),
					fieldWithPath("favoriteCourts").type(JsonFieldType.ARRAY).description("즐겨찾기한 농구장 목록"),
					fieldWithPath("favoriteCourts[].courtId").type(JsonFieldType.NUMBER).description("즐겨찾기한 농구장 구별키"),
					fieldWithPath("favoriteCourts[].courtName").type(JsonFieldType.STRING).description("즐겨찾기한 농구장 이름"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("사용자 정보 최초 생성시간"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("사용자 정보 최근 수정시간")
				)
			));
	}

	@Test
	void updateUserProfileImage() throws Exception {
		// given
		ProfileImageRequest request = new ProfileImageRequest("이미지 base64로 인코딩된 값");

		ProfileImageResponse response = new ProfileImageResponse("s3에 저장된 이미지 url");

		given(userService.updateUserProfileImage(anyLong(), any())).willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/myprofile/image")
				.header("Authorization", jwtToken)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions.andExpect(status().isAccepted())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andDo(document("users/user-updateUserProfileImage", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지")
				),
				responseFields(
					fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지")
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
			.andDo(document("users/user-deleteUserProfileImage", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("profileImage").type(JsonFieldType.NULL).description("사용자 프로필 이미지")
				)
			));
	}

}