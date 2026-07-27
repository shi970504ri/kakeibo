package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TopController {
	@RequestMapping(path = "/balance/top")
	public String topShow() {
		return "balance/top";
	}
}