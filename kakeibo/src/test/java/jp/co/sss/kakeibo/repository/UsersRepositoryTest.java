package jp.co.sss.kakeibo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.sss.kakeibo.entity.UsersEntity;

@ExtendWith(MockitoExtension.class)
class UsersRepositoryTest {
	@Mock
	private UsersRepository usersRepository;
	@Test
	@DisplayName("findByEmail: 存在するメールアドレスでユーザーを取得できること")
	void testFindByEmailSuccess() {
		//準備
		UsersEntity expectedUser = new UsersEntity();
		expectedUser.setEmail("test@example.com");
		when(usersRepository.findByEmail("test@example.com")).thenReturn(expectedUser);
		//実行
		UsersEntity found = usersRepository.findByEmail("test@example.com");
		//検証
		assertNotNull(found);
		assertEquals("test@example.com", found.getEmail());
	}
	@Test
	@DisplayName("findByEmail: 存在しないメールアドレスの場合はnullが返ること")
	void testFindByEmailNotFound() {
		//準備
		when(usersRepository.findByEmail("notfound@example.com")).thenReturn(null);
		//実行
		UsersEntity found = usersRepository.findByEmail("notfound@example.com");
		//検証
		assertNull(found);
	}
}