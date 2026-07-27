package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EditController {
	@RequestMapping(path = "/balance/edit")
	public String editShow() {
		return "balance/edit";
	}
}