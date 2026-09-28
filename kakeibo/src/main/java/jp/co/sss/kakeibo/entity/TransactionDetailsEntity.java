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
 * 明細管理テーブル（transaction_details）のエンティティクラス
 */
@Entity
@Table(name = "transaction_details")
public class TransactionDetailsEntity {
	/**
	 * 明細識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_detail_id", nullable = false, unique = true)
	private Integer transactionDetailId;
	/**
	 * 取引識別
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private TransactionsEntity transaction;
	/**
	 * カテゴリ識別
	 */
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private CategoriesEntity category;
	/**
	 * 収支タイプ
	 */
	@Column(name = "type", nullable = false, length = 10)
	private String type;
	/**
	 * 商品名or項目
	 */
	@Column(name = "item_name", nullable = false, length = 255)
	private String itemName;
	/**
	 * 金額
	 */
	@Column(name = "amount", nullable = false, precision = 12, scale = 2)
	private Integer amount;
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
	
	/**
	 * デフォルトコンストラクタ
	 */
	public TransactionDetailsEntity() {
	}
	
	/**
	 * 明細識別を取得する
	 */
	public Integer getTransactionDetailId() {
		return transactionDetailId;
	}
	
	/**
	 * 明細識別を設定する
	 */
	public void setTransactionDetailId(Integer transactionDetailId) {
		this.transactionDetailId = transactionDetailId;
	}
	/**
	 * 取引識別を取得する
	 */
	public TransactionsEntity getTransaction() {
		return transaction;
	}
	/**
	 * 取引識別を設定する
	 */
	public void setTransaction(TransactionsEntity transaction) {
		this.transaction = transaction;
	}
	/**
	 * カテゴリを取得する
	 */
	public CategoriesEntity getCategory() {
		return category;
	}
	/**
	 * カテゴリを設定する
	 */
	public void setCategory(CategoriesEntity category) {
		this.category = category;
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
	 * 品名を取得する
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * 品名を設定する
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * 金額を取得する
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * 金額を設定する
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
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
}