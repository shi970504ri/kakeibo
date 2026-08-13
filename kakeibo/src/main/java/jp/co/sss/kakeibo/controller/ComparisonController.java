package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ComparisonController {
	@GetMapping("/balance/comparison")
	public String comparisonShow() {
		return "balance/comparison";
	}
}