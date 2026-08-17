package jp.co.sss.kakeibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.form.TransactionForm;
import jp.co.sss.kakeibo.repository.TransactionsRepository;

@Controller
public class EditController {
	@Autowired
	private TransactionsRepository transactionsRepository;
	@GetMapping("/balance/edit")
	public String showEdit(@RequestParam("id") Integer id, Model model) {
		TransactionsEntity transactionsEntity = transactionsRepository.findById(id).orElse(null);
		TransactionForm form = new TransactionForm();
		if (transactionsEntity != null) {
			form.setTransactionId(transactionsEntity.getTransactionId());
			form.setTargetDate(transactionsEntity.getDate());
			form.setStoreName(transactionsEntity.getStoreName());
		}
		model.addAttribute("transactionForm", form);
		return "balance/edit";
	}
}