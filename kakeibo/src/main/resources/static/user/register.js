/**
 * =========================================================
 * 利用規約･プライバシーポリシー･免責事項モーダル取得処理
 * =========================================================
 */

// 現在開いている規約の種類（terms, privacy, disclaimer）を保持する
let currentType = '';

// 各URLの定義オブジェクトを参照する
const fetchUrls = URLS;

// 各規約種別とモーダルタイトルの対応マップを定義する
const modalTitles = {
	terms: '利用規約',
	privacy: 'プライバシーポリシー',
	disclaimer: '免責事項'
};

// モーダルを開いて規約内容を非同期で取得・表示する関数
async function openModal(type) {
	// 現在開いている規約の種類を更新する
	currentType = type;

	// モーダルのタイトルテキストを設定する
	document.getElementById('modal_title').textContent = modalTitles[type];
	// モーダル要素を表示状態にする
	document.getElementById('terms_modal').style.display = 'flex';

	// モーダル本文の表示領域要素を取得する
	const modalBody = document.getElementById('modal_body');

	// 読み込み中テキストを初期表示として設定する
	modalBody.innerHTML = "読み込み中...";

	// 規約HTMLレスポンスの例外発生に備えてtryブロックを開始する
	try {
		// 規約HTMLコンテンツを取得する
		const response = await fetch(fetchUrls[type] + "?modal=true");

		// レスポンスエラーが発生した場合
		if (!response.ok) {
			// 例外を発生させる
			throw new Error('読込みに失敗しました。');
		}

		// レスポンスをテキストデータとして読み込む
		const htmlText = await response.text();

		// モーダル内に取得したHTMLを挿入する
		modalBody.innerHTML = htmlText;

	// エラーが発生した場合
	} catch(error) {
		// モーダル内にエラーメッセージを表示する
		modalBody.innerHTML = "<span style='color: rgba(255, 0, 0, 1);'>内容の読込みに失敗しました。</span>";

		// コンソールにエラー詳細を出力する
		console.error(error);
	}
}

// モーダルを閉じ、閲覧した項目の同意チェックボックスに自動チェックを入れる関数
function closeModal() {
	// モーダル要素を非表示にする
	document.getElementById('terms_modal').style.display = 'none';

	// 現在の規約種類が「利用規約（terms）」の場合
	if (currentType === 'terms') {
		// 利用規約のチェックボックスにチェックを入れる
		document.getElementById('chk_terms').checked = true;

	// 現在の規約種類が「プライバシーポリシー（privacy）」の場合
	} else if (currentType === 'privacy') {
		// プライバシーポリシーのチェックボックスにチェックを入れる
		document.getElementById('chk_privacy').checked = true;

	// 現在の規約種類が「免責事項（disclaimer）」の場合
	} else if (currentType === 'disclaimer') {
		// 免責事項のチェックボックスにチェックを入れる
		document.getElementById('chk_disclaimer').checked = true;
	}

	// 全条件の判定処理を実行する
	checkAllAgreed();
}

// メールアドレス入力要素を取得する
const emailInput = document.querySelector('.email_input');
// パスワード入力要素を取得する
const passwordInput = document.querySelector('.password_input');
// パスワード確認用入力要素を取得する
const confirmInput = document.querySelector('.password_confirm_input');
// メールアドレス用エラー表示要素を取得する
const emailErrorDiv = document.querySelector('.email_error');
// 確認用パスワード用エラー表示要素を取得する
const confirmErrorDiv = document.querySelector('.passwordConfirm_error');
// メールアドレスチェック用の正規表現を定義する
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// メールアドレスの形式チェックを行う関数
function checkEmail() {
	// 値が入力されていて、かつ形式が不適切な場合
	if (emailInput.value !== '' && !emailRegex.test(emailInput.value)) {
		// エラーメッセージを返す
		return "メールアドレスの形式が正しくありません。";
	}

	// エラーがない場合は空文字を返す
	return "";
}

// パスワードの入力条件を検証表示する関数
function validatePasswordConditions() {
	// パスワードの入力値を取得する
	const val = passwordInput.value;
	// 各達成条件の判定結果オブジェクトを作成する
	const conditions = {
		// 英大文字が含まれているか検証する
		req_upper: /[A-Z]/.test(val),
		// 英小文字が含まれているか検証する
		req_lower: /[a-z]/.test(val),
		// 数字が含まれているか検証する
		req_number: /[0-9]/.test(val),
		// 指定記号が含まれているか検証する
		req_symbol: /[!?#$%&@*/]/.test(val),
		// 8文字以上あるか検証する
		req_length: val.length >= 8
	};

	// 全条件クリアフラグを true で初期化する
	let allValid = true;

	// 各条件要素の表示状態を更新する
	for (const [id, isValid] of Object.entries(conditions)) {
		// 条件のリスト項目要素を取得する
		const el = document.getElementById(id);
		// アイコン表示要素を取得する
		const icon = el.querySelector('.icon');

		// 条件を満たしている場合
		if (isValid) {
			// 達成済みクラスを付与する
			el.classList.add('valid');
			// アイコンをチェックマークに変更する
			icon.textContent = '✅';

		// 条件を満たしていない場合
		} else {
			// 達成済みクラスを削除する
			el.classList.remove('valid');
			// アイコンを未達成マークに変更する
			icon.textContent = '□';
			// 全条件クリアフラグを false に設定する
			allValid = false;
		}
	}

	// 全条件達成判定結果を返す
	return allValid;
}

// パスワードと確認用入力の一致チェックを行う関数
function checkConfirm() {
	// 確認用が入力されていて、かつパスワードと一致しない場合
	if (confirmInput.value !== '' && passwordInput.value !== confirmInput.value) {
		// エラーメッセージを返す
		return "パスワードが一致していません。";
	}

	// 問題がない場合は空文字を返す
	return "";
}

// すべての入力項目および規約同意チェックを判定し、送信ボタンの有効/無効を切り替える関数
function checkAllAgreed() {
	// 利用規約のチェック状態を取得する
	const isTermsChecked = document.getElementById('chk_terms').checked;
	// プライバシーポリシーのチェック状態を取得する
	const isPrivacyChecked = document.getElementById('chk_privacy').checked;
	// 免責事項のチェック状態を取得する
	const isDisclaimerChecked = document.getElementById('chk_disclaimer').checked;
	// フォーム送信ボタン要素を取得する
	const submitBtn = document.getElementById('submit_btn');
	// 必須入力欄がすべて入力されているか確認する
	const isAllFilled = emailInput.value !== '' && passwordInput.value !== '' && confirmInput.value !== '';
	// メールアドレスのバリデーション結果を確認する
	const isEmailvalid = checkEmail() === "";
	// パスワード条件の検証結果を取得する
	const isPasswordValid = validatePasswordConditions();
	// パスワード一致チェック結果を確認する
	const isConfirmValid = checkConfirm() === "";
	// 全規約への同意状態を確認する
	const isAllChecked = isTermsChecked && isPrivacyChecked && isDisclaimerChecked;

	// すべての条件を満たしている場合のみ送信ボタンを有効化する
	submitBtn.disabled = !(isAllFilled && isEmailvalid && isPasswordValid && isConfirmValid && isAllChecked);
}

// メールアドレス入力欄からフォーカスが外れた時
emailInput.addEventListener('blur', () => {
	// メールアドレスのチェックを実行する
	emailErrorDiv.textContent = checkEmail();
});

// パスワード入力欄からフォーカスが外れた時
passwordInput.addEventListener('blur', () => {
	// 確認用入力が存在する場合
	if (confirmInput.value !== '') {
		// 確認用パスワードとの整合性を再確認する
		confirmErrorDiv.textContent = checkConfirm();
	}
});

// 確認用パスワード欄からフォーカスが外れた時
confirmInput.addEventListener('blur', () => {
	// 一致チェックを実行する
	confirmErrorDiv.textContent = checkConfirm();
});

// メールアドレス入力時
emailInput.addEventListener('input', () => {
	// リアルタイムでチェックを実行する
	emailErrorDiv.textContent = checkEmail();
	// 送信ボタンの有効化判定を実行する
	checkAllAgreed(); 
});

// パスワードの入力・キー離下時
['input', 'keyup'].forEach(eventType => {
	passwordInput.addEventListener(eventType, () => {
		// パスワード条件の表示を更新する
		validatePasswordConditions();

		// 確認用エラーが表示中、または確認用に入力がある場合
		if (confirmErrorDiv.textContent !== "" || confirmInput.value !== "") {
			// 一致チェック結果を更新する
			confirmErrorDiv.textContent = checkConfirm();
			// 送信ボタンの有効化判定を実行する
			checkAllAgreed();
		}
	});
});

// 確認用パスワード入力時
confirmInput.addEventListener('input', () => {
	// リアルタイムで一致チェックを実行する
	confirmErrorDiv.textContent = checkConfirm();
	// 送信ボタンの有効化判定を実行する
	checkAllAgreed();
});

// 全チェックボックスに対して変更イベントを設定する
document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
	// チェック状態変更時、送信ボタンの有効化判定を実行する
	checkbox.addEventListener('change', checkAllAgreed);
});

// フォーム送信ボタン押下時
document.querySelector('form').addEventListener('submit', function(event) {
	// メールエラーを取得する
	const emailErr = checkEmail();
	// パスワード条件結果を取得する
	const isPasswordValid = validatePasswordConditions();
	// パスワード一致エラーを取得する
	const confErr = checkConfirm();

	// メール用エラーメッセージを表示する
	emailErrorDiv.textContent = emailErr;
	// 確認用パスワードのエラーメッセージを表示する
	confirmErrorDiv.textContent = confErr;

	// いずれかのエラーが存在する場合
	if (emailErr !== "" || !isPasswordValid || confErr !== "") {
		// フォームの送信処理を中断する
		event.preventDefault();
	}
});

// パスワード入力の表示切替を行う関数
function togglePasswordVisibility(inputSelector, btnElement) {
	// 対象の入力要素を取得する
	const inputElement = document.querySelector(inputSelector);

	// 入力タイプが password の場合
	if (inputElement.type === 'password') {
		// 入力タイプを text に変更して文字を表示する
		inputElement.type = 'text';
		// ボタンに表示状態クラスを付与する
		btnElement.classList.add('is_visible');

	// 入力タイプが password じゃない場合
	} else {
		// 入力タイプを password に変更して非表示にする
		inputElement.type = 'password';
		// ボタンから表示状態クラスを削除する
		btnElement.classList.remove('is_visible');
	}
}

// パスワード確認用の表示切替を行う関数
function togglePasswordConfirmVisibility(inputSelector, btnElement) {
	// 対象の入力要素を取得する
	const inputElement = document.querySelector(inputSelector);

	// 入力タイプが password の場合
	if (inputElement.type === 'password') {
		// 入力タイプを text に変更して文字を表示する
		inputElement.type = 'text';
		// ボタンに表示状態クラスを付与する
		btnElement.classList.add('is_visible');

	// 入力タイプが password じゃない場合
	} else {
		// 入力タイプを password に変更して非表示にする
		inputElement.type = 'password';
		// ボタンから表示状態クラスを削除する
		btnElement.classList.remove('is_visible');
	}
}

// 全角英数字記号を半角に自動変換するユーティリティ関数
function convertToHalfWidth(str) {
	// 全角英数字記号の範囲（！〜～）を置換する
	return str.replace(/[！-～]/g, function(s) {
		// 文字コードを減算して半角化する
		return String.fromCharCode(s.charCodeAt(0) - 0xfee0);

	// 特殊な全角記号を半角記号へ変換して返す
	}).replace(/”/g, '"').replace(/’/g, "'").replace(/‘/g, "`").replace(/￥/g, '\\');
}

// パスワード入力欄と確認用入力欄に対する入力補助設定
[passwordInput, confirmInput].forEach(input => {
	// 入力要素が存在しない場合
	if (!input) {
		// 処理を強制終了する
		return
	};

	// 文字入力時、リアルタイムで全角から半角へ自動変換する処理
	input.addEventListener('input', (e) => {
		// 半角変換後の文字列を取得する
		let converted = convertToHalfWidth(e.target.value);

		// 全角が含まれていて変換が生じた場合
		if (e.target.value !== converted) {
			// 変換後の半角文字列を入力欄に再セットする
			e.target.value = converted;
		}
	});

	// フォーカス獲得時、自動補完制御のため一瞬だけ type を text に変更する処理
	input.addEventListener('focus', function() {
		// 元の type（password等）を保持する
		const originalType = this.type;

		// 一時的に type を text に変更する
		this.type = 'text';

		// 10ミリ秒後に実行する
		setTimeout(() => {
			// 元の type に戻す
			this.type = originalType;
		}, 10);
	});
});