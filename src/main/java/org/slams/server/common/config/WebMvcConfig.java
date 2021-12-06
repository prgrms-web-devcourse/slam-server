package org.slams.server.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins("*")
			.allowedMethods(
				HttpMethod.POST.name(),
				HttpMethod.GET.name(),
				HttpMethod.DELETE.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.PUT.name()
			);
	}
}