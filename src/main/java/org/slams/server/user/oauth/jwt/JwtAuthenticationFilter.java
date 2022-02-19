package org.slams.server.user.oauth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.slams.server.user.exception.UserNotAuthenticatedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;


@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final String headerKey; // header에서 jwt 토큰을 꺼내올 때 사용
	private final Jwt jwt;
	private final ServletContext servletContext;

	public JwtAuthenticationFilter(String headerKey, Jwt jwt, ServletContext servletContext) {
		this.headerKey = headerKey;
		this.jwt = jwt;
		this.servletContext = servletContext;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
						 FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (Objects.nonNull(request.getParameter("redirect_uri"))) {
			servletContext.setAttribute("redirectUri", request.getParameter("redirect_uri"));
		}

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String token = getToken(request);
			if (token != null) {
				try {
					Jwt.Claims claims = verify(token);
					log.debug("Jwt parse result: {}", claims);

					Long userId = claims.userId;
					List<GrantedAuthority> authorities = getAuthorities(claims);

					if (userId != null && authorities.size() > 0) {
						JwtAuthenticationToken authentication =
							new JwtAuthenticationToken(new JwtAuthentication(token, userId), null, authorities);
						authentication.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				} catch (Exception e) {
					log.error("Invalid token:{}", e.getMessage());
					throw new UserNotAuthenticatedException();
				}
			}
		} else {
			log.debug(
				"SecurityContextHolder not populated with security token, as it already contained: '{}'",
				SecurityContextHolder.getContext().getAuthentication());
		}

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