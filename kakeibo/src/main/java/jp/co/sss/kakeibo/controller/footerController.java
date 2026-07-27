package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class footerController {
	@RequestMapping(path = "/footer/footer")
	public String footerShow() {
		return "footer/footer";
	}
}