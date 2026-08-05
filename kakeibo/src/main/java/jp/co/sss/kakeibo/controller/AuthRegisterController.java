package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthRegisterController {
	@RequestMapping(path = "/auth/register")
	public String authRegisterShow() {
		return "auth/register";
	}
}