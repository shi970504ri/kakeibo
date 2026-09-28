/**
 * =========================================================
 * カレンダー描画＆日本の祝日判定処理
 * =========================================================
 */

// 【修正】id名「yearMonthPicker」を持つ要素（年月選択ピッカー）を取得する
const yearMonthPicker = document.getElementById("month");
// id名「calendarGrid」を持つ要素（カレンダーの日付マスを入れる要素）を取得する
const calendarGrid = document.getElementById("calendarGrid");
// 現在の今日の日付情報を取得して変数に代入する
const today = new Date();
// カレンダー容器から「data-current-year-month」属性の値（例: "2026-09"）を取得する（容器がない場合はnull）
const currentYearMonthAttr = calendarGrid ? calendarGrid.getAttribute("data-current-year-month") : null;

// 表示対象の「年」と「月」を保持する変数を宣言する
let currentYear, currentMonth;

// HTML側に「data-current-year-month」の値が設定されていた場合
if (currentYearMonthAttr) {
	// ハイフンで文字列を分割する（例: ["2026", "09"]）
	const parts = currentYearMonthAttr.split("-");
	
	// 最初の部分を10進数の数値に変換して「年」に代入
	currentYear = parseInt(parts[0], 10);
	// 2番目の部分を10進数数値に変換してJSの月（0～11）に併せるため1を引いて「月」に代入する
	currentMonth = parseInt(parts[1], 10) - 1;

// HTML側に設定がない場合
} else {
	// 今日の「年」を取得して代入する
	currentYear = today.getFullYear();
	// 今日の「月」（0～11）を取得して代入する
	currentMonth = today.getMonth();
}

// データが登録されている日付のリストを取得する関数を定義する
function getRegisteredDates() {
	// カレンダー容器がなければ空の配列を返す
	if (!calendarGrid) {
		return []
	};
	
	// HTMLの「data-registered-dates」属性から文字列データを取得する
	const rawData = calendarGrid.getAttribute("data-registered-dates");
	
	// データが設定されてなければ空の配列を返す
	if (!rawData) {
		return []
	};
	
	// 記号や空白を全て削除してカンマ区切りで分割して配列にして返す
	return rawData.replace(/[\[\]\s]/g, "").split(",");
}

// 登録済み日付リストを取得して変数に保持する
const registeredDates = getRegisteredDates();

// 指定した年月日が日本の祝日及び振替休日かどうか判定する関数を定義する
function isJapaneseHoliday(year, month, day) {
	// 指定した年月日のDateオブジェクトを作成する
	const dateObj = new Date(year, month, day);
	// JSの月（0～11）に1を足して一般的な月（1～12）に変換する
	const m = month + 1;
	// 固定祝日や計算祝日かを判定する内部関数を作成する
	const isBasicHoliday = (y, mVal, dVal) => {
		// 1月1日: 元日
		if (mVal === 1 && dVal === 1) {
			return true
		};
		// 2月11日: 建国記念の日
		if (mVal === 2 && dVal === 11) {
			return true
		};
		// 2月23日: 天皇誕生日
		if (mVal === 2 && dVal === 23) {
			return true
		};
		// 4月29日: 昭和の日
		if (mVal === 4 && dVal === 29) {
			return true
		};
		// 5月3日: 憲法記念日
		if (mVal === 5 && dVal === 3) {
			return true
		};
		// 5月4日: みどりの日
		if (mVal === 5 && dVal === 4) {
			return true
		};
		// 5月5日: こどもの日
		if (mVal === 5 && dVal === 5) {
			return true
		};
		// 8月11日: 山の日
		if (mVal === 8 && dVal === 11) {
			return true
		};
		// 11月3日: 文化の日
		if (mVal === 11 && dVal === 3) {
			return true
		};
		// 11月23日: 勤労感謝の日
		if (mVal === 11 && dVal === 23) {
			return true
		};
		// 3月の春分の日を簡易計算公式で判定
		if (mVal === 3 && dVal === Math.floor(20.8431 + 0.242194 * (y - 1980) - Math.floor((y - 1980) / 4))) {
			return true
		};
		// 9月の秋分の日を簡易計算公式で判定
		if (mVal === 9 && dVal === Math.floor(23.2488 + 0.242194 * (y - 1980) - Math.floor((y - 1980) / 4))) {
			return true
		};
		
		// 指定した月の「第n月曜日」の日付を計算する関数
		const getNthMonday = (n) => {
			// 月曜日の出現回数をカウントする変数
			let count = 0;
			
			// 1日から31日まで順番にチェックする
			for (let d = 1; d <= 31; d++) {
				// 曜日が「1（月曜日）」の場合
				if (new Date(y, mVal - 1, d).getDay() === 1) {
					// カウントを増やす
					count++;
					
					// 目的の第n月曜日ならその「日」を返す
					if (count === n) return d;
				}
			}
			
			// 見つからなければカウント数を0で返す
			return 0;
		};
		
		// 1月第2月曜: 成人の日
		if (mVal === 1 && dVal === getNthMonday(2)) {
			return true
		};
		// 7月第3月曜日: 海の日
		if (mVal === 7 && dVal === getNthMonday(3)) {
			return true
		};
		// 9月第3月曜日: 敬老の日
		if (mVal === 9 && dVal === getNthMonday(3)) {
			return true
		};
		// 10月第2月曜日: スポーツの日
		if (mVal === 10 && dVal === getNthMonday(2)) {
			return true
		};
		
		// いずれの祝日にも該当しない場合はfalse
		return false;
	};
	
	// 基本的な祝日に該当していればtrueを返す
	if (isBasicHoliday(year, m, day)) {
		return true
	};
	
	// チェック対象のDateオブジェクトを作成する
	let checkDate = new Date(year, month, day);
	
	// 日曜日以外の場合に振替休日判定を実施
	if (checkDate.getDay() !== 0) {
		// 最大3日前までさかのぼって確認
		for (let i = 1; i <= 3; i++) {
			// i日前の日付を作成する
			let prev = new Date(year, month, day - i);
			
			// i日前が基本祝日である場合
			if (isBasicHoliday(prev.getFullYear(), prev.getMonth() + 1, prev.getDate())) {
				// その祝日が日曜日だった場合
				if (prev.getDay() === 0) {
					// 振替休日と判定
					return true;
				}
			
			// i日前が基本祝日でない場合
			} else {
				// ループを中断
				break;
			}
		}
	}
	
	// 祝日でも振替休日でもない場合はfalse
	return false;
}

