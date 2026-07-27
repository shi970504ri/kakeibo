package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterAuthController {
	@RequestMapping(path = "/auth/registerAuth")
	public String registerAuthShow() {
		return "auth/registerAuth";
	}
}