package jp.co.sss.kakeibo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ヘッダー画面を制御するコントローラクラス
 */
@Controller
public class headerController {
	/**
	 * 【画面表示】ヘッダー画面を表示する
	 */
	@GetMapping("/header/header")
	public String headerShow(
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// ヘッダー画面を表示する
		return "header/header";
	}
}