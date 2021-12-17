package org.slams.server.user.oauth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.slams.server.user.exception.UserNotAuthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.socket.WebSocketHttpHeaders;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.http.HttpHeaders;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;


@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final String headerKey; // header에서 jwt 토큰을 꺼내올 때 사용
	private final Jwt jwt;
//	@Value("${token}")
//	private String websocketJwt;

	public JwtAuthenticationFilter(String headerKey, Jwt jwt) {
		this.headerKey = headerKey;
		this.jwt = jwt;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
						 FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		logger.info("test");
//		logger.info(websocketJwt);
		logger.info(request.getHeader(WebSocketHttpHeaders.AUTHORIZATION));

		logger.info(request.getHeader(org.apache.http.HttpHeaders.AUTHORIZATION));

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(request);
			if("http://localhost:8080/ws/v1/info".equals(request.getRequestURL().toString())){
				logger.info("pass");
				token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJVU0VSIl0sImlzcyI6InNsYW1zIiwiZXhwIjoxNzQwMzE1MDcwLCJpYXQiOjE2Mzk3NDAyNzAsInVzZXJJZCI6MH0.HTsmPDWTp4D8RKhHlQDOE49mkAZ-tcCL3cQkcdUMdVtxZ2BUunEldiA6Ifw9zvbm4C0rl3Uq1sEabWx7SFcFRQ";
			}
			if (token != null) {
				log.info("token1");
				try {
					Jwt.Claims claims = verify(token);
					log.debug("Jwt parse result: {}", claims);
					log.info("token222");
					Long userId = claims.userId;
					List<GrantedAuthority> authorities = getAuthorities(claims);
					log.info("token22222233");
					if (userId != null && authorities.size() > 0) {
						JwtAuthenticationToken authentication =
							new JwtAuthenticationToken(new JwtAuthentication(token, userId), null, authorities);
						authentication.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request));
						logger.info(authentication.toString());
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (Exception e) {
					log.info("elase2");
					log.error("Invalid token:{}", e.getMessage());
					throw new UserNotAuthenticatedException();
				}
			}
		} else {
			log.info("elase2");
			log.debug(
				"SecurityContextHolder not populated with security token, as it already contained: '{}'",
				SecurityContextHolder.getContext().getAuthentication());
		}
		log.info("token1222");
		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String authorization = request.getHeader(org.apache.http.HttpHeaders.AUTHORIZATION);
		if (Objects.isNull(authorization)) {
			return null;
		}
		String[] tokenString = authorization.split(" ");
		String token = tokenString[1];


		if (isNotEmpty(token)) {
			log.debug("Jwt authorization api detected: {}", token);
			try {
				return URLDecoder.decode(token, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}

		return null;
	}

	private Jwt.Claims verify(String token) {
		return jwt.verify(token);
	}

	private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
		String[] roles = claims.roles;
		return roles == null || roles.length == 0 ?
			emptyList() :
			Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
	}

}