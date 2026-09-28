package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 利用規約画面を制御するコントローラクラス
 * 通常の画面表示及びモーダル表示用の部分テンプレート返却を制御する
 */
@Controller
public class TermsController {
	/**
	 * 【画面表示】利用規約画面またはモーダル用コンテンツを表示する
	 */
	@GetMapping("/another/terms")
	public String termsShow(
			@RequestParam(name = "modal", defaultValue = "false") boolean modal
	) {
		// モーダル表示要求の場合
		if (modal) {
			// 利用規約のHTMLから部分テンプレート（terms_content）のみを返す
			return "another/terms :: terms_content";
		}
		
		// 通常アクセス時は利用規約画面全体を表示する
		return "another/terms";
	}
}