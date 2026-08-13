package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class headerController {
	@GetMapping("/header/header")
	public String headerShow() {
		return "header/header";
	}
}