package jp.co.sss.kakeibo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 未認証ユーザーのリクエストをログイン画面へアクセス制限・判定するインターセプタークラス
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
	/**
	 * コントローラの処理実行前にログイン承認チェック及びモーダルアクセスの判定を行う
	 */
	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler
	)
	throws Exception {
		// リクエストパラメータからモーダルフラグを取得する
		String isModal = request.getParameter("modal");
		
		// モーダル表示からのアクセスである場合
		if ("true".equals(isModal)) {
			// チェックをスキップして皇族の処理継続を許可する
			return true;
		}
		
		// HTTPセッションオブジェクトを取得する
		HttpSession session = request.getSession();
		
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面へ強制リダイレクトする
			response.sendRedirect(request.getContextPath() + "/user/login");
			
			// 後続のコントローラ処理実行を中断する
			return false;
		}
		
		// ログイン済みとして後続の処理継続を許可する
		return true;
	}
}