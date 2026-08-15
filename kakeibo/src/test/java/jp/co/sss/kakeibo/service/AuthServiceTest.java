package jp.co.sss.kakeibo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@InjectMocks
	private AuthService authService;
	@Mock
	private UsersRepository usersRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JavaMailSender mailSender;
	@Nested
	@DisplayName("isEmailAlreadyRegisteredのテスト")
	class EmailRegisteredTest {
		@Test
		@DisplayName("登録済みメールアドレスの場合、trueを返す")
		void testRegistered() {
			//準備
			when(usersRepository.findByEmail("test@example.com")).thenReturn(new UsersEntity());
			//実行
			boolean result = authService.isEmailAlreadyRegistered("test@example.com");
			//検証
			assertTrue(result);
		}
		@Test
		@DisplayName("未登録メールアドレスの場合、falseを返す")
		void testNotRegistered() {
			//準備
			when(usersRepository.findByEmail("new@example.com")).thenReturn(null);
			//実行
			boolean result = authService.isEmailAlreadyRegistered("new@example.com");
			//検証
			assertFalse(result);
		}
	}
	@Test
	@DisplayName("generateAuthCode: 6桁の数字文字列が生成されること")
	void testGenerateAuthCode() {
		//準備(なし)
		//実行
		String code = authService.generateAuthCode();
		//検証
		assertNotNull(code);
		assertEquals(6, code.length());
		assertTrue(code.matches("\\d{6}"));
	}
	@Test
	@DisplayName("sendEmail: メール送信処理が呼び出されること")
	void testSendEmail() {
		//準備
		String email = "test@example.com";
		String code = "123456";
		//実行
		authService.sendEmail(email, code);
		//検証
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}
	@Nested
	@DisplayName("completeRegistrationのテスト")
	class CompleteRegistrationTest {
		@Test
		@DisplayName("認証コード不一致の場合は登録失敗(false)")
		void testCodeMismatch() {
			//準備
			UsersEntity tempUser = new UsersEntity();
			String sessionCode = "123456";
			String inputCode = "999999";
			LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
			//実行
			boolean result = authService.completeRegistration(tempUser, sessionCode, inputCode, expireTime);
			//検証
			assertFalse(result);
		}
		@Test
		@DisplayName("有効期限切れの場合は登録失敗(false)")
		void testExpired() {
			//準備
			UsersEntity tempUser = new UsersEntity();
			String sessionCode = "123456";
			String inputCode = "123456";
			LocalDateTime expireTime = LocalDateTime.now().minusMinutes(1);
			//実行
			boolean result = authService.completeRegistration(tempUser, sessionCode, inputCode, expireTime);
			//検証
			assertFalse(result);
		}
		@Test
		@DisplayName("メールアドレス重複の場合は登録失敗(false)")
		void testEmailExists() {
			//準備
			UsersEntity tempUser = new UsersEntity();
			tempUser.setEmail("registered@example.com");
			String sessionCode = "123456";
			String inputCode = "123456";
			LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
			when(usersRepository.findByEmail("registered@example.com")).thenReturn(new UsersEntity());
			//実行
			boolean result = authService.completeRegistration(tempUser, sessionCode, inputCode, expireTime);
			//検証
			assertFalse(result);
		}
		@Test
		@DisplayName("正常系: パスワードが暗号化され作成・更新日時がセットされて保存(true)")
		void testSuccess() {
			//準備
			UsersEntity tempUser = new UsersEntity();
			tempUser.setEmail("new@example.com");
			tempUser.setPassword("rawPassword");
			String sessionCode = "123456";
			String inputCode = "123456";
			LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
			when(usersRepository.findByEmail("new@example.com")).thenReturn(null);
			when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
			//実行
			boolean result = authService.completeRegistration(tempUser, sessionCode, inputCode, expireTime);
			//検証
			assertTrue(result);
			assertEquals("encodedPassword", tempUser.getPassword());
			assertNotNull(tempUser.getCreatedAt());
			assertNotNull(tempUser.getUpdatedAt());
			verify(usersRepository, times(1)).save(tempUser);
		}
	}
	@Nested
	@DisplayName("verifyLoginAuthCodeのテスト")
	class VerifyLoginAuthCodeTest {
		@Test
		@DisplayName("認証コード不一致の場合はfalse")
		void testMismatch() {
			//準備
			String sessionCode = "123456";
			String inputCode = "000000";
			LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
			//実行
			boolean result = authService.verifyLoginAuthCode(sessionCode, inputCode, expireTime);
			//検証
			assertFalse(result);
		}
		@Test
		@DisplayName("有効期限がnullの場合はfalse")
		void testNullExpiresAt() {
			//準備
			String sessionCode = "123456";
			String inputCode = "123456";
			LocalDateTime expireTime = null;
			//実行
			boolean result = authService.verifyLoginAuthCode(sessionCode, inputCode, expireTime);
			//検証
			assertFalse(result);
		}
		@Test
		@DisplayName("有効期限切れの場合はfalse")
		void testExpired() {
			//準備
			String sessionCode = "123456";
			String inputCode = "123456";
			LocalDateTime expireTime = LocalDateTime.now().minusMinutes(1);
			//実行
			boolean result = authService.verifyLoginAuthCode(sessionCode, inputCode, expireTime);
			//検証
			assertFalse(result);
		}
		@Test
		@DisplayName("正常系: コード一致かつ有効期限内の場合はtrue")
		void testSuccess() {
			//準備
			String sessionCode = "123456";
			String inputCode = "123456";
			LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
			//実行
			boolean result = authService.verifyLoginAuthCode(sessionCode, inputCode, expireTime);
			//検証
			assertTrue(result);
		}
	}
}