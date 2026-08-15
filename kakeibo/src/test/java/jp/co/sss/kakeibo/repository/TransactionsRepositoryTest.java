package jp.co.sss.kakeibo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.co.sss.kakeibo.entity.TransactionsEntity;

@ExtendWith(MockitoExtension.class)
class TransactionsRepositoryTest {
	@Mock
	private TransactionsRepository transactionsRepository;
	@Test
	@DisplayName("saveおよびfindById: 取引データの保存と主キー検索ができること")
	void testSaveAndFindById() {
		//準備
		TransactionsEntity transaction = new TransactionsEntity();
		transaction.setStoreName("スーパー〇〇");
		when(transactionsRepository.save(transaction)).thenReturn(transaction);
		when(transactionsRepository.findById(1)).thenReturn(Optional.of(transaction));
		//実行
		TransactionsEntity saved = transactionsRepository.save(transaction);
		TransactionsEntity found = transactionsRepository.findById(1).orElse(null);
		//検証
		assertNotNull(saved);
		assertNotNull(found);
		assertEquals("スーパー〇〇", found.getStoreName());
	}
}