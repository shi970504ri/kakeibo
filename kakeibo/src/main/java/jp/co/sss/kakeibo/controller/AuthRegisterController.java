package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.service.AuthService;

@Controller
public class AuthRegisterController {
	@Autowired
	private AuthService authService;
	@GetMapping("/auth/register")
	public String authRegisterShow(HttpSession session, Model model) {
		if (session.getAttribute("tempUser") == null) {
			return "redirect:/user/register";
		}
		return "auth/register";
	}
	@PostMapping("/auth/register")
	public String authRegisterProcess(
		@RequestParam(value = "code", required = false) String code,
		@RequestParam("action") String action,
		HttpSession session,
		Model model
	) {
		UsersEntity tempUser = (UsersEntity) session.getAttribute("tempUser");
		if (tempUser == null) {
			return "redirect:/user/register";
		}
		if ("resend".equals(action)) {
			try {
				String newCode = authService.generateAuthCode();
				session.setAttribute("authCode", newCode);
				session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
				authService.sendEmail(tempUser.getEmail(), newCode);
				model.addAttribute("message", "認証コードを再送しました。");
			} catch (Exception e) {
				model.addAttribute("error", e.getMessage());
			}
			return "auth/register";
		}
		String sessionCode = (String) session.getAttribute("authCode");
		LocalDateTime expireTime = (LocalDateTime) session.getAttribute("expireTime");
		boolean isSuccess = authService.completeRegistration(tempUser, sessionCode, code, expireTime);
		if (!isSuccess) {
			model.addAttribute("error", "正しいコードを入力して下さい。（または有効期限切れです）");
			return "auth/register";
		}
		session.removeAttribute("tempUser");
		session.removeAttribute("authCode");
		session.removeAttribute("expireTime");
		return "redirect:/user/login";
	}
}