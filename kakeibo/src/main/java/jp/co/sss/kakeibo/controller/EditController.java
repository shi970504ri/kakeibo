package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditController {
	@GetMapping("/balance/edit")
	public String editShow() {
		return "balance/edit";
	}
}