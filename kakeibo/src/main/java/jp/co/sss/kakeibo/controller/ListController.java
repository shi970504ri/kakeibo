package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ListController {
	@RequestMapping(path = "/balance/list")
	public String listShow() {
		return "balance/list";
	}
}