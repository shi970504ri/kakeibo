package jp.co.sss.kakeibo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;

@ExtendWith(MockitoExtension.class)
class TransactionDetailsRepositoryTest {
	@Mock
	private TransactionDetailsRepository transactionDetailsRepository;
	@Test
	@DisplayName("saveおよびfindById: 明細データの保存と主キー検索ができること")
	void testSaveAndFindById() {
		//準備
		TransactionDetailsEntity detail = new TransactionDetailsEntity();
		detail.setAmount(500);
		detail.setItemName("お弁当");
		when(transactionDetailsRepository.save(detail)).thenReturn(detail);
		when(transactionDetailsRepository.findById(1)).thenReturn(Optional.of(detail));
		//実行
		TransactionDetailsEntity saved = transactionDetailsRepository.save(detail);
		TransactionDetailsEntity found = transactionDetailsRepository.findById(1).orElse(null);
		//検証
		assertNotNull(saved);
		assertNotNull(found);
		assertEquals(500, found.getAmount());
		assertEquals("お弁当", found.getItemName());
	}
}