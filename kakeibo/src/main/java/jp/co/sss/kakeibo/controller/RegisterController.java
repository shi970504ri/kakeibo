package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.AuthService;

@Controller
public class RegisterController {
	@Autowired
	private AuthService authService;
	@GetMapping("/user/register")
	public String registerShow() {
		return "user/register";
	}
	@PostMapping("/user/register")
	public String registerProcess(
		@RequestParam("email") String email,
		@RequestParam("password") String password,
		HttpSession session,
		RedirectAttributes redirectAttributes
	) {
		try {
			if (authService.isEmailAlreadyRegistered(email)) {
				throw new IllegalStateException("このメールアドレスは既に登録されています。");
			}
			UsersEntity tempUser = new UsersEntity();
			tempUser.setEmail(email);
			tempUser.setPassword(password);
			String code = authService.generateAuthCode();
			session.setAttribute("tempUser", tempUser);
			session.setAttribute("regEmail", email);
			session.setAttribute("authCode", code);
			session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
			authService.sendEmail(email, code);
		} catch (IllegalStateException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/user/register";
		}
		return "redirect:/auth/register";
	}
}