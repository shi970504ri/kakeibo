package jp.co.sss.kakeibo.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoriesEntityTest {
	@Test
	@DisplayName("CategoriesEntity: Getter/Setterの正常値設定･取得テスト")
	void testGetterAndSetter() {
		//準備
		CategoriesEntity category = new CategoriesEntity();
		UsersEntity user = new UsersEntity();
		user.setUserId(1);
		Integer categoryId = 5;
		String name = "食費";
		String type = "expense";
		LocalDateTime now = LocalDateTime.now();
		//実行
		category.setCategoryId(categoryId);
		category.setUser(user);
		category.setName(name);
		category.setType(type);
		category.setCreatedAt(now);
		category.setUpdatedAt(now);
		//検証
		assertAll("CategoriesEntityの各プロパティ確認",
			() -> assertEquals(categoryId, category.getCategoryId()),
			() -> assertEquals(user, category.getUser()),
			() -> assertEquals(name, category.getName()),
			() -> assertEquals(type, category.getType()),
			() -> assertEquals(now, category.getCreatedAt()),
			() -> assertEquals(now, category.getUpdatedAt())
		);
	}
}