// 指定された年月でカレンダーを作成及び画面表示する関数
function renderCalendar(year, month){
	// カレンダー表示エリアを一旦空にする
	calendarGrid.innerHTML = "";
	
	// 【修正】年月選択ピッカー要素が存在する場合、表示値を「YYYY-MM」形式でセットする
	if (yearMonthPicker) {
		const formattedMonth = String(month + 1).padStart(2, '0');
		yearMonthPicker.value = `${year}-${formattedMonth}`;
	}
	
	// 表示月の1日のDateオブジェクトを作成する
	const firstDay = new Date(year, month, 1);
	// 表示月の最終日のDateオブジェクトを作成する
	const lastDay =	new Date(year, month + 1, 0);
	// 前月の最終日のDateオブジェクトを作成する
	const prevLastDay =	new Date(year, month, 0);
	// 1日の曜日（0: 日～6: 土）を取得する
	const startDay = firstDay.getDay();
	// 表示月の総日数（例: 28 or 29 or 30 or 31）を取得する
	const daysInMonth = lastDay.getDate();
	// 前月の総日数（例: 28 or 29 or 30 or 31）を取得する
	const prevMonthDays = prevLastDay.getDate();
	// カレンダーの全マス目数（7列 × 6行 = 42）
	const totalCells = 42;
	
	// 当月の日付カレンダー（1日からスタート）
	let dayCount = 1;
	// 翌月の日付カレンダー（1日からスタート）
	let nextDay = 1;
	
	// 42個のマスを順番に生成するループ
	for(let i=0;i<totalCells;i++){
		// マス目となる<div>要素を作成する
		const cell = document.createElement("div");
		
		// 「day」クラスを付与する
		cell.classList.add("day");
		
		// セルに対する「年」を保持する変数
		let cellYear = year;
		// セルに対する「月」を保持する変数
		let cellMonth = month;
		// マスに表示する「日」の数字を入れる変数
		let displayDate;
		
		// 1日より前のマス（前月の日付領域）の場合
		if(i < startDay){
			// 前月の日付を計算
			displayDate = prevMonthDays - startDay + i + 1;
			
			// 「other_month」クラスを付与して薄く表示する
			cell.classList.add("other_month");
			// 月を前月に表示する
			cellMonth = month - 1;
			
			// 1月より前（前年12月）にまたぐ場合
			if (cellMonth < 0) {
				// 12月にセットする
				cellMonth = 11;
				// 年を1引く
				cellYear--;
			}
		
		// 当月の日付領域の場合
		}else if(dayCount <= daysInMonth){
			// 当月の日付をセットする
			displayDate = dayCount;
			
			// 月を2桁の文字列にする（例: "09"）
			const formattedMonth = String(month + 1).padStart(2, '0');
			// 日を2桁の文字列にする（例: "05"）
			const formattedDay = String(dayCount).padStart(2, '0');
			// 「YYYY-MM-DD」形式の文字列を作成する
			const dateStr = `${year}-${formattedMonth}-${formattedDay}`;
			
			// 今日の年月日と完全一致する場合
			if(year === today.getFullYear() && month === today.getMonth() && dayCount === today.getDate()
			){
				// 今日なら「today」クラスを付与する
				cell.classList.add("today");
			}
			
			// データ登録済み日付リストに含まれている場合
			if(registeredDates.includes(dateStr)){
				// todayクラスを削除する
				cell.classList.remove("today");
				// データあり用スタイル「has_data」クラスを付与する
				cell.classList.add("has_data");
				// マス目がクリックされた時に明細一覧ページへ遷移する処理を登録
				cell.addEventListener("click", () => {
					window.location.href = `/kakeibo/balance/list?date=${dateStr}`;
				});
			}
			
			// 日付カウンターを1進める
			dayCount++;
		
		// 当月が終わって翌月の日付領域の場合
		}else{
			// 翌月の日付をセットしてカウンターを1増やす
			displayDate = nextDay++;
			
			// 「other_month」クラスを付与する
			cell.classList.add("other_month");
			// 月を翌月に変更する
			cellMonth = month + 1;
			
			// 12月を超えて翌年1月になる場合
			if (cellMonth > 11) {
				// 1月にセットする
				cellMonth = 0;
				
				// 年を1増やす
				cellYear++;
			}
		}
		
		// 該当の日付が祝日の場合
		if(isJapaneseHoliday(cellYear, cellMonth, displayDate)){
			// 「holiday」クラスを付与する
			cell.classList.add("holiday");
		}
		
		// 今日が1列目の場合
		if(i % 7 === 0){
			// 「sunday」クラスを付与する
			cell.classList.add("sunday");
		}
		
		// 今日が7列目の場合
		if(i % 7 === 6){
			// 「saturday」クラスを付与する
			cell.classList.add("saturday");
		}
		
		// セルの中に表示日付の数字をセットする
		cell.textContent = displayDate;
		
		// 完成したセルをカレンダー容器に追加する
		calendarGrid.appendChild(cell);
	}
}

