package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;
import jp.co.sss.kakeibo.service.AuthService;

/**
 * ログイン及びログアウト処理を制御するコントローラクラス
 */
@Controller
public class LoginController {
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	// PasswordEncoderクラスのインスタンスを自動注入（DI）する
	@Autowired
	private PasswordEncoder passwordEncoder;
	// AuthServiceクラスのインスタンスを自動注入（DI）する
	@Autowired
	private AuthService authService;
	
	/**
	 * 【画面表示】ルートパスへのアクセス時にログイン画面を表示する
	 */
	@GetMapping("/")
	public String Login() {
		// "http://localhost:2040/kakeibo/"ログイン画面を表示する
		return "user/Login";
	}
	
	/**
	 * 【画面表示】ルートパスへのアクセス時にログイン画面を表示する
	 */
	@GetMapping("/user/login")
	public String loginShow() {
		// "http://localhost:2040/kakeibo/user/login"ログイン画面を表示する
		return "user/login";
	}
	
	/**
	 * 【処理】ユーザー認証を行って二段階認証コードを発行・メール送信をする
	 */
	@PostMapping("/user/login")
	public String loginProcess(
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			HttpSession session,
			Model model
	) {
		// メールアドレスに一致するユーザー情報をデータベースから検索する
		UsersEntity user = usersRepository.findByEmail(email);
		
		// パスワードが不一致であるかユーザーが存在しない場合
		if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
			// 認証エラーメッセージをモデルにセットする
			model.addAttribute("error", "メールかパスワードが違います。");
			
			// ログイン画面を再表示する
			return "user/login";
		}
		
		// ランダムな二段階認証コードを生成する
		String authCode = authService.generateAuthCode();
		
		// 仮ログインユーザー情報をセッションに追加する
		session.setAttribute("tempUser", user);
		// 生成した認証コードをセッションに保存する
		session.setAttribute("authCode", authCode);
		// 認証コードの有効期限（現在時刻から10分後）をセッションに保存する
		session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
		
		// ユーザーのメールアドレス宛てに二段階認証を送信する
		authService.sendLoginAuthEmail(user.getEmail(), authCode, user.getNickname());
		
		// 二段階認証画面を表示する
		return "redirect:/auth/login";
	}
	
	/**
	 * 
	 */
	@GetMapping("/user/logout")
	public String logout(
			HttpSession session
	) {
		// セッションを破棄してログイン状態を解除する
		session.invalidate();
		
		// ログイン画面を表示する
		return "redirect:/user/login";
	}
}