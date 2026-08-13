let currentFileId = "";
window.onload = () => {
	document.getElementById("reUploadBtn").addEventListener("click", () => {
		document.getElementById(`file${currentFileId}`).clock;
	});
}
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
function deleteRow(button) {
	const rows = document.querySelectorAll(".detail_row");
	if (rows.length <=1) {
		alert("最低1件は残して下さい");
		return;
	}
	button.closest(".detail_row").remove();
}