/**
 * =========================================================
 * 入力フォーム、明細行操作、非同期ファイルアップロード機能
 * =========================================================
 */

// メインファイルを非同期アップロードする関数を定義する
async function uploadMainFile(input) {
	// 選択された1つ目のファイルオブジェクトを取得する
	const file = input.files[0];

	// ファイルが選択されていない場合は処理を終了する
	if (!file) return;

	// 送信用ファイルパスを保持する隠し要素を取得する
	const hiddenInput = document.getElementById("mainFilePath");
	// 画面上のファイル名表示要素を取得する
	const fileNameDisplay = document.getElementById("fileNameDisplay");
	// 店舗名を入力する要素を取得する
	const storeInput = document.querySelector('input[name="storeName"]');
	// 店舗名要素が存在すれば前後空白を除去した値を取得し、存在しなければ空文字を設定する
	const storeName = storeInput ? storeInput.value.trim() : "";

	// フォームデータ送信用の FormData オブジェクトを生成する
	const formData = new FormData();
	// 送信データにファイルオブジェクトを追加する
	formData.append("file", file);
	// 送信データに店舗名を追加する
	formData.append("storeName", storeName);

	// コンテキストパス情報を持つ meta 要素を取得する
	const contextPathMeta = document.querySelector('meta[name="context-path"]');
	// meta 要素が存在する場合は content 属性の値を取得し、なければ '/' を設定する
	let contextPath = contextPathMeta ? contextPathMeta.getAttribute('content') : '/';

	// コンテキストパスの末尾が '/' で終わっていない場合
	if (!contextPath.endsWith('/')) {
		// 末尾に '/' を追加する
		contextPath += '/';
	}

	// 通信の例外発生に備えてtryブロックを開始する
	try {
		// 指定のURLへ POST メソッドでファイルデータを非同期送信する
		const response = await fetch(contextPath + "upload-file", {
			// リクエストメソッドに POST を指定する
			method: "POST",
			// リクエストボディに FormData を設定する
			body: formData
		});

		// サーバーからのレスポンスを JSON 形式として解析する
		const result = await response.json();

		// 通信レスポンスが正常（ステータス 200-299）の場合
		if (response.ok) {
			// サーバーから返却されたファイル名、または元のファイル名を隠し要素に設定する
			hiddenInput.value = result.fileName || file.name;

			// ファイル名表示要素が存在する場合
			if (fileNameDisplay) {
				// 表示要素に選択中のファイル名テキストを設定する
				fileNameDisplay.textContent = `選択中: ${file.name}`;
				// 表示要素の文字色を青色に設定する
				fileNameDisplay.style.color = "#0284c7";
			}

		// 通信レスポンスがエラーの場合
		} else {
			// サーバーのエラーメッセージまたはステータスコード付きエラーを表示する
			alert(result.error || "画像のアップロードに失敗しました (Status: " + response.status + ")");
			// ファイル選択欄の値をクリアする
			input.value = "";
			// 隠し要素のファイルパスをクリアする
			hiddenInput.value = "";

			// ファイル名表示要素が存在する場合
			if (fileNameDisplay) {
				// 表示テキストをクリアする
				fileNameDisplay.textContent = "";
			}
		}

	// 通信例外などのエラーが発生した場合
	} catch (error) {
		// コンソールにエラー詳細を出力する
		console.error(error);
		// エラー発生を通知するアラートを表示する
		alert("通信エラーが発生しました");
		// ファイル選択欄の値をクリアする
		input.value = "";
		// 隠し要素のファイルパスをクリアする
		hiddenInput.value = "";

		// ファイル名表示要素が存在する場合
		if (fileNameDisplay) {
			// 表示テキストをクリアする
			fileNameDisplay.textContent = "";
		}
	}
}

// フォーム送信時の入力検証を行うバリデーション関数を定義する
function validateForm(event) {
	// エラーメッセージを保持する空の配列を作成する
	let errors = [];

	// メインファイルのパスを取得し、前後空白を除去する
	const mainFilePath = document.getElementById("mainFilePath").value.trim();

	// ファイルパスが空（未アップロード）の場合
	if (mainFilePath === "") {
		// メインファイル未選択のエラーメッセージを追加する
		errors.push("【基本情報】添付画像を選択・アップロードしてください。");
	}

	// 画面上の全明細行要素を取得する
	const rows = document.querySelectorAll(".detail_row");
	// 正しく入力されている明細行の件数を保持する変数を初期化する
	let validCount = 0;

	// 各明細行要素に対して検証処理をループ実行する
	rows.forEach((row, index) => {
		// 行番号バッジが存在すればそのテキストを取得し、なければデフォルト番号を作成する
		const noText = row.querySelector(".no_badge") ? row.querySelector(".no_badge").textContent : `No.${index + 1}`;
		// 商品名入力欄の値を取得し、前後空白を除去する
		const itemName = row.querySelector(".item_name input").value.trim();
		// 金額入力欄の値を取得し、前後空白を除去する
		const amount = row.querySelector(".item_amount input").value.trim();
		// カテゴリ選択欄の値を取得し、前後空白を除去する
		const categoryId = row.querySelector(".item_category select").value.trim();
		// メモ入力欄の値を取得し、前後空白を除去する
		const memo = row.querySelector(".item_memo input").value.trim();

		// 行内のいずれかの項目に文字が入力されているか判定する
		const hasAnyInput = (itemName !== "" || amount !== "" || categoryId !== "" || memo !== "");

		// 1つでも入力項目が存在する場合
		if (hasAnyInput) {
			// 当該行の未入力項目名を格納する配列を作成する
			const missing = [];

			// 商品名が未入力の場合
			if (itemName === "") missing.push("商品名");
			// 金額が未入力の場合
			if (amount === "") missing.push("金額");
			// カテゴリが未選択の場合
			if (categoryId === "") missing.push("カテゴリ");

			// 未入力項目が存在する場合
			if (missing.length > 0) {
				// 未入力項目を列挙したエラーメッセージを追加する
				errors.push(`${noText}: 未入力項目があります（${missing.join("、")}）`);

			// すべての必須項目が入力されている場合
			} else {
				// 有効な明細行のカウントを1増やす
				validCount++;
			}
		}
	});

	// 有効な明細行が0件で、かつ他のエラーもない場合
	if (validCount === 0 && errors.length === 0) {
		// 明細行未入力のエラーメッセージを追加する
		errors.push("【詳細情報】明細を少なくとも1件は入力してください。");
	}

	// エラーが1件以上存在する場合
	if (errors.length > 0) {
		// 蓄積されたエラーメッセージを改行区切りでアラート表示する
		alert("【入力エラー】\n" + errors.join("\n"));
		// フォームの送信動作をキャンセルする
		event.preventDefault();

		// 検証失敗として false を返す
		return false;
	}

	// 検証成功として true を返す
	return true;
}

// 指定した件数分の明細行を動的に追加する関数を定義する
function addRows(count) {
	// 明細行を追加する親コンテナ要素を取得する
	const detailList = document.querySelector(".detail_list");
	// テンプレート要素からカテゴリの選択肢 HTML 文字列を取得する
	const categoryOptionsHtml = document.getElementById("categoryTemplate").innerHTML;

	// 指定された件数分だけループ処理を実行する
	for (let i = 0; i < count; i++) {
		// 新しい明細行となる <div> 要素を生成する
		const row = document.createElement("div");
		// 生成した要素にクラス名「detail_row」を設定する
		row.className = "detail_row";

		// 明細行要素の内部 HTML 構造を設定する
		row.innerHTML = `
			<div class="no_badge">No.000</div>
			<div class="item_name">
				<input type="text" name="" placeholder="商品名または項目" autocomplete="off">
			</div>
			<div class="item_amount">
				<input type="number" inputmode="numeric" name="" placeholder="0">
			</div>
			<div class="item_category">
				<select name="">
					${categoryOptionsHtml}
				</select>
			</div>
			<div class="item_memo">
				<input type="text" name="" placeholder="メモ" autocomplete="off">
			</div>
			<div class="item_delete">
				<button type="button" class="delete_btn" onclick="deleteRow(this)" title="行を削除">削除</button>
			</div>
		`;

		// 作成した明細行要素を親コンテナに追加する
		detailList.appendChild(row);
	}

	// 全明細行の番号と name 属性を更新する
	renumberRows();
}

// 削除ボタンが押された明細行を削除する関数を定義する
function deleteRow(button) {
	// 現在存在するすべての明細行要素を取得する
	const rows = document.querySelectorAll(".detail_row");

	// 明細行が1件以下の場合
	if (rows.length <= 1) {
		// 削除不可のアラートを表示する
		alert("最低1件は残して下さい");
		// 処理を中断する
		return;
	}

	// 押されたボタンの親要素にあたる明細行要素を DOM から削除する
	button.closest(".detail_row").remove();

	// 全明細行の番号と name 属性を更新する
	renumberRows();
}

// 全明細行の表示番号とフォーム送信用の name 属性を振り直す関数を定義する
function renumberRows() {
	// 現在存在するすべての明細行要素を取得する
	const rows = document.querySelectorAll(".detail_row");

	// 各明細行に対して順次番号振り直し処理を実行する
	rows.forEach((row, index) => {
		// 番号表示バッジ要素のテキストを3桁ゼロ埋め形式（例: No.001）で更新する
		row.querySelector(".no_badge").textContent = `No.${String(index + 1).padStart(3, "0")}`;

		// 商品名入力欄の name 属性をインデックス付きに再設定する
		row.querySelector(".item_name input").name = `details[${index}].itemName`;
		// 金額入力欄の name 属性をインデックス付きに再設定する
		row.querySelector(".item_amount input").name = `details[${index}].amount`;
		// カテゴリ選択欄の name 属性をインデックス付きに再設定する
		row.querySelector(".item_category select").name = `details[${index}].categoryId`;
		// メモ入力欄の name 属性をインデックス付きに再設定する
		row.querySelector(".item_memo input").name = `details[${index}].memo`;
	});
}

// ページ読み込み完了時の初期化処理を登録する
document.addEventListener("DOMContentLoaded", () => {
	// 現在存在する明細行要素をすべて取得する
	const currentRows = document.querySelectorAll(".detail_row");

	// 明細行が5件未満の場合
	if (currentRows.length < 5) {
		// 5件になるまで不足分を追加する
		addRows(5 - currentRows.length);

	// 5件以上存在する場合
	} else {
		// 既存行の番号と name 属性を最新状態に更新する
		renumberRows();
	}
});