// id名「prevMonth」のボタン（前月ボタン）にクリックイベントを追加する
document.getElementById("prevMonth").addEventListener("click", () => {
	// 遷移先の年を変数化する
	let targetYear = currentYear;
	// 遷移先の月（前月）を変数化する
	let targetMonth = currentMonth - 1;
	
	// 1月未満になった場合
	if(targetMonth < 0){
		// 12月にセットする
		targetMonth = 11;
		
		// 年を1減らす
		targetYear--;
	}
	
	// 月を2桁の文字列にする（例: "09"）
	const formattedMonth = String(targetMonth + 1).padStart(2, '0');
	
	// 前月のカレンダーページURLへ遷移する
	window.location.href = `/kakeibo/balance/calendar?yearMonth=${targetYear}-${formattedMonth}`;
});

// id名「nextMonth」のボタン（次月ボタン）にクリックイベントを追加する
document.getElementById("nextMonth").addEventListener("click", () => {
	// 遷移先の年を変数化する
	let targetYear = currentYear;
	// 遷移先の月（翌月）を変数化する
	let targetMonth = currentMonth + 1;
	
	// 12月を超えた場合
	if(targetMonth > 11){
		// 1月にセットする
		targetMonth = 0;
		
		// 年を1増やす
		targetYear++;
	}
	
	// 月を2桁の文字列にする（例: "09"）
	const formattedMonth = String(targetMonth + 1).padStart(2, '0');
	
	// 翌月のカレンダーページURLへ遷移する
	window.location.href = `/kakeibo/balance/calendar?yearMonth=${targetYear}-${formattedMonth}`;
});

// 年月選択ピッカー要素が存在する場合
if (yearMonthPicker) {
	// 年月選択ピッカーの値が変更されたときに
	yearMonthPicker.addEventListener("change", (e) => {
		// 選択された年月（例: "2026-10"）を取得する
		const selectedYearMonth = e.target.value;
		
		// 選択された年月が存在する場合
		if (selectedYearMonth) {
			// 選択された年月のカレンダー画面URLへ遷移する
			window.location.href = `/kakeibo/balance/calendar?yearMonth=${selectedYearMonth}`;
		}
	});
}

// 画面表示時に現在の年月でカレンダーを描画する
renderCalendar(currentYear, currentMonth);