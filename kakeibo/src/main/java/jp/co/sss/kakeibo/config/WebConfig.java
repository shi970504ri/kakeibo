package jp.co.sss.kakeibo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jp.co.sss.kakeibo.interceptor.LoginInterceptor;

/**
 * Web MVC関連設定クラス
 * 
 * ログインチェック用インターセプターの登録や
 * 静的リソース（ファイルアップロードパス）のマッピング定義を行う
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	// 設定対象】ログインチェックを行うインターセプター
	@Autowired
	public LoginInterceptor loginInterceptor;
	
	/**
	 * 画面遷移時のインターセプター（ログインチェック）と適用パスを登録する
	 */
	@Override
	public void addInterceptors(
			InterceptorRegistry registry
	) {
		// ログインチェックを実施するURLパターンを指定して登録する
		registry.addInterceptor(loginInterceptor).addPathPatterns(
			// 利用規約
			"/another/terms",
			// プライバシーポリシー
			"/another/privacy",
			// 免責事項
			"/another/disclaimer",
			// API関連
			"/api/**",
			// 編集
			"/balance/edit",
			// カレンダー
			"/balance/calendar",
			// カテゴリ
			"/balance/category",
			// 比較
			"/balance/compaison",
			// 入力
			"/balance/input",
			// 一覧
			"/balance/list",
			// マイページ
			"/balance/mypage",
			// トップ
			"/balance/top",
			// フッター
			"/footer/footer",
			// ヘッダー
			"/heander/header"
		);
	}
	
	/**
	 * 【リソースハンドラー設定】アップロードされた外部ファイルへのアクセス用リソースハンドラーを登録する
	 */
	@Override
	public void addResourceHandlers(
			ResourceHandlerRegistry registry
		) {
		// uploadsフォルダの相対パスを絶対パスに変換する
		Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
		
		// Stringリソース設定に必要な｢file:///...｣形式の文字列（URI）に変換する
		String uploadPath = uploadDir.toUri().toString();
		
		// 末尾が"/"で終わっていない場合
		if (!uploadPath.endsWith("/")) {
			// 末尾に"/"を補填する
			uploadPath += "/";
		}
		
		// Web上のアクセスパスと物理フォルダのマッピングを実施する
		registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath);
	}
}