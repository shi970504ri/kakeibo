const codeInput = document.querySelector('.register_input');
const verifyBtn = document.getElementById('auth_btn');
const codeRegex = /^\d{6}$/;
function validateCodeInput() {
	if (!codeInput || !verifyBtn) return;
	// コードが6桁の数字の場合はボタンを無効化、6桁の数字が入力されていれば有効化
	verifyBtn.disabled = !codeRegex.test(codeInput.value.trim());
}
if (codeInput && verifyBtn) {
	// 読み込み時の初期チェック
	validateCodeInput();
	// 入力値の変化を監視
	['input', 'change', 'keyup'].forEach(eventType => {
		codeInput.addEventListener(eventType, validateCodeInput);
	});
}