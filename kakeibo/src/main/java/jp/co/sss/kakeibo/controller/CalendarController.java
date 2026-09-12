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

@Controller
public class CalendarController {
	@Autowired
	private TransactionsRepository transactionsRepository;
	@Autowired
	private HttpSession session;
	@GetMapping("/balance/calendar")
	public String showCalendar(
		@RequestParam(name = "yearMonth", required = false) String yearMonthStr,
		Model model
	) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = new UsersEntity();
		user.setUserId(userId);
		YearMonth targetYearMonth;
		if (yearMonthStr != null && !yearMonthStr.isEmpty()) {
			targetYearMonth = YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern("yyyy-MM"));
		} else {
			targetYearMonth = YearMonth.now();
		}
		LocalDate startDate = targetYearMonth.atDay(1);
		LocalDate endDate = targetYearMonth.atEndOfMonth();
		List<TransactionsEntity> transactions = transactionsRepository.findByUserAndDateBetween(user, startDate, endDate);
		Set<String> registeredDates = transactions.stream().map(t -> t.getDate().toString()).collect(Collectors.toSet());
		model.addAttribute("currentYearMonth", targetYearMonth.toString());
		model.addAttribute("registeredDates", registeredDates);
		return "balance/calendar";
	}
}