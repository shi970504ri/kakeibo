package jp.co.sss.kakeibo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.kakeibo.service.AuthService;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {
	@Mock
	private AuthService authService;
	@InjectMocks
	private RegisterController registerController;
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
	}
	@Test
	@DisplayName("会員登録画面表示: /user/register で 'user/register' ビューを返すこと")
	void testRegisterShow() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/user/register"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("user/register"));
	}
	@Test
	@DisplayName("会員登録処理: 成功時に認証コードを発行・メール送信し /auth/register にリダイレクト")
	void testRegisterProcessSuccess() throws Exception {
		//準備
		String email = "newuser@example.com";
		String password = "password123";
		when(authService.isEmailAlreadyRegistered(email)).thenReturn(false);
		when(authService.generateAuthCode()).thenReturn("123456");
		//実行
		ResultActions result = mockMvc.perform(post("/user/register").param("email", email).param("password", password));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/auth/register"));
		verify(authService).sendEmail(email, "123456");
	}
	@Test
	@DisplayName("会員登録処理: メール重複時にエラーメッセージをFlash属性に設定し /user/register にリダイレクト")
	void testRegisterProcessEmailDuplicate() throws Exception {
		//準備
		String email = "existing@example.com";
		when(authService.isEmailAlreadyRegistered(email)).thenReturn(true);
		//実行
		ResultActions result = mockMvc.perform(post("/user/register").param("email", email).param("password", "password"));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/register")).andExpect(flash().attribute("error", "このメールアドレスは既に登録されています。"));
	}
}