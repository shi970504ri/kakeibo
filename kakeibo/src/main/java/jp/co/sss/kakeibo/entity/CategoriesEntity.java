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
import jakarta.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * カテゴリ管理テーブル（categories）のエンティティクラス
 */
@Entity
@Table(
	name = "categories",
			uniqueConstraints = {
			@UniqueConstraint(columnNames = {"user_id", "type", "name"})
	}
)
public class CategoriesEntity {
	/**
	 * カテゴリ名識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, unique = true)
	private Integer categoryId;
	/**
	 * 対象ユーザー
	 */
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UsersEntity user;
	/**
	 * カテゴリ名
	 */
	@Column(name = "name", nullable = false, length = 255)
	private String name;
	/**
	 * 収支タイプ
	 */
	@Column(name = "type", nullable = false, length = 10)
	private String type;
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
	public CategoriesEntity() {
	}
	
	/**
	 * カテゴリ名識別を取得する	
	 */
	public Integer getCategoryId() {
		return categoryId;
	}
	/**
	 * カテゴリ名識別を設定する	
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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
	 * カテゴリ名を取得する	
	 */
	public String getName() {
		return name;
	}
	/**
	 * カテゴリ名を設定する	
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 収支タイプを取得する	
	 */
	public String getType() {
		return type;
	}
	/**
	 * 収支タイプを設定する	
	 */
	public void setType(String type) {
		this.type = type;
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