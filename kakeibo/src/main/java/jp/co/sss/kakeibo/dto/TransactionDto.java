package jp.co.sss.kakeibo.dto;

import jp.co.sss.kakeibo.entity.TransactionsEntity;


/**
 * 【データ転送】収支エンティティと集計額（収入合計、支出合計、差額）を保持するDTOクラス
 */
public class TransactionDto {
	// TransactionsEntityクラスのインスタンスを自動注入（DI）する
	private TransactionsEntity transaction;
	// 収入合計金額
	private int incomeTotal;
	// 支出合計金額
	private int expenseTotal;
	// 収支差額（収入 - 支出）
	private int balance;
	
	/**
	 * コンストラクタ
	 */
	public TransactionDto(
			TransactionsEntity transaction,
			int incomeTotal,
			int expenseTotal,
			int balance
	) {
		// 収支エンティティをフィールドに設定する
		this.transaction = transaction;
		// 収入合計金額をフィールドに設定する
		this.incomeTotal = incomeTotal;
		// 支出合計金額をフィールドに設定する
		this.expenseTotal = expenseTotal;
		// 収支差額をフィールドに設定する
		this.balance = balance;
	}
	
	/**
	 * 収支エンティティを取得する
	 */
	public TransactionsEntity getTransaction() {
		// 収支エンティティを返す
		return transaction;
	}
	
	/**
	 * 収入合計金額を取得する
	 */
	public int getIncomeTotal() {
		// 収入合計金額を返す
		return incomeTotal;
	}
	
	/**
	 * 支出合計金額を取得する
	 */
	public int getExpenseTotal() {
		// 支出合計金額を返す
		return expenseTotal;
	}
	
	/**
	 * 収支差額を取得する
	 */
	public int getBalance() {
		// 収支差額を返す
		return balance;
	}
}