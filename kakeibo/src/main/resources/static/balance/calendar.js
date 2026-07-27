const monthTitle = document.getElementById("monthTitle");
const calendarGrid = document.getElementById("calendarGrid");
const today = new Date();
let currentYear = today.getFullYear();
let currentMonth = today.getMonth();
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
		let displayDate;
		if(i < startDay){
			displayDate =
				prevMonthDays - startDay + i + 1;
			cell.classList.add("other_month");
		}else if(dayCount <= daysInMonth){
			displayDate = dayCount;
			if(
				year === today.getFullYear() &&
				month === today.getMonth() &&
				dayCount === today.getDate()
			){
				cell.classList.add("today");
			}
			dayCount++;
		}else{
			displayDate = nextDay++;
			cell.classList.add("other_month");
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
	currentMonth--;
	if(currentMonth < 0){
		currentMonth = 11;
		currentYear--;
	}
	renderCalendar(
		currentYear,
		currentMonth
	);
});
document
.getElementById("nextMonth")
.addEventListener("click", () => {
	currentMonth++;
	if(currentMonth > 11){
		currentMonth = 0;
		currentYear++;
	}
	renderCalendar(
		currentYear,
		currentMonth
	);
});
renderCalendar(
	currentYear,
	currentMonth
);