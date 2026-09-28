/**
 * =========================================================
 * 画像モーダル表示＆パスワード表示切替
 * =========================================================
 */

// 指定した画像URLでモーダルを表示する関数
function showImage(imageSrc) {
	// 画像URLが空の場合
	if (!imageSrc) {
		// 次の処理に進む
		return
	};

	// 画像用モーダル要素を取得する
	var modal = document.getElementById("imageModal");
	// モーダル内の画像表示要素を取得する
	var modalImg = document.getElementById("modalImg");

	// 画像の表示元を設定する
	modalImg.src = imageSrc;

	// モーダルを表示する
	modal.style.display = "flex";
}

// 画像用モーダルを非表示にする関数
function closeImage() {
	// 画像用モーダル要素を取得する
	var modal = document.getElementById("imageModal");

	// 非表示に設定する
	modal.style.display = "none";
}