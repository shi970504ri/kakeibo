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
class AuthRegisterControllerTest {
	@Mock
	private AuthService authService;
	@InjectMocks
	private AuthRegisterController authRegisterController;
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(authRegisterController).build();
	}
	@Test
	@DisplayName("登録認証画面表示: tempUserがない場合は /user/register にリダイレクト")
	void testAuthRegisterShowWithoutTempUser() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/auth/register"));
		//検証
		result.andExpect(status().is3xxRedirection())
			  .andExpect(redirectedUrl("/user/register"));
	}
	@Test
	@DisplayName("登録認証画面表示: tempUserがある場合は auth/register ビューを返すこと")
	void testAuthRegisterShowWithTempUser() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("tempUser", new UsersEntity());
		//実行
		ResultActions result = mockMvc.perform(get("/auth/register").session(session));
		//検証
		result.andExpect(status().isOk())
			  .andExpect(view().name("auth/register"));
	}
	@Test
	@DisplayName("登録認証処理: コード再送(resend)時に新しいコードを送信して画面を表示すること")
	void testAuthRegisterProcessResend() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		UsersEntity tempUser = new UsersEntity();
		tempUser.setEmail("test@example.com");
		session.setAttribute("tempUser", tempUser);
		when(authService.generateAuthCode()).thenReturn("654321");
		//実行
		ResultActions result = mockMvc.perform(post("/auth/register").session(session).param("action", "resend"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("auth/register")).andExpect(model().attribute("message", "認証コードを再送しました。"));
		verify(authService).sendEmail("test@example.com", "654321");
	}
	@Test
	@DisplayName("登録認証処理: 認証成功時にセッション情報を削除し /user/login へリダイレクト")
	void testAuthRegisterProcessSuccess() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		UsersEntity tempUser = new UsersEntity();
		session.setAttribute("tempUser", tempUser);
		session.setAttribute("authCode", "123456");
		session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
		when(authService.completeRegistration(eq(tempUser), eq("123456"), eq("123456"), any())).thenReturn(true);
		//実行
		ResultActions result = mockMvc.perform(post("/auth/register").session(session).param("code", "123456").param("action", "verify"));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));
	}
	@Test
	@DisplayName("登録認証処理: 認証失敗時にエラーメッセージを設定して auth/register へ戻ること")
	void testAuthRegisterProcessFailure() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		UsersEntity tempUser = new UsersEntity();
		session.setAttribute("tempUser", tempUser);
		when(authService.completeRegistration(any(), any(), any(), any())).thenReturn(false);
		//実行
		ResultActions result = mockMvc.perform(post("/auth/register").session(session).param("code", "000000").param("action", "verify"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("auth/register")).andExpect(model().attributeExists("error"));
	}
}