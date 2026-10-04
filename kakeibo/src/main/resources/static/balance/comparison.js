// HTMLからcanvas要素を取得する
const canvas = document.getElementById("expenseChart");

// キャンバスの2D描画コンテキストを取得する
const ctx = canvas.getContext("2d");

// X軸ラベルとなる1月〜12月の文字列配列
const months = [
	"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"
];

// グローバル変数で計算された座標データを保持する（マウス判定用）
let currentPoints = [];

// 現在ホバー中のポイントのインデックスを取得する（非ホバー時は-1）
let hoveredIndex = -1;

// データ配列を保持する
let globalMonthlyData = [];

// HTML5 Canvas APIを直接使って折れ線グラフを描画する関数
function drawChart(data, hoverIdx = -1) {
	// キャンバスの横幅を取得する
	const width = canvas.width;
	// キャンバスの高さを取得する
	const height = canvas.height;

	// 描画領域の左余白（Y軸ラベル表示用）
	const paddingLeft = 80;
	// 描画領域の右余白
	const paddingRight = 40;
	// 描画領域の上余白（数値ラベル表示用）
	const paddingTop = 50;
	// 描画領域の下余白（X軸ラベル表示用）
	const paddingBottom = 50;

	// 実際のグラフ描画領域の幅を計算する
	const graphWidth = width - paddingLeft - paddingRight;
	// 実際のグラフ描画領域の高さを計算する
	const graphHeight = height - paddingTop - paddingBottom;

	// キャンバス全体をクリアにして初期化する
	ctx.clearRect(0, 0, width, height);

	// データ内の最大値を取得する（すべて0の場合は最小10,000円で計算）
	let maxDataValue = Math.max(...data, 10000);

	// キリの良い最大値（1万円単位で切り上げ）を算出する
	const yMax = Math.ceil(maxDataValue / 10000) * 10000;

	/**
	 * グリッド線とY軸目盛の描画
	 */
	// Y軸の分割数
	const gridCount = 5;
	// 線の太さ
	ctx.lineWidth = 1;
	// テキストを右寄せに設定
	ctx.textAlign = "right";
	// テキスト上下位置を中央に設定
	ctx.textBaseline = "middle";
	// フォント設定
	ctx.font = "15px sans-serif";

	for (let i = 0; i <= gridCount; i++) {
		// Y軸目盛りの描画位置Y座標を計算
		const y = paddingTop + (graphHeight / gridCount) * i;
		// 該当目盛りの表示金額値を計算
		const value = Math.round(yMax - (yMax / gridCount) * i);

		// 背景グリッド横線を描画（スペル修正：strokeStyle）
		ctx.strokeStyle = "rgba(225, 225, 225, 1)";
		ctx.beginPath();
		ctx.moveTo(paddingLeft, y);
		ctx.lineTo(width - paddingRight, y);
		ctx.stroke();

		// Y軸金額テキストを描画
		ctx.fillStyle = "rgba(100, 100, 100, 1)";
		ctx.fillText(value.toLocaleString() + "円", paddingLeft - 10, y);
	}

	/**
	 * X軸ラベル（1月〜12月）の描画
	 */
	// テキストを中央寄せに設定
	ctx.textAlign = "center";
	// テキスト位置を上揃えに設定
	ctx.textBaseline = "top";
	// X軸目盛り間隔（スペル修正：xStep）
	const xStep = graphWidth / (months.length - 1);

	for (let i = 0; i < months.length; i++) {
		// 各月の描画位置X座標を計算
		const x = paddingLeft + xStep * i;
		ctx.fillStyle = "rgba(80, 80, 80, 1)";
		// 月テキストを表示
		ctx.fillText(months[i], x, height - paddingBottom + 10);
	}

	/**
	 * データ座標の計算と折れ線の描画
	 */
	currentPoints = [];
	for (let i = 0; i < data.length; i++) {
		const x = paddingLeft + xStep * i;
		// 金額に対応するY座標を反転計算
		const y = height - paddingBottom - (data[i] / yMax) * graphHeight;
		currentPoints.push({ x: x, y: y, value: data[i], month: months[i] });
	}

	// 折れ線のスタイリングと描画
	ctx.strokeStyle = "rgba(50, 95, 50, 1)";
	ctx.lineWidth = 3;
	ctx.beginPath();

	for (let i = 0; i < currentPoints.length; i++) {
		if (i === 0) {
			ctx.moveTo(currentPoints[i].x, currentPoints[i].y);
		} else {
			ctx.lineTo(currentPoints[i].x, currentPoints[i].y);
		}
	}
	ctx.stroke();

	/**
	 * データポイント（丸ポチ）と数値ラベルの描画
	 */
	for (let i = 0; i < currentPoints.length; i++) {
		const p = currentPoints[i];

		// 現在処理中の要素がホバー対象かどうかを判定する
		const isHovered = (i === hoverIdx);

		// 円（丸ポチ）を描画
		ctx.fillStyle = isHovered ? "rgba(45, 90, 45, 1)" : "rgba(255, 255, 255, 1)";
		ctx.strokeStyle = "rgba(45, 90, 45, 1)";
		ctx.lineWidth = 2;
		ctx.beginPath();
		// ホバー中の丸ポチは少し大きく（半径7.5px、通常は5px）
		ctx.arc(p.x, p.y, isHovered ? 7.5 : 5, 0, Math.PI * 2);
		ctx.fill();
		ctx.stroke();
	}

	/**
	 * カーソル合わせ（ホバー）時のみ金額吹き出しラベルを表示
	 */
	if (hoverIdx >= 0 && hoverIdx < currentPoints.length) {
		const p = currentPoints[hoverIdx];
		const text = p.value.toLocaleString() + "円";

		ctx.font = "bold 15px sans-serif";
		const textWidth = ctx.measureText(text).width;
		
		// ツールチップ背景のサイズと位置
		const rectWidth = textWidth + 15;
		const rectHeight = 30;
		const rectX = p.x - rectWidth / 2;
		const rectY = p.y - 40;

		// ツールチップ背景
		ctx.fillStyle = "rgba(45, 90, 45, 1)";
		ctx.beginPath();
		ctx.roundRect(rectX, rectY, rectWidth, rectHeight, 5);
		ctx.fill();

		// ツールチップの小さな三角形（矢印）
		ctx.beginPath();
		ctx.moveTo(p.x - 5, rectY + rectHeight);
		ctx.lineTo(p.x + 5, rectY + rectHeight);
		ctx.lineTo(p.x, rectY + rectHeight + 5);
		ctx.closePath();
		ctx.fill();

		// ツールチップ内金額テキスト描画
		ctx.fillStyle = "rgba(255, 255, 255, 1)";
		ctx.textAlign = "center";
		ctx.textBaseline = "middle";
		ctx.fillText(text, p.x, rectY + rectHeight / 2);
	}
}

/**
 * マウス移動時の判定とホバー処理（イベントリスナー）
 */
canvas.addEventListener("mousemove", (e) => {
	const rect = canvas.getBoundingClientRect();
	// CSSでの表示幅とCanvas解像度の比率調整
	const scaleX = canvas.width / rect.width;
	const scaleY = canvas.height / rect.height;

	const mouseX = (e.clientX - rect.left) * scaleX;
	const mouseY = (e.clientY - rect.top) * scaleY;

	let foundIndex = -1;
	// 丸ポチ周辺の判定感度（半径15px以内で反応）
	const hoverRadius = 15;

	for (let i = 0; i < currentPoints.length; i++) {
		const p = currentPoints[i];
		const dx = mouseX - p.x;
		const dy = mouseY - p.y;
		// 距離計算（三平方の定理）で丸ポチ付近にカーソルがあるか判定
		if (Math.sqrt(dx * dx + dy * dy) <= hoverRadius) {
			foundIndex = i;
			break;
		}
	}

	// ホバー対象が変わった場合のみ再描画
	if (foundIndex !== hoveredIndex) {
		hoveredIndex = foundIndex;
		canvas.style.cursor = hoveredIndex !== -1 ? "pointer" : "default";
		drawChart(globalMonthlyData, hoveredIndex);
	}
});

// キャンバス外にマウスが外れた場合はツールチップを消去
canvas.addEventListener("mouseleave", () => {
	if (hoveredIndex !== -1) {
		hoveredIndex = -1;
		canvas.style.cursor = "default";
		drawChart(globalMonthlyData, -1);
	}
});

// フォームの送信処理を行う関数
function submitForm() {
	// HTML内のフォーム要素を取得して送信実行（画面再読み込み）
	document.getElementById("comparisonForm").submit();
}

// 「年」セレクトボックス変更時にフォーム送信イベントを登録
document.getElementById("yearSelect").addEventListener("change", submitForm);

// 「カテゴリ」セレクトボックス変更時にフォーム送信イベントを登録
document.getElementById("categorySelect").addEventListener("change", submitForm);

// HTMLの canvas 要素から data-totals 属性（例: "[0, 0, 12000, ...]"）を取得
const totalsAttribute = canvas.getAttribute("data-totals");

// 属性値が存在する場合に JSON 解析を行って描画処理を実行
if (totalsAttribute) {
	// 文字列の配列データをJavaScript配列オブジェクトに変換
	globalMonthlyData = JSON.parse(totalsAttribute);
	// グラフを描画する関数を呼び出し
	drawChart(globalMonthlyData);
}