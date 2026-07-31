let currentType = '';
const fetchUrls = URLS;
const modalTitles = {
	terms: '利用規約',
	privacy: 'プライバシーポリシー',
	disclaimer: '免責事項'
}
async function openModal(type) {
	currentType = type;
	document.getElementById('modal_title').textContent = modalTitles[type];
	document.getElementById('terms_modal').style.display = 'flex';
	const modalBody = document.getElementById('modal_body');
	modalBody.innerHTML = "読み込み中...";
	try {
		const response = await fetch(fetchUrls[type] + "?modal=true");
		if (!response.ok) throw new Error('読込みに失敗しました。');
		const htmlText = await response.text();
		modalBody.innerHTML = htmlText;
	} catch(error) {
		modalBody.innerHTML = "<span style='color: rgba(255, 0, 0, 1);'>内容の読込みに失敗しました。</span>";
		console.error(error);
	}
}
function closeModal() {
	document.getElementById('terms_modal').style.display = 'none';
	if (currentType === 'terms') {
		document.getElementById('chk_terms').checked = true;
	} else if (currentType === 'privacy') {
		document.getElementById('chk_privacy').checked = true;
	} else if (currentType === 'disclaimer') {
		document.getElementById('chk_disclaimer').checked = true;
	}
	checkAllAgreed();
}
const emailInput = document.querySelector('.email_input');
const passwordInput = document.querySelector('.password_input');
const confirmInput = document.querySelector('.password_confirm_input');
const emailErrorDiv = document.querySelector('.email_error');
const confirmErrorDiv = document.querySelector('.passwordConfirm_error');
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
function checkEmail() {
	if (emailInput.value !== '' && !emailRegex.test(emailInput.value)) {
		return "メールアドレスの形式が正しくありません。";
	}
	return "";
}
function validatePasswordConditions() {
	const val = passwordInput.value;
	const conditions = {
		req_upper: /[A-Z]/.test(val),
		req_lower: /[a-z]/.test(val),
		req_number: /[0-9]/.test(val),
		req_symbol: /[!?#$%&@*/]/.test(val),
		req_length: val.length >= 8
	};
	let allValid = true;
	for (const [id, isValid] of Object.entries(conditions)) {
		const el = document.getElementById(id);
		const icon = el.querySelector('.icon');
		if (isValid) {
			el.classList.add('valid');
			icon.textContent = '✅';
		} else {
			el.classList.remove('valid');
			icon.textContent = '□';
			allValid = false;
		}
	}
	return allValid;
}
function checkConfirm() {
	if (confirmInput.value !== '' && passwordInput.value !== confirmInput.value) {
		return "パスワードが一致していません。";
	}
	return "";
}
function checkAllAgreed() {
	const isTermsChecked = document.getElementById('chk_terms').checked;
	const isPrivacyChecked = document.getElementById('chk_privacy').checked;
	const isDisclaimerChecked = document.getElementById('chk_disclaimer').checked;
	const submitBtn = document.getElementById('submit_btn');
	const isAllFilled = emailInput.value !== '' && passwordInput.value !== '' && confirmInput.value !== '';
	const isEmailvalid = checkEmail() === "";
	const isPasswordValid = validatePasswordConditions();
	const isConfirmValid = checkConfirm() === "";
	const isAllChecked = isTermsChecked && isPrivacyChecked && isDisclaimerChecked;
	submitBtn.disabled = !(isAllFilled && isEmailvalid && isPasswordValid && isConfirmValid && isAllChecked);
}
emailInput.addEventListener('blur', () => {
	emailErrorDiv.textContent = checkEmail();
});
passwordInput.addEventListener('blur', () => {
	if (confirmInput.value !== '') {
		confirmErrorDiv.textContent = checkConfirm();
	}
});
confirmInput.addEventListener('blur', () => {
	confirmErrorDiv.textContent = checkConfirm();
});
emailInput.addEventListener('input', () => {
	emailErrorDiv.textContent = checkEmail();
	checkAllAgreed(); 
});
['input', 'keyup'].forEach(eventType => {
	passwordInput.addEventListener(eventType, () => {
		validatePasswordConditions();
		if (confirmErrorDiv.textContent !== "" || confirmInput.value !== "") {
			confirmErrorDiv.textContent = checkConfirm();
			checkAllAgreed();
		}
	});
})
confirmInput.addEventListener('input', () => {
	confirmErrorDiv.textContent = checkConfirm();
	checkAllAgreed();
});
document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
	checkbox.addEventListener('change', checkAllAgreed);
});
document.querySelector('form').addEventListener('submit', function(event) {
	const emailErr = checkEmail();
	const isPasswordValid = validatePasswordConditions();
	const confErr = checkConfirm();
	emailErrorDiv.textContent = emailErr;
	confirmErrorDiv.textContent = confErr;
	if (emailErr !== "" || !isPasswordValid || confErr !== "") {
		event.preventDefault();
	}
});
function togglePasswordVisibility(inputSelector, btnElement) {
	const inputElement = document.querySelector(inputSelector);
	if (inputElement.type === 'password') {
		inputElement.type = 'text';
		btnElement.classList.add('is_visible');
	} else {
		inputElement.type = 'password';
		btnElement.classList.remove('is_visible');
	}
}
function togglePasswordConfirmVisibility(inputSelector, btnElement) {
	const inputElement = document.querySelector(inputSelector);
	if (inputElement.type === 'password') {
		inputElement.type = 'text';
		btnElement.classList.add('is_visible');
	} else {
		inputElement.type = 'password';
		btnElement.classList.remove('is_visible');
	}
}
function convertToHalfWidth(str) {
	return str.replace(/[！-～]/g,function(s){
		return String.fromCharCode(s.charCodeAt(0) - 0xfee0);
	}).replace(/”/g, '"').replace(/’/g, "'").replace(/‘/g, "`").replace(/￥/g, '\\');
}
[passwordInput, confirmInput].forEach(input => {
	if (!input) return;
	input.addEventListener('input', (e) => {
		let converted = convertToHalfWidth(e.target.value);
		if (e.target.value !== converted) {
			e.target.value = converted;
		}
	});
	input.addEventListener('focus', function() {
		const originalType = this.type;
		this.type = 'text';
		setTimeout(() => {
			this.type = originalType;
		}, 10);
	});
});