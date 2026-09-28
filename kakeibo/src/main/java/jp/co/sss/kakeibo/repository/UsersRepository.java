package jp.co.sss.kakeibo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.kakeibo.entity.UsersEntity;

/**
 * ユーザーテーブル（users）アクセス用リポジトリインターフェース
 */
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
	/**
	 * 指定したメールアドレスに対応するユーザーを取得する
	 */
	UsersEntity findByEmail(String email);
}