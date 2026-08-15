package jp.co.sss.kakeibo.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionDetailsEntityTest {
	@Test
	@DisplayName("TransactionDetailsEntity: Getter/Setterおよびリレーション設定テスト")
	void testGetterAndSetter() {
		// 準備
		TransactionDetailsEntity detail = new TransactionDetailsEntity();
		TransactionsEntity transaction = new TransactionsEntity();
		transaction.setTransactionId(10);
		CategoriesEntity category = new CategoriesEntity();
		category.setCategoryId(5);
		Integer transactionDetailId = 100;
		String type = "expense";
		String itemName = "りんご";
		Integer amount = 150;
		String file = "item_image_001.jpg";
		String memo = "青森県産";
		LocalDateTime now = LocalDateTime.now();
		// 実行
		detail.setTransactionDetailId(transactionDetailId);
		detail.setTransaction(transaction);
		detail.setCategory(category);
		detail.setType(type);
		detail.setItemName(itemName);
		detail.setAmount(amount);
		detail.setFile(file);
		detail.setMemo(memo);
		detail.setCreatedAt(now);
		detail.setUpdatedAt(now);
		// 検証
		assertAll("TransactionDetailsEntityの各プロパティ確認",
			() -> assertEquals(transactionDetailId, detail.getTransactionDetailId()),
			() -> assertEquals(transaction, detail.getTransaction()),
			() -> assertEquals(10, detail.getTransaction().getTransactionId()),
			() -> assertEquals(category, detail.getCategory()),
			() -> assertEquals(5, detail.getCategory().getCategoryId()),
			() -> assertEquals(type, detail.getType()),
			() -> assertEquals(itemName, detail.getItemName()),
			() -> assertEquals(amount, detail.getAmount()),
			() -> assertEquals(file, detail.getFile()),
			() -> assertEquals(memo, detail.getMemo()),
			() -> assertEquals(now, detail.getCreatedAt()),
			() -> assertEquals(now, detail.getUpdatedAt())
		);
	}
}