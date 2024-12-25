<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="kitchen_screen">
        <div class="header">
            <div @click="api_fetAllOrders">
                <img src="@/assets/images/icon/Renew.svg" />
                <p>更新</p>
            </div>
            <div @click="undo" class="undo" :class="{ disabled: !canUndo }">
                <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24">
                    <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                        stroke-width="2">
                        <path d="M3 7v6h6" />
                        <path d="M21 17a9 9 0 0 0-9-9a9 9 0 0 0-6 2.3L3 13" />
                    </g>
                </svg>
                <p>元に戻す</p>
            </div>
            <div @click="redo" class="redo" :class="{ disabled: !canRedo }">
                <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24">
                    <g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                        stroke-width="2">
                        <path d="M21 7v6h-6" />
                        <path d="M3 17a9 9 0 0 1 9-9a9 9 0 0 1 6 2.3l3 2.7" />
                    </g>
                </svg>
                <p>やり直す</p>
            </div>
            <div @click="() => { isVisible = true }">
                <img src="@/assets/images/icon/History.svg" />
                <p>履歴</p>
            </div>
            <div @click="api_resetAllOrderAmdDishState" class="reset" :class="{ disabled: !canReset }">
                <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24">
                    <path fill="currentColor"
                        d="M7.207 2.543a1 1 0 0 1 0 1.414L5.414 5.75h7.836a8 8 0 1 1-8 8a1 1 0 1 1 2 0a6 6 0 1 0 6-6H5.414l1.793 1.793a1 1 0 0 1-1.414 1.414l-3.5-3.5a1 1 0 0 1 0-1.414l3.5-3.5a1 1 0 0 1 1.414 0" />
                </svg>
                <p>リセット</p>
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
                        @click="api_changeOrderDishState(dish.orderId, dish.dishId)"
                        :class="{ active: isActiveList[index][dishIndex] }">
                        <div></div>
                        <p><img src="@/assets/images/icon/Checkmark.svg" />{{ dish.name }}</p>
                        <div>
                            <p>{{ dish.count }}</p>
                        </div>
                    </div>
                    <div class="order-fooder">
                        <p :style="{ color: timeColor[index] }">{{ computedOrderAfterTime(index, order.orderTime) }}</p>
                        <button @click="api_changeOrderState(order.id)">すべて調理済</button>
                    </div>
                </li>
            </ul>
        </div>
    </section>
    <OrderHistoryDialog :isVisible="isVisible" @close="closeModal" />
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeUnmount, computed } from "vue";

/*************************************
* オーダー履歴モーダル
**************************************/
import OrderHistoryDialog from "@/components/OrderHistoryDialog.vue";
const isVisible = ref(false)
// モーダルを閉じる関数
const closeModal = () => {
    isVisible.value = false;
};

/*************************************
* ページの初期化時に実行
**************************************/
onMounted(() => {
    api_fetAllOrders();
    api_initializationVersion();
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

        // 最も低い列を見つける
        const minHeight = Math.min(...columnHeights.value);
        const columnIndex = columnHeights.value.indexOf(minHeight);

        // 注文の位置を設定
        order.top = minHeight;
        order.left = columnIndex * (orderWidth.value + 20) + 40;

        // 列の高さを更新（注文の高さと間隔を加算）
        columnHeights.value[columnIndex] += order.height + 20;
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
        orders.value = filteredOrders(res.data.data)
        initializeOrderPosition()
        setOrderHeight()
        setTimeColor()
        setIsActiveList()
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
}

const filteredOrders = (orders) => {
    return orders.filter(order => order.state === '0')
        .sort((a, b) => new Date(a.orderTime) - new Date(b.orderTime)); // 按时间升序排序（时间越久排越前）;
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
        order.dishes.map(dish =>
            dish.state == '0' ? false : true  // 状態に基づいて true または false を設定
        )
    );
};

/*************************************
* ユーザーが注文後の時、画面をリフレッシュ
* SSE（Server-Sent Events）を使用してリアルタイム更新を実現
**************************************/
import SseService from "@/utils/sseService";
/**
* SSE サービスのインスタンスを作成
* @param url サーバーの URL
* @param callback 新しいメッセージが届いた時の処理
*/
const sseService = new SseService("http://localhost:8080/api/order/kitchen", () => {
    // 注文リストをリフレッシュ
    api_fetAllOrders();
    api_initializationVersion();
});
// SSE 接続を開始
sseService.connect();
//コンポーネントが破棄される前に呼び出されるフック
onBeforeUnmount(() => {
    sseService.close(); // SSE 接続を閉じる
});

