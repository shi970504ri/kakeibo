package jp.co.sss.kakeibo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class FooterControllersTest {
	@Test
	@DisplayName("footerController: /footer/footer で 'footer/footer' を返すこと")
	void testFooterController() throws Exception {
		//準備
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new footerController()).build();
		//実行
		ResultActions result = mockMvc.perform(get("/footer/footer"));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("footer/footer"));
	}
}