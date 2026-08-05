package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthLoginController {
	@RequestMapping(path = "/auth/login")
	public String authLoginShow() {
		return "auth/login";
	}
}