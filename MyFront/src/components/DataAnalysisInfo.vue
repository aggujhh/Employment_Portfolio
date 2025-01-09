<template>
    <section id="dataAnalysisInfo">
        <div :class="{ expanded: isExpanded.salesReport }">
            <div id="salesReport"></div>
            <p>{{ dateName }} 総売上: {{ salesTotal }}</p>
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                @click="toggleExpand('salesReport')">
                <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    :d="isExpanded.salesReport
                        ? 'M15 19v-2a2 2 0 0 1 2-2h2M15 5v2a2 2 0 0 0 2 2h2M5 15h2a2 2 0 0 1 2 2v2M5 9h2a2 2 0 0 0 2-2V5'
                        : 'M4 8V6a2 2 0 0 1 2-2h2m8 0h2a2 2 0 0 1 2 2v2m0 8v2a2 2 0 0 1-2 2h-2m-8 0H6a2 2 0 0 1-2-2v-2'" />
            </svg>
        </div>
        <div :class="{ expanded: isExpanded.visitCountReport }">
            <div id="visitCountReport"></div>
            <p>{{ dateName }} 総来店客数: {{ customerCountTotal }}</p>
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                @click="toggleExpand('visitCountReport')">
                <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    :d="isExpanded.visitCountReport
                        ? 'M15 19v-2a2 2 0 0 1 2-2h2M15 5v2a2 2 0 0 0 2 2h2M5 15h2a2 2 0 0 1 2 2v2M5 9h2a2 2 0 0 0 2-2V5'
                        : 'M4 8V6a2 2 0 0 1 2-2h2m8 0h2a2 2 0 0 1 2 2v2m0 8v2a2 2 0 0 1-2 2h-2m-8 0H6a2 2 0 0 1-2-2v-2'" />
            </svg>
        </div>
        <div :class="{ expanded: isExpanded.tableTurnoverRate }">
            <div id="tableTurnoverRate"></div>
            <p>{{ dateName }} 平均回転率: {{ averageTurnoverRate }}</p>
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                @click="toggleExpand('tableTurnoverRate')">
                <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    :d="isExpanded.tableTurnoverRate
                        ? 'M15 19v-2a2 2 0 0 1 2-2h2M15 5v2a2 2 0 0 0 2 2h2M5 15h2a2 2 0 0 1 2 2v2M5 9h2a2 2 0 0 0 2-2V5'
                        : 'M4 8V6a2 2 0 0 1 2-2h2m8 0h2a2 2 0 0 1 2 2v2m0 8v2a2 2 0 0 1-2 2h-2m-8 0H6a2 2 0 0 1-2-2v-2'" />
            </svg>
        </div>
        <div :class="{ expanded: isExpanded.ranking }">
            <div id="ranking"></div>
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24"
                @click="toggleExpand('ranking')">
                <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    :d="isExpanded.ranking
                        ? 'M15 19v-2a2 2 0 0 1 2-2h2M15 5v2a2 2 0 0 0 2 2h2M5 15h2a2 2 0 0 1 2 2v2M5 9h2a2 2 0 0 0 2-2V5'
                        : 'M4 8V6a2 2 0 0 1 2-2h2m8 0h2a2 2 0 0 1 2 2v2m0 8v2a2 2 0 0 1-2 2h-2m-8 0H6a2 2 0 0 1-2-2v-2'" />
            </svg>
        </div>
    </section>
    <button @click="openDialog">AI分析</button>
    <AiAnalysisDialog :isVisible="isVisible.ai" :reportData="reportData" @close="closeModal"/>
</template>

<script setup>
import { computed, watch, ref, reactive } from "vue";
import { useRoute } from "vue-router";
import * as echarts from 'echarts';

/*************************************
* 画面モーダル設定
**************************************/
import AiAnalysisDialog from "./AiAnalysisDialog.vue";
const isVisible = reactive({
    ai: false,
})
// モーダルを閉じる関数
const closeModal = () => {
    isVisible.ai = false;
};

/*************************************
* AI分析画面を開く
**************************************/
const openDialog = () => {
    isVisible.ai = true;
}


/*************************************
*統計データをフェッチする
**************************************/
import { fetchReportDataByCountDays } from "@/api/reportApi";

