package jp.co.sss.kakeibo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.form.TransactionForm;
import jp.co.sss.kakeibo.repository.TransactionsRepository;

/**
 * 収支編集画面を制御するコントローラクラス
 */
@Controller
public class EditController {
	// TransactionsRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private TransactionsRepository transactionsRepository;
	
	/**
	 * 【画面表示】収支データの編集画面を表示する
	 */
	@GetMapping("/balance/edit")
	public String showEdit(
			@RequestParam("id") Integer id,
			HttpSession session,
			Model model
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// データベースからログインしているuserIdの収支データを取得する
		TransactionsEntity transactionsEntity = transactionsRepository.findById(id).orElse(null);
		// TransactionFormクラスから情報を全部取得する
		TransactionForm form = new TransactionForm();
		
		// データが存在する場合
		if (transactionsEntity != null) {
			// フォームに取引IDをセットする
			form.setTransactionId(transactionsEntity.getTransactionId());
			// フォームに対象の日付をセットする
			form.setTargetDate(transactionsEntity.getDate());
			// フォームに店舗名をセットする
			form.setStoreName(transactionsEntity.getStoreName());
		}
		
		// フォームにセットした情報を画面に渡す
		model.addAttribute("transactionForm", form);
		
		// 編集画面を再表示する
		return "balance/edit";
	}
}