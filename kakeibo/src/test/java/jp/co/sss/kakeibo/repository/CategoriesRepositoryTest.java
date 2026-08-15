package jp.co.sss.kakeibo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

@ExtendWith(MockitoExtension.class)
class CategoriesRepositoryTest {
	@Mock
	private CategoriesRepository categoriesRepository;
	@Test
	@DisplayName("findByUser: 指定したユーザーのカテゴリー一覧が取得できること")
	void testFindByUser() {
		//準備
		UsersEntity user = new UsersEntity();
		CategoriesEntity category1 = new CategoriesEntity();
		category1.setName("食費");
		CategoriesEntity category2 = new CategoriesEntity();
		category2.setName("給料");
		List<CategoriesEntity> expectedList = List.of(category1, category2);
		when(categoriesRepository.findByUser(user)).thenReturn(expectedList);
		//実行
		List<CategoriesEntity> result = categoriesRepository.findByUser(user);
		//検証
		assertNotNull(result);
		assertEquals(2, result.size());
	}
}