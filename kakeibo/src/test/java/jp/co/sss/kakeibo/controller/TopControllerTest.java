package jp.co.sss.kakeibo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class TopControllersTest {
	@Test
	@DisplayName("TopController: /balance/top で 'balance/top' を返すこと")
	void testTopController() throws Exception {
		//準備
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TopController()).build();
		//実行
		ResultActions result = mockMvc.perform(get("/balance/top"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("balance/top"));
	}
}