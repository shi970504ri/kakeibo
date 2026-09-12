const monthTitle = document.getElementById("monthTitle");
const calendarGrid = document.getElementById("calendarGrid");
const today = new Date();
const currentYearMonthAttr = calendarGrid ? calendarGrid.getAttribute("data-current-year-month") : null;
let currentYear, currentMonth;
if (currentYearMonthAttr) {
	const parts = currentYearMonthAttr.split("-");
	currentYear = parseInt(parts[0], 10);
	currentMonth = parseInt(parts[1], 10) - 1;
} else {
	currentYear = today.getFullYear();
	currentMonth = today.getMonth();
}
function getRegisteredDates() {
	if (!calendarGrid) return [];
	const rawData = calendarGrid.getAttribute("data-registered-dates");
	if (!rawData) return [];
	return rawData.replace(/[\[\]\s]/g, "").split(",");
}
const registeredDates = getRegisteredDates();
function isJapaneseHoliday(year, month, day) {
	const dateObj = new Date(year, month, day);
	const m = month + 1;
	const isBasicHoliday = (y, mVal, dVal) => {
		if (mVal === 1 && dVal === 1) return true;
		if (mVal === 2 && dVal === 11) return true;
		if (mVal === 2 && dVal === 23) return true;
		if (mVal === 4 && dVal === 29) return true;
		if (mVal === 5 && dVal === 3) return true;
		if (mVal === 5 && dVal === 4) return true;
		if (mVal === 5 && dVal === 5) return true;
		if (mVal === 8 && dVal === 11) return true;
		if (mVal === 11 && dVal === 3) return true;
		if (mVal === 11 && dVal === 23) return true;
		if (mVal === 3 && dVal === Math.floor(20.8431 + 0.242194 * (y - 1980) - Math.floor((y - 1980) / 4))) return true;
		if (mVal === 9 && dVal === Math.floor(23.2488 + 0.242194 * (y - 1980) - Math.floor((y - 1980) / 4))) return true;
		const getNthMonday = (n) => {
			let count = 0;
			for (let d = 1; d <= 31; d++) {
				if (new Date(y, mVal - 1, d).getDay() === 1) {
					count++;
					if (count === n) return d;
				}
			}
			return 0;
		};
		if (mVal === 1 && dVal === getNthMonday(2)) return true;
		if (mVal === 7 && dVal === getNthMonday(3)) return true;
		if (mVal === 9 && dVal === getNthMonday(3)) return true;
		if (mVal === 10 && dVal === getNthMonday(2)) return true;
		return false;
	};
	if (isBasicHoliday(year, m, day)) return true;
	let checkDate = new Date(year, month, day);
	if (checkDate.getDay() !== 0) {
		for (let i = 1; i <= 3; i++) {
			let prev = new Date(year, month, day - i);
			if (isBasicHoliday(prev.getFullYear(), prev.getMonth() + 1, prev.getDate())) {
				if (prev.getDay() === 0) {
					return true;
				}
			} else {
				break;
			}
		}
	}
	return false;
}
function renderCalendar(year, month){
	calendarGrid.innerHTML = "";
	monthTitle.textContent =
		`${year}年 ${month + 1}月`;
	const firstDay =
		new Date(year, month, 1);
	const lastDay =
		new Date(year, month + 1, 0);
	const prevLastDay =
		new Date(year, month, 0);
	const startDay =
		firstDay.getDay();
	const daysInMonth =
		lastDay.getDate();
	const prevMonthDays =
		prevLastDay.getDate();
	const totalCells = 42;
	let dayCount = 1;
	let nextDay = 1;
	for(let i=0;i<totalCells;i++){
		const cell =
			document.createElement("div");
		cell.classList.add("day");
		let cellYear = year;
		let cellMonth = month;
		let displayDate;
		if(i < startDay){
			displayDate = prevMonthDays - startDay + i + 1;
			cell.classList.add("other_month");
			cellMonth = month - 1;
			if (cellMonth < 0) {
				cellMonth = 11;
				cellYear--;
			}
		}else if(dayCount <= daysInMonth){
			displayDate = dayCount;
			const formattedMonth = String(month + 1).padStart(2, '0');
			const formattedDay = String(dayCount).padStart(2, '0');
			const dateStr = `${year}-${formattedMonth}-${formattedDay}`;
			if(
				year === today.getFullYear() &&
				month === today.getMonth() &&
				dayCount === today.getDate()
			){
				cell.classList.add("today");
			}
			if(registeredDates.includes(dateStr)){
				cell.classList.remove("today");
				cell.classList.add("has_data");
				cell.addEventListener("click", () => {
					window.location.href = `/kakeibo/balance/list?date=${dateStr}`;
				});
			}
			dayCount++;
		}else{
			displayDate = nextDay++;
			cell.classList.add("other_month");
			cellMonth = month + 1;
			if (cellMonth > 11) {
				cellMonth = 0;
				cellYear++;
			}
		}
		if(isJapaneseHoliday(cellYear, cellMonth, displayDate)){
			cell.classList.add("holiday");
		}
		if(i % 7 === 0){
			cell.classList.add("sunday");
		}
		if(i % 7 === 6){
			cell.classList.add("saturday");
		}
		cell.textContent = displayDate;
		calendarGrid.appendChild(cell);
	}
}
document
.getElementById("prevMonth")
.addEventListener("click", () => {
	let targetYear = currentYear;
	let targetMonth = currentMonth - 1;
	if(targetMonth < 0){
		targetMonth = 11;
		targetYear--;
	}
	const formattedMonth = String(targetMonth + 1).padStart(2, '0');
	window.location.href = `/kakeibo/balance/calendar?yearMonth=${targetYear}-${formattedMonth}`;
});
document
.getElementById("nextMonth")
.addEventListener("click", () => {
	let targetYear = currentYear;
	let targetMonth = currentMonth + 1;
	if(targetMonth > 11){
		targetMonth = 0;
		targetYear++;
	}
	const formattedMonth = String(targetMonth + 1).padStart(2, '0');
	window.location.href = `/kakeibo/balance/calendar?yearMonth=${targetYear}-${formattedMonth}`;
});
renderCalendar(
	currentYear,
	currentMonth
);