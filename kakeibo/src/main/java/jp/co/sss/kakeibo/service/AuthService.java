package jp.co.sss.kakeibo.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;

/**
 * ユーザー登録及びログイン時の二段階認証お処理を行うサービスクラス
 */
@Service
public class AuthService {
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	// PasswordEncoderクラスのインスタンスを自動注入（DI）する
	@Autowired
	private PasswordEncoder passwordEncoder;
	// メール送信処理用オブジェクト
	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * 【検証】メールアドレスが既に登録済みか判定する
	 */
	public boolean isEmailAlreadyRegistered(
			String email
		) {
		// 指定されたメールアドレスのユーザーが存在するか判定する
		return usersRepository.findByEmail(email) != null;
	}
	
	/**
	 * 【処理】6桁のランダムな数字認証jコードを生成する
	 */
	public String generateAuthCode() {
		// 0～999999までのランダムな数値を取得して6桁のゼロ埋め文字列にフォーマットして返却する
		return String.format("%06d", new Random().nextInt(1000000));
	}
	
	/**
	 * 【処理】新規登録用の認証コードメールを送信する
	 */
	public void sendRegistrationEmail(
			String email,
			String code,
			String userName
	) {
		// メール送信用のメッセージオブジェクトを生成する
		SimpleMailMessage message = new SimpleMailMessage();
		
		// ユーザー名が空の場合は表示名を判定してセットする
		String displayName = (userName != null && !userName.trim().isEmpty()) ? userName : "ユーザー";
		
		// 送信先メールアドレスを設定する
		message.setTo(email);
		// 新規登録用メール件名を設定する
		message.setSubject("【家計簿管理システム】新規登録用認証コード");
		// 新規登録用メール本文を設定する
		message.setText(
				displayName + "　様\n\n" +
				"\n" +
				"家計簿管理アプリをご利用いただきありがとうございます。\n\n" +
				"セキュリティ保護のため、アカウントの新規登録手続きを完了するにはメール本文を開き、下記に記載されている認証コードを入力して下さい。\n\n" +
				"※セキュリティ上の理由により、通知やプレビュー画面にはコードを表示しておりません。\n" +
				"※この手続きに心当たりがない場合は、第三者が不正にアクセスした可能性があります。\n" +
				"※お手数ですが本メールを破棄し、速やかにパスワードの変更をお願い致します。\n\n" +
				"--------------------------------------------------\n" +
				"以下の認証コードを画面に入力してください。\n" +
				"--------------------------------------------------\n\n" +
				"ログインの認証コードは以下の6桁です。\n" +
				"認証コード：" + code + "\n" +
				"有効期限は10分です。"
		);
		
		// 設定した内容でメールを送信する
		mailSender.send(message);
	}
	
	/**
	 * 【処理】ログイン二段階認証用の認証コードメールを送信する
	 */
	public void sendLoginAuthEmail(
			String email,
			String code,
			String userName
	) {
		// メール送信用のメッセージオブジェクトを生成する
		SimpleMailMessage message = new SimpleMailMessage();
		
		// ユーザー名が空の場合は表示名を判定してセットする
		String displayName = (userName != null && !userName.trim().isEmpty()) ? userName : "ユーザー";
		
		// 送信メールアドレスを設定する
		message.setTo(email);
		// ログイン認証用メール件名を設定する
		message.setSubject("【家計簿管理アプリ】ログイン認証コード");
		// ログイン認証用メール本文を設定する
		message.setText(
				displayName + "　様\n\n" +
				"\n" +
				"いつも家計簿管理アプリをご利用いただきありがとうございます。\n\n" +
				"セキュリティ保護のため、アカウントへのログイン手続きを完了するにはメール本文を開き、下記に記載されている認証コードを入力して下さい。\n\n" +
				"※セキュリティ上の理由により、通知やプレビュー画面にはコードを表示しておりません。\n" +
				"※この手続きに心当たりがない場合は、第三者が不正にアクセスした可能性があります。\n" +
				"※お手数ですが本メールを破棄し、速やかにパスワードの変更をお願い致します。\n\n" +
				"--------------------------------------------------\n" +
				"以下の認証コードを画面に入力してください。\n" +
				"--------------------------------------------------\n\n" +
				"ログインの認証コードは以下の6桁です。\n" +
				"認証コード：" + code + "\n" +
				"有効期限は10分です。"
		);
		// 設定した内容でメール送信する
		mailSender.send(message);
	}
	
	/**
	 * 【登録・検証】会員登録認証コードチェックを行い、パスワードを暗号化してユーザー情報を永続化する
	 */
	@Transactional
	public boolean completeRegistration(
			UsersEntity tempUser,
			String sessionCode,
			String inputCode,
			LocalDateTime expireTime
	) {
		// セッションが存在しないか入力されたコードと一致しない場合
		if (sessionCode == null || !sessionCode.equals(inputCode)) {
			// 検証失敗として「false」を返す
			return false;
		}
		
		// 有効期限が設定されていないか現在時刻が有効期限を過ぎている場合
		if (expireTime == null || LocalDateTime.now().isAfter(expireTime)) {
			// 検証失敗として「false」を返す
			return false;
		}
		
		// メールアドレスが既に登録済みである場合
		if (isEmailAlreadyRegistered(tempUser.getEmail())) {
			// 検証失敗として「false」を返す
			return false;
		}
		
		// 現在のシステム日時を取得する
		LocalDateTime now = LocalDateTime.now();
		
		// パスワードを暗号化（ハッシュ化）してユーザーエンティティに設定する
		tempUser.setPassword(passwordEncoder.encode(tempUser.getPassword()));
		// ユーザーエンティティに作成日時を設定する
		tempUser.setCreatedAt(now);
		// ユーザーエンティティに更新日時を設定する
		tempUser.setUpdatedAt(now);
		
		// ユーザー情報をデータベースに登録する
		usersRepository.save(tempUser);
		
		// 登録完了として「true」を返す
		return true;
	}
	
	/**
	 * 【検証】ログイン時に二段階認証コード及び有効期限を検証する
	 */
	public boolean verifyLoginAuthCode(
			String sessionCode,
			String inputCode,
			LocalDateTime expireTime
	) {
		// セッションコードが存在しないか入力されたコードと一致しない場合
		if (sessionCode == null || !sessionCode.equals(inputCode)) {
			// 認証失敗として「false」を返す
			return false;
		}

		// 有効期限が設定されていないか現在時刻が有効期限を過ぎている場合
		if (expireTime == null || LocalDateTime.now().isAfter(expireTime)) {
			// 認証失敗として「false」を返す
			return false;
		}
		
		// 認証成功として「true」を返す
		return true;
	}
}