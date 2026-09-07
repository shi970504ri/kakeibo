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

@Service
public class BalanceService {
	@Autowired
	private TransactionsRepository transactionsRepository;
	@Autowired
	private CategoriesRepository categoriesRepository;
	public Map<String, Object> getTopData(UsersEntity user) {
		Map<String, Object> result = new HashMap<>();
		List<CategoriesEntity> incomeCategories = categoriesRepository.findByUserAndType(user, "income");
		List<CategoriesEntity> expenseCategories = categoriesRepository.findByUserAndType(user, "expense");
		result.put("incomeCategories", formatCategories(incomeCategories));
		result.put("expenseCategories", formatCategories(expenseCategories));
		List<TransactionsEntity> transactions = transactionsRepository.findByUserOrderByDateAsc(user);
		Map<String, Map<String, List<Map<String, Object>>>> transactionData = new HashMap<>();
		DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
		for (TransactionsEntity t : transactions) {
			String monthKey = t.getDate().format(ymFormatter);
			transactionData.putIfAbsent(monthKey, createEmptyMonthMap());
			if (t.getDetails() == null) continue;
			for (TransactionDetailsEntity detail : t.getDetails()) {
				String type = detail.getType();
				if (!"income".equals(type) && !"expense".equals(type)) continue;
				CategoriesEntity cat = detail.getCategory();
				String catName = (cat != null) ? cat.getName() : "未分類";
				String className = generateColorClass(cat);
				List<Map<String, Object>> list = transactionData.get(monthKey).get(type);
				Map<String, Object> existing = list.stream()
						.filter(m -> m.get("category").equals(catName))
						.findFirst().orElse(null);
				if (existing != null) {
					int currentAmt = (int) existing.get("amount");
					existing.put("amount", currentAmt + detail.getAmount());
				} else {
					Map<String, Object> item = new HashMap<>();
					item.put("category", catName);
					item.put("amount", detail.getAmount());
					item.put("className", className);
					list.add(item);
				}
			}
		}
		result.put("transactionData", transactionData);
		return result;
	}
	private String generateColorClass(CategoriesEntity cat) {
		if (cat == null || cat.getCategoryId() == null) {
			return "cat-color-0";
		}
		int colorIndex = Math.abs(cat.getCategoryId()) % 10;
		return "cat-color-" + colorIndex;
	}
	private List<Map<String, String>> formatCategories(List<CategoriesEntity> list) {
		List<Map<String, String>> res = new ArrayList<>();
		for (CategoriesEntity c : list) {
			Map<String, String> map = new HashMap<>();
			map.put("name", c.getName());
			map.put("className", generateColorClass(c));
			res.add(map);
		}
		return res;
	}
	private Map<String, List<Map<String, Object>>> createEmptyMonthMap() {
		Map<String, List<Map<String, Object>>> monthMap = new HashMap<>();
		monthMap.put("income", new ArrayList<>());
		monthMap.put("expense", new ArrayList<>());
		return monthMap;
	}
}