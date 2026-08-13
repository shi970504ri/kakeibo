const codeInput = document.querySelector('.register_input');
const verifyBtn = document.getElementById('auth_btn');
const codeRegex = /^\d{6}$/;
function validateCodeInput() {
	if (!codeInput || !verifyBtn) return;
	verifyBtn.disabled = !codeRegex.test(codeInput.value.trim());
}
if (codeInput && verifyBtn) {
	validateCodeInput();
	['input', 'change', 'keyup'].forEach(eventType => {
		codeInput.addEventListener(eventType, validateCodeInput);
	});
}