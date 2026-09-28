/**
 * =========================================================
 * ログイン画面表示バリデーション･パスワード表示切替
 * =========================================================
 */

// メールアドレス入力欄を取得する
const emailInput = document.querySelector('.email_input');
// パスワード入力欄を取得する
const passwordInput = document.querySelector('.password_input');
// メールエラー表示領域を取得する
const emailErrorDiv = document.querySelector('.email_error');
// 送信ボタンを取得する
const submitBtn = document.getElementById('submit_btn');
// メールアドレスフォーマットチェック用の正規表現する
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// メールアドレスの形式チェック関数
function checkEmail() {
	// 入力があり、形式が不正な場合
	if (emailInput.value !== '' && !emailRegex.test(emailInput.value)) {
		// エラーメッセージを返す
		return "メールアドレスの形式が正しくありません。";
	}

	// 問題ない場合は空文字を返す
	return "";
}

// フォーム入力完了状態の判定関数
function validateForm() {
	// 必須要素がない場合
	if (!emailInput || !passwordInput || !submitBtn) {
		// 処理をスキップして終了する
		return
	};

	// メールアドレスが正しいか判定
	const isEmailValid = emailRegex.test(emailInput.value.trim());
	// パスワードが入力されているか判定
	const isPasswordFilled = passwordInput.value.trim() !== '';

	// メール正かつパスワード入力済でなければボタン無効化する
	submitBtn.disabled = !(isEmailValid && isPasswordFilled);
}

// メールアドレスとパスワードの入力要素が存在する場合
if (emailInput && passwordInput) {
	// 各イベント（入力、変更、フォーカス外れ）の発生時
	['input', 'change', 'blur'].forEach(eventType => {
		emailInput.addEventListener(eventType, () => {
			// メールアドレスのチェック結果をエラーメッセージとして表示する
			emailErrorDiv.textContent = checkEmail();

			// フォーム全体の入力状態をチェックする
			validateForm();
		});

		// パスワードの各イベント発生時もフォーム全体の入力状態をチェックする
		passwordInput.addEventListener(eventType, validateForm);
	});

	// 画面の読込み完了時、フォーム全体の入力状態をチェックする
	document.addEventListener('DOMContentLoaded', validateForm);
}

// パスワード入力の表示/非表示切替用関数（クラス切り替え版）
function togglePasswordVisibility(inputSelector, btnElement) {
	// 対象入力要素を取得する
	const inputElement = document.querySelector(inputSelector);

	// 入力要素がない場合
	if (!inputElement) {
		// 処理をスキップして終了する
		return
	};

	// パスワード表示の場合
	if (inputElement.type === 'password') {
		// テキスト表示に変更する
		inputElement.type = 'text';

		// 表示中スタイル用クラス「is_visible」を追加する
		btnElement.classList.add('is_visible');

	// テキスト表示の場合
	} else {

		// パスワード表示に戻す
		inputElement.type = 'password';

		// クラス「is_visible」を削除する
		btnElement.classList.remove('is_visible');
	}
}