package jp.co.sss.kakeibo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

/**
 * 収支親テーブル（transactions）アクセス用リポジトリインターフェース
 */
public interface TransactionsRepository extends JpaRepository<TransactionsEntity, Integer> {
	/**
	 * 【処理】対象ユーザーの収支データを日付昇順、取引ID昇順でページ分けして取得する
	 */
	Page<TransactionsEntity> findByUserOrderByDateAscTransactionIdAsc(UsersEntity user, Pageable pageable);
	
	/**
	 * 【処理】対象ユーザーの全収支データを日付昇順で取得する
	 */
	List<TransactionsEntity> findByUserOrderByDateAsc(UsersEntity user);
	
	/**
	 * 【処理】対象ユーザーの指定期間内における収支データを取得する
	 */
	List<TransactionsEntity> findByUserAndDateBetween(UsersEntity user, LocalDate startDate, LocalDate endDate);
	
	/**
	 * 【処理】対象ユーザーの特定日付における収支データを取引ID昇順でページ分けして取得する
	 */
	Page<TransactionsEntity> findByUserAndDateOrderByTransactionIdAsc(UsersEntity user, LocalDate date, Pageable pageable);
}