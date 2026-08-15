package jp.co.sss.kakeibo.form;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionFormTest {
	@Test
	@DisplayName("TransactionForm: 初期状態でdetailsリストがインスタンス化されているか確認")
	void testInitialDetailsList() {
		//準備
		TransactionForm form = new TransactionForm();
		//実行
		List<TransactionDetailForm> details = form.getDetails();
		//検証
		assertNotNull(form.getDetails());
		assertEquals(0, form.getDetails().size());
	}
	@Test
	@DisplayName("TransactionForm: Getter/Setterおよび明細リスト(details)の設定・取得テスト")
	void testGetterAndSetter() {
		//準備
		TransactionForm form = new TransactionForm();
		LocalDate targetDate = LocalDate.of(2026, 8, 15);
		LocalTime targetTime = LocalTime.of(14, 30);
		String storeName = "コンビニエンスストア";
		String memo = "休日買い物";
		List<TransactionDetailForm> details = new ArrayList<>();
		TransactionDetailForm detail1 = new TransactionDetailForm();
		detail1.setItemName("お茶");
		detail1.setAmount(150);
		TransactionDetailForm detail2 = new TransactionDetailForm();
		detail2.setItemName("サンドイッチ");
		detail2.setAmount(300);
		details.add(detail1);
		details.add(detail2);
		//実行
		form.setTargetDate(targetDate);
		form.setTargetTime(targetTime);
		form.setStoreName(storeName);
		form.setMemo(memo);
		form.setDetails(details);
		//検証
		assertAll("TransactionFormの各プロパティ確認",
			() -> assertEquals(targetDate, form.getTargetDate()),
			() -> assertEquals(targetTime, form.getTargetTime()),
			() -> assertEquals(storeName, form.getStoreName()),
			() -> assertEquals(memo, form.getMemo()),
			() -> assertEquals(2, form.getDetails().size()),
			() -> assertEquals("お茶", form.getDetails().get(0).getItemName()),
			() -> assertEquals("サンドイッチ", form.getDetails().get(1).getItemName())
		);
	}
}