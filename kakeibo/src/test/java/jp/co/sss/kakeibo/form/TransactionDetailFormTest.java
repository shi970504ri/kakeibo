package jp.co.sss.kakeibo.form;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionDetailFormTest {

	@Test
	@DisplayName("TransactionDetailForm: Getter/Setterの正常値設定・取得テスト")
	void testGetterAndSetter() {
		//準備
		TransactionDetailForm detailForm = new TransactionDetailForm();
		String type = "expense";
		Integer amount = 1200;
		Integer categoryId = 3;
		String file = "sample_receipt.png";
		String memo = "お弁当代";
		String itemName = "幕の内弁当";
		//実行
		detailForm.setType(type);
		detailForm.setAmount(amount);
		detailForm.setCategoryId(categoryId);
		detailForm.setFile(file);
		detailForm.setMemo(memo);
		detailForm.setItemName(itemName);
		//検証
		assertAll("TransactionDetailFormの各プロパティ確認",
			() -> assertEquals(type, detailForm.getType()),
			() -> assertEquals(amount, detailForm.getAmount()),
			() -> assertEquals(categoryId, detailForm.getCategoryId()),
			() -> assertEquals(file, detailForm.getFile()),
			() -> assertEquals(memo, detailForm.getMemo()),
			() -> assertEquals(itemName, detailForm.getItemName())
		);
	}
}