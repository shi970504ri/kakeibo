package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * プライバシーポリシー画面を制御するコントローラクラス
 * 通常の画面表示及びモーダル表示用の部分テンプレート返却を制御する
 */
@Controller
public class PrivacyController {
	/**
	 * 【画面表示】プライバシーポリシー画面またはモーダル用コンテンツを表示する
	 */
	@GetMapping("/another/privacy")
	public String privacyShow(
			// モーダル表示フラグのを取得する（未指定時はデフォルトでfalse）
			@RequestParam(name = "modal", defaultValue = "false") boolean modal
	) {
		// モーダル表示要求の場合
		if (modal) {
			// プライバシーポリシーのHTMLから部分テンプレート（privacy_content）のみを返す
			return "another/privacy :: privacy_content";
		}
		
		// 通常アクセス時はプライバシーポリシー画面全体を表示する
		return "another/privacy";
	}
}