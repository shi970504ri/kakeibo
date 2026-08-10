package jp.co.sss.kakeibo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
	List<CategoriesEntity> findByUser(UsersEntity user);
}