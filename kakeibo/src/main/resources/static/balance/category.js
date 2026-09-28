/**
 * =========================================================
 * 家計簿カテゴリの追加･取得･削除処理
 * =========================================================
 */

// DOM（HTML）の読込みが完全に完了したタイミングで実行する
document.addEventListener("DOMContentLoaded", () => {
	// カテゴリ一覧をサーバーから取得する
	fetchCategories();
	
	// カテゴリ追加フォーム要素を取得する
	const addForm = document.getElementById("addCategoryForm");
	
	// フォーム送信（submit）時のイベントを登録する
	addForm.addEventListener("submit", function(event) {
		// フォーム送信による通常のページリロードを止める
		event.preventDefault();
		
		// カテゴリ名入力欄を取得する
		const nameInput = document.getElementById("categoryName");
		// カテゴリタイプ選択欄を取得する
		const typeInput = document.getElementById("categoryType");
		
		// 新規カテゴリ追加APIへデータをリクエストする
		fetch("/kakeibo/api/categories", {
			// POSTメソッドで送信する
			method: "POST",
			// 送信データをJSON形式と指定する
			headers: {
				"Content-Type": "application/json"
			},
			// 入力された値をJSON形式に変換する
			body: JSON.stringify({
				name: nameInput.value,
				type: typeInput.value
			})
		})
		
		// レスポンスを受け取ったときの処理を記載する
		.then(async response => {
			// 認証エラー（セッション切れ）の場合
			if (response.status === 401) {
				// 警告を表示する
				alert("セッションが切れました。ログイン画面へ移動します。");
				
				// ログイン画面へ強制リダイレクトする
				window.location.href = "/kakeibo/user/login";
				
				// 処理を中断する
				return;
			}
			
			// リクエスト成功（ステータス200系）の場合
			if (response.ok) {
				// カテゴリ名入力欄を空にする
				nameInput.value = "";
				
				// カテゴリ一覧を再取得して表示を最新化する
				fetchCategories();
			
			// エラーレスポンスの場合
			} else {
				// エラー詳細テキストを取得する
				const errorText = await response.text();
				
				// エラーメッセージを表示する
				alert(`追加に失敗しました。(ステータスコード: ${response.status})`);
				
				// コンソールに詳細ログを出力する
				console.error("エラー詳細:", response.status, errorText);
			}
		})
		
		// ネットワークエラー時のログを出力する
		.catch(error => console.error("Error:", error));
	});
});


// カテゴリ一覧をサーバーから取得して画面に表示する関数
function fetchCategories() {
	// カテゴリ取得APIにGETリクエストする
	fetch("/kakeibo/api/categories").then(response => {
		// 未ログインの場合
		if (response.status === 401) {
			// ログイン画面へ移動する
			window.location.href = "/kakeibo/user/login";

			// エラーを投げて後続処理をストップする
			throw new Error("Unauthorized");
		}

		// レスポンスのJSONデータを解析して次へ渡す
		return response.json();
	})

	.then(data => {
		// 支出カテゴリ表示用リスト要素を取得する
		const expenseList = document.getElementById("expenseList");
		// 収入カテゴリ表示用リスト要素を取得する
		const incomeList = document.getElementById("incomeList");

		// 支出リストの中身を全クリアする
		expenseList.innerHTML = "";

		// 収入リストの中身を全クリアする
		incomeList.innerHTML = "";

		// 取得したカテゴリデータを1つずつループ処理する
		data.forEach(category => {
			// <li> 要素を作成する
			const li = document.createElement("li");
			// <span> 要素を作成する
			const span = document.createElement("span");

			// カテゴリ名をセットする
			span.textContent = category.name + " ";

			// <li> の中に <span> を追加する
			li.appendChild(span);

			// 削除ボタン要素を作成する
			const delete_btn = document.createElement("button");

			// ボタンテキストを「削除」に指定する
			delete_btn.textContent = "削除";
			// 「delete_btn」クラスを付与する
			delete_btn.classList.add("delete_btn");
			// クリック時に deleteCategory 関数を呼び出すよう設定する
			delete_btn.onclick = () => deleteCategory(category.categoryId); 

			// <li> の中に削除ボタンを追加する
			li.appendChild(delete_btn);

			// カテゴリ種別が支出の場合
			if (category.type === "expense") {
				// 支出用クラスを付与する
				li.classList.add("expense_list");

				// 支出リストに追加する
				expenseList.appendChild(li);
			} else if (category.type === "income") {

				// 収入用クラスを付与する
				li.classList.add("income_list");

				// 収入リストに追加する
				incomeList.appendChild(li);
			}
		});
	})

	// エラー時のログ出力する
	.catch(error => console.error("Error:", error));
}

// 指定されたIDのカテゴリを削除する関数
function deleteCategory(id) {
	// 確認ダイアログで「キャンセル」なら処理中断する
	if (!confirm("本当に削除しますか？")) {
		return
	};

	// 該当IDのカテゴリ削除APIへリクエストする
	fetch(`/kakeibo/api/categories/${id}`, {
		// DELETEメソッドを指定する
		method: "DELETE"
	})

	// 認証切れの場合
	.then(response => {
		if (response.status === 401) {
			alert("セッションが切れました。ログイン画面へ移動します。");

			window.location.href = "/kakeibo/user/login";

			return;
		}

		// 削除成功の場合
		if (response.ok) {
			// 最新のカテゴリ一覧を再読み込み
			fetchCategories();

		// 削除失敗の場合
		} else {
			alert("削除に失敗しました。");
		}
	})
	.catch(error => {
		// エラーログ出力
		console.error("Error:", error);

		// 警告表示
		alert("エラーが発生しました。");
	});
}