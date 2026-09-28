package jp.co.sss.kakeibo.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.kakeibo.entity.AuthCodesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

/**
 * 認証コードテーブル（auth_codes）アクセス用リポジトリインターフェイス
 */
@Repository
public interface AuthCodesRepository extends JpaRepository<AuthCodesEntity, Integer> {
	/**
	 * 【処理】指定したユーザー、認証目的、現在日時条件に基づく未使用かつ有効期限ないの認証コードを取得する
	 */
	@Query("SELECT a FROM AuthCodesEntity a WHERE a.user = :user AND a.purpose = :purpose AND a.usedAt IS NULL AND a.expiresAt > :now")
	AuthCodesEntity findValidAuthCode(
		@Param("user") UsersEntity user,
		@Param("purpose") String purpose,
		@Param("now") LocalDateTime now
	);
	
	/**
	 * 【処理】指定したユーザー及び認証目的に該当する最新の認証コードレコードを取得する
	 */
	AuthCodesEntity findLatestByUserAndPurpose(
		UsersEntity user,
		String purpose
	);
}