package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ComparisonController {
	@RequestMapping(path = "/balance/comparison")
	public String comparisonShow() {
		return "balance/comparison";
	}
}