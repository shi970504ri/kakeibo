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

@Entity
@Table(name = "auth_codes")
public class AuthCodesEntity {
	/*
	 * レコード識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_code_id", nullable = false, unique = true)
	private Integer authCodeId;
	/*
	 * 対象ユーザー
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private UsersEntity user;
	/*
	 * 認証コード
	 */
	@Column(name = "code", nullable = false, unique = true, length = 6)
	private String code;
	/*
	 * email/sms
	 */
	@Column(name = "purpose", nullable = false, length = 20)
	private String purpose;
	/*
	 * 有効期限
	 */
	@Column(name = "expires_at", nullable = false)
	private LocalDateTime expiresAt;
	/*
	 * 使用済み判定
	 */
	@Column(name = "used_at", unique = true)
	private LocalDateTime usedAt;
	/*
	 * 回数制限
	 */
	@Column(name = "attempt_count", nullable = false)
	private Integer attemptCount;
	/*
	 * 作成日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	public AuthCodesEntity() {
	}
	/*
	 * get＆set
	 */
	public Integer getAuthCodeId() {
		return authCodeId;
	}
	public void setAuthCodeId(Integer authCodeId) {
		this.authCodeId = authCodeId;
	}
	public UsersEntity getUser() {
		return user;
	}
	public void setUser(UsersEntity user) {
		this.user = user;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}
	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	public LocalDateTime getUsedAt() {
		return usedAt;
	}
	public void setUsedAt(LocalDateTime usedAt) {
		this.usedAt = usedAt;
	}
	public Integer getAttemptCount() {
		return attemptCount;
	}
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}