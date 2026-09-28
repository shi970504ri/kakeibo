package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.kakeibo.dto.TransactionDto;
import jp.co.sss.kakeibo.entity.TransactionDetailsEntity;
import jp.co.sss.kakeibo.entity.TransactionsEntity;
import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.TransactionsRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

/**
 * 収支データの一覧表示及び削除を利を制御するコントローラクラス
 */
@Controller
public class ListController {
	// TransactionsRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private TransactionsRepository transactionsRepository;
	// UsersRepositoryクラスのインスタンスを自動注入（DI）する
	@Autowired
	private UsersRepository usersRepository;
	// 画像ファイルのアップロード先ディレクトリパス
	private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";
	
	/**
	 * 【画面表示】収支データの一覧画面を表示する（ページネーション、日付絞り込み対応）
	 */
	@GetMapping("/balance/list")
	public String listShow(
			// 表示対象のページを取得（デフォルト値：０）
			@RequestParam(name = "page", defaultValue = "0") int page,
			// 検索条件の日付を取得
			@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
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
		
		// ログインユーザーのエンティティ情報をデータベースから取得する
		UsersEntity user = usersRepository.findById(userId).orElse(null);
		
		// ユーザー情報を取得できなかった場合
		if (user == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// 1ページ当り20件取得するためのページネーション情報を生成する
		Pageable pageable = PageRequest.of(page, 20);
		
		// ページング結果を保有する変数を宣言する
		Page<TransactionsEntity> transactionPage;
		
		// 検索日付が指定されている場合
		if (date != null) {
			// ユーザーIDと指定日付で一致するデータを昇順で取得する
			transactionPage = transactionsRepository.findByUserAndDateOrderByTransactionIdAsc(user, date, pageable);
			
			// 選択中の日付をモデルに追加する
			model.addAttribute("selectedDate", date);
		
		// 検索結果が指定されていない場合
		} else {
			// ユーザーIDに紐づく全データを日付昇順で取得する
			transactionPage = transactionsRepository.findByUserOrderByDateAscTransactionIdAsc(user, pageable);
		}
		
		// 取得したページデータからエンティティのリストを取り出しDTOへ変換する
		List<TransactionDto> transactionDtoList = transactionPage.getContent().stream().map(t -> {
			// 収入合計金額保持用変数を初期化する
			int income = 0;
			// 支出合計金額保持用変数を初期化する
			int expense = 0;
			
			// 明細リストが存在する場合
			if (t.getDetails() != null) {
				// 各明細データをループ処理する
				for (TransactionDetailsEntity d : t.getDetails()) {
					// 明細タイプと金額が存在する場合
					if (d.getType() != null && d.getAmount() != null) {
						// 明細タイプの文字列から前後の空白を除去する
						String type = d.getType().trim();
						
						// 明細タイプが収入である場合
						if ("INCOME".equalsIgnoreCase(type) || "収入".equals(type) || "1".equals(type)) {
							// 収入金額に加算する
							income += d.getAmount();
							
						// 明細タイプが収入でない場合
						} else {
							// 支出金額に加算する
							expense += d.getAmount();
						}
					}
				}
			}
			
			// エンティティ、収入、支出、収支差額を保持するDTOオブジェクトを生成して返す
			return new TransactionDto(t, income, expense, income - expense);
		}).toList();
		
		// モデルにDTO変換後の収支リストを追加する
		model.addAttribute("transactionList", transactionDtoList);
		// モデルにページネーションを追加する
		model.addAttribute("transactionPage", transactionPage);
		// モデルにページ番号を追加する
		model.addAttribute("currentPage", page);
		// モデルに全ページ数を追加する
		model.addAttribute("totalPages", transactionPage.getTotalPages());
		// モデルにデータが空かどうかを示す判定結果を追加する
		model.addAttribute("isEmpty", transactionPage.isEmpty());
		
		// 一覧画面を表示する
		return "balance/list";
	}
	
	/**
	 * 【削除】指定された収支データ及び関連する画像ファイルを削除する
	 */
	@PostMapping("/balance/delete")
	public String deleteTransaction(
			@RequestParam("transactionId") Integer transactionId,
			HttpSession session
	) {
		// セッションからログインユーザーIDを取得する
		Integer userId = (Integer) session.getAttribute("userId");
		
		// ログインしていない場合
		if (userId == null) {
			// ログイン画面に強制リダイレクトする
			return "redirect:/user/login";
		}
		
		// 削除対象の収支データをデータベースから取得する
		TransactionsEntity transaction = transactionsRepository.findById(transactionId).orElse(null);
		
		// ログインユーザー本人の所有データが存在する場合
		if (transaction != null && transaction.getUser() != null
				&& transaction.getUser().getUserId() != null
				&& transaction.getUser().getUserId().equals(userId)) {
			// 紐づく画像ファイルが存在する場合は物理削除する
			deleteFileIfExists(transaction.getFile());
			
			// 収支データをデータベースから削除する
			transactionsRepository.delete(transaction);
		}
		// 一覧画面を再表示する
		return "redirect:/balance/list";
	}
	
	/**
	 * 【処理】サーバー上に保存されている画像ファイルを安全に物理削除する
	 */
	private void deleteFileIfExists(
			String fileName
	) {
		// 空文字でないファイル名が存在する場合
		if (fileName != null && !fileName.trim().isEmpty()) {
			// ファイル削除処理の例外発生に備えてtryブロックを開始する
			try {
				// ディレクトリパスとファイル名を結合し正規化した絶対パスを取得する
				Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).toAbsolutePath().normalize();
				
				// ファイル名が存在する場合に物理削除を実行する
				Files.deleteIfExists(filePath);
				
			// エラーが発生した場合
			} catch (IOException e) {
				// エラーのスタックトレースを出力する
				e.printStackTrace();
			}
		}
	}
}