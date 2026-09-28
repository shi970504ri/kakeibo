package jp.co.sss.kakeibo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * カテゴリ画面を制御するコントローラクラス
 * カテゴリ管理画面の表示を行う
 */
@Controller
public class CategoryController {
	/**
	 * 【画面表示】カテゴリ管理画面を表示する
	 */
	@GetMapping("/balance/category")
	public String categoryShow(
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// カテゴリ画面を表示する
		return "balance/category";
	}
}