/*************************************
* 調理済み料理の状態を変更する
**************************************/
import { changeOrderDishState } from "@/api/kitchenApi";

const api_changeOrderDishState = async (orderId, dishId) => {
    try {
        const res = await changeOrderDishState({ orderId: orderId, dishId: dishId });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            saveVersion(res.data.data)
            api_fetAllOrders();
            canReset.value = true
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("状態変更を失敗しました。もう一度お試しください。");
    }
}

/*************************************************
このオーダー内のすべての料理を調理済み状態に変更します
**************************************************/
import { changeOrderState } from "@/api/kitchenApi";
const api_changeOrderState = async (orderId) => {
    try {
        const res = await changeOrderState({ id: orderId });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            saveVersion(res.data.data)
            api_fetAllOrders();
            canReset.value = true
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("状態変更を失敗しました。もう一度お試しください。");
    }
}

/*************************************
* すべて料理の状態を最初の状態をリセットする
**************************************/
import { resetAllOrderAmdDishState } from "@/api/kitchenApi";
const canReset = ref(true)
const api_resetAllOrderAmdDishState = async () => {
    try {
        const res = await resetAllOrderAmdDishState();
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            saveVersion(res.data.data)
            api_fetAllOrders();
            canReset.value = false
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("リセット失敗しました。もう一度お試しください。");
    }
}


/*************************************
* 元に戻すとやり直す機能
**************************************/
import Stack from "@/utils/stack";
// 定义 undo 和 redo 栈
const undoStack = ref(new Stack(5)); // 最大容量为 5
const redoStack = ref(new Stack()); // 不限制容量
const currentVersion = ref(null); // 当前状态

import { initializationVersion } from "@/api/kitchenApi";
const api_initializationVersion = async () => {
    try {
        const res = await initializationVersion();
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            saveVersion(res.data.data)
        } else {
            alert(res.data.msg); // エラーメッセージを表示
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("リセット失敗しました。もう一度お試しください。");
    }
}

// 是否可以撤销
const canUndo = computed(() => !undoStack.value.isEmpty());

// 是否可以重做
const canRedo = computed(() => !redoStack.value.isEmpty());

// バージョンを保存
// 保存版本
const saveVersion = (version) => {
    if (currentVersion.value !== null) {
        undoStack.value.push(currentVersion.value); // 当前版本入栈
    }
    currentVersion.value = version; // 更新当前版本
    redoStack.value.clear(); // 清空 redo 栈
};

// 元に戻す操作
import { undoAllOrderAmdDishState } from "@/api/kitchenApi";
const undo = async () => {
    if (canUndo.value) {
        redoStack.value.push(currentVersion.value); // 当前版本入 redo 栈
        currentVersion.value = undoStack.value.pop(); // 从 undo 栈弹出
        try {
            const res = await undoAllOrderAmdDishState({ version: currentVersion.value });
            const code = res.data.code; // ステータスコードを取得
            if (code === 1) {
                api_fetAllOrders(); // 最新の注文データを再取得
            } else {
                alert(res.data.msg); // エラーメッセージを表示
                console.log(res.data.msg);
            }
        } catch (error) {
            // エラー処理
            console.error("リクエストエラー:", error);
            alert("リセット失敗しました。もう一度お試しください。");
        }
    }
};

// やり直し操作
import { redoAllOrderAmdDishState } from "@/api/kitchenApi";
const redo = async () => {
    if (canRedo.value) {
        undoStack.value.push(currentVersion.value); // 当前版本入 undo 栈
        currentVersion.value = redoStack.value.pop(); // 从 redo 栈弹出
        try {
            const res = await redoAllOrderAmdDishState({ version: currentVersion.value });
            const code = res.data.code; // ステータスコードを取得
            if (code === 1) {
                // ポインタを進める
                api_fetAllOrders(); // 最新の注文データを再取得
            } else {
                alert(res.data.msg); // エラーメッセージを表示
                console.log(res.data.msg);
            }
        } catch (error) {
            // エラー処理
            console.error("リクエストエラー:", error);
            alert("リセット失敗しました。もう一度お試しください。");
        }
    }
};
</script>
<style lang="less" scoped>
@import '@/assets/css/kitchen_screen.less';
</style>
