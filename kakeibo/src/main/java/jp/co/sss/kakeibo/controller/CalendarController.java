package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CalendarController {
	@RequestMapping(path = "/balance/calendar")
	public String calendarShow() {
		return "balance/calendar";
	}
}