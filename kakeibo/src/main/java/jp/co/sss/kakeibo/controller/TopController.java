package jp.co.sss.kakeibo.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.BalanceService;

@Controller
public class TopController {
	@Autowired
	private BalanceService balanceService;
	@GetMapping("/balance/top")
	public String topShow(Model model, HttpSession session) {
		UsersEntity user = (UsersEntity) session.getAttribute("user");
		if (user == null) {
			user = new UsersEntity();
			user.setUserId(1);
		}
		Map<String, Object> topData = balanceService.getTopData(user);
		model.addAttribute("incomeCategories", topData.get("incomeCategories"));
		model.addAttribute("expenseCategories", topData.get("expenseCategories"));
		model.addAttribute("transactionData", topData.get("transactionData"));
		return "balance/top";
	}
}