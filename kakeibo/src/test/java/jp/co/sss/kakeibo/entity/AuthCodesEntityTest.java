package jp.co.sss.kakeibo.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthCodesEntityTest {
	@Test
	@DisplayName("AuthCodesEntity: Getter/SetterおよびUsersEntityとのリレーション設定テスト")
	void testGetterAndSetter() {
		//準備
		AuthCodesEntity authCode = new AuthCodesEntity();
		UsersEntity user = new UsersEntity();
		user.setUserId(1);
		Integer authCodeId = 100;
		String code = "123456";
		String purpose = "REGISTER";
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expiresAt = now.plusMinutes(15);
		LocalDateTime usedAt = now.plusMinutes(5);
		Integer attemptCount = 1;
		//実行
		authCode.setAuthCodeId(authCodeId);
		authCode.setUser(user);
		authCode.setCode(code);
		authCode.setPurpose(purpose);
		authCode.setExpiresAt(expiresAt);
		authCode.setUsedAt(usedAt);
		authCode.setAttemptCount(attemptCount);
		authCode.setCreatedAt(now);
		authCode.setUpdatedAt(now);
		//検証
		assertAll("AuthCodesEntityの各プロパティ確認",
			() -> assertEquals(authCodeId, authCode.getAuthCodeId()),
			() -> assertEquals(user, authCode.getUser()),
			() -> assertEquals(1, authCode.getUser().getUserId()),
			() -> assertEquals(code, authCode.getCode()),
			() -> assertEquals(purpose, authCode.getPurpose()),
			() -> assertEquals(expiresAt, authCode.getExpiresAt()),
			() -> assertEquals(usedAt, authCode.getUsedAt()),
			() -> assertEquals(attemptCount, authCode.getAttemptCount()),
			() -> assertEquals(now, authCode.getCreatedAt()),
			() -> assertEquals(now, authCode.getUpdatedAt())
		);
	}
}