package jp.co.sss.kakeibo.form;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class TransactionForm {
	private Integer transactionId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate targetDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime targetTime;
	private String storeName;
	private String file;
	private String memo;
	private List<TransactionDetailForm> details = new ArrayList<>();
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public LocalDate getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}
	public LocalTime getTargetTime() {
		return targetTime;
	}
	public void setTargetTime(LocalTime targetTime) {
		this.targetTime = targetTime;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
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
	public List<TransactionDetailForm> getDetails() {
		return details;
	}
	public void setDetails(List<TransactionDetailForm> details) {
		this.details = details;
	}
}