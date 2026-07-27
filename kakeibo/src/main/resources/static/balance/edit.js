//初期設定
let currentFileId = "";
window.onload = () => {
	document.getElementById("reUploadBtn").addEventListener("click", () => {
		document.getElementById(`file${currentFileId}`).clock;
	});
}
//画像アップロード
async function showFile(no) {
	currentFileId = no;
	try {
		const response = await fetch(`/image/${no}`);
		const result = await response.json();
		document.getElementById("previewFile").src = result.imageUrl;
		document.getElementById("fileModal").style.display = "flex";
	}
	catch(error) {
		console.error(error);
		alert("画像取得失敗");
	}
}
function closeModail() {
	document.getElementById("fileModal").style.display = "none";
}
document.getElementById("reUploadBtn").addEventListener("click", () => {
	document.getElementById(`file${currentFileId}`).click();
})
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
}