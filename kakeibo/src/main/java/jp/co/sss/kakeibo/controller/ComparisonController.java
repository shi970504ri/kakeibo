package jp.co.sss.kakeibo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;
import jp.co.sss.kakeibo.service.BalanceService;

/**
 * 比較画面を制御するコントローラクラス
 */
@Controller
public class ComparisonController {

	// CategoriesRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private CategoriesRepository categoriesRepository;

	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;

	// BalanceServiceクラスのインスタンスを自動注入（DI）する
	@Autowired
	private BalanceService balanceService;

	/**
	 * 【画面表示】比較画面を表示し、指定年・カテゴリの集計データをModelへ渡す
	 */
	@GetMapping("/balance/comparison")
	public String comparisonShow(
			// URLパラメータから「年」を取得する（未指定時はnull）
			@RequestParam(name = "year", required = false) Integer year,
			// URLパラメータから「カテゴリID」を取得する（未指定時はnull）
			@RequestParam(name = "categoryId", required = false) Integer categoryId,
			// セッション情報を取得する
			HttpSession session,
			// 画面に渡すModelオブジェクト
			Model model
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");

		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}

		// DBからユーザー情報を取得する（取得できない場合はnullにする）
		UsersEntity user = usersRepository.findById(userId).orElse(null);

		// ユーザーが存在しない場合
		if (user == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}

		// ログイン中ユーザーに紐づく取引データから存在する「年の一覧」を取得する
		List<Integer> years = balanceService.getAvailableYears(user);

		// ログインユーザーに紐づくカテゴリ一覧をDBから取得する
		List<CategoriesEntity> categories = categoriesRepository.findByUser(user);

		// 年が指定されていないか取得した年リストに含まれていない場合
		if (year == null || !years.contains(year)) {
		// 最新年を設定する
			year = years.get(0);
		}

		// カテゴリIDが指定されていない場合は一覧の1件目をデフォルトに設定する
		if (categoryId == null && !categories.isEmpty()) {
			categoryId = categories.get(0).getCategoryId();
		}

		// 指定ユーザー、年、カテゴリIDの1月～12月月別合計金額リストを取得する
		List<Integer> monthlyTotals = balanceService.getCategoryMonthlyTotals(user, year, categoryId);

		// thymeLeafに渡すデータとして「年」リストをModelに登録する
		model.addAttribute("years", years);
		// Thymeleafに渡すデータとしてカテゴリリストをModelに登録する
		model.addAttribute("categories", categories);
		// thymeLeafに渡すデータとして選択中の年をModelに登録する
		model.addAttribute("selectedYear", year);
		// thymeLeafに渡すデータとして選択中のカテゴリIDをModelに登録する
		model.addAttribute("selectedCategoryId", categoryId);
		// thymeLeafに渡すデータとして集計した12ヶ月分の金額リストをModelに登録する
		model.addAttribute("monthlyTotals", monthlyTotals);

		// 比較画面のHTMLテンプレート（balance/comparison.html）を表示する
		return "balance/comparison";
	}
}