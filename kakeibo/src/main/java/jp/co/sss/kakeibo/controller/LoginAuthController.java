package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginAuthController {
	@RequestMapping(path = "/auth/login")
	public String loginAuthShow() {
		return "auth/login";
	}
}