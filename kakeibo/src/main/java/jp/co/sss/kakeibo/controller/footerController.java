package jp.co.sss.kakeibo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * フッター画面を制御するコントローラクラス
 */
@Controller
public class footerController {
	/**
	 * 【画面表示】フッター画面を表示する
	 */
	@GetMapping("/footer/footer")
	public String footerShow(
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if(userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// フッター画面を表示する
		return "footer/footer";
	}
}