package jp.co.sss.kakeibo.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.UsersRepository;

@Service
public class AuthService {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JavaMailSender mailSender;
	public boolean isEmailAlreadyRegistered(String email) {
		return usersRepository.findByEmail(email) != null;
	}
	public String generateAuthCode() {
		return String.format("%06d", new Random().nextInt(1000000));
	}
	public void sendEmail(String email, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("【家計簿管理アプリ】会員登録用認証コード");
		message.setText(
			"家計簿管理アプリをご利用いただきありがとうございます。\n\n認証コードは以下の6桁です。\n\n" + code + "\n\n有効期限は10分です。"
		);
		mailSender.send(message);
	}
	@Transactional
	public boolean completeRegistration(UsersEntity tempUser, String sessionCode, String inputCode, LocalDateTime expireTime) {
		if (sessionCode == null || !sessionCode.equals(inputCode)) {
			return false;
		}
		if (LocalDateTime.now().isAfter(expireTime)) {
			return false;
		}
		if (isEmailAlreadyRegistered(tempUser.getEmail())) {
			return false;
		}
		tempUser.setPassword(passwordEncoder.encode(tempUser.getPassword()));
		LocalDateTime now = LocalDateTime.now();
		tempUser.setCreatedAt(now);
		tempUser.setUpdatedAt(now);
		usersRepository.save(tempUser);
		return true;
	}
}