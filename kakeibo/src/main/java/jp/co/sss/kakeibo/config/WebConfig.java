package jp.co.sss.kakeibo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jp.co.sss.kakeibo.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	public LoginInterceptor loginInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor).addPathPatterns(
			"/another/terms",
			"/another/privacy",
			"/another/disclaimer",
			"/balance/top",
			"/balance/om@it",
			"/balance/edit",
			"/balance/calendar",
			"/balance/compaison",
			"/balance/category",
			"/balance/mypage",
			"/api/**"
		);
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
		String uploadPath = uploadDir.toUri().toString();
		if (!uploadPath.endsWith("/")) {
			uploadPath += "/";
		}
		registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath);
	}
}