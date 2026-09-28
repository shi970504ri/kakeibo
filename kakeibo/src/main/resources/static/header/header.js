/**
 * =========================================================
 * 検索処理＆ログインフォームバリデーション
 * =========================================================
 */

// id名「searchBtn」のボタンクリック時に検索条件をアラート表示する処理
document.getElementById("searchBtn").addEventListener("click", function() {
	// キーワード選択値を取得する
	const keyword = document.getElementById("keywordSelect").value;
	// 月選択値を取得する
	const month = document.getElementById("monthSelect").value;

	// コンソールログに入力された検索キーワードを出力する
	console.log("検索キーワード；", keyword);
	// コンソールログに選択された月を出力する
	console.log("選択月：", month);

	// 確認用アラートダイアログを表示する
	alert("検索実行\n\n" + "キーワード：" + keyword + "\n月：" + month);
})