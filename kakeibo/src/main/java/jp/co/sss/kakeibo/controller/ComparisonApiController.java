package jp.co.sss.kakeibo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;
import jp.co.sss.kakeibo.service.BalanceService;

public class ComparisonApiController {
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	
	// BalanceServiceクラスのインスタンスを自動注入（DI）する
	@Autowired
	private BalanceService balanceService;

	/**
	 * 比較情報の操作を行うREST APIコントローラクラス
	 * 非同期通信によるカテゴリ一覧取得を制御する
	 */
	@GetMapping("/api/balance/comparison")
	// 戻り値のList<Integer>を自動でJSON配列形式に変換して返却する
	@ResponseBody
	public List<Integer> getComparisonData(
			// URLパラメータから「年」を取得する
			@RequestParam("year") int year,
			// URLパラメータから「カテゴリID」を取得する
			@RequestParam("categoryId") Integer categoryId,
			// session情報を取得する
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// 空のリストを返却する
			return List.of();
		}
		
		// DBからユーザー情報を取得する（取得できない場合はnullにする）
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		
		// ユーザーが存在しない場合
		if (user == null) {
			// 空のリストを返却する
			return List.of();
		}
		
		// サービス処理を呼び出して指定年、カテゴリの1月～12月の合計金額リスト（12要素のJSON）を取得して返却する
		return balanceService.getCategoryMonthlyTotals(user, year, categoryId);
	}
}