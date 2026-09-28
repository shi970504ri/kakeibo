package jp.co.sss.kakeibo.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.TransactionsRepository;

/**
 * カレンダー画面を制御するコントローラクラス
 * 指定年月のカケイボデータ存在日を取得してカレンダー画面を表示する
 */
@Controller
public class CalendarController {
	// TransactionsRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private TransactionsRepository transactionsRepository;
	
	/**
	 * 【画面表示】指定年月のカレンダー画面を表示する
	 */
	@GetMapping("/balance/calendar")
	public String showCalendar(
			// クリエータパラメータから対象年月（yyyy-MM）を取得する
			@RequestParam(name = "yearMonth", required = false) String yearMonthStr,
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
		
		// 検索用のアカウント情報を取得する
		UsersEntity user = new UsersEntity();
		user.setUserId(userId);
		
		// 対象年月の判定処理用変数を宣言する
		YearMonth targetYearMonth;
		
		// URLパラメータが存在する場合
		if (yearMonthStr != null && !yearMonthStr.isEmpty()) {
			// 年月を"yyyy-MM"型オブジェクトに変換してセットする
			targetYearMonth = YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern("yyyy-MM"));
		// パラメータがない場合
		} else {
			// 現在のシステム日付の「今月」を取得してセットする
			targetYearMonth = YearMonth.now();
		}
		
		// 確定したパラメータから月初と月末を作ってDB検索に使用する
		LocalDate startDate = targetYearMonth.atDay(1);
		LocalDate endDate = targetYearMonth.atEndOfMonth();
		
		// 対象ユーザーの該当期間の取引データを全件取得する
		List<TransactionsEntity> transactions = transactionsRepository.findByUserAndDateBetween(user, startDate, endDate);
		
		// 登録のある日付のリストを作成する
		Set<String> registeredDates = transactions.stream().map(t -> t.getDate().toString()).collect(Collectors.toSet());
		
		// 現在のシステム日付の「今月」を画面に渡す
		model.addAttribute("currentYearMonth", targetYearMonth.toString());
		// 家計簿データが存在する「日付リスト」を画面に渡す
		model.addAttribute("registeredDates", registeredDates);
		
		// カレンダー画面を再表示する
		return "balance/calendar";
	}
}