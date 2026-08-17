package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;
import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.form.TransactionDetailForm;
import jp.co.sss.kakeibo.form.TransactionForm;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.TransactionDetailsRepository;
import jp.co.sss.kakeibo.repository.TransactionsRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

@Controller
@RequestMapping("/balance")
public class InputController {
	@Autowired
	private TransactionsRepository transactionsRepository;
	@Autowired
	private TransactionDetailsRepository transactionDetailsRepository;
	@Autowired
	private CategoriesRepository categoriesRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private HttpSession session;
	private final Path uploadDir = Paths.get("src/main/resources/static/uploads");
	@GetMapping("/input")
	public String showInputForm(Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		TransactionForm transactionForm = new TransactionForm();
		List<TransactionDetailForm> details = new ArrayList<>();
		details.add(new TransactionDetailForm());
		transactionForm.setDetails(details);

		model.addAttribute("transactionForm", transactionForm);
		model.addAttribute("categories", categoriesRepository.findAll());
		return "balance/input";
	}
	@PostMapping("/save")
	public String saveTransaction(
			@Validated @ModelAttribute("transactionForm") TransactionForm transactionForm,
			BindingResult bindingResult,
			Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoriesRepository.findAll());
			return "balance/input";
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		String originalFile = transactionForm.getFile();
		String finalFileName = null;
		if (originalFile != null && !originalFile.isBlank()) {
			String ext = "";
			int dotIndex = originalFile.lastIndexOf(".");
			if (dotIndex != -1) {
				ext = originalFile.substring(dotIndex);
			}
			String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
			String storeName = transactionForm.getStoreName() != null ? transactionForm.getStoreName() : "store";
			String uniqueKey = UUID.randomUUID().toString().substring(0, 8);
			finalFileName = dateStr + "_" + timeStr + "_" + storeName + "_" + uniqueKey + ext;
			Path oldPath = uploadDir.resolve(originalFile);
			Path newPath = uploadDir.resolve(finalFileName);
			if (Files.exists(oldPath)) {
				try {
					Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
					finalFileName = originalFile;
				}
			} else {
				finalFileName = originalFile;
			}
		}
		LocalDateTime now = LocalDateTime.now();
		TransactionsEntity transactionEntity = new TransactionsEntity();
		transactionEntity.setUser(user);
		transactionEntity.setDate(transactionForm.getTargetDate());
		transactionEntity.setStoreName(transactionForm.getStoreName());
		transactionEntity.setFile(finalFileName);
		transactionEntity.setMemo(transactionForm.getMemo());
		transactionEntity.setCreatedAt(now);
		transactionEntity.setUpdatedAt(now);
		TransactionsEntity savedTransaction = transactionsRepository.save(transactionEntity);
		if (transactionForm.getDetails() != null) {
			List<TransactionDetailsEntity> detailEntities = new ArrayList<>();
			for (TransactionDetailForm detailForm : transactionForm.getDetails()) {
				if ((detailForm.getItemName() == null || detailForm.getItemName().isBlank()) && detailForm.getAmount() == null) {
					continue;
				}
				TransactionDetailsEntity detailEntity = new TransactionDetailsEntity();
				detailEntity.setTransaction(savedTransaction);
				detailEntity.setItemName(detailForm.getItemName());
				detailEntity.setAmount(detailForm.getAmount());
				if (detailForm.getCategoryId() != null) {
					CategoriesEntity cat = categoriesRepository.findById(detailForm.getCategoryId()).orElse(null);
					detailEntity.setCategory(cat);
					if (cat != null) {
						detailEntity.setType(cat.getType());
					} else {
						detailEntity.setType("EXPENSE");
					}
				} else {
					detailEntity.setType("EXPENSE");
				}
				detailEntity.setMemo(detailForm.getMemo());
				detailEntity.setCreatedAt(now);
				detailEntity.setUpdatedAt(now);
				detailEntities.add(detailEntity);
			}
			transactionDetailsRepository.saveAll(detailEntities);
		}
		return "redirect:/balance/input";
	}
}