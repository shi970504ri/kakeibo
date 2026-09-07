document.addEventListener("DOMContentLoaded", () => {
	const transactionData = window.dbTransactionData || {};
	const today = new Date();
	let currentYear = today.getFullYear();
	let currentMonth = today.getMonth() + 1;
	const prevBtn = document.getElementById("prevMonth");
	const nextBtn = document.getElementById("nextMonth");
	const monthLabel = document.getElementById("currentMonthLabel");
	const totalIncomeEl = document.querySelector(".income_mony");
	const totalExpenseEl = document.querySelector(".expenditure_mony");
	const balanceElements = document.querySelectorAll(".balance_mony");
	const carryOverEl = balanceElements[0];
	const balanceEl = balanceElements[1];
	const incomeBarWrapper = document.querySelector(".in_bar");
	const expenseBarWrapper = document.querySelector(".ex_bar");
	let tooltip = document.querySelector(".chart-tooltip");
	if (!tooltip) {
		tooltip = document.createElement("div");
		tooltip.className = "chart-tooltip";
		document.body.appendChild(tooltip);
	}
	function formatCurrency(num) {
		return `¥ ${num.toLocaleString()}`;
	}
	function getCeilUpperLimit(amount) {
		if (amount <= 0) return 100000;
		const digit = Math.pow(10, Math.floor(Math.log10(amount)));
		return Math.ceil(amount / digit) * digit;
	}
	function update() {
		const monthKey = `${currentYear}-${String(currentMonth).padStart(2, "0")}`;
		if (monthLabel) {
			monthLabel.textContent = `${currentYear}年${currentMonth}月`;
		}
		let carryOver = 0;
		Object.keys(transactionData).sort().forEach(key => {
			if (key < monthKey) {
				const inc = transactionData[key].income?.reduce((s, i) => s + i.amount, 0) || 0;
				const exp = transactionData[key].expense?.reduce((s, i) => s + i.amount, 0) || 0;
				carryOver += (inc - exp);
			}
		});
		const currentData = transactionData[monthKey] || { income: [], expense: [] };
		const totalIncome = currentData.income ? currentData.income.reduce((s, i) => s + i.amount, 0) : 0;
		const totalExpense = currentData.expense ? currentData.expense.reduce((s, i) => s + i.amount, 0) : 0;
		const balance = (totalIncome + carryOver) - totalExpense;
		if (carryOverEl) carryOverEl.textContent = formatCurrency(carryOver);
		if (totalIncomeEl) totalIncomeEl.textContent = formatCurrency(totalIncome);
		if (totalExpenseEl) totalExpenseEl.textContent = formatCurrency(totalExpense);
		if (balanceEl) balanceEl.textContent = formatCurrency(balance);
		const maxVal = getCeilUpperLimit(Math.max(totalIncome, totalExpense));
		renderStackedBar(incomeBarWrapper, currentData.income, maxVal);
		renderStackedBar(expenseBarWrapper, currentData.expense, maxVal);
	}
	function renderStackedBar(targetEl, items, maxVal) {
		if (!targetEl) return;
		targetEl.innerHTML = "";
		if (!items || items.length === 0) return;
		items.forEach(item => {
			if (item.amount <= 0) return;
			const seg = document.createElement("div");
			const heightPercent = (item.amount / maxVal) * 100;
			seg.className = `bar-segment ${item.className || ""}`;
			seg.style.height = `${heightPercent}%`;
			seg.addEventListener("mousemove", (e) => {
				tooltip.style.display = "block";
				tooltip.innerHTML = `<strong>${item.category}</strong>: ¥ ${item.amount.toLocaleString()}`;
				tooltip.style.left = `${e.pageX + 12}px`;
				tooltip.style.top = `${e.pageY + 12}px`;
			});
			seg.addEventListener("mouseleave", () => {
				tooltip.style.display = "none";
			});
			targetEl.appendChild(seg);
		});
	}
	if (prevBtn) {
		prevBtn.addEventListener("click", () => {
			currentMonth--;
			if (currentMonth < 1) {
				currentMonth = 12;
				currentYear--;
			}
			update();
		});
	}
	if (nextBtn) {
		nextBtn.addEventListener("click", () => {
			currentMonth++;
			if (currentMonth > 12) {
				currentMonth = 1;
				currentYear++;
			}
			update();
		});
	}
	update();
});