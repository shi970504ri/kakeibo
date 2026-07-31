const emailInput = document.querySelector('.email_input');
const passwordInput = document.querySelector('.password_input');
const emailErrorDiv = document.querySelector('.email_error');
const submitBtn = document.getElementById('submit_btn');
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
function checkEmail() {
	if (emailInput.value !== '' && !emailRegex.test(emailInput.value)) {
		return "メールアドレスの形式が正しくありません。";
	}
	return "";
}
function validateForm() {
	if (!emailInput || !passwordInput || !submitBtn) return;
	const isEmailValid = emailRegex.test(emailInput.value.trim());
	const isPasswordFilled = passwordInput.value.trim() !== '';
	submitBtn.disabled = !(isEmailValid && isPasswordFilled);
}
if (emailInput && passwordInput) {
	['input', 'change', 'blur'].forEach(eventType => {
		emailInput.addEventListener(eventType, () => {
			emailErrorDiv.textContent = checkEmail();
			validateForm();
		});
		passwordInput.addEventListener(eventType, validateForm);
	});
	document.addEventListener('DOMContentLoaded', validateForm);
}
function togglePasswordVisibility(inputSelector, btnElement) {
	const inputElement = document.querySelector(inputSelector);
	if (!inputElement) return;

	if (inputElement.type === 'password') {
		inputElement.type = 'text';
		btnElement.classList.add('is_visible');
	} else {
		inputElement.type = 'password';
		btnElement.classList.remove('is_visible');
	}
}