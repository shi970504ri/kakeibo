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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;
import jp.co.sss.kakeibo.service.AuthService;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
	@Mock
	private UsersRepository usersRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private AuthService authService;
	@InjectMocks
	private LoginController loginController;
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}
	@Test
	@DisplayName("ログイン画面表示: /user/login でビュー 'user/login' を返すこと")
	void testLoginShow() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/user/login"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("user/login"));
	}
	@Test
	@DisplayName("ログイン処理: 認証成功時にセッション設定され /auth/login にリダイレクトされること")
	void testLoginProcessSuccess() throws Exception {
		//準備
		UsersEntity user = new UsersEntity();
		user.setEmail("test@example.com");
		user.setPassword("hashedPassword");
		when(usersRepository.findByEmail("test@example.com")).thenReturn(user);
		when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
		when(authService.generateAuthCode()).thenReturn("123456");
		//実行
		ResultActions result = mockMvc.perform(post("/user/login").param("email", "test@example.com").param("password", "password"));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/auth/login"));
		verify(authService, times(1)).sendEmail("test@example.com", "123456");
	}
	@Test
	@DisplayName("ログイン処理: パスワード不一致時にエラーメッセージを設定してログイン画面へ戻ること")
	void testLoginProcessFailure() throws Exception {
		//準備
		UsersEntity user = new UsersEntity();
		user.setEmail("test@example.com");
		user.setPassword("hashedPassword");
		when(usersRepository.findByEmail("test@example.com")).thenReturn(user);
		when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);
		//実行
		ResultActions result = mockMvc.perform(post("/user/login").param("email", "test@example.com").param("password", "wrongpassword"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("user/login")).andExpect(model().attributeExists("error"));
	}
	@Test
	@DisplayName("ログアウト: セッションを破棄して /user/login にリダイレクトされること")
	void testLogout() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/user/logout"));
		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));
	}
}