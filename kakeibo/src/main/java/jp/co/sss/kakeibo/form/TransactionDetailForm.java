package jp.co.sss.kakeibo.form;

/**
 * 収支明細入力用のフォームクラス
 */
public class TransactionDetailForm {
	/**
	 * 明細ID
	 */
	private Integer transactionDetailId;
	/**
	 * 収支タイプ
	 */
	private String type;
	/**
	 * 金額
	 */
	private Integer amount;
	/**
	 * カテゴリID
	 */
	private Integer categoryId;
	/**
	 * メモ
	 */
	private String memo;
	/**
	 * 品名
	 */
	private String itemName;
	
	/**
	 * 明細IDを取得する
	 */
	public Integer getTransactionDetailId() {
		return transactionDetailId;
	}
	/**
	 * 明細IDを設定する
	 */
	public void setTransactionDetailId(Integer transactionDetailId) {
		this.transactionDetailId = transactionDetailId;
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
	 * カテゴリIDを取得する
	 */
	public Integer getCategoryId() {
		return categoryId;
	}
	/**
	 * カテゴリIDを設定する
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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
}