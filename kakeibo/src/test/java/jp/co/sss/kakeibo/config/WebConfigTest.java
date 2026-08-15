package jp.co.sss.kakeibo.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import jp.co.sss.kakeibo.interceptor.LoginInterceptor;

class WebConfigTest {
	@Test
	@DisplayName("WebConfig: addInterceptorsでLoginInterceptorおよび対象パスパターンが正常に登録されること")
	void testAddInterceptors() {
		//準備
		WebConfig webConfig = new WebConfig();
		LoginInterceptor loginInterceptor = new LoginInterceptor();
		webConfig.loginInterceptor = loginInterceptor;
		InterceptorRegistry registry = mock(InterceptorRegistry.class);
		InterceptorRegistration registration = mock(InterceptorRegistration.class);
		when(registry.addInterceptor(loginInterceptor)).thenReturn(registration);
		//実行
		webConfig.addInterceptors(registry);
		//検証
		verify(registry, times(1)).addInterceptor(loginInterceptor);
		verify(registration, times(1)).addPathPatterns(
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
}