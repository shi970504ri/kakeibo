package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
public class InputController {
	@Autowired
	private CategoriesRepository categoriesRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private TransactionsRepository transactionsRepository;
	@Autowired
	private TransactionDetailsRepository transactionDetailsRepository;
	@GetMapping("/balance/input")
	public String inputShow(HttpSession session, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		List<CategoriesEntity> categories = categoriesRepository.findByUser(user);
		model.addAttribute("categories", categories);
		TransactionForm form = new TransactionForm();
		for (int i = 0; i < 5; i++) {
			form.getDetails().add(new TransactionDetailForm());
		}
		model.addAttribute("transactionForm", form);
		return "balance/input";
	}
	@PostMapping("/balance/save")
	public String register(@ModelAttribute TransactionForm form, HttpSession session, Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = usersRepository.findById(userId).orElseThrow();
		List<TransactionDetailForm> validDetails = new ArrayList<>();
		for (int i = 0; i < form.getDetails().size(); i++) {
			TransactionDetailForm detail = form.getDetails().get(i);
			boolean hasItemName = detail.getItemName() != null && !detail.getItemName().isBlank();
			boolean hasAmount = detail.getAmount() != null;
			boolean hasCategory = detail.getCategoryId() != null;
			boolean hasFile = detail.getFile() != null && !detail.getFile().isBlank();
			boolean hasMemo = detail.getMemo() != null && !detail.getMemo().isBlank();
			boolean hasAnyInput = hasItemName || hasAmount || hasCategory || hasFile || hasMemo;
			if (hasAnyInput) {
				if (!hasItemName || !hasAmount || !hasCategory || !hasFile) {
					List<CategoriesEntity> categories = categoriesRepository.findByUser(user);
					model.addAttribute("categories", categories);
					model.addAttribute("errorMessage", String.format("No.%05d の必須項目が入力されていません。", i + 1));
					return "balance/input";
				}
				validDetails.add(detail);
			}
		}
		if (validDetails.isEmpty()) {
			List<CategoriesEntity> categories = categoriesRepository.findByUser(user);
			model.addAttribute("categories", categories);
			model.addAttribute("errorMessage", "明細を少なくとも1件入力してください。");
			return "balance/input";
		}
		String dateStr = form.getTargetDate() != null ? form.getTargetDate().toString().replace("-", "") : "";
		String timeStr = form.getTargetTime() != null ? form.getTargetTime().toString().replace(":", "") : "";
		if (timeStr.length() > 4) {
			timeStr = timeStr.substring(0, 4);
		}
		String storeName = form.getStoreName() != null ? form.getStoreName() : "";
		Path uploadDir = Paths.get("uploads/");
		for (int i = 0; i < validDetails.size(); i++) {
			TransactionDetailForm detailForm = validDetails.get(i);
			String originalFile = detailForm.getFile();
			if (originalFile != null && !originalFile.isBlank()) {
				String ext = "";
				int dotIndex = originalFile.lastIndexOf(".");
				if (dotIndex != -1) {
					ext = originalFile.substring(dotIndex);
				}
				String itemName = detailForm.getItemName() != null ? detailForm.getItemName() : "";
				String newFileName = dateStr + "_" + timeStr + "_" + storeName + "_" + itemName + ext;
				Path oldPath = uploadDir.resolve(originalFile);
				Path newPath = uploadDir.resolve(newFileName);
				if (Files.exists(oldPath)) {
					try {
						Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
						detailForm.setFile(newFileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					detailForm.setFile(newFileName);
				}
			}
		}
		LocalDateTime now = LocalDateTime.now();
		TransactionsEntity transactions = new TransactionsEntity();
		transactions.setUser(user);
		transactions.setDate(form.getTargetDate());
		transactions.setStoreName(form.getStoreName());
		transactions.setItemName(validDetails.get(0).getItemName());
		transactions.setFile(validDetails.get(0).getFile());
		transactions.setMemo(form.getMemo());
		transactions.setCreatedAt(now);
		transactions.setUpdatedAt(now);
		TransactionsEntity savedTransaction = transactionsRepository.save(transactions);
		for (TransactionDetailForm detailForm : validDetails) {
			CategoriesEntity category = categoriesRepository.findById(detailForm.getCategoryId()).orElse(null);
			if (category == null) continue;
			TransactionDetailsEntity detail = new TransactionDetailsEntity();
			detail.setTransaction(savedTransaction);
			detail.setCategory(category);
			detail.setType(category.getType());
			detail.setItemName(detailForm.getItemName());
			detail.setAmount(detailForm.getAmount());
			detail.setFile(detailForm.getFile());
			detail.setMemo(detailForm.getMemo());
			detail.setCreatedAt(now);
			detail.setUpdatedAt(now);
			transactionDetailsRepository.save(detail);
		}
		return "redirect:/balance/input";
	}
}