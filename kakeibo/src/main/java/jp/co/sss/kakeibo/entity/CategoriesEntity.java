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
@Table(name = "categories")
public class CategoriesEntity {
	/*
	 * カテゴリ名識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, unique = true)
	private Integer categoryId;
	/*
	 * 対象ユーザー
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UsersEntity user;
	/*
	 * カテゴリ名
	 */
	@Column(name = "name", nullable = false, unique = true, length = 255)
	private String name;
	/*
	 * 収入or支出
	 */
	@Column(name = "type", nullable = false, length = 10)
	private String type;
	/*
	 * 作成日時
	 */
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	/*
	 * 更新日時
	 */
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	public CategoriesEntity() {
	}
	/*
	 * get＆set	
	 */
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public UsersEntity getUser() {
		return user;
	}
	public void setUser(UsersEntity user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}