/**
 * =========================================================
 * ダッシュボードの月別収支グラフ＆繰越金額計算処理
 * =========================================================
 */

// HTML文書の読み込み完了時の処理
document.addEventListener("DOMContentLoaded", () => {
	// グローバル変数から取引データを取得する（データがない場合は空オブジェクトをセットする）
	const transactionData = window.dbTransactionData || {};
	// 今日の日付情報を取得する
	const today = new Date();

	// 表示対象の「年」を取得する
	let currentYear = today.getFullYear();
	// 表示対象の「月（1～12）」を取得する
	let currentMonth = today.getMonth() + 1;

	// 前月移動ボタン要素を取得する
	const prevBtn = document.getElementById("prevMonth");
	// 翌月移動ボタン要素を取得する
	const nextBtn = document.getElementById("nextMonth");
	// 現在月表示ラベル要素を取得する
	const monthLabel = document.getElementById("currentMonthLabel");
	// 年月選択ピッカー要素を取得する
	const monthPicker = document.getElementById("monthPicker");
	// 総収入表示要素を取得する
	const totalIncomeEl = document.querySelector(".income_mony");
	// 総支出表示要素を取得する
	const totalExpenseEl = document.querySelector(".expenditure_mony");
	// 残高表示要素のリストを取得する
	const balanceElements = document.querySelectorAll(".balance_mony");
	// 1つ目の残高表示要素（繰越金用）を取得する
	const carryOverEl = balanceElements[0];
	// 2つ目の残高表示要素（当月差引残高用）を取得する
	const balanceEl = balanceElements[1];
	// 収入グラフの表示枠要素を取得する
	const incomeBarWrapper = document.querySelector(".in_bar");
	// 支出グラフの表示枠要素を取得する
	const expenseBarWrapper = document.querySelector(".ex_bar");

	// ツールチップ表示用の要素を取得する
	let tooltip = document.querySelector(".chart-tooltip");

	// ツールチップ要素が存在しない場合
	if (!tooltip) {
		// <div> 要素を新規作成する
		tooltip = document.createElement("div");

		// クラス名「chart-tooltip」を付与する
		tooltip.className = "chart-tooltip";

		// body要素の末尾に追加する
		document.body.appendChild(tooltip);
	}

	// 数値を「¥ 1,234」形式の通貨表記に変換する関するを定義する
	function formatCurrency(num) {
		// 数値を3桁カンマ区切りに変換して先頭に「¥ 」をつけて返す
		return `¥ ${num.toLocaleString()}`;
	}

	// グラフ描画用の最大上限金額を計算する関数
	function getCeilUpperLimit(amount) {
		// 金額が0以下の場合
		if (amount <= 0) {
			// 初期上限値とし10を返す
			return 100000;
		}

		// 桁数を算出する
		const digit = Math.pow(10, Math.floor(Math.log10(amount)));

		// キリの良い数値に切り上げて返す
		return Math.ceil(amount / digit) * digit;
	}

	// 画面の金額表示とグラフ描画を最新状態に更新する関数
	function update() {
		// 月を2桁文字列（例: "09"）に変換する
		const formattedMonth = String(currentMonth).padStart(2, "0");
		// 照合用の「YYYY-MM」キー文字列を作成する
		const monthKey = `${currentYear}-${formattedMonth}`;

		// 現在月表示ラベルが存在する場合
		if (monthLabel) {
			// ラベルのテキストを表示対象年月（例: "2026年9月"）に更新する
			monthLabel.textContent = `${currentYear}年${currentMonth}月`;
		}

		// 年月選択ピッカーが存在する場合
		if (monthPicker) {
			// ピッカーの選択値を表示対象年月（例: "2026-09"）に更新する
			monthPicker.value = `${currentYear}-${formattedMonth}`;
		}

		// 繰越金額を0で初期化する
		let carryOver = 0;
		// 取引データの全月キーを昇順ソートして繰越金を計算する
		Object.keys(transactionData).sort().forEach(key => {
			// 選択月より過去のデータの場合
			if (key < monthKey) {
				// 該当月の収入合計を算出する
				const inc = transactionData[key].income?.reduce((s, i) => s + i.amount, 0) || 0;

				// 該当月の支出合計を算出する
				const exp = transactionData[key].expense?.reduce((s, i) => s + i.amount, 0) || 0;

				// 差引金額（収入－支出）を繰越金に加算する
				carryOver += (inc - exp);
			}
		});

		// 選択月のデータ（存在しない場合は空データ）を取得する
		const currentData = transactionData[monthKey] || { income: [], expense: [] };
		// 当月の収入合計を算出する
		const totalIncome = currentData.income ? currentData.income.reduce((s, i) => s + i.amount, 0) : 0;
		// 当月の支出合計を算出する
		const totalExpense = currentData.expense ? currentData.expense.reduce((s, i) => s + i.amount, 0) : 0;
		// 当月の差引残高（前月繰越＋当月収入－当月支出）を算出する
		const balance = (totalIncome + carryOver) - totalExpense;

		// 繰越金表示要素が存在する場合
		if (carryOverEl) {
			// 繰越金額をフォーマットして表示する
			carryOverEl.textContent = formatCurrency(carryOver);
		}

		// 総収入表示要素が存在する場合
		if (totalIncomeEl) {
			// 当月収入合計をフォーマットして表示する
			totalIncomeEl.textContent = formatCurrency(totalIncome);
		}

		// 総支出表示要素が存在する場合
		if (totalExpenseEl) {
			// 当月支出合計をフォーマットして表示する
			totalExpenseEl.textContent = formatCurrency(totalExpense);
		}

		// 当月差引残高表示要素が存在する場合
		if (balanceEl) {
			// 差引残高をフォーマットして表示する
			balanceEl.textContent = formatCurrency(balance);
		}

		// 収入と支出のうち大きい方の金額を元にグラフの最大基準値を算出する
		const maxVal = getCeilUpperLimit(Math.max(totalIncome, totalExpense));

		// 収入積み上げグラフを描画する
		renderStackedBar(incomeBarWrapper, currentData.income, maxVal);
		// 支出積み上げグラフを描画する
		renderStackedBar(expenseBarWrapper, currentData.expense, maxVal);
	}

	// 積み上げ棒グラフを描画する関数
	function renderStackedBar(targetEl, items, maxVal) {
		// 描画先の親要素が存在しない場合
		if (!targetEl) {
			// 処理を終了する
			return;
		}

		// 描画エリアの内部要素を一旦クリアする
		targetEl.innerHTML = "";

		// 内訳データが存在しない場合
		if (!items || items.length === 0) {
			// 処理を終了する
			return;
		}

		// 各内訳データごとにループ処理を行う
		items.forEach(item => {
			// 金額が0以下のデータの場合
			if (item.amount <= 0) {
				// 描画をスキップする
				return;
			}

			// グラフの1要素（ブロック）となる <div> を作成する
			const seg = document.createElement("div");
			// 最大値に対する割合（％）を計算する
			const heightPercent = (item.amount / maxVal) * 100;

			// ブロックにCSSクラス名をセットする
			seg.className = `bar-segment ${item.className || ""}`;
			// 計算した割合を高さとしてCSSに設定する
			seg.style.height = `${heightPercent}%`;

			// マウス移動時
			seg.addEventListener("mousemove", (e) => {
				// ツールチップを表示状態にする
				tooltip.style.display = "block";
				// カテゴリ名と金額をツールチップにセットする
				tooltip.innerHTML = `<strong>${item.category}</strong>: ¥ ${item.amount.toLocaleString()}`;
				// マウス位置の右下（12pxオフセット）にツールチップを配置する
				tooltip.style.left = `${e.pageX + 12}px`;
				tooltip.style.top = `${e.pageY + 12}px`;
			});

			// マウスが離れた時
			seg.addEventListener("mouseleave", () => {
				// ツールチップを非表示にする
				tooltip.style.display = "none";
			});

			// 作成したブロック要素を親要素に追加する
			targetEl.appendChild(seg);
		});
	}

	// 前月移動ボタンが存在する場合
	if (prevBtn) {
		// 前月移動ボタンがクリックされた時
		prevBtn.addEventListener("click", () => {
			// 表示月を1減らす
			currentMonth--;

			// 1月未満（0月）になった場合
			if (currentMonth < 1) {
				// 前年の12月に設定する
				currentMonth = 12;
				currentYear--;
			}

			// 画面表示を更新する
			update();
		});
	}

	// 翌月移動ボタンが存在する場合
	if (nextBtn) {
		// 翌月移動ボタンがクリックされた時
		nextBtn.addEventListener("click", () => {
			// 表示月を1増やす
			currentMonth++;

			// 12月を超えた（13月）場合
			if (currentMonth > 12) {
				// 翌年の1月に設定する
				currentMonth = 1;
				currentYear++;
			}

			// 画面表示を更新する
			update();
		});
	}

	// 年月選択ピッカーが存在する場合
	if (monthPicker) {
		// ピッカーの値が変更された時
		monthPicker.addEventListener("change", (e) => {
			// 選択された文字列（例: "2026-09"）を取得する
			const val = e.target.value;

			// 値が存在する場合
			if (val) {
				// ハイフンで分割して年・月を数値として取得する
				const [year, month] = val.split("-").map(Number);
				// 表示年を更新する
				currentYear = year;
				// 表示月を更新する
				currentMonth = month;

				// 画面表示を更新する
				update();
			}
		});
	}

	// 現在月表示ラベルと年月選択ピッカーが両方存在する場合
	if (monthLabel && monthPicker) {
		// ラベルのマウスカーソルをポインターに変更する
		monthLabel.style.cursor = "pointer";

		// 現在月表示ラベルがクリックされた時
		monthLabel.addEventListener("click", () => {
			// showPicker メソッドがサポートされている場合
			if (typeof monthPicker.showPicker === "function") {
				// ブラウザ標準のピッカー起動処理を実行する
				monthPicker.showPicker();
			} else {
				// 非対応ブラウザの場合は通常の click イベントを実行する
				monthPicker.click();
			}
		});
	}

	// 初期表示時、画面描画処理を実行する
	update();
});