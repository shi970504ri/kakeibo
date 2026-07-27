package jp.co.sss.kakeibo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyPageController {
	@RequestMapping(path = "/balance/mypage")
	public String myPageShow() {
		return "balance/mypage";
	}
}