package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InputController {
	@RequestMapping(path = "/balance/input")
	public String inputShow() {
		return "balance/input";
	}
}