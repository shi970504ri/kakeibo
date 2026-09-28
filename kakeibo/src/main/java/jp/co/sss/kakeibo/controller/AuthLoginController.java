package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.AuthService;

/**
 * ログイン用二段階認証を制御するコントローラクラス
 * メールを用いる
 * 画面表示、検証、コード再送を制御する
 */
@Controller
public class AuthLoginController {
	// AuthServiceクラスのインスタンスを自動注入（DI）する
	@Autowired
	private AuthService authService;

	/**
	 * 【画面表示】ログイン用二段階認証画面またはログイン画面にアクセスする
	 */
	@GetMapping("/auth/login")
	public String authLoginShow(
			HttpSession session,
			Model model
	) {
		// ユーザー情報がない状態でアクセスを試みた場合
		if (session.getAttribute("tempUser") == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// ログイン用二段階認証画面にアクセスする
		return "auth/login";
	}
	
	/**
	 * 【検証】認証コードの検証及び再送処理
	 * フォーム送信の受付
	 */
	@PostMapping("/auth/login")
	public String authLoginProcess(
			// 画面から送信された入力コードを取得
			@RequestParam(value = "code", required = false) String code,
			// 押されたボタンの種別を取得（未指定時はデフォルトで"verify"=検証）
			@RequestParam(value = "action", defaultValue = "verify") String action,
			HttpSession session,
			Model model
	) {
		// "tempUser"という名前で保存されているデータを取得する
		UsersEntity tempUser = (UsersEntity) session.getAttribute("tempUser");
		
		// データが取得できなかった場合
		if (tempUser == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// 再送ボタンが押された場合
		if ("resend".equals(action)) {
			try {
				// AuthServiceを利用して新しいランダムな認証コードを発行する
				String authCode = authService.generateAuthCode();
				
				// 新しく発行した認証コードをセッションに保存する
				session.setAttribute("authCode", authCode);
				// 有効期限を「現在時刻から10分後」に更新してセッションに保存する
				session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
				
				// ログイン成功している仮ユーザー（tempUser）のメールアドレス宛に新しい認証コードをメール送信する
				authService.sendLoginAuthEmail(tempUser.getEmail(), authCode, tempUser.getNickname());
				
				// 画面表示用に完了メッセージをmodelにセットする
				model.addAttribute("message", "認証コードを再送しました。");
			// 例外が発生した際に
			} catch (Exception e) {
				// エラー内容をmodelにセットして画面表示する
				model.addAttribute("error", e.getMessage());
			}
			
			// ログイン用二段階認証画面を再表示する
			return "auth/login";
		}
		
		// セッションに保存していた「正解の認証コード」を取得する
		String sessionCode = (String) session.getAttribute("authCode");
		// コード発行時にセッションに保存した「有効期限（発行から10分後）」を取得する
		LocalDateTime expireTime = (LocalDateTime) session.getAttribute("expireTime");
		
		// 「sessionCode（サーバーに保存した正解のコード）」「code（画面でユーザーが入力したコード）」「expireTime（有効期限）」をAuthServiceに渡して検証を実施する
		boolean isSuccess = authService.verifyLoginAuthCode(sessionCode, code, expireTime);
		
		// 検証結果がfalse（検証失敗）の場合
		if (!isSuccess) {
			// modelにエラーメッセージをセットする
			model.addAttribute("error", "認証コードが違います。");
			// ログイン用二段階認証画面を再表示する
			return "auth/login";
		}
		
		// セッションから仮ユーザー情報を破棄する
		session.removeAttribute("tempUser");
		// セッションから正解コードを破棄する
		session.removeAttribute("authCode");
		// セッションから有効期限を破棄する
		session.removeAttribute("expireTime");
		
		// ログイン時に取得した仮ユーザー情報（tempUser）からユーザーIDを取り出してセッションに格納する
		session.setAttribute("userId", tempUser.getUserId());
		
		// トップ画面に強制リダイレクトする
		return "redirect:/balance/top";
	}
}