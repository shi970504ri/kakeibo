package jp.co.sss.kakeibo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
class CategoryApiControllerTest {
	@Mock
	private CategoriesRepository categoriesRepository;
	@Mock
	private UsersRepository usersRepository;
	@Mock
	private HttpSession session;
	@InjectMocks
	private CategoryApiController categoryApiController;
	@Test
	@DisplayName("getCategories: 未ログイン時は空リストが返ること")
	void testGetCategoriesUnauthenticated() {
		//準備
		when(session.getAttribute("userId")).thenReturn(null);
		//実行
		List<CategoriesEntity> result = categoryApiController.getCategories();
		//検証
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
	@Test
	@DisplayName("getCategories: ログイン時はユーザーのカテゴリー一覧が返ること")
	void testGetCategoriesSuccess() {
		//準備
		when(session.getAttribute("userId")).thenReturn(1);
		CategoriesEntity category = new CategoriesEntity();
		category.setName("食費");
		when(categoriesRepository.findByUser(any(UsersEntity.class))).thenReturn(List.of(category));
		//実行
		List<CategoriesEntity> result = categoryApiController.getCategories();
		//検証
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("食費", result.get(0).getName());
	}
	@Test
	@DisplayName("addCategory: 未ログイン時は401 Unauthorizedが返ること")
	void testAddCategoryUnauthenticated() {
		//準備
		when(session.getAttribute("userId")).thenReturn(null);
		Map<String, String> body = Map.of("name", "食費", "type", "expense");
		//実行
		ResponseEntity<?> response = categoryApiController.addCategory(body);
		//検証
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals("Unauthorized", response.getBody());
	}
	@Test
	@DisplayName("addCategory: ユーザーが存在しない場合は400 Bad Requestが返ること")
	void testAddCategoryUserNotFound() {
		//準備
		when(session.getAttribute("userId")).thenReturn(1);
		when(usersRepository.findById(1)).thenReturn(Optional.empty());
		Map<String, String> body = Map.of("name", "食費", "type", "expense");
		//実行
		ResponseEntity<?> response = categoryApiController.addCategory(body);
		//検証
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("User not found", response.getBody());
	}
	@Test
	@DisplayName("addCategory: 正常にカテゴリーが保存され200 OKが返ること")
	void testAddCategorySuccess() {
		//準備
		when(session.getAttribute("userId")).thenReturn(1);
		UsersEntity user = new UsersEntity();
		user.setUserId(1);
		when(usersRepository.findById(1)).thenReturn(Optional.of(user));
		Map<String, String> body = Map.of("name", "食費", "type", "expense");
		//実行
		ResponseEntity<?> response = categoryApiController.addCategory(body);
		//検証
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(categoriesRepository, times(1)).save(any(CategoriesEntity.class));
	}
	@Test
	@DisplayName("deleteCategory: 指定IDのカテゴリーが削除され200 OKが返ること")
	void testDeleteCategorySuccess() {
		//準備
		Integer categoryId = 10;
		//実行
		ResponseEntity<?> response = categoryApiController.deleteCategory(categoryId);
		//検証
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(categoriesRepository, times(1)).deleteById(categoryId);
	}
}