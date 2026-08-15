package jp.co.sss.kakeibo.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersEntityTest {
	@Test
	@DisplayName("usersEntity: Getter/Setterの正常値設定･取得テスト")
	void testGetterAndSetter() {
		//準備
		UsersEntity user = new UsersEntity();
		Integer userId = 1;
		String email = "test@example.com";
		String password = "Passwordad!1";
		LocalDateTime now = LocalDateTime.now();
		//実行
		user.setUserId(userId);
		user.setEmail(email);
		user.setPassword(password);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		//検証
		assertAll("UsersEntityの各プロパティ確認",
			() -> assertEquals(userId, user.getUserId()),
			() -> assertEquals(email, user.getEmail()),
			() -> assertEquals(password, user.getPassword()),
			() -> assertEquals(now, user.getCreatedAt()),
			() -> assertEquals(now, user.getUpdatedAt())
		);
	}
}