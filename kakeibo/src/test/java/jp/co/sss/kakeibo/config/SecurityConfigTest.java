package jp.co.sss.kakeibo.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class SecurityConfigTest {
	@Test
	@DisplayName("SecurityConfig: PasswordEncoderが生成され、パスワードの暗号化と照合ができるか確認")
	void testPasswordEncoder() {
		//準備
		SecurityConfig config = new SecurityConfig();
		//実行
		PasswordEncoder encoder = config.passwordEncoder();
		String rawPassword = "Passwordad?2";
		String encodedPassword = encoder.encode(rawPassword);
		//検証
		assertNotNull(encoder);
		assertNotEquals(rawPassword, encodedPassword);
		assertTrue(encoder.matches(rawPassword, encodedPassword));
	}
}