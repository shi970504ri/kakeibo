package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;
import jp.co.sss.kakeibo.service.AuthService;

@Controller
public class LoginController {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthService authService;
	@GetMapping("/")
	public String Login() {
		return "user/Login";
	}
	@GetMapping("/user/login")
	public String loginShow() {
		return "user/login";
	}
	@PostMapping("/user/login")
	public String loginProcess(
		@RequestParam("email") String email,
		@RequestParam("password") String password,
		HttpSession session,
		Model model
	) {
		UsersEntity user = usersRepository.findByEmail(email);
		if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
			model.addAttribute("error", "メールかパスワードが違います。");
			return "user/login";
		}
		session.setAttribute("tempUser", user);
		String authCode = authService.generateAuthCode();
		session.setAttribute("authCode", authCode);
		session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));
		authService.sendEmail(user.getEmail(), authCode);
		return "redirect:/auth/login";
	}
}