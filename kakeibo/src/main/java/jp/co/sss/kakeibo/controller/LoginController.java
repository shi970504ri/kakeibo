package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	@RequestMapping(path = "/")
	public String Login() {
		return "user/Login";
	}
	@RequestMapping(path = "/user/login")
	public String loginShow() {
		return "user/login";
	}
}