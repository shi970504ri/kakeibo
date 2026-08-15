package jp.co.sss.kakeibo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.sss.kakeibo.entity.AuthCodesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

@ExtendWith(MockitoExtension.class)
class AuthCodesRepositoryTest {
	@Mock
	private AuthCodesRepository authCodesRepository;
	@Test
	@DisplayName("findValidAuthCode: 未使用かつ有効期限内の認証コードが取得できること")
	void testFindValidAuthCodeSuccess() {
		//準備
		UsersEntity user = new UsersEntity();
		LocalDateTime now = LocalDateTime.now();
		AuthCodesEntity expectedCode = new AuthCodesEntity();
		expectedCode.setCode("123456");
		when(authCodesRepository.findValidAuthCode(user, "REGISTER", now)).thenReturn(expectedCode);
		//実行
		AuthCodesEntity result = authCodesRepository.findValidAuthCode(user, "REGISTER", now);
		//検証
		assertNotNull(result);
		assertEquals("123456", result.getCode());
	}
	@Test
	@DisplayName("findValidAuthCode: 有効期限切れの場合はnullが返ること")
	void testFindValidAuthCodeExpired() {
		//準備
		UsersEntity user = new UsersEntity();
		LocalDateTime now = LocalDateTime.now();
		when(authCodesRepository.findValidAuthCode(user, "REGISTER", now)).thenReturn(null);
		//実行
		AuthCodesEntity result = authCodesRepository.findValidAuthCode(user, "REGISTER", now);
		//検証
		assertNull(result);
	}
	@Test
	@DisplayName("findLatestByUserAndPurpose: ユーザーと目的を指定して認証コードが取得できること")
	void testFindLatestByUserAndPurpose() {
		//準備
		UsersEntity user = new UsersEntity();
		AuthCodesEntity expectedCode = new AuthCodesEntity();
		expectedCode.setCode("654321");
		when(authCodesRepository.findLatestByUserAndPurpose(user, "REGISTER")).thenReturn(expectedCode);
		//実行
		AuthCodesEntity result = authCodesRepository.findLatestByUserAndPurpose(user, "REGISTER");
		//検証
		assertNotNull(result);
		assertEquals("654321", result.getCode());
	}
}