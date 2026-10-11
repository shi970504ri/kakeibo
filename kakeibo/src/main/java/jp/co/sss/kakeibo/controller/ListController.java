package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * 収支データの一覧表示及び削除を制御するコントローラクラス
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
     * 【画面表示】収支データを検索・並び替えし、一覧画面を表示する
     */
    @GetMapping("/balance/list")
    public String listShow(
            // 表示対象のページを取得する
            @RequestParam(name = "page", defaultValue = "0") int page,
            // 検索対象の月を取得する
            @RequestParam(name = "month", required = false)
			// 検索対象の年月をyyyy-MM形式で取得する
            @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            // 従来機能との互換性のため特定日付を取得する
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            // 店名・商品名・項目・カテゴリなどの検索キーワードを取得する
            @RequestParam(name = "keyword", required = false) String keyword,
            // 指定された並び替え方法を取得する
            @RequestParam(name = "sort", defaultValue = "date") String sort,
			// セッション情報を取得する
            HttpSession session,
			// 画面表示用のモデル情報を取得する
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

        // キーワードが入力されている場合
        if (keyword != null) {
			// 前後の空白を取り除く
            keyword = keyword.trim();
        }

        // キーワードが空文字列の場合
        if (keyword != null && keyword.isEmpty()) {
			// nullに変換する
            keyword = null;
        }

        // 許可された並び替え方法以外の場合
        if (!List.of("date", "no", "amountAsc", "amountDesc").contains(sort)) {
			// 従来の並び順にする
            sort = "date";
        }

        // 検索期間の開始日を初期化する
        LocalDate startDate = null;
		// 検索期間の終了日を初期化する
        LocalDate endDate = null;

        // 従来の特定日付検索が指定されている場合
        if (date != null) {
			// その日だけを検索する
            startDate = date;
            endDate = date;

        // 月が指定されている場合
        } else if (month != null) {
			// その月の初日から末日を検索する
            startDate = month.atDay(1);
            endDate = month.atEndOfMonth();
        }

        // 1ページ当たり20件を表示するページネーション情報を生成する
        Pageable pageable = PageRequest.of(page, 20);

        // 検索条件に一致した取引IDをページ分けして取得する
        Page<Integer> transactionIdPage = transactionsRepository.searchTransactionIds(
            userId,
            startDate,
            endDate,
            keyword,
            sort,
            pageable
        );

        // 取得した取引IDに対応する取引情報をデータベースから取得する
        Iterable<TransactionsEntity> foundTransactions = transactionsRepository.findAllById(transactionIdPage.getContent());

        // 取引IDから取引情報を参照できるマップを作成する
        Map<Integer, TransactionsEntity> transactionMap = new HashMap<>();

		// 取引IDに対応する取引情報の件数分ループ処理を行う
        for (TransactionsEntity transaction : foundTransactions) {
			// 取引IDをキー、取引情報を値としてマップに追加する
            transactionMap.put(transaction.getTransactionId(), transaction);
        }

        // ID検索結果の順序を維持して、一覧表示用の取引リストを作成する
        List<TransactionsEntity> transactions = new ArrayList<>();
		//　取得した取引IDの件数分ループ処理を行う
        for (Integer transactionId : transactionIdPage.getContent()) {
			// 取引IDに対応する取引情報をマップから取得する
            TransactionsEntity transaction = transactionMap.get(transactionId);
			// 取引情報が存在する場合
            if (transaction != null) {
				// リストに追加する
                transactions.add(transaction);
            }
        }

        // 取引リストとページ情報を組み合わせてページング結果を作成する
        Page<TransactionsEntity> transactionPage = new PageImpl<>(
            transactions,
            pageable,
            transactionIdPage.getTotalElements()
        );

        // 取引情報を画面表示用DTOに変換する
        List<TransactionDto> transactionDtoList = transactionPage.getContent().stream().map(transaction -> {
            // 収入合計金額を初期化する
            int income = 0;
            // 支出合計金額を初期化する
            int expense = 0;

            // 明細リストが存在する場合
            if (transaction.getDetails() != null) {
				// 明細リストの件数分ループ処理を行う
                for (TransactionDetailsEntity detail : transaction.getDetails()) {
                    // 明細タイプと金額が存在する場合
                    if (detail.getType() != null && detail.getAmount() != null) {
                        // 明細タイプの前後の空白を取り除く
                        String type = detail.getType().trim();

                        // 収入タイプの明細の場合
                        if ("INCOME".equalsIgnoreCase(type)
                                || "収入".equals(type)
                                || "1".equals(type)) {
							// 収入合計に加算する
                            income += detail.getAmount();

                        // 収入以外の明細は支出合計に加算する
                        } else {
							// 支出合計に加算する
                            expense += detail.getAmount();
                        }
                    }
                }
            }

            // 取引情報と集計結果を画面表示用DTOに変換する
            return new TransactionDto(transaction, income, expense, income - expense);
        }).toList();

        // 一覧表示用の収支リストをモデルに追加する
        model.addAttribute("transactionList", transactionDtoList);
        // ページネーション情報をモデルに追加する
        model.addAttribute("transactionPage", transactionPage);
        // 現在のページ番号をモデルに追加する
        model.addAttribute("currentPage", page);
        // 全ページ数をモデルに追加する
        model.addAttribute("totalPages", transactionPage.getTotalPages());
        // 検索結果が空かどうかをモデルに追加する
        model.addAttribute("isEmpty", transactionPage.isEmpty());
        //　検索対象の年月をモデルに追加する
        model.addAttribute("selectedDate", date);
		// 検索対象の年をモデルに追加する
        model.addAttribute("selectedMonth", month);
		// 検索キーワードをモデルに追加する
        model.addAttribute("selectedKeyword", keyword);
		// 並び替え方法をモデルに追加する
        model.addAttribute("selectedSort", sort);

        // 収支一覧画面を表示する
        return "balance/list";
    }

    /**
     * 【削除】指定された収支データ及び関連する画像ファイルを削除する
     */
    @PostMapping("/balance/delete")
    public String deleteTransaction(
			// 削除対象の収支データの取引IDを取得する
            @RequestParam("transactionId") Integer transactionId,
			// セッション情報を取得する
            HttpSession session
    ) {
        // セッションからログインユーザーIDを取得する
        Integer userId = (Integer) session.getAttribute("userId");

        // ログインしていない場合はログイン画面に遷移する
        if (userId == null) {
            return "redirect:/user/login";
        }

        // 削除対象の収支データをデータベースから取得する
        TransactionsEntity transaction = transactionsRepository.findById(transactionId).orElse(null);

        // ログインユーザー本人の所有データが存在する場合
        if (transaction != null
                && transaction.getUser() != null
                && transaction.getUser().getUserId() != null
                && transaction.getUser().getUserId().equals(userId)) {
            // 紐づく画像ファイルを物理削除する
            deleteFileIfExists(transaction.getFile());

            // 収支データをデータベースから削除する
            transactionsRepository.delete(transaction);
        }

        // 一覧画面に戻る
        return "redirect:/balance/list";
    }

    /**
     * 【処理】サーバー上に保存されている画像ファイルを物理削除する
     */
    private void deleteFileIfExists(String fileName) {
        // 空でないファイル名が指定されている場合
        if (fileName != null && !fileName.trim().isEmpty()) {
			// 画像ファイル保存の例外発生に備えてtryブロックを開始する
            try {
                // 保存先ディレクトリとファイル名から正規化した絶対パスを作成する
                Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).toAbsolutePath().normalize();

                // 対象ファイルが存在する場合に削除する
                Files.deleteIfExists(filePath);

            // ファイル削除でエラーが発生した場合はスタックトレースを出力する
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}