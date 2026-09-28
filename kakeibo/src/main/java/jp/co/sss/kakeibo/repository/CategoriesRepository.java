package jp.co.sss.kakeibo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;

/**
 * カテゴリテーブル（categories）アクセス用リポジトリインターフェイス
 */
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
	/**
	 * 【処理】対象ユーザーのカテゴリ一覧を取得する
	 */
	List<CategoriesEntity> findByUser(UsersEntity user);
	
	/**
	 * 対象ユーザー及び収支タイプに該当するカテゴリ一覧を取得する
	 */
	List<CategoriesEntity> findByUserAndType(UsersEntity user, String type);
}