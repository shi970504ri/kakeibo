package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 非同期ファイルアップロードを制御するAPIコントローラクラス
 */
@RestController
public class FileUploadController {
	// アップロード用ディレクトリパス（InputControllerと統一を推奨）
	private static final String UPLOAD_DIR = "uploads/";
	
	/**
	 * 【API】ファイルアップロード処理を行い、保存されたファイル名を返す
	 * 画面から送信された画像の受領、リサイズ、圧縮処理、及びサーバー内への保存を行う
	 */
	@PostMapping("/upload-file")
	public ResponseEntity<Map<String, String>> handleFileUpload(
			// 必須のアップロードファイルを受け取る
			@RequestParam("file") MultipartFile file,
			// 任意の店舗名を受け取る（未入力なら空文字）
			@RequestParam(name = "storeName", required = false, defaultValue = "") String storeName,
			// 任意の品名を受け取る（未入力なら空文字）
			@RequestParam(name = "itemName", required = false, defaultValue = "") String itemName
	) {		
		// 画面へ返却するJSONデータを格納するマップを生成する
		Map<String, String> response = new HashMap<>();
		
		// 送信されたファイルが空である場合
		if (file.isEmpty()) {
			// レスポンスMapにエラーメッセージを設定する
			response.put("error", "ファイルが空です");
			
			// 400 Bad Request エラーを返す
			return ResponseEntity.badRequest().body(response);
		}
		
		// ファイル保存処理の例外発生に備えてtryブロックを開始する
		try {
			// 保存先のフォルダパスを取得する
			Path uploadPath = Paths.get(UPLOAD_DIR);
			
			// 保存先フォルダが存在しない場合
			if (!Files.exists(uploadPath)) {
				// 新規作成する
				Files.createDirectories(uploadPath);
			}
			
			// 送信された元のファイル名を取得する
			String originalFilename = file.getOriginalFilename();
			// 拡張子保持用の変数を空文字で初期化する
			String extension = "";
			
			// "."が含まてれいるファイル名が存在する
			if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
				// ファイル名から拡張子部分を切り出して取得する
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			
			// 現在日時を「yyyyMMdd_HHmm」形式の文字列として生成する
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
			// 店舗名からファイル名に使えない不適切な記号をアンダースコアに置換してトリムする
			String safeStoreName = storeName.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
			// 品名からファイル名に使えない不適切な記号をアンダースコアに置換してトリムする
			String safeItemName = itemName.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
			
			// 店舗名が空文字の場合はデフォルト値"store"を設定する
			if (safeStoreName.isEmpty()) safeStoreName = "store";
			
			// 品名が空文字の場合はデフォルト値"item"を設定する
			if (safeItemName.isEmpty()) safeItemName = "item";
			
			// 「日時_店舗名_品名.拡張子」のフォーマットで保存用ファイル名を生成する
			String savedFileName = String.format("%s_%s_%s%s", timestamp, safeStoreName, safeItemName, extension);
			
			// 保存先フォルダとファイル名を結合して古パスを生成する
			Path filePath = uploadPath.resolve(savedFileName);
			
			// 同名のファイル名が存在する場合
			if (Files.exists(filePath)) {
				// エラーメッセージをMapに設定する
				response.put("error", "選択した画像は既に保存されています。");
				// 400 Bad Request エラーを返す
				return ResponseEntity.badRequest().body(response);
			}
			
			// アップロードされたファイルﾉMIMEタイプを取得する
			String contentType = file.getContentType();
			
			// 「image/」で始まるMIMEタイプが存在する場合
			if (contentType != null && contentType.startsWith("image/")) {
				// 画像リサイズ、圧縮処理を実行する
				try {
					// 画像の幅を1200px、画質を80%にリサイズしてファイルに保存する
					Thumbnails.of(file.getInputStream()).width(1200).outputQuality(0.8).toFile(filePath.toFile());
				// エラーになった際
				} catch (Exception e) {
					// ファイルをそのまま直接コピーして保存する
					Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				}
				// 「image/」で始まるMIMEタイプが存在しない場合
			} else {
				// 画像以外のファイルは直接コピーして保存する
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			
			// レスポンスMapに保存完了したファイル名を設定する
			response.put("fileName", savedFileName);
			
			// 200 OK エラーを返す
			return ResponseEntity.ok(response);

		} catch (IOException e) {
			// 例外のスタックトレースを出力する
			e.printStackTrace();
			
			// レスポンスMapに保存失敗エラーメッセージを設定する
			response.put("error", "ファイル保存失敗");
			
			// 500 Internal Sever Error でエラーレスポンスを返す
			return ResponseEntity.internalServerError().body(response);
		}
	}
}