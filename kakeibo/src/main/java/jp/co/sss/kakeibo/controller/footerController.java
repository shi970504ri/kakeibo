package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class footerController {
	@GetMapping("/footer/footer")
	public String footerShow() {
		return "footer/footer";
	}
}