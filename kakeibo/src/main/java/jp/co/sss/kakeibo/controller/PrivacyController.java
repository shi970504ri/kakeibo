package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrivacyController {
	@RequestMapping(path = "/another/privacy")
	public String privacyShow(@RequestParam(name = "modal", defaultValue = "false") boolean modal) {
		if (modal) {
			return "another/privacy :: privacy_content";
		}
		return "another/privacy";
	}
}