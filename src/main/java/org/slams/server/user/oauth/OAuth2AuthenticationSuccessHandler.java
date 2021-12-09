package org.slams.server.user.oauth;

import lombok.extern.slf4j.Slf4j;
import org.slams.server.user.entity.User;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slams.server.user.service.OAuthUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private final Jwt jwt;
	private final OAuthUserService oAuthUserService;

	public OAuth2AuthenticationSuccessHandler(Jwt jwt, OAuthUserService oAuthUserService) {
		this.jwt = jwt;
		this.oAuthUserService = oAuthUserService;
	}

	// 인증이 완료되고 호출되는 메서드
	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws ServletException, IOException {
		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
			OAuth2User principal = oauth2Token.getPrincipal();
			String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

			User user = processUserOAuth2UserJoin(principal, registrationId);
			String loginSuccessJson = generateLoginSuccessJson(user);
			response.setContentType("application/json;charset=UTF-8");
			response.setContentLength(loginSuccessJson.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(loginSuccessJson);
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

	private User processUserOAuth2UserJoin(OAuth2User oAuth2User, String registrationId) {
		return oAuthUserService.join(oAuth2User, registrationId);
	}

	private String generateLoginSuccessJson(User user) {
		String token = generateToken(user);
		return new StringBuilder()
			.append("{\"token\":\"")
			.append(token).append("\", \"UserId\":\"")
			.append(user.getId()).append("\",  \"role\":\"")
			.append(user.getRole().toString())
			.append("\"}").toString();

	}

	private String generateToken(User user) {
		return jwt.sign(Jwt.Claims.from(user.getId(), new String[]{"USER"}));
	}


}
