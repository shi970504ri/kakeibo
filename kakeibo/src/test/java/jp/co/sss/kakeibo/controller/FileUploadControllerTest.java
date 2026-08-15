package jp.co.sss.kakeibo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class FileUploadControllerTest {
	private FileUploadController fileUploadController;
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		fileUploadController = new FileUploadController();
		mockMvc = MockMvcBuilders.standaloneSetup(fileUploadController).build();
	}
	@Test
	@DisplayName("ファイルアップロード: 空ファイル送信時に400エラーが返ること")
	void testHandleFileUploadEmpty() throws Exception {
		//準備
		MockMultipartFile emptyFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);
		//実行
		ResultActions result = mockMvc.perform(multipart("/upload-file").file(emptyFile));
		//検証
		result.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("ファイルが空です"));
	}
	@Test
	@DisplayName("ファイルアップロード: 正常ファイル送信時に保存後のファイル名が返ること")
	void testHandleFileUploadSuccess() throws Exception {
		//準備
		MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
		//実行
		ResultActions result = mockMvc.perform(multipart("/upload-file").file(file));
		//検証
		result.andExpect(status().isOk()).andExpect(jsonPath("$.fileName").exists());
	}
}