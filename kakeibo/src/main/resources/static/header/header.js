document.getElementById("searchBtn").addEventListener("click", function() {
	const keyword = document.getElementById("keywordSelect").value;
	const month = document.getElementById("monthSelect").value;
	console.log("検索キーワード；", keyword);
	console.log("選択月：", month);
	alert("検索実行\n\n" + "キーワード：" + keyword + "\n月：" + month);
})