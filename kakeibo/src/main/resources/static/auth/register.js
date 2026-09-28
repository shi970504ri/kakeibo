/**
 * =========================================================
 * 新規登録入力の認証コード（６桁）バリデーション
 * =========================================================
 */

// class名「register_input」を持つ入力要素（新規登録用コード入力欄）を取得する
const codeInput = document.querySelector('.register_input'); 
// id名「auth_btn」を持つボタン要素を取得する
const verifyBtn = document.getElementById('auth_btn');
// 「半角数字の6桁」であるか判定するための正規表現を定義する
const codeRegex = /^\d{6}$/;

// 新規登録用の認証コードの入力チェックを行う関数を定義する
function validateCodeInput() {
	// 入力欄またはボタンが見つからない場合
	if (!codeInput || !verifyBtn) {
		// 処理をスキップして終了する
		return
	};
	
	// 入力値の前後の余白を除き、6桁数字ルールに合格しなければボタンを無効化にする
	verifyBtn.disabled = !codeRegex.test(codeInput.value.trim());
}

// 入力欄とボタンが存在する場合
if (codeInput && verifyBtn) {
	// 初期状態のチェックを実行する
	validateCodeInput();
	
	// 入力、変更、キー離下時のイベントを設定するためのループ
	['input', 'change', 'keyup'].forEach(eventType => {
		// イベント発生時に入力チェック関数を呼び出す
		codeInput.addEventListener(eventType, validateCodeInput);
	});
}