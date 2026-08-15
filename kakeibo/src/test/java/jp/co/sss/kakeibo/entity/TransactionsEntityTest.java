package jp.co.sss.kakeibo.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionsEntityTest {
	@Test
	@DisplayName("TransactionsEntity: Getter/Setterの正常値設定･取得テスト")
	void testGetterAndSetter() {
		//準備
		TransactionsEntity transaction = new TransactionsEntity();
		UsersEntity user = new UsersEntity();
		user.setUserId(1);
		Integer transactionId = 10;
		LocalDate date = LocalDate.of(20260, 8, 14);
		String storeName = "スーパー〇〇";
		String itemName = "夕食材料";
		String file = "20260814_0412_スーパー〇〇_夕食食材.jpeg";
		String memo = "特売日";
		LocalDateTime now = LocalDateTime.now();
		//実行
		transaction.setTransactionId(transactionId);
		transaction.setUser(user);
		transaction.setDate(date);
		transaction.setStoreName(storeName);
		transaction.setItemName(itemName);
		transaction.setFile(file);
		transaction.setMemo(memo);
		transaction.setCreatedAt(now);
		transaction.setUpdatedAt(now);
		//検証
		assertAll("TransactionsEntityの各プロパティ確認",
			() -> assertEquals(transactionId, transaction.getTransactionId()),
			() -> assertEquals(user, transaction.getUser()),
			() -> assertEquals(date, transaction.getDate()),
			() -> assertEquals(storeName, transaction.getStoreName()),
			() -> assertEquals(itemName, transaction.getItemName()),
			() -> assertEquals(file, transaction.getFile()),
			() -> assertEquals(memo, transaction.getMemo()),
			() -> assertEquals(now, transaction.getCreatedAt()),
			() -> assertEquals(now, transaction.getUpdatedAt())
		);
	}
}