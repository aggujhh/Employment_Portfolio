<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="kitchen_screen">
        <div class="header">
            <div>
                <img src="@/assets/images/icon/Renew.svg" />
                <p>更新</p>
            </div>
            <div>
                <img src="@/assets/images/icon/Return.svg" />
                <p>戻る</p>
            </div>
            <div>
                <img src="@/assets/images/icon/History.svg" />
                <p>履歴</p>
            </div>
        </div>
        <div class="main">
            <ul>
                <li v-for="(order, index) in orders" :key="index" class="order"
                    :style="{ top: `${order.top}px`, left: `${order.left}px`, width: `${orderWidth}px` }">
                    <div class="order_head">
                        <h3>テーブル<span>{{ order.deskId }}</span></h3>
                        <p>{{ order.guestCount }}名</p>
                    </div>
                    <div v-for="(dish, dishIndex) in order.dishes" :key="dishIndex" class="dish"
                        @click="checkedDish(index, dishIndex)" :class="{ active: isActiveList[index][dishIndex] }">
                        <div></div>
                        <p><img src="@/assets/images/icon/Checkmark.svg" />{{ dish.name }}</p>
                        <div>
                            <p>{{ dish.count }}</p>
                        </div>
                    </div>
                    <div class="order-fooder">
                        <p :style="{ color: timeColor[index] }">{{ computedOrderAfterTime(index, order.orderTime) }}</p>
                        <button>すべて調理済</button>
                    </div>
                </li>
            </ul>
        </div>
    </section>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";

/*************************************
* ページの初期化時に実行
**************************************/
onMounted(() => {
    api_fetAllOrders();
});

/*************************************
* 瀑布流レイアウトの計算
**************************************/
const columnCount = 5; // 列数を定義
const columnHeights = ref(new Array(columnCount).fill(40)); // 各列の初期高さを設定
const orderWidth = ref(0); // 各注文の幅を保持する変数
const setOrderHeight = () => {
    // コンテナの幅を取得
    const containerWidth = document.querySelector(".main>ul").scrollWidth;
    // 各列の幅を計算
    orderWidth.value = ((containerWidth - 160) / columnCount);
    // 各列の高さをリセット
    columnHeights.value = new Array(columnCount).fill(40);

    // 各注文を順番に処理し、位置を設定
    orders.value.forEach((order, index) => {
        // 注文内の料理数を取得し、高さを計算
        const count = order.dishes.length;
        order.height = count * 60 + 157; // 注文の高さを計算
        console.log(order);


        // 最も低い列を見つける
        const minHeight = Math.min(...columnHeights.value);
        const columnIndex = columnHeights.value.indexOf(minHeight);

        // 注文の位置を設定
        order.top = minHeight;
        order.left = columnIndex * (orderWidth.value + 20) + 40;

        // 列の高さを更新（注文の高さと間隔を加算）
        columnHeights.value[columnIndex] += order.height + 40;
    });
};

/*************************************
* 注文のオーダーをすべてフェッチ
**************************************/
import { fetAllOrders } from "@/api/kitchenApi";
const orders = ref([])
const api_fetAllOrders = async () => {
    try {
        const res = await fetAllOrders();
        orders.value = res.data.data
        initializeOrderPosition()
        setOrderHeight()
        setTimeColor()
        setIsActiveList()
        console.log("オーダー", orders.value);
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
}

const initializeOrderPosition = () => {
    orders.value.forEach(item => {
        item.height = 0
        item.top = 0
        item.left = 0
    })
}

/*************************************
* 注文後の時間の計算
**************************************/
const timeColor = ref([]); // 注文ごとの時間に応じた色を保存する配列
// timeColor 配列の初期化
const setTimeColor = () => {
    timeColor.value = new Array(orders.value.length).fill("red"); // 初期値はすべて "red"
};
// 注文後の時間を計算し、時間に応じて色を動的に設定
const computedOrderAfterTime = (index, time) => {
    const specifiedTime = new Date(time); // 注文時刻を Date オブジェクトに変換
    const timeDiff = now.value - specifiedTime; // 現在時刻との時間差を計算
    const totalSeconds = Math.floor(timeDiff / 1000); // 総秒数を計算
    const hours = Math.floor(totalSeconds / 3600); // 時間
    const minutes = Math.floor((totalSeconds % 3600) / 60); // 分
    const seconds = totalSeconds % 60; // 秒
    // 時間に応じた色を動的に設定
    if (hours === 0 && minutes < 5) {
        timeColor.value[index] = "#5AE2A9"; // 5分未満：緑色
    } else if (hours === 0 && minutes >= 5 && minutes < 10) {
        timeColor.value[index] = "yellow"; // 5分以上10分未満：黄色
    } else {
        timeColor.value[index] = "red"; // 10分以上：赤色
    }
    // フォーマットされた時間を返す
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
};
// 現在時刻を保存する ref
const now = ref(new Date());
// 現在時刻を1秒ごとに更新
setInterval(() => {
    now.value = new Date();
}, 1000);


/*************************************
* 注文後の時間の計算
**************************************/
const isActiveList = ref([])
// アクティブ状態リストを初期化する関数
const setIsActiveList = () => {
    // 再実行時にリストをリセット
    isActiveList.value = orders.value.map(order =>
        new Array(order.dishes.length).fill(false) // 各注文の料理ごとに false を設定
    );
    console.log("isActiveList",isActiveList.value);
};

// 指定された料理をアクティブ状態に変更する関数
const checkedDish = (index1, index2) => {
    isActiveList.value[index1][index2] = true; // 指定された料理を true に設定
};



</script>
<style lang="less" scoped>
@import '@/assets/css/kitchen_screen.less';
</style>