const reportData = ref()
const send_fetchReportDataByCountDays = async (countDays) => {
    try {
        const res = await fetchReportDataByCountDays({ countDays: countDays });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            reportData.value = res.data.data;
            
            if (countDays == 30) {
                reportData.value.date = ["1~5日", "6~10日", "11~15日", "16~20日", "21~25日", "26日~ラスト"]
            } else if (countDays == 365) {
                reportData.value.date = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
            }
            setEcharts(
                reportData.value.date,
                reportData.value.sales,
                "salesReport",
                "売上高統計",
                "売上"
            );
            setEcharts(
                reportData.value.date,
                reportData.value.customerCount,
                "visitCountReport",
                "来店客数統計",
                "客数"
            );
            setEcharts(
                reportData.value.date,
                reportData.value.turnoverRate,
                "tableTurnoverRate",
                "テーブル回転率統計",
                "回転率(%)"
            );
            setRankingEcharts(
                reportData.value.topDishesNames,
                reportData.value.topDishesTotalCounts,
            );
        } else {
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}


// ルーターとルートの取得
const route = useRoute()

const countDays = ref(0)
const dateName = ref()

watch(
    () => route.params.dateType,
    (newValue) => {
        if (newValue === "3day") {
            countDays.value = 3
            dateName.value = "三日間"
        } else if (newValue === "5day") {
            countDays.value = 5
            dateName.value = "五日間"
        } else if (newValue === "1week") {
            countDays.value = 7
            dateName.value = "今週"
        } else if (newValue === "1mooth") {
            countDays.value = 30
            dateName.value = "今月"
        } else {
            countDays.value = 365
            dateName.value = "今年"
        }
        send_fetchReportDataByCountDays(countDays.value);
    },
    { immediate: true }
);

const setEcharts = (xAxisData, seriesData, idName, title, legend) => {
    const dom = document.getElementById(idName);

    // 检查是否已存在实例并销毁
    if (echarts.getInstanceByDom(dom)) {
        echarts.dispose(dom);
    }

    // 初始化新的图表实例
    const myChart = echarts.init(dom);

    const option = {
        title: {
            text: title,
        },
        tooltip: {},
        legend: {
            data: [legend],
        },
        xAxis: {
            type: "category",
            axisTick: {
                alignWithLabel: true,
            },
            data: xAxisData,
        },
        yAxis: {},
        series: [
            {
                name: legend,
                type: "line",
                data: seriesData,
                lineStyle: {
                    color: "#fbb034",
                },
                itemStyle: {
                    color: "#3b3632",
                },
                label: {
                    show: true,
                    position: "top",
                    fontSize: 12,
                },
            },
        ],
    };
    myChart.setOption(option);
}

const setRankingEcharts = (xAxisData, seriesData) => {
    const dom = document.getElementById("ranking");

    // 检查是否已存在实例并销毁
    if (echarts.getInstanceByDom(dom)) {
        echarts.dispose(dom);
    }

    // 初始化新的图表实例
    const myChart = echarts.init(dom);

    const option = {
        title: {
            text: "人気料理ランキング",
        },
        tooltip: {},
        legend: {
            data: ["オーダー数"],
        },
        grid: {
            left: "23%",
        },
        xAxis: {},
        yAxis: {
            type: "category",
            axisTick: {
                alignWithLabel: true,
            },
            data: xAxisData,
            align: "center",
            inverse: true,
        },
        series: [
            {
                name: "オーダー数",
                type: "bar",
                data: seriesData,
                itemStyle: {
                    color: "#5c7bd9",
                    barMaxHight: "10%",
                },
                label: {
                    show: true,
                    position: "right",
                    fontSize: 12,
                },
            },
        ],
    };
    myChart.setOption(option);
}

const salesTotal = computed(() => {
    const sales = reportData.value?.sales;
    if (!sales || !Array.isArray(sales)) return 0;
    return reportData.value.sales.reduce((sum, sale) => sum + sale, 0);
});

const customerCountTotal = computed(() => {
    const customerCount = reportData.value?.customerCount;
    if (!customerCount || !Array.isArray(customerCount)) return 0;
    return reportData.value.customerCount.reduce((sum, sale) => sum + sale, 0);
});

const averageTurnoverRate = computed(() => {
    const turnoverRate = reportData.value?.turnoverRate;
    if (!turnoverRate || !Array.isArray(turnoverRate)) return '0%';
    let averageTurnoverRate = reportData.value.turnoverRate.reduce((total, num) => total + num, 0);
    averageTurnoverRate = (averageTurnoverRate / countDays.value).toFixed(2) + "%";
    return averageTurnoverRate;
})



/*************************************
*ウィンドウを拡大と縮小
**************************************/

// 控制放大的状态
const isExpanded = reactive({
    salesReport: false,
    visitCountReport: false,
    tableTurnoverRate: false,
    ranking: false,
});

import { nextTick } from "vue";
// 切换放大状态
const toggleExpand = (key) => {
    isExpanded[key] = !isExpanded[key]

    nextTick(() => {
        const chartInstance = echarts.getInstanceByDom(document.getElementById(key));
        if (chartInstance) {
            chartInstance.resize();
        }
    });
};

</script>

<style lang="less" scoped>
@import '@/assets/css/dataAnalysisInfo.less';
/* 引入 Less 样式文件 */
</style>
