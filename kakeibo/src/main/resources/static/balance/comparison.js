const ctx = document.getElementById("expenseChart");
let chart;
const months = [
	"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"
];
function drawChart(data){
    if(chart){
        chart.destroy();
    }
    chart = new Chart(ctx,{
        type:"line",
        data:{
            labels:months,
            datasets:[{
                label:"支出",
                data:data,
                borderColor:"#2f5d2f",
                backgroundColor:"#ffffff",
                borderWidth:2,
                pointRadius:7,
                pointHoverRadius:10,
                pointBackgroundColor:"#ffffff",
                pointBorderColor:"#2f5d2f",
                tension:0
            }]
        },
        options:{
            responsive:true,
            maintainAspectRatio:false,
            plugins:{
                legend:{
                    display:false
                },
                tooltip:{
                    callbacks:{
                        label:function(context){
                            return context.raw.toLocaleString() + "円";
                        }
                    }
                }
            },
            scales:{
                y:{
                    beginAtZero:true,
                    ticks:{
                        callback:function(value){
                            return value.toLocaleString() + "円";
                        }
                    }
                }
            }
        }
    });
}
async function loadData(){
    const year =
        document.getElementById("yearSelect").value;
    const category =
        document.getElementById("categorySelect").value;
    /*
      SpringBoot API呼び出し例
      GET
      /api/graph/monthly?year=2026&category=food
    */
    /*
    const response = await fetch(
        `/api/graph/monthly?year=${year}&category=${category}`
    );
    const data = await response.json();
    drawChart(data);
    */
    // サンプルデータ
    const data = [
        25000,
        33000,
        29000,
        23000,
        32000,
        25000,
        35000,
        28000,
        25000,
        36000,
        26000,
        35000
    ];
    drawChart(data);
}
document
    .getElementById("yearSelect")
    .addEventListener("change",loadData);
document
    .getElementById("categorySelect")
    .addEventListener("change",loadData);
loadData();