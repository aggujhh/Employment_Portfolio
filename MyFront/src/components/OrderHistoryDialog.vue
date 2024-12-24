<template>
    <section id='orderHistoryDialog' v-if="isVisible">
        <div class="dialog">
            <h3>オーダー履歴</h3>
            <button class="close_btn" @click="close">×</button>
            <div class="header">
                <p>オーダー番号</p>
                <p>テーブル番号</p>
                <p>料理名</p>
                <p>数量</p>
                <p>仕上がり時間</p>
                <p>所要時間</p>
            </div>
            <ul>
                <li v-for="(item, index) in orderHistory" :key="index">
                    <p>{{ item.orderId }}</p>
                    <p>{{ item.deskId }}</p>
                    <p>{{ item.dishName }}</p>
                    <p>{{ item.count }}</p>
                    <p>{{ item.completionTime }}</p>
                    <p>{{ item.requiredTime }}</p>
                </li>
            </ul>
        </div>
    </section>

</template>

<script setup>
import { ref, watch, onMounted } from "vue"

/*************************************
* ページの初期化時に実行
**************************************/
onMounted(() => {
    send_fetchAllOrderHistory(); // ページが初期化されるときに実行
});

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible'])
const emit = defineEmits(['close']);
const close = () => {
    emit('close');
}

/*************************************
* isVisibleの変更を監視
**************************************/
watch(
    () => props.isVisible, // propsのisVisibleを監視
    (newVal) => {
        if (newVal) {
            send_fetchAllOrderHistory(); // モーダルが表示されたときにデータを取得
        }
    }
);

/*************************************
*すべての注文履歴をフェッチする
**************************************/
import { fetchAllOrderHistory } from "@/api/kitchenApi";
const orderHistory = ref([])
const send_fetchAllOrderHistory = async () => {
    try {
        const res = await fetchAllOrderHistory();
        const code = res.data.code;
        console.log(res.data.data);
        if (code === 1) {
            orderHistory.value = res.data.data
            computedRequiredTime()
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("フェッチ失敗しました。もう一度お試しください。");
    }
}

/*************************************
* 所要時間の計算
**************************************/
const computedRequiredTime = () => {
    orderHistory.value.forEach(item => {
        // ISO 8601 時間文字列を Date オブジェクトに変換
        const orderTime = new Date(item.orderTime);
        const completionTime = new Date(item.completionTime);
        const timeDiff = completionTime - orderTime
        const totalSeconds = Math.floor(timeDiff / 1000); // 総秒数を計算
        const hours = Math.floor(totalSeconds / 3600); // 時間
        const minutes = Math.floor((totalSeconds % 3600) / 60); // 分
        const seconds = totalSeconds % 60; // 秒
        const requiredTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
        item['requiredTime'] = requiredTime
        item.completionTime = item.completionTime.replace("T", "\n");
    })
};


</script>

<style lang="less" scoped>
@import '@/assets/css/orderHistoryDialog.less';
</style>
