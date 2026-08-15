package jp.co.sss.kakeibo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.kakeibo.entity.UsersEntity;
import jp.co.sss.kakeibo.repository.CategoriesRepository;
import jp.co.sss.kakeibo.repository.TransactionDetailsRepository;
import jp.co.sss.kakeibo.repository.TransactionsRepository;
import jp.co.sss.kakeibo.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
class InputControllerTest {
	@Mock
	private CategoriesRepository categoriesRepository;
	@Mock
	private UsersRepository usersRepository;
	@Mock
	private TransactionsRepository transactionsRepository;
	@Mock
	private TransactionDetailsRepository transactionDetailsRepository;
	@InjectMocks
	private InputController inputController;
	private MockMvc mockMvc;
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(inputController).build();
	}
	@Test
	@DisplayName("入力画面表示: 未ログイン時に /user/login へリダイレクト")
	void testInputShowUnauthenticated() throws Exception {
		//準備(なし)
		//実行
		ResultActions result = mockMvc.perform(get("/balance/input"));

		//検証
		result.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/login"));
	}
	@Test
	@DisplayName("入力画面表示: ログイン時に画面を表示しフォームとカテゴリー一覧を設定")
	void testInputShowSuccess() throws Exception {
		//準備
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("userId", 1);
		UsersEntity user = new UsersEntity();
		when(usersRepository.findById(1)).thenReturn(Optional.of(user));
		when(categoriesRepository.findByUser(user)).thenReturn(List.of());
		//実行
		ResultActions result = mockMvc.perform(get("/balance/input").session(session));
		//検証
		result.andExpect(status().isOk()).andExpect(view().name("balance/input")).andExpect(model().attributeExists("categories", "transactionForm"));
	}
}