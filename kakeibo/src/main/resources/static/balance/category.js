document.getElementById("addBtn").addEventListener("click", function() {
	const categoryName = document.getElementById("categoryName").value.trim();
	const categoryType = document.getElementById("categoryType").value;
	if (categoryName === "") {
		alert("カテゴリ名を入力して下さい");
		return;
	}
	const li = document.createElement("li");
	if (categoryType === "income") {
		li.className = "income_list";
	} else {
		li.className = "expense_list";
	}
	li.innerHTML =
	`
		<span>
			${categoryName}
		</span>
		<button class="delete_btn">
			削除
		</button>
	`;
	if (categoryType === "income") {
		document.getElementById("incomeList").appendChild(li);
	} else {
		document.getElementById("expenseList").appendChild(li);
	}
	document.getElementById("categoryName").value = "";
});
document.addEventListener("click", function(e) {
	if (e.target.classList.contains("delete_btn")) {
		if (confirm("削除しますか？")) {
			e.target.parentElement.remove();
		}
	}
});