package jp.co.sss.kakeibo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PrivacyControllerTest {
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new PrivacyController()).build();
	}
	@Test
	@DisplayName("プライバシーポリシー画面表示: modal=false (デフォルト) の場合は通常ビューを返すこと")
	void testPrivacyShowDefault() throws Exception {
		//準備なし)
		//実行
		ResultActions result = mockMvc.perform(get("/another/privacy"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("another/privacy"));
	}
	@Test
	@DisplayName("プライバシーポリシー画面表示: modal=true の場合はフラグメント用のビュー名を返すこと")
	void testPrivacyShowModal() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/another/privacy").param("modal", "true"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("another/privacy :: privacy_content"));
	}
}