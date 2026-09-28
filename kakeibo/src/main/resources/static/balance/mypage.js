/**
 * =========================================================
 * マイページ機能（パスワード表示切替）
 * =========================================================
 */

// グローバル空間にパスワード表示切り替え関数を定義
window.togglePassword = function() {
	// パスワード入力要素を取得する
	const passwordInput = document.getElementById("passwordInput");
	// 目のアイコン画像要素を取得する
	const eyeIcon = document.getElementById("eyeIcon");

	// どちらか存在しなければ処理終了する
	if (!passwordInput || !eyeIcon) {
		return
	};

	// data-open-icon 属性からアイコン画像URLを取得する
	const openIcon = eyeIcon.dataset.openIcon;
	// data-close-icon 属性からアイコン画像URLを取得する
	const closeIcon = eyeIcon.dataset.closeIcon;

	// 現在がパスワード伏字表示の場合
	if (passwordInput.type === "password") {
		// テキスト表示（可視化）に変更する
		passwordInput.type = "text";

		// アイコンを開いた目の画像に変更する
		eyeIcon.src = openIcon;

	// 現在がテキスト表示の場合
	} else {
		// パスワード表示（伏字）に戻す
		passwordInput.type = "password";

		// アイコンを閉じた目の画像に変更する
		eyeIcon.src = closeIcon;
	}
};