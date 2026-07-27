package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {
	@RequestMapping(path = "/user/register")
	public String registerShow() {
		return "user/register";
	}
}