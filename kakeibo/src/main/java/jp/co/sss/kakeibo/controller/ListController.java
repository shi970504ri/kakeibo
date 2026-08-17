package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.dto.TransactionDto;
import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;
import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.TransactionsRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

@Controller
public class ListController {
	@Autowired
	private TransactionsRepository transactionsRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private HttpSession session;
	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
	@GetMapping("/balance/list")
	public String listShow(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		if (user == null) {
			return "redirect:/user/login";
		}
		Pageable pageable = PageRequest.of(page, 20);
		Page<TransactionsEntity> transactionPage = transactionsRepository.findByUserOrderByDateAscTransactionIdAsc(user, pageable);
		List<TransactionDto> transactionDtoList = transactionPage.getContent().stream().map(t -> {
			int income = 0;
			int expense = 0;
			if (t.getDetails() != null) {
				for (TransactionDetailsEntity d : t.getDetails()) {
					if (d.getType() != null) {
						String type = d.getType().trim();
						if ("INCOME".equalsIgnoreCase(type) || "収入".equals(type) || "1".equals(type)) {
							income += d.getAmount();
						} else {
							expense += d.getAmount();
						}
					}
				}
			}
			return new TransactionDto(t, income, expense, income - expense);
		}).toList();
		model.addAttribute("transactionList", transactionDtoList);
		model.addAttribute("transactionPage", transactionPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", transactionPage.getTotalPages());
		model.addAttribute("isEmpty", transactionPage.isEmpty());
		return "balance/list";
	}
	@PostMapping("/balance/delete")
	public String deleteTransaction(@RequestParam("transactionId") Integer transactionId) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		TransactionsEntity transaction = transactionsRepository.findById(transactionId).orElse(null);
		if (transaction != null && transaction.getUser().getUserId().equals(userId)) {
			deleteFileIfExists(transaction.getFile());
			transactionsRepository.delete(transaction);
		}
		return "redirect:/balance/list";
	}
	private void deleteFileIfExists(String fileName) {
		if (fileName != null && !fileName.trim().isEmpty()) {
			try {
				Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).toAbsolutePath().normalize();
				Files.deleteIfExists(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}