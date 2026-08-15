package jp.co.sss.kakeibo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthLoginControllerTest {
	@Mock
	private AuthService authService;
	@InjectMocks
	private AuthLoginController authLoginController;
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(authLoginController).build();
	}
	@Test
	@DisplayName("認証画面表示: tempUserがない場合は /user/login にリダイレクト")
	void testAuthLoginShowWithoutTempUser() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/auth/login"));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));
	}
	@Test
	@DisplayName("認証画面表示: tempUserがある場合は auth/login を表示")
	void testAuthLoginShowWithTempUser() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("tempUser", new UsersEntity());
		//実行
		ResultActions result = mockMvc.perform(get("/auth/login").session(session));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("auth/login"));
	}
	@Test
	@DisplayName("認証処理: 認証コード一致で /balance/top にリダイレクト")
	void testAuthLoginProcessVerifySuccess() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		UsersEntity tempUser = new UsersEntity();
		tempUser.setUserId(1);
		session.setAttribute("tempUser", tempUser);
		session.setAttribute("authCode", "123456");
		session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
		when(authService.verifyLoginAuthCode(eq("123456"), eq("123456"), any())).thenReturn(true);
		//実行
		ResultActions result = mockMvc.perform(post("/auth/login").session(session).param("code", "123456").param("action", "verify"));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/balance/top"));
	}
	@Test
	@DisplayName("認証処理: コード再送(resend)アクション実行時に新しいコードを発行してメール送信すること")
	void testAuthLoginProcessResend() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		UsersEntity tempUser = new UsersEntity();
		tempUser.setEmail("test@example.com");
		session.setAttribute("tempUser", tempUser);
		when(authService.generateAuthCode()).thenReturn("654321");
		//実行
		ResultActions result = mockMvc.perform(post("/auth/login").session(session).param("action", "resend"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("auth/login")).andExpect(model().attribute("message", "認証コードを再送しました。"));
		verify(authService).sendEmail("test@example.com", "654321");
	}
}