package jp.co.sss.kakeibo.dto;

import jp.co.sss.kakeibo.entity.TransactionsEntity;

public class TransactionDto {
	private TransactionsEntity transaction;
	private int incomeTotal;
	private int expenseTotal;
	private int balance;
	public TransactionDto(TransactionsEntity transaction, int incomeTotal, int expenseTotal, int balance) {
		this.transaction = transaction;
		this.incomeTotal = incomeTotal;
		this.expenseTotal = expenseTotal;
		this.balance = balance;
	}
	public TransactionsEntity getTransaction() {
		return transaction;
	}
	public int getIncomeTotal() {
		return incomeTotal;
	}
	public int getExpenseTotal() {
		return expenseTotal;
	}
	public int getBalance() {
		return balance;
	}
}