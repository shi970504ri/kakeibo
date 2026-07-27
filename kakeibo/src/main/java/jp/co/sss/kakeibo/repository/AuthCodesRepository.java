package jp.co.sss.kakeibo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.AuthCodesEntity;

public interface AuthCodesRepository extends JpaRepository<AuthCodesEntity, Integer> {
	
}