package jp.co.sss.kakeibo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Integer> {
	Page<TransactionsEntity> findByUserOrderByDateAscTransactionIdAsc(UsersEntity user, Pageable pageable);
}