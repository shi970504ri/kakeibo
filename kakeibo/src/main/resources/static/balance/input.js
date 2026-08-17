async function uploadMainFile(input) {
	const file = input.files[0];
	if (!file) return;
	const hiddenInput = document.getElementById("mainFilePath");
	const fileNameDisplay = document.getElementById("fileNameDisplay");
	const storeInput = document.querySelector('input[name="storeName"]');
	const storeName = storeInput ? storeInput.value.trim() : "";
	const formData = new FormData();
	formData.append("file", file);
	formData.append("storeName", storeName);
	const contextPathMeta = document.querySelector('meta[name="context-path"]');
	let contextPath = contextPathMeta ? contextPathMeta.getAttribute('content') : '/';
	if (!contextPath.endsWith('/')) {
		contextPath += '/';
	}
	try {
		const response = await fetch(contextPath + "upload-file", {
			method: "POST",
			body: formData
		});
		const result = await response.json();
		if (response.ok) {
			hiddenInput.value = result.fileName || file.name;
			if (fileNameDisplay) {
				fileNameDisplay.textContent = `選択中: ${file.name}`;
				fileNameDisplay.style.color = "#0284c7";
			}
		} else {
			alert(result.error || "画像のアップロードに失敗しました (Status: " + response.status + ")");
			input.value = "";
			hiddenInput.value = "";
			if (fileNameDisplay) fileNameDisplay.textContent = "";
		}
	} catch (error) {
		console.error(error);
		alert("通信エラーが発生しました");
		input.value = "";
		hiddenInput.value = "";
		if (fileNameDisplay) fileNameDisplay.textContent = "";
	}
}
function validateForm(event) {
	let errors = [];
	const mainFilePath = document.getElementById("mainFilePath").value.trim();
	if (mainFilePath === "") {
		errors.push("【基本情報】添付画像を選択・アップロードしてください。");
	}
	const rows = document.querySelectorAll(".detail_row");
	let validCount = 0;
	rows.forEach((row, index) => {
		const noText = row.querySelector(".no_badge") ? row.querySelector(".no_badge").textContent : `No.${index + 1}`;
		const itemName = row.querySelector(".item_name input").value.trim();
		const amount = row.querySelector(".item_amount input").value.trim();
		const categoryId = row.querySelector(".item_category select").value.trim();
		const memo = row.querySelector(".item_memo input").value.trim();
		const hasAnyInput = (itemName !== "" || amount !== "" || categoryId !== "" || memo !== "");
		if (hasAnyInput) {
			const missing = [];
			if (itemName === "") missing.push("商品名");
			if (amount === "") missing.push("金額");
			if (categoryId === "") missing.push("カテゴリ");

			if (missing.length > 0) {
				errors.push(`${noText}: 未入力項目があります（${missing.join("、")}）`);
			} else {
				validCount++;
			}
		}
	});
	if (validCount === 0 && errors.length === 0) {
		errors.push("【詳細情報】明細を少なくとも1件は入力してください。");
	}
	if (errors.length > 0) {
		alert("【入力エラー】\n" + errors.join("\n"));
		event.preventDefault();
		return false;
	}
	return true;
}
function addRows(count) {
	const detailList = document.querySelector(".detail_list");
	const categoryOptionsHtml = document.getElementById("categoryTemplate").innerHTML;
	for (let i = 0; i < count; i++) {
		const row = document.createElement("div");
		row.className = "detail_row";
		row.innerHTML = `
			<div class="no_badge">No.000</div>
			<div class="item_name">
				<input type="text" name="" placeholder="商品名または項目" autocomplete="off">
			</div>
			<div class="item_amount">
				<input type="number" inputmode="numeric" name="" placeholder="0">
			</div>
			<div class="item_category">
				<select name="">
					${categoryOptionsHtml}
				</select>
			</div>
			<div class="item_memo">
				<input type="text" name="" placeholder="メモ" autocomplete="off">
			</div>
			<div class="item_delete">
				<button type="button" class="delete_btn" onclick="deleteRow(this)" title="行を削除">削除</button>
			</div>
		`;
		detailList.appendChild(row);
	}
	renumberRows();
}
function deleteRow(button) {
	const rows = document.querySelectorAll(".detail_row");
	if (rows.length <= 1) {
		alert("最低1件は残して下さい");
		return;
	}
	button.closest(".detail_row").remove();
	renumberRows();
}
function renumberRows() {
	const rows = document.querySelectorAll(".detail_row");
	rows.forEach((row, index) => {
		row.querySelector(".no_badge").textContent = `No.${String(index + 1).padStart(3, "0")}`;
		row.querySelector(".item_name input").name = `details[${index}].itemName`;
		row.querySelector(".item_amount input").name = `details[${index}].amount`;
		row.querySelector(".item_category select").name = `details[${index}].categoryId`;
		row.querySelector(".item_memo input").name = `details[${index}].memo`;
	});
}
document.addEventListener("DOMContentLoaded", () => {
	const currentRows = document.querySelectorAll(".detail_row");
	if (currentRows.length < 5) {
		addRows(5 - currentRows.length);
	} else {
		renumberRows();
	}
});