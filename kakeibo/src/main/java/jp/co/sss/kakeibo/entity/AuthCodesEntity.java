package jp.co.sss.kakeibo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * 認証コード管理テーブル（auth_codes）のエンティティクラス
 */
@Entity
@Table(name = "auth_codes")
public class AuthCodesEntity {
	/**
	 * レコード識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_code_id", nullable = false, unique = true)
	private Integer authCodeId;
	/**
	 * 対象ユーザー
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private UsersEntity user;
	/**
	 * 認証コード
	 */
	@Column(name = "code", nullable = false, unique = true, length = 6)
	private String code;
	/**
	 * email
	 */
	@Column(name = "purpose", nullable = false, length = 20)
	private String purpose;
	/**
	 * 有効期限
	 */
	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;
	/**
	 * 使用済み判定
	 */
	@Column(name = "used_at", unique = true)
	private LocalDateTime usedAt;
	/**
	 * 回数制限
	 */
	@Column(name = "attempt_count", nullable = false)
	private Integer attemptCount;
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
	public AuthCodesEntity() {
	}
	
	/**
	 * レコード識別を取得する
	 */
	public Integer getAuthCodeId() {
		return authCodeId;
	}
	/**
	 * レコード識別を設定する
	 */
	public void setAuthCodeId(Integer authCodeId) {
		this.authCodeId = authCodeId;
	}
	/**
	 * 対象ユーザーを取得する
	 */
	public UsersEntity getUser() {
		return user;
	}
	/**
	 * 対象ユーザーを設定する
	 */
	public void setUser(UsersEntity user) {
		this.user = user;
	}
	/**
	 * 認証コードを取得する
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 認証コードを設定する
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * emailを取得する
	 */
	public String getPurpose() {
		return purpose;
	}
	/**
	 * emailを設定する
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	/**
	 * 有効期限を取得する
	 */
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	/**
	 * 有効期限を設定する
	 */
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	/**
	 * 使用済み判定を取得する
	 */
	public LocalDateTime getUsedAt() {
		return usedAt;
	}
	/**
	 * 使用済み判定を設定する
	 */
	public void setUsedAt(LocalDateTime usedAt) {
		this.usedAt = usedAt;
	}
	/**
	 * 回数制限を取得する
	 */
	public Integer getAttemptCount() {
		return attemptCount;
	}
	/**
	 * 回数制限を設定する
	 */
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
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