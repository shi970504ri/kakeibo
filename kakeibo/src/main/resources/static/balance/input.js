//初期設定
let nextNo;
window.onload = () => {
	nextNo = document.querySelectorAll(".detail_row").length + 1;
};
//画像アップロード
async function uploadFile(input) {
	//選択されたファイル取得
	const file = input.files[0];
	if (!file) {
		return;
	}
	//ボタン表示変更
	const button =  input.parentElement.querySelector(".file_btn");
	button.innerHTML = `選択済み<br>再選択可能`;
	//サーバーへ送信
	const formData = new FormData();
	formData.append("file", file);
	try {
		const response = await fetch("/upload-file", {
			method: "POST",
			body: formData
		});
		const result = await response.json();
		console.log(result);
		console.log("アップデート完了");
	} catch (error) {
		console.error(error);
		console.log("アップデート失敗");
	}
}
//明細行追加
function addRows(count) {
	//明細一覧取得
	const detailList = document.querySelector(".detail_list");
	//現在の行数取得
	let no = document.querySelectorAll(".detail_row").length;
	for (let i = 0; i < count; i ++) {
		//連番採番
		no++;
		//新規行生成
		const row = document.createElement("div");
		row.className = "detail_row";
		//HTML生成
		row.innerHTML = `
			<p class="no">
				No.${String(no).padStart(5, "0")}
			</p>
			<select name="type${String(no).padStart(5, "0")}">
				<option value="">
					収入or支出
				</option>
				<option value="income">
					収入
				</option>
				<option value="expense">
					支出
				</option>
			</select>
			<div class="item_amount">
				<input type="number" name="amount${String(no).padStart(5, "0")}" placeholder="金額">
			</div>
			<div class="item_category">
				<input type="text" name="category${String(no).padStart(5, "0")}" placeholder="カテゴリ">
			</div>
			<div class="item_file">
				<input type="file" id="file${String(no).padStart(5, "0")}" class="file_input" name="file${String(no).padStart(5, "0")}" hidden accept="image/*" onchange="uploadFile(this)">
				<button class="file_btn" type="button" onclick="document.getElementById('file${String(no).padStart(5, "0")}').click()">
					画像選択
				</button>
			</div>
			<div class="item_memo">
				<input type="text" name="memo${String(no).padStart(5, "0")}" placeholder="メモ">
			</div>
			<div class="item_delete">
				<button type="button" class="delete_btn" onclick="deleteRow(this)">
					削除
				</button>
			</div>
		`;
		//画面へ追加
		detailList.appendChild(row);
	}
}
//明細行削除
function deleteRow(button) {
	//現在の行数取得
	const rows = document.querySelectorAll(".detail_row");
	//最低1件は残す
	if (rows.length <=1) {
		alert("最低1件は残して下さい");
		return;
	}
	//押下された行を削除
	button.closest(".detail_row").remove();
	//No.採番
	renumberRows();
}
//No.振り直し
function renumberRows() {
	const rows = document.querySelectorAll(".detail_row");
	rows.forEach((row, index) => {
		row.querySelector(".no").textContent = `No.${String(index + 1).padStart(5, "0")}`;
	});
}