package jp.co.sss.kakeibo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetailsEntity, Integer> {
}