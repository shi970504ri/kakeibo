package jp.co.sss.kakeibo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ユーザー管理テーブル（users）のエンティティクラス
 */
@Entity
@Table(name = "users")
public class UsersEntity {
	/**
	 * ユーザー識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, unique = true)
	private Integer userId;
	/**
	 * メールアドレス
	 */
	@Column(name = "email", nullable = false, unique = true, length = 255)
	private String email;
	/**
	 * ユーザー名
	 */
	@Column(name = "nickname", nullable = false, length = 20)
	private String nickname;
	/**
	 * パスワード
	 */
	@Column(name = "password", nullable = false, length = 255)
	private String password;
	/**
	 * 作成日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	/**
	 * 更新日時
	 */
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	/**
	 * デフォルトコンストラクタ
	 */
	public UsersEntity() {
	}
	
	/**
	 * ユーザー識別を取得する
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * ユーザー識別を設定する
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * メールアドレスを取得する
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * メールアドレスを設定する
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * ユーザー名を取得する
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * ユーザー名を設定する
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * パスワードを取得する
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * パスワードを設定する
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 作成日時を取得する
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	/**
	 * 作成日時を設定する
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	/**
	 * 更新日時を取得する
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	/**
	 * 更新日時を設定する
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}