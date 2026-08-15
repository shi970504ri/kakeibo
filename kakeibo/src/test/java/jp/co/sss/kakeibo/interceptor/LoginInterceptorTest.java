package jp.co.sss.kakeibo.interceptor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class LoginInterceptorTest {
	@Test
	@DisplayName("モーダル表示(modal=true)の場合、未ログインでもアクセスを許可すること")
	void testPreHandleModalTrue() throws Exception {
		//準備
		LoginInterceptor interceptor = new LoginInterceptor();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setParameter("modal", "true");
		//実行
		boolean result = interceptor.preHandle(request, response, new Object());
		//検証
		assertTrue(result);
	}
	@Test
	@DisplayName("ログイン済み(セッションにuserIdあり)の場合、アクセスを許可すること")
	void testPreHandleLoggedIn() throws Exception {
		//準備
		LoginInterceptor interceptor = new LoginInterceptor();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.getSession().setAttribute("userId", 1);
		//実行
		boolean result = interceptor.preHandle(request, response, new Object());
		//検証
		assertTrue(result);
	}
	@Test
	@DisplayName("未ログイン(userIdなし)の場合、ログイン画面にリダイレクトしfalseを返すこと")
	void testPreHandleNotLoggedIn() throws Exception {
		//準備
		LoginInterceptor interceptor = new LoginInterceptor();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContextPath("/kakeibo");
		//実行
		boolean result = interceptor.preHandle(request, response, new Object());
		//検証
		assertFalse(result);
		assertEquals("/kakeibo/user/login", response.getRedirectedUrl());
	}
}