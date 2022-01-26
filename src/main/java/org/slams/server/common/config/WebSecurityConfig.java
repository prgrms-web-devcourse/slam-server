package org.slams.server.common.config;

import lombok.extern.slf4j.Slf4j;
import org.slams.server.user.oauth.OAuth2AuthenticationSuccessHandler;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slams.server.user.oauth.jwt.JwtAuthenticationFilter;
import org.slams.server.user.service.OAuthUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfig jwtConfig;
	private final OAuthUserService oAuthUserService;
	private final ServletContext servletContext;

	public WebSecurityConfig(JwtConfig jwtConfig, OAuthUserService oAuthUserService, ServletContext servletContext) {
		this.jwtConfig = jwtConfig;
		this.oAuthUserService = oAuthUserService;
		this.servletContext = servletContext;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/assets/**", "/h2-console/**",
			"/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs",
			"/webjars/**", "/webjars/springfox-swagger-ui/*.{js,css}");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().and()
			.authorizeRequests()
			.antMatchers("/ws/v1/**").permitAll()
			.antMatchers("/api/v1/chat/**",
				"/api/v1/favorites/**",
				"/api/v1/follow/**",
				"/api/v1/notification/**",
				"/api/v1/reservations/**",
				"/api/v1/users/**").hasAnyAuthority("USER", "ADMIN")
			.antMatchers(HttpMethod.GET, "/api/v1/courts/{courtId}/reservations/{date}").hasAnyAuthority("USER", "ADMIN")
			.antMatchers(HttpMethod.POST, "/api/v1/courts/new").hasAnyAuthority("USER", "ADMIN")
			.antMatchers("/api/v1/management/**").hasAuthority("ADMIN")
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
		//.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
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
		return new JwtAuthenticationFilter(jwtConfig.getHeader(), jwt, servletContext);
	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new OAuth2AuthenticationSuccessHandler(jwt, oAuthUserService, servletContext);
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
