package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class headerController {
	@RequestMapping(path = "/header/header")
	public String headerShow() {
		return "header/header";
	}
}