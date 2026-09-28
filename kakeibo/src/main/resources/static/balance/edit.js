/**
 * =========================================================
 * 画面プレビュー＆行削除処理
 * =========================================================
 */

// 現在選択されているファイルIDを保持する変数を作成する
let currentFileId = "";
// ページ全体の読み込み完了時のイベント処理
window.onload = () => {
	// id名「reUploadBtn」のボタンにクリックイベントを登録する
	document.getElementById("reUploadBtn").addEventListener("click", () => {
		// 該当ファイル入力要素を呼び出す
		document.getElementById(`file${currentFileId}`).click();
	});
}

// 指定された番号のファイル画像を取得してモーダル表示する関数
async function showFile(no) {
	// 選択されたファイルIDを保持する
	currentFileId = no;
	
	// 画像取得APIの非同期化リクエストの例外発生に備えてtryブロックを開始する
	try {
		// 画像取得APIへ非同期リクエストする
		const response = await fetch(`/image/${no}`);
		// 結果をJSONとして解析する
		const result = await response.json();

		// プレビュー画像要素の src に画像URLを設定する
		document.getElementById("previewFile").src = result.imageUrl;
		// モーダルを表示状態（flex）にする
		document.getElementById("fileModal").style.display = "flex";
	}
	catch(error) {
		// エラー出力する
		console.error(error);

		// 失敗アラートを表示する
		alert("画像取得失敗");
	}
}

// 画像プレビューモーダルを閉じる関数
function closeModal() {
	// モーダルを非表示にする
	document.getElementById("fileModal").style.display = "none";
}

// 再アップロードボタンクリック時に対応する input[type="file"] をクリック発火させる
document.getElementById("reUploadBtn").addEventListener("click", () => {
	document.getElementById(`file${currentFileId}`).click();
})

// テーブル等の明細行を削除する関数
function deleteRow(button) {
	// クラス名「detail_row」を持つ全行を取得する
	const rows = document.querySelectorAll(".detail_row");

	// 残り1行以下の場合
	if (rows.length <=1) {
		// アラートを表示する
		alert("最低1件は残して下さい");

		// 処理を中断
		return; 
	}

	// クリックされたボタンに最も近い親要素「.detail_row」を削除する
	button.closest(".detail_row").remove(); 
}