package jp.co.sss.kakeibo.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;
import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.TransactionsRepository;


/**
 * トップ画面用のカテゴリ情報および収支データの集計を行うサービスクラス
 */
@Service
public class BalanceService {
	// TransactionsRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private TransactionsRepository transactionsRepository;
	// CategoriesRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private CategoriesRepository categoriesRepository;
	
	/**
	 * 【処理】ユーザーの収支カテゴリ及び年月別の収支データを取得して集計する
	 */
	public Map<String, Object> getTopData(
			UsersEntity user
	) {
		// 集計結果を保持するマップを生成する
		Map<String, Object> result = new HashMap<>();
		
		// ユーザーに紐づく収入カテゴリ一覧を取得する
		List<CategoriesEntity> incomeCategories = categoriesRepository.findByUserAndType(user, "income");
		// ユーザーに紐づく支出カテゴリ一覧を取得する
		List<CategoriesEntity> expenseCategories = categoriesRepository.findByUserAndType(user, "expense");
		
		// フォーマットした収入カテゴリ一覧を結果マップに設定する
		result.put("incomeCategories", formatCategories(incomeCategories));
		// フォーマットした支出カテゴリ一覧を結果マップに設定する
		result.put("expenseCategories", formatCategories(expenseCategories));
		
		// ユーザーに紐づく収支データを日付順で取得する
		List<TransactionsEntity> transactions = transactionsRepository.findByUserOrderByDateAsc(user);
		
		// 年月毎の収支データを保持するマップを生成する
		Map<String, Map<String, List<Map<String, Object>>>> transactionData = new HashMap<>();
		
		// 年月フォーマット（yyyy-MM）を定義する
		DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
		
		// 取得した収支データ一覧をループ処理する
		for (TransactionsEntity t : transactions) {
			// 取引日付から年月キー（yyyy-MM）を生成する
			String monthKey = t.getDate().format(ymFormatter);
			
			// 対象年月のマップが存在しない場合は空のマップを初期設定する
			transactionData.putIfAbsent(monthKey, createEmptyMonthMap());
			
			// 取引明細が存在しない場合は取引をスキップする
			if (t.getDetails() == null) continue;
			
			// 取引明細一覧をループ処理する
			for (TransactionDetailsEntity detail : t.getDetails()) {
				// 明細の収支タイプを取得する
				String type = detail.getType();
				
				// 収入と支出以外のタイプの場合はスキップする
				if (!"income".equals(type) && !"expense".equals(type)) continue;
				
				// 明細のカテゴリ情報を取得する
				CategoriesEntity cat = detail.getCategory();
				
				// カテゴリ名を取得する（未設定の場合は"未分類"）
				String catName = (cat != null) ? cat.getName() : "未分類";
				// カテゴリIDに基づくカラークラス名を生成する
				String className = generateColorClass(cat);
				
				// 対象年月日及び収支タイプの明細リストを取得する
				List<Map<String, Object>> list = transactionData.get(monthKey).get(type);
				
				// 同一カテゴリが既にリストにぞんざいするか検索する
				Map<String, Object> existing = list.stream()
						.filter(m -> m.get("category").equals(catName))
						.findFirst().orElse(null);
				
				// 同一カテゴリが既に存在している場合
				if (existing != null) {
					// 既存の合計金額を取得する
					int currentAmt = (int) existing.get("amount");
					// 既存金額に今回の明細金額を加算して更新する
					existing.put("amount", currentAmt + detail.getAmount());

				// 同一のカテゴリが存在していない場合
				} else {
					// 新規カテゴリ項目のマップを生成する
					Map<String, Object> item = new HashMap<>();
					
					// カテゴリ名を設定する
					item.put("category", catName);
					// 金額を設定する
					item.put("amount", detail.getAmount());
					// カラークラス名を設定する
					item.put("className", className);
					// 明細リストに項目を追加する
					list.add(item);
				}
			}
		}
		
		// 集計した年月別収支データを結果マップに設定する
		result.put("transactionData", transactionData);
		
		// 集計結果マップを返す
		return result;
	}
	
	/**
	 * 【生成】カテゴリIDに基づき表示用のCSSカラークラス名を生成する
	 */
	private String generateColorClass(
			CategoriesEntity cat
	) {
		// カテゴリまたはカテゴリIDが存在しない場合
		if (cat == null || cat.getCategoryId() == null) {
			// デフォルトのカラークラス名を返す
			return "cat-color-0";
		}
		
		// カテゴリIDの絶対値を10で割った余りからインデックスを計算する
		int colorIndex = Math.abs(cat.getCategoryId()) % 10;
		
		// インデックスに応じたカラークラス名を返す
		return "cat-color-" + colorIndex;
	}
	
	/**
	 * 【整形】カテゴリエンティティ一覧を表示用のマップリスト形式に変換する
	 */
	private List<Map<String, String>> formatCategories(
			List<CategoriesEntity> list
	) {
		// 返却用リストを生成する
		List<Map<String, String>> res = new ArrayList<>();
		
		// カテゴリ一覧をループ処理する
		for (CategoriesEntity c : list) {
			// カテゴリ情報を保有するマップを生成する
			Map<String, String> map = new HashMap<>();
			
			// カテゴリ名を設定する
			map.put("name", c.getName());
			// カラークラス名を設定する
			map.put("className", generateColorClass(c));
			
			// 結果リストに追加する
			res.add(map);
		}
		
		// 背敬語のカテゴリリストを返す
		return res;
	}
	
	/**
	 * 【処理】収入及び支出の空リストを保持する月次初期マップを生成する
	 */
	private Map<String, List<Map<String, Object>>> createEmptyMonthMap() {
		// 月次用のマップを生成する
		Map<String, List<Map<String, Object>>> monthMap = new HashMap<>();
		
		// 収入用リストを初期化して追加する
		monthMap.put("income", new ArrayList<>());
		// 支出用リストを初期化して追加する
		monthMap.put("expense", new ArrayList<>());
		
		// 初期化された月次マップを返す
		return monthMap;
	}
}