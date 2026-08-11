package jp.co.sss.kakeibo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.TransactionsEntity;

public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Integer> {
}