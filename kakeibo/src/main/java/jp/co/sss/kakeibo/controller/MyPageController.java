package jp.co.sss.kakeibo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;

@Controller
public class MyPageController {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private HttpSession session;
	@GetMapping("/balance/mypage")
	public String myPageShow(Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		if (user == null) {
			return "redirect:/user/login";
		}
		model.addAttribute("user", user);
			return "balance/mypage";
	}
	@PostMapping("/update/userinfo")
	public String updateNickname(
		@RequestParam("email")String email,
		@RequestParam("nickname") String nickname,
		RedirectAttributes redirectAttributes
	) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		if (user != null) {
			user.setEmail(email);
			user.setNickname(nickname);
			usersRepository.save(user);
			session.setAttribute("userName", nickname);
			redirectAttributes.addFlashAttribute("successMsg", "ユーザー情報を更新しました。");
		}
		return "redirect:/balance/mypage";
	}
	@PostMapping("/update/password")
	public String updatePassword(
		@RequestParam("password") String password,
		RedirectAttributes redirectAttributes
	) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/login";
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		if (user != null && password != null && !password.trim().isEmpty()) {
			user.setPassword(password);
			usersRepository.save(user);
			redirectAttributes.addFlashAttribute("successMsg", "パスワードを更新しました。");
		}
		return "redirect:/balance/mypage";
	}
}