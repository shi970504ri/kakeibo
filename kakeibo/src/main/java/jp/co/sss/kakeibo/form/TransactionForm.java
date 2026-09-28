package jp.co.sss.kakeibo.form;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 取引親情報（日付、時間、店舗名、添付ファイル、メモ及び明細リスト）の入力用フォームクラス
 */
public class TransactionForm {
	/**
	 * 取引ID
	 */
	private Integer transactionId;
	/**
	 * 取引日付（yyyy-MM-dd形式）
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate targetDate;
	/**
	 * 時間（HH:mm形式）
	 */
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime targetTime;
	/**
	 * 店舗名、利用先名称
	 */
	private String storeName;
	/**
	 * 添付ファイルパス、ファイル名
	 */
	private String file;
	/**
	 * メモ
	 */
	private String memo;
	/**
	 * 取引明細フォームのリスト
	 */
	private List<TransactionDetailForm> details = new ArrayList<>();
	
	/**
	 * 取引IDを取得する
	 */
	public Integer getTransactionId() {
		return transactionId;
	}
	/**
	 * 取引IDを設定する
	 */
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * 取引日付を取得する
	 */
	public LocalDate getTargetDate() {
		return targetDate;
	}
	/**
	 * 取引日付を設定する
	 */
	public void setTargetDate(LocalDate targetDate) {
		this.targetDate = targetDate;
	}
	/**
	 * 時間を取得する
	 */
	public LocalTime getTargetTime() {
		return targetTime;
	}
	/**
	 * 時間を設定する
	 */
	public void setTargetTime(LocalTime targetTime) {
		this.targetTime = targetTime;
	}
	/**
	 * 店舗名、利用先名称を取得する
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * 店舗名、利用先名称を設定する
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * 添付ファイルパス、ファイル名を取得する
	 */
	public String getFile() {
		return file;
	}
	/**
	 * 添付ファイルパス、ファイル名を設定する
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
	 * 取引明細フォームリストを取得する
	 */
	public List<TransactionDetailForm> getDetails() {
		return details;
	}
	/**
	 * 取引明細フォームリストを設定する
	 */
	public void setDetails(List<TransactionDetailForm> details) {
		this.details = details;
	}
}