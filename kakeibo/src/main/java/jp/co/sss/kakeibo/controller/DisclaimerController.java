package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 免責事項画面を制御するコントローラクラス
 * 通常の画面表示及びモーダル表示用の部分テンプレート返却を制御する
 */
@Controller
public class DisclaimerController {
	/**
	 * 【画面表示】免責事項画面またはモーダル用コンテンツを表示する
	 */
	@GetMapping("/another/disclaimer")
	public String disclaimerShow(
			// モーダル表示フラグのを取得する（未指定時はデフォルトでfalse）
			@RequestParam(name = "modal", defaultValue = "false") boolean modal
	) {
		// モーダル表示要求の場合
		if (modal) {
			// 免責事項のHTMLから部分テンプレート（disclaimer_content）のみを返す
			return "another/disclaimer :: disclaimer_content";
		}
		
		// 通常アクセス時は免責事項画面全体を表示する
		return "another/disclaimer";
	}
}