package jp.co.sss.kakeibo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;

/**
 * マイページの表示及びユーザー情報（基本情報・パスワード）の更新を制御するコントローラクラス
 */
@Controller
public class MyPageController {
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	// PasswordEncoderクラスのインスタンスを自動注入（DI）する
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 【画面表示】マイページ画面を表示する
	 */
	@GetMapping("/balance/mypage")
	public String myPageShow(
			HttpSession session,
			Model model
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// ログインユーザーのエンティティ情報をデータベースから取得する
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		
		// ユーザー情報が取得できなかった場合
		if (user == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// モデルにユーザー情報を追加する
		model.addAttribute("user", user);
		
		// マイページ画面を表示する
		return "balance/mypage";
	}
	
	/**
	 * 【処理】ユーザーの基本情報（メールアドレス、ニックネーム）を更新する
	 */
	@PostMapping("/update/userinfo")
	public String updateNickname(
			// 画面から送信されたメールアドレスを取得
			@RequestParam("email")String email,
			// 画面から送信されたニックネームを取得
			@RequestParam("nickname") String nickname,
			RedirectAttributes redirectAttributes,
			HttpSession session
	) {
		// sessionからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// ログインユーザーのエンティティ情報をデータベースから取得する
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		// ユーザー情報が存在する場合
		if (user != null) {
			// ユーザーエンティティに新しいメールアドレスを設定する
			user.setEmail(email);
			// ユーザーエンティティに新しいニックネームを設定する
			user.setNickname(nickname);
			
			// 変更後のユーザー情報をデータベースに保存する
			usersRepository.save(user);
			
			// セッションの表示用ユーザー名を新しいニックネームに更新する
			session.setAttribute("userName", nickname);
			
			// リダイレクト策に完了メッセージをセットする
			redirectAttributes.addFlashAttribute("successMsg", "ユーザー情報を更新しました。");
		}
		// マイページを再表示する
		return "redirect:/balance/mypage";
	}
	
	/**
	 * 【処理】パスワードをハッシュ化して更新する
	 */
	@PostMapping("/update/password")
	public String updatePassword(
			// 画面から送信された新しいパスワードを取得
			@RequestParam("password") String password,
			RedirectAttributes redirectAttributes,
			HttpSession session
	) {
		// ログインユーザーのエンティティ情報をデータベースから取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// ログインユーザーのエンティティ情報をデータベースから取得する
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		
		// ユーザー情報が存在して、かつパスワードが入力されている場合
		if (user != null && password != null && !password.trim().isEmpty()) {
			// パスワードを暗号化（ハッシュ化）してエンティティに設定する
			user.setPassword(passwordEncoder.encode(password));
			
			// 変更後のユーザー情報をデータベースに保存する
			usersRepository.save(user);
			
			// リダイレクト先に完了メッセージを表示する
			redirectAttributes.addFlashAttribute("successMsg", "パスワードを更新しました。");
		}
		
		// マイページ画面を再表示する
		return "redirect:/balance/mypage";
	}
}