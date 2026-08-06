package jp.co.sss.kakeibo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	UsersEntity findByEmail(String email);
}