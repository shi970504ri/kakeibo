package jp.co.sss.kakeibo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	Optional<UsersEntity> findByEmail(String email);
	Optional<UsersEntity> findByTel(String tel);
}