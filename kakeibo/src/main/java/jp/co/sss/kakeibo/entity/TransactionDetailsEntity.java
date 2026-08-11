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
@Table(name = "transaction_details")
public class TransactionDetailsEntity {
	/*
	 * 明細識別
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_detail_id", nullable = false, unique = true)
	private Integer transactionDetailId;
	/*
	 * 取引識別
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private TransactionsEntity transaction;
	/*
	 * カテゴリ識別
	 */
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false, unique = true)
	private CategoriesEntity category;
	/*
	 * 収入or支出
	 */
	@Column(name = "type", nullable = false, length = 10)
	private String type;
	/*
	 * 商品名or項目
	 */
	@Column(name = "item_name", nullable = false, length = 255)
	private String itemName;
	/*
	 * 金額
	 */
	@Column(name = "amount", nullable = false, precision = 12, scale = 2)
	private Integer amount;
	/*
	 * 画像
	 */
	@Column(name = "file", nullable = false, unique = true, length = 255)
	private String file;
	/*
	 * メモ
	 */
	@Column(name = "memo", length = 255)
	private String memo;
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
	public TransactionDetailsEntity() {
	}
	/*
	 * get＆set
	 */
	public Integer getTransactionDetailId() {
		return transactionDetailId;
	}
	public void setTransactionDetailId(Integer transactionDetailId) {
		this.transactionDetailId = transactionDetailId;
	}
	public TransactionsEntity getTransaction() {
		return transaction;
	}
	public void setTransaction(TransactionsEntity transaction) {
		this.transaction = transaction;
	}
	public CategoriesEntity getCategory() {
		return category;
	}
	public void setCategory(CategoriesEntity category) {
		this.category = category;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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