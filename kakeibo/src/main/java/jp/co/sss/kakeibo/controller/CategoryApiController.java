package jp.co.sss.kakeibo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
	@Autowired
	private CategoriesRepository categoriesRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private HttpSession session;
	@GetMapping
	public List<CategoriesEntity> getCategories() {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return List.of();
		}
		UsersEntity user = new UsersEntity();
		user.setUserId(userId);
		return categoriesRepository.findByUser(user);
	}
	@PostMapping
	public ResponseEntity<?> addCategory(@RequestBody Map<String, String> body) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return ResponseEntity.status(401).body("Unauthorized");
		}
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("User not found");
		}
		CategoriesEntity category = new CategoriesEntity();
		category.setName(body.get("name"));
		category.setType(body.get("type"));
		category.setUser(user);
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		categoriesRepository.save(category);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
		categoriesRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}