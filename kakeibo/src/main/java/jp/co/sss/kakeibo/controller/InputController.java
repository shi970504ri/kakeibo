package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.kakeibo.entity.CategoriesEntity;
import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;
import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.form.TransactionDetailForm;
import jp.co.sss.kakeibo.form.TransactionForm;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.TransactionDetailsRepository;
import jp.co.sss.kakeibo.repository.TransactionsRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;


/**
 * 入力された収支データ及明細データを保存、画面表示するコントローラメソッド
 */
@Controller
@RequestMapping("/balance")
public class InputController {
	// TransactionsRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private TransactionsRepository transactionsRepository;
	// TransactionDetailsRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private TransactionDetailsRepository transactionDetailsRepository;
	// CategoriesRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private CategoriesRepository categoriesRepository;
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	
	//画像ファイルのアップロード先ディレクトリパス
	private final Path uploadDir = Paths.get("src/main/resources/static/uploads");
	
	/**
	 * 【画面表示】収支データの新規入力画面を表示する
	 */
	@GetMapping("/input")
	public String showInputForm(
			HttpSession session,
			Model model
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// 収支情報を取得する
		TransactionForm transactionForm = new TransactionForm();
		
		// 明細フォームに明細リストをセットする
		List<TransactionDetailForm> details = new ArrayList<>();
		
		// 初期表示用に空の明細フォームを1件追加する
		details.add(new TransactionDetailForm());
		
		// 収支フォームに明細リストをセットする
		transactionForm.setDetails(details);
		
		// 収支フォームオブジェクトをモデルに追加する
		model.addAttribute("transactionForm", transactionForm);
		
		// 全カテゴリ一覧を取得してモデルに追加する
		model.addAttribute("categories", categoriesRepository.findAll());
		
		// 収支入力画面を表示する		
		return "balance/input";
	}
	
	/**
	 * 【登録】入力された収支データ及び明細データを保存する
	 */
	@PostMapping("/save")
	public String saveTransaction(
			@Validated @ModelAttribute("transactionForm") TransactionForm transactionForm,
			BindingResult bindingResult,
			HttpSession session,
			Model model
	) {
		// // セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// もし入力チェックでエラーが発生した場合
		if (bindingResult.hasErrors()) {
			// モデルに全カテゴリ一覧を再セットする
			model.addAttribute("categories", categoriesRepository.findAll());
			
			// 収支入力画面を再表示する
			return "balance/input";
		}
		
		// ログインユーザーのエンティティ情報を取得する
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		
		// フォームからアップロードされた仮ファイル名を取得する
		String originalFile = transactionForm.getFile();
		
				// 正式保存用ファイル名保持変数を初期化する
		String finalFileName = null;
		
		// ファイル名がアップロードされている場合
		if (originalFile != null && !originalFile.isBlank()) {
			// 拡張子保存用変数を空文字で初期化する
			String ext = "";
			
			// ファイル名の中のドットの位置を取得する
			int dotIndex = originalFile.lastIndexOf(".");
			
			// ファイル名に拡張子が含まれている場合
			
			if (dotIndex != -1) {
				// ファイル名から拡張子部分抽出する
				ext = originalFile.substring(dotIndex);
			}
			
			// 現在日付を「yyyyMMdd」形式の文字列で取得する
			String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			// 現在時刻を「HHmmss」形式の文字列で取得する
			String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
			// 店舗名を取得し、未入力の場合は"store"を設定する
			String storeName = transactionForm.getStoreName() != null ? transactionForm.getStoreName() : "store";
			// 重複防止用の8桁のランダムキーを生成する
			String uniqueKey = UUID.randomUUID().toString().substring(0, 8);
			
			// 「日付_時刻_店舗名_ユニークキー.拡張子」の形式で正式ファイル名を生成する
			finalFileName = dateStr + "_" + timeStr + "_" + storeName + "_" + uniqueKey + ext;
			
			// 仮ファイル名の旧パスを取得する
			Path oldPath = uploadDir.resolve(originalFile);
			
			// 正式ファイルの新しい保存パスを取得する
			Path newPath = uploadDir.resolve(finalFileName);
			
			// 正式ファイルが指定ディレクトリに存在する場合
			if (Files.exists(oldPath)) {
				// ファイル移動処理の例外発生に備えてtryブロックを開始する
				try {
					// 仮ファイル名を正式ファイル名に変更して移動する
					Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
					
				// エラーが発生した場合
				} catch (IOException e) {
					// エラーのスタックトレースを出力する
					e.printStackTrace();
					
					// リネーム失敗時は元のファイル名をセットする
					finalFileName = originalFile;
				}
			
			// 正式ファイル名が指定ディレクトリに存在しない場合
			} else {
				// 元の仮ファイル名をそのまま利用する
				finalFileName = originalFile;
			}
		}
		
		// 現在のシステムの日時を取得する
		LocalDateTime now = LocalDateTime.now();
		
		// 収支親エンティティのインスタンスを生成する
		TransactionsEntity transactionEntity = new TransactionsEntity();
		
		// 収支親エンティティにログインユーザー情報をセットする
		transactionEntity.setUser(user);
		// 収支親エンティティに対象日時をセットする
		transactionEntity.setDate(transactionForm.getTargetDate());
		// 収支親エンティティに店舗名をセットする
		transactionEntity.setStoreName(transactionForm.getStoreName());
		// 収支親エンティティに正式ファイル名をセットする
		transactionEntity.setFile(finalFileName);
		// 収支親エンティティに全体メモをセットする
		transactionEntity.setMemo(transactionForm.getMemo());
		// 収支親エンティティに作成日時をセットする
		transactionEntity.setCreatedAt(now);
		// 収支親エンティティに更新日時をセットする
		transactionEntity.setUpdatedAt(now);
		
		// 収支親エンティティをデータベースに保存する
		TransactionsEntity savedTransaction = transactionsRepository.save(transactionEntity);
		
		// フォームに明細データが存在する場合
		if (transactionForm.getDetails() != null) {
			// 保存用の明細エンティティリストを生成する
			List<TransactionDetailsEntity> detailEntities = new ArrayList<>();
			
			// 入力された明細フォームの各行をループ処理する
			for (TransactionDetailForm detailForm : transactionForm.getDetails()) {
				// 品名と金額の両方が空の入力行である場合
				if ((detailForm.getItemName() == null || detailForm.getItemName().isBlank()) && detailForm.getAmount() == null) {
					// 登録対象外としてスキップする
					continue;
				}
				
				// 明細エンティティのインスタンスを生成する
				TransactionDetailsEntity detailEntity = new TransactionDetailsEntity();
				
				// 明細エンティティに親の収支エンティティを紐づける
				detailEntity.setTransaction(savedTransaction);
				// 明細エンティティに品名をセットする
				detailEntity.setItemName(detailForm.getItemName());
				// 明細エンティティに金額をセットする
				detailEntity.setAmount(detailForm.getAmount());
				
				// カテゴリIDが選択された場合
				if (detailForm.getCategoryId() != null) {
					// カテゴリIDでDBからカテゴリ情報を取得する
					CategoriesEntity cat = categoriesRepository.findById(detailForm.getCategoryId()).orElse(null);
					
					// 明細エンティティにカテゴリ情報をセットする
					detailEntity.setCategory(cat);
					
					// カテゴリ情報が存在する場合
					if (cat != null) {
						// カテゴリに設定された収支区分（収入/支出）をセットする
						detailEntity.setType(cat.getType());
					
					// カテゴリ情報が存在しない場合
					} else {
						// デフォルトで支出（EXPENSE）をセットする
						detailEntity.setType("EXPENSE");
					}
				// カテゴリIDが選択されない場合
				} else {
					// デフォルトで支出（EXPENSE）をセットする
					detailEntity.setType("EXPENSE");
				}
				
				// 明細エンティティに明細メモをセットする
				detailEntity.setMemo(detailForm.getMemo());
				// 明細エンティティに作成日時をセットする
				detailEntity.setCreatedAt(now);
				// 明細エンティティに更新日時をセットする
				detailEntity.setUpdatedAt(now);
				
				// 保存用リストに明細エンティティを追加する
				detailEntities.add(detailEntity);
			}
			
			// 明細エンティティのリストを一括してデータベースに保存する
			transactionDetailsRepository.saveAll(detailEntities);
		}
		
		// 登録完了後に入力画面へリダイレクトする
		return "redirect:/balance/input";
	}
}