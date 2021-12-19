package org.slams.server.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class CorsFilter extends OncePerRequestFilter {

	@Value("${slam.cors.allow}")
	private String allowOrigin;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", allowOrigin);
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
		response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			filterChain.doFilter(request, response);
		}
	}
}