package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.AuthService;

/**
 * 新規登録処理及び認証コード送信を制御するコントローラクラス
 */
@Controller
public class RegisterController {
	// AuthServiceクラスのインスタンスを自動注入（DI）する
	@Autowired
	private AuthService authService;
	
	/**
	 * 【画面表示】新規登録画面を表示する
	 */
	@GetMapping("/user/register")
	public String registerShow() {
		// 新規登録画面を表示する
		return "user/register";
	}
	
	/**
	 * 【処理】仮ユーザー情報を生成して新規登録用の認証コードをメール送信する
	 */
	@PostMapping("/user/register")
	public String registerProcess(
			// 画面から送信されたメールアドレスを取得する
			@RequestParam("email") String email,
			// 画面から送信されたパスワードを取得する
			@RequestParam("password") String password,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		// ファイル移動処理の例外発生に備えてtryブロックを開始する
		try {
			// メールアドレスが既に登録済みである場合
			if (authService.isEmailAlreadyRegistered(email)) {
				// 例外を発生させる
				throw new IllegalStateException("このメールアドレスは既に登録されています。");
			}
			
			// 仮ユーザー保存用のエンティティインスタンスを生成する
			UsersEntity tempUser = new UsersEntity();
			
			// メールアドレスを仮ユーザーエンティティに設定する
			tempUser.setEmail(email);
			// デフォルトのニックネーム（ユーザー名）を仮ユーザーエンティティに設定する
			tempUser.setNickname("ユーザー");
			// パスワードを仮ユーザーエンティティに設定する
			tempUser.setPassword(password);
			
			// 6桁の認証コードを生成する
			String authCode = authService.generateAuthCode();
			
			// 仮ユーザー情報をセッションに保存する
			session.setAttribute("tempUser", tempUser);
			// 登録用メールアドレスをセッションに保存する
			session.setAttribute("regEmail", email);
			// 生成した認証コードをセッションに保存する
			session.setAttribute("authCode", authCode);
			// 認証コードの有効期限（現在時刻から10分後）をセッションに保存する
			session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
			
			// 新規登録用の認証コードメールを送信する
			authService.sendRegistrationEmail(tempUser.getEmail(), authCode, tempUser.getNickname());

		// エラーが発生した場合
		} catch (IllegalStateException e) {
			// エラーメッセージをフラッシュスコープに設定する
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			
			// 新規登録画面を表示する
			return "redirect:/user/register";
		}
		
		// 新規登録用二段階認証画面を表示する
		return "redirect:/auth/register";
	}
}