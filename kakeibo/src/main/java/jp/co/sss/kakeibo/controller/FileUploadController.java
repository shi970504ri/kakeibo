package jp.co.sss.kakeibo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

@RestController
public class FileUploadController {
	private static final String UPLOAD_DIR = "uploads/";
	@PostMapping("/upload-file")
	public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
		Map<String, String> response = new HashMap<>();
		if (file.isEmpty()) {
			response.put("error", "ファイルが空です");
			return ResponseEntity.badRequest().body(response);
		}
		try {
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			String savedFileName = UUID.randomUUID().toString() + extension;
			Path filePath = uploadPath.resolve(savedFileName);
			String contentType = file.getContentType();
			if (contentType != null && contentType.startsWith("image/")) {
				try {
					Thumbnails.of(file.getInputStream())
						.width(1200)
						.outputQuality(0.8)
						.toFile(filePath.toFile());
				} catch (Exception e) {
					Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				}
			} else {
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			response.put("fileName", savedFileName);
			return ResponseEntity.ok(response);
		} catch (IOException e) {
			e.printStackTrace();
			response.put("error", "ファイル保存失敗");
			return ResponseEntity.internalServerError().body(response);
		}
	}
}