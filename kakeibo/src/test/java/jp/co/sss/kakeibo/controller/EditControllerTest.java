package jp.co.sss.kakeibo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EditControllersTest {
	@Test
	@DisplayName("EditController: /balance/edit で 'balance/edit' を返すこと")
	void testEditController() throws Exception {
		//準備
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new EditController()).build();
		//実行
		ResultActions result = mockMvc.perform(get("/balance/edit"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("balance/edit"));
	}
}