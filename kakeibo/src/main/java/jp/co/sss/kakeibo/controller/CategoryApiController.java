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
import org.springframework.web.bind.annotation.RestController;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

/**
 * カテゴリ情報の操作を行うREST APIコントローラクラス
 * 非同期通信によるカテゴリ一覧取得、新規追加、削除を制御する
 */
@RestController
public class CategoryApiController {
	// CategoriesRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private CategoriesRepository categoriesRepository;
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	
	/**
	 * 【画面表示】ログインユーザーのカテゴリ一覧をJSON形式で取得して画面に表示する
	 */
	@GetMapping("/api/categories")
	public List<CategoriesEntity> getCategories(
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// 空のリストを返す
			return List.of();
		}
		
		// 検索用変数生成
		UsersEntity user = new UsersEntity();
		// 検索用変数にユーザーIDをセットする
		user.setUserId(userId);
		
		// 該当するユーザーのカテゴリ一覧を返す
		return categoriesRepository.findByUser(user);
	}
	
	/**
	 * 【新規追加】カテゴリ情報を登録する
	 */
	@PostMapping("/api/categories")
	public ResponseEntity<?> addCategory(
			@RequestBody Map<String, String> body,
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// 401 Unauthorized エラーを返す
			return ResponseEntity.status(401).body("Unauthorized");
		}
		
		// データベースからユーザー情報を取得する
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		
		//ユーザー情報が存在しない場合
		if (user == null) {
			// 400 Bad Request エラーを返す
			return ResponseEntity.badRequest().body("User not found");
		}
		
		// 入力値（カテゴリ名）チェックを実施する
		String name = body.get("name");
		String type = body.get("type");
		
		if (name == null || name.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Category name is Required");
		}
		
		// カテゴリエンティティを生成、セットする
		CategoriesEntity category = new CategoriesEntity();
		
		// カテゴリエンティティの各項目を生成、セットする
		category.setName(name);
		category.setType(type);
		category.setUser(user);
		category.setCreatedAt(LocalDateTime.now());
		category.setUpdatedAt(LocalDateTime.now());
		
		// カテゴリ情報をデータベースに保存する
		categoriesRepository.save(category);
		
		// 200 OKを返す
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 【削除】指定されたIDのカテゴリを削除する
	 */
	@DeleteMapping("/api/categories/{id}")
	public ResponseEntity<?> deleteCategory(
			@PathVariable Integer id,
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// 401 Unauthorized エラーを返す
			return ResponseEntity.status(401).body("Unauthorized");
		}
		
		// 削除対象のカテゴリをデータベースから取得する
		CategoriesEntity category = categoriesRepository.findById(id).orElse(null);
		
		// 該当するカテゴリがない場合
		if (category == null) {
			// 404 Not Found エラーを返す
			return ResponseEntity.notFound().build();
		}
		
		// 他人のカテゴリを削除しようとした場合（所有者チェック）
		if (!category.getUser().getUserId().equals(userId)) {
			// 403 Forbidden エラーを返す
			return ResponseEntity.status(403).body("Forbidden");
		}
		
		// カテゴリを削除する
		categoriesRepository.deleteById(id);
		
		// 200 OK を返す
		return ResponseEntity.ok().build();
	}
}