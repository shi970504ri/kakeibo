package jp.co.sss.kakeibo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * セキュリティ関連設定クラス
 * 
 * 【設定】アプリケーション内で使用する暗号化処理等のBean定義を行う
 */
@Configuration
public class SecurityConfig {
	/**
	 * 
	 * パスワードのハッシュ化及び照合を行うPasswordEncoderをBean定義する
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		// BCryptPasswordEncoder（パスワードハッシュ化器）を生成する
		return new BCryptPasswordEncoder();
	}
}