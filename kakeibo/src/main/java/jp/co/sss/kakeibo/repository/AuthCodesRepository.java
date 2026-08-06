package jp.co.sss.kakeibo.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.kakeibo.entity.AuthCodesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

@Repository
public interface AuthCodesRepository extends JpaRepository<AuthCodesEntity, Integer> {
	@Query("SELECT a FROM AuthCodesEntity a WHERE a.user = :user AND a.purpose = :purpose AND a.usedAt IS NULL AND a.expiresAt > :now")
	AuthCodesEntity findValidAuthCode(
		@Param("user") UsersEntity user,
		@Param("purpose") String purpose,
		@Param("now") LocalDateTime now
	);
	AuthCodesEntity findLatestByUserAndPurpose(
		UsersEntity user,
		String purpose
	);
}