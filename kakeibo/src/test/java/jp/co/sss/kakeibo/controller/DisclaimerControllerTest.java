package jp.co.sss.kakeibo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class DisclaimerControllerTest {
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new DisclaimerController()).build();
	}
	@Test
	@DisplayName("免責事項画面表示: modal=false (デフォルト) の場合は通常ビューを返すこと")
	void testDisclaimerShowDefault() throws Exception {
		//準備なし)
		//実行
		ResultActions result = mockMvc.perform(get("/another/disclaimer"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("another/disclaimer"));
	}
	@Test
	@DisplayName("免責事項画面表示: modal=true の場合はフラグメント用のビュー名を返すこと")
	void testDisclaimerShowModal() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/another/disclaimer").param("modal", "true"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("another/disclaimer :: disclaimer_content"));
	}
}