package jp.co.sss.kakeibo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;
import jp.co.sss.kakeibo.entity.TransactionsEntity;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetailsEntity, Integer> {
	List<TransactionDetailsEntity> findByTransaction(TransactionsEntity transaction);
	void deleteByTransaction(TransactionsEntity transaction);
}