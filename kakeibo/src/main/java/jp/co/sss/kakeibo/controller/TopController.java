package jp.co.sss.kakeibo.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.BalanceService;

/**
 * トップ画面の表示及び各種集計データの取得を制御するコントローラクラス
 */
@Controller
public class TopController {
	// BalanceServiceクラスのインスタンスを自動注入（DI）する
	@Autowired
	private BalanceService balanceService;
	
	/**
	 * 【画面表示】トップ画面（ダッシュボード）を表示する
	 */
	@GetMapping("/balance/top")
	public String topShow(
			Model model,
			HttpSession session
	) {
		// セッションからログインオブジェクトを取得する
		UsersEntity user = (UsersEntity) session.getAttribute("user");
		
		// ユーザーオブジェクトが取得できなかった場合
		if (user == null) {
			// セッションからユーザーIDを取得する
			Integer userId = (Integer) session.getAttribute("userId");
			
			// ユーザーエンティティのインスタンスを生成する
			user = new UsersEntity();
			
			// ユーザーIDを設定する（未設定の場合はデフォルト値1を設定）
			user.setUserId(userId == null ? 1 : userId);
		}
		
		// Serviceからトップ画面用の集計データ（カテゴリ、収支データ）を取得する
		Map<String, Object> topData = balanceService.getTopData(user);
		
		// モデルに収入カテゴリデータを追加する
		model.addAttribute("incomeCategories", topData.get("incomeCategories"));
		// モデルに支出カテゴリデータを追加する
		model.addAttribute("expenseCategories", topData.get("expenseCategories"));
		// モデルに収支取引データを取得する
		model.addAttribute("transactionData", topData.get("transactionData"));
		
		// トップ画面を表示する
		return "balance/top";
	}
}