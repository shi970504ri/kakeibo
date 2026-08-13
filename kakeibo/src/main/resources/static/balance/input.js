async function uploadFile(input) {
	const file = input.files[0];
	if (!file) return;
	const row = input.closest(".detail_row");
	const button = row.querySelector(".file_btn");
	const hiddenInput = row.querySelector(".file_path_input");
	const formData = new FormData();
	formData.append("file", file);
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
		if (response.ok) {
			const result = await response.json();
			button.innerHTML = `選択済み<br>再選択可能`;
			hiddenInput.value = result.fileName || file.name;
		} else {
			alert("画像のアップロードに失敗しました (Status: " + response.status + ")");
		}
	} catch (error) {
		console.error(error);
		alert("通信エラーが発生しました");
	}
}
function validateForm(event) {
	const rows = document.querySelectorAll(".detail_row");
	let validCount = 0;
	let errors = [];
	rows.forEach((row, index) => {
		const noText = row.querySelector(".no_badge") ? row.querySelector(".no_badge").textContent : `No.${index + 1}`;
		const itemName = row.querySelector(".item_name input").value.trim();
		const amount = row.querySelector(".item_amount input").value.trim();
		const categoryId = row.querySelector(".item_category select").value.trim();
		const filePath = row.querySelector(".file_path_input").value.trim();
		const memo = row.querySelector(".item_memo input").value.trim();
		const hasAnyInput = (itemName !== "" || amount !== "" || categoryId !== "" || filePath !== "" || memo !== "");
		if (hasAnyInput) {
			const missing = [];
			if (itemName === "") missing.push("商品名");
			if (amount === "") missing.push("金額");
			if (categoryId === "") missing.push("カテゴリ");
			if (filePath === "") missing.push("画像");
			if (missing.length > 0) {
				errors.push(`${noText}: 未入力項目があります（${missing.join("、")}）`);
			} else {
				validCount++;
			}
		}
	});
	if (errors.length > 0) {
		alert("【入力エラー】\n" + errors.join("\n"));
		event.preventDefault();
		return false;
	}
	if (validCount === 0) {
		alert("明細を少なくとも1件は完全に入力してください。");
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
			<div class="no_badge">No.00000</div>
			<div class="item_name">
				<input type="text" name="" placeholder="商品名または項目" autocomplete="off">
			</div>
			<div class="item_amount">
				<input type="number" name="" placeholder="0">
			</div>
			<div class="item_category">
				<select name="">
					${categoryOptionsHtml}
				</select>
			</div>
			<div class="item_file">
				<input type="hidden" class="file_path_input" name="">
				<input type="file" class="file_input" hidden accept="image/*" onchange="uploadFile(this)">
				<button class="file_btn" type="button">📁 画像選択</button>
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
		row.querySelector(".item_file .file_path_input").name = `details[${index}].file`;
		row.querySelector(".item_memo input").name = `details[${index}].memo`;
		const fileInput = row.querySelector(".file_input");
		const fileBtn = row.querySelector(".file_btn");
		const fileId = `file_${index}`;
		fileInput.id = fileId;
		fileBtn.setAttribute("onclick", `document.getElementById('${fileId}').click()`);
	});
}
document.addEventListener("DOMContentLoaded", () => {
	renumberRows();
});