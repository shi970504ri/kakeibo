package jp.co.sss.kakeibo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * 取引管理テーブル（transactions）のエンティティクラス
 */
@Entity
@Table(name = "transactions")
public class TransactionsEntity {
	/**
	 * 取引識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id", nullable = false, unique = true)
	private Integer transactionId;
	/**
	 * 対象ユーザー
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UsersEntity user;
	/**
	 * 取引日
	 */
	@Column(name = "date", nullable = false)
	private LocalDate date;
	/**
	 * 店名
	 */
	@Column(name = "store_name", nullable = false, length = 255)
	private String storeName;
	/**
	 * 画像
	 */
	@Column(name = "file", nullable = false, unique = true, length = 255)
	private String file;
	/**
	 * メモ
	 */
	@Column(name = "memo", length = 255)
	private String memo;
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
	@OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransactionDetailsEntity> details;
	
	/**
	 * デフォルトコンストラクタ
	 */
	public TransactionsEntity() {
	}
	
	/**
	 * 取引識別を取得する
	 */
	public Integer getTransactionId() {
		return transactionId;
	}
	/**
	 * 取引識別を設定する
	 */
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
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
	 * 取引識別を取得する
	 */
	public LocalDate getDate() {
		return date;
	}
	/**
	 * 取引識別を設定する
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	/**
	 * 店舗名を取得する
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * 店舗名を設定する
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * 画像を取得する
	 */
	public String getFile() {
		return file;
	}
	/**
	 * 画像を設定する
	 */
	public void setFile(String file) {
		this.file = file;
	}
	/**
	 * メモを取得する
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * メモを設定する
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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
	/**
	 * 取引明細フォームリストを取得する
	 */
	public List<TransactionDetailsEntity> getDetails() {
		return details;
	}
	/**
	 * 取引明細フォームリストを設定する
	 */
	public void setDetails(List<TransactionDetailsEntity> details) {
		this.details = details;
	}
}