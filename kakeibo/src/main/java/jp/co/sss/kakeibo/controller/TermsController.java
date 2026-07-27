package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TermsController {
	@RequestMapping(path = "/another/terms")
	public String termsShow(@RequestParam(name = "modal", defaultValue = "false") boolean modal) {
		if (modal) {
			return "another/terms :: terms_content";
		}
		return "another/terms";
	}
}