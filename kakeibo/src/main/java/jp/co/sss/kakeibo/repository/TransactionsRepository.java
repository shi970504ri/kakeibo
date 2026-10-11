package jp.co.sss.kakeibo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

	/**
	 * 【処理】対象ユーザーの収支を検索条件で絞込み、指定順で取引IDをページ分けして取得する
	 */
	@Query(
			value = """
					SELECT t.transaction_id
					FROM transactions t
					WHERE t.user_id = :userId
							AND (:startDate IS NULL OR t.date >= :startDate)
							AND (:endDate IS NULL OR t.date <= :endDate)
							AND (
									:keyword IS NULL
									OR LOWER(COALESCE(t.store_name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
									OR LOWER(COALESCE(t.memo, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
									OR EXISTS (
											SELECT 1
											FROM transaction_details d
											LEFT JOIN categories c ON c.category_id = d.category_id
											WHERE d.transaction_id = t.transaction_id
													AND (
															LOWER(COALESCE(d.item_name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
															OR LOWER(COALESCE(d.type, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
															OR LOWER(COALESCE(c.name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
															OR LOWER(COALESCE(d.memo, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
													)
									)
							)
					ORDER BY
							CASE WHEN :sort = 'amountAsc' THEN (
									SELECT COALESCE(SUM(d.amount), 0)
									FROM transaction_details d
									WHERE d.transaction_id = t.transaction_id
							) END ASC,
							CASE WHEN :sort = 'amountDesc' THEN (
									SELECT COALESCE(SUM(d.amount), 0)
									FROM transaction_details d
									WHERE d.transaction_id = t.transaction_id
							) END DESC,
							CASE WHEN :sort = 'date' THEN t.date END ASC,
							t.transaction_id ASC
					""",
			countQuery = """
					SELECT COUNT(*)
					FROM transactions t
					WHERE t.user_id = :userId
							AND (:startDate IS NULL OR t.date >= :startDate)
							AND (:endDate IS NULL OR t.date <= :endDate)
							AND (
									:keyword IS NULL
									OR LOWER(COALESCE(t.store_name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
									OR EXISTS (
										SELECT 1
										FROM transaction_details d
										LEFT JOIN categories c ON c.category_id = d.category_id
										WHERE d.transaction_id = t.transaction_id
												AND (
														LOWER(COALESCE(d.item_name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
														OR LOWER(COALESCE(d.type, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
														OR LOWER(COALESCE(c.name, '')) LIKE CONCAT('%', LOWER(:keyword), '%')
												)
										)
									)
							)
					""",
			nativeQuery = true
	)
	Page<Integer> searchTransactionIds(
		@Param("userId") Integer userId,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate,
		@Param("keyword") String keyword,
		@Param("sort") String sort,
		Pageable pageable
	);
}