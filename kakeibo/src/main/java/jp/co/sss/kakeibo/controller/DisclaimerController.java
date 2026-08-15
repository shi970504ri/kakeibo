package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DisclaimerController {
	@GetMapping("/another/disclaimer")
	public String disclaimerShow(@RequestParam(name = "modal", defaultValue = "false") boolean modal) {
		if (modal) {
			return "another/disclaimer :: disclaimer_content";
		}
		return "another/disclaimer";
	}
}