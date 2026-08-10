document.addEventListener("DOMContentLoaded", () => {
	fetchCategories();
	const addForm = document.getElementById("addCategoryForm");
	addForm.addEventListener("submit", function(event) {
		event.preventDefault();
		const nameInput = document.getElementById("categoryName");
		const typeInput = document.getElementById("categoryType");
		fetch("/kakeibo/api/categories", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				name: nameInput.value,
				type: typeInput.value
			})
		})
		.then(async response => {
			if (response.status === 401) {
				alert("セッションが切れました。ログイン画面へ移動します。");
				window.location.href = "/kakeibo/user/login";
				return;
			}
			if (response.ok) {
				nameInput.value = "";
				fetchCategories();
			} else {
				const errorText = await response.text();
				alert(`追加に失敗しました。(ステータスコード: ${response.status})`);
				console.error("エラー詳細:", response.status, errorText);
			}
		})
		.catch(error => console.error("Error:", error));
	});
});
function fetchCategories() {
	fetch("/kakeibo/api/categories")
	.then(response => {
		if (response.status === 401) {
			window.location.href = "/kakeibo/user/login";
			throw new Error("Unauthorized");
		}
		return response.json();
	})
	.then(data => {
		const expenseList = document.getElementById("expenseList");
		const incomeList = document.getElementById("incomeList");
		expenseList.innerHTML = "";
		incomeList.innerHTML = "";
		data.forEach(category => {
			const li = document.createElement("li");
			const span = document.createElement("span");
			span.textContent = category.name + " ";
			li.appendChild(span);
			const delete_btn = document.createElement("button");
			delete_btn.textContent = "削除";
			delete_btn.classList.add("delete_btn");
			delete_btn.onclick = () => deleteCategory(category.categoryId);
			li.appendChild(delete_btn);
			if (category.type === "expense") {
				li.classList.add("expense_list");
				expenseList.appendChild(li);
			} else if (category.type === "income") {
				li.classList.add("income_list");
				incomeList.appendChild(li);
			}
		});
	})
	.catch(error => console.error("Error:", error));
}
function deleteCategory(id) {
	if (!confirm("本当に削除しますか？")) return;
	fetch(`/kakeibo/api/categories/${id}`, {
		method: "DELETE"
	})
	.then(response => {
		if (response.status === 401) {
			alert("セッションが切れました。ログイン画面へ移動します。");
			window.location.href = "/kakeibo/user/login";
			return;
		}
		if (response.ok) {
			fetchCategories();
		} else {
			alert("削除に失敗しました。");
		}
	})
	.catch(error => {
		console.error("Error:", error);
		alert("エラーが発生しました。");
	});
}