package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryController {
	@RequestMapping(path = "/balance/category")
	public String categoryShow() {
		return "balance/category";
	}
}