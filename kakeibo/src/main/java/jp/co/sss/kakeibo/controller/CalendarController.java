package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {
	@GetMapping("/balance/calendar")
	public String calendarShow() {
		return "balance/calendar";
	}
}