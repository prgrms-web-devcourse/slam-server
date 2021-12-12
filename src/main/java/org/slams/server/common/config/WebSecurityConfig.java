package org.slams.server.common.config;

import lombok.extern.slf4j.Slf4j;
import org.slams.server.user.oauth.OAuth2AuthenticationSuccessHandler;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slams.server.user.oauth.jwt.JwtAuthenticationFilter;
import org.slams.server.user.service.OAuthUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfig jwtConfig;
	public final OAuthUserService oAuthUserService;

	public WebSecurityConfig(JwtConfig jwtConfig, OAuthUserService oAuthUserService) {
		this.jwtConfig = jwtConfig;
		this.oAuthUserService = oAuthUserService;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/assets/**", "/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
//			.antMatchers("/api/v1/users/me").hasAnyRole("USER", "ADMIN")
			.anyRequest().permitAll()
			.and()
			/**
			 * formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
			 */
			.formLogin()
			.disable()
			.csrf()
			.disable()
			.headers()
			.disable()
			.httpBasic()
			.disable()
			.rememberMe()
			.disable()
			.logout()
			.disable()
			/**
			 * Session 사용하지 않음
			 */
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			/**
			 * OAuth2 설정
			 */
			.oauth2Login()
			.successHandler(getApplicationContext().getBean(OAuth2AuthenticationSuccessHandler.class))
			.and()
			/**
			 * 예외처리 핸들러
			 */
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.and()
			/**
			 * Jwt 필터
			 */
			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
		;
	}

	@Bean
	public Jwt jwt() {
		return new Jwt(
			jwtConfig.getIssuer(),
			jwtConfig.getClientSecret(),
			jwtConfig.getExpirySeconds()
		);
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new JwtAuthenticationFilter(jwtConfig.getHeader(), jwt);
	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new OAuth2AuthenticationSuccessHandler(jwt, oAuthUserService);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, e) -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication != null ? authentication.getPrincipal() : null;
			log.warn("{} is denied", principal, e);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("ACCESS DENIED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

}
