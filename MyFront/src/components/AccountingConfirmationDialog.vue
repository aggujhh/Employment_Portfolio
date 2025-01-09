<template>
    <section id='accountingConfirmation' v-if="isVisible">
        <div class="dialog">
            <button class="close_btn" @click="close">×</button>
            <div>
                <h3>会計確認</h3>
                <div class="header">
                    <p>料理名</p>
                    <p>料理単価</p>
                    <p>注文数</p>
                </div>
                <ul>
                    <li v-for="(item, index) in accountingData.dishes" :key="index">
                        <p>{{ item.name }}</p>
                        <p>{{ item.price }}</p>
                        <p>{{ item.count }}</p>
                    </li>
                </ul>
                <div class="totalPrice">
                    <p>お会計(税込)</p>
                    <p>¥{{ accountingData.amount.toLocaleString() }}</p>
                </div>
            </div>
            <div>
                <ul>
                    <li>
                        <p>テーブル番号: {{ deskId }}</p>
                    </li>
                    <li>
                        <p>合計金額</p>
                        <p>¥{{ accountingData.amount.toLocaleString() }}</p>
                    </li>
                    <li>
                        <p>内消費税</p>
                        <p>¥{{ Math.floor(accountingData.amount * 0.1).toLocaleString() }}</p>
                    </li>
                    <li>
                        <p>合計人数</p>
                        <p>{{ accountingData.guestCount }}人</p>
                    </li>
                    <li>
                        <p>お一人様の支払い</p>
                        <p>¥{{ Math.floor(accountingData.amount / accountingData.guestCount).toLocaleString() }}</p>
                    </li>
                    <li>
                        <p>支払方法</p>
                        <p>{{ setPayMethodByNum(accountingData.payMethod) }}</p>
                    </li>
                </ul>
                <button @click="seed_accountingConfirmationCompleted">確認</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref, watch } from "vue"

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'deskId'])
const emit = defineEmits(['close']);
const close = () => {
    emit('close');
}

/*************************************
* isVisibleの変更を監視
**************************************/
watch(
    () => props.isVisible,
    (newVal) => {
        if (newVal) {
            send_fetchAllAccountingOrders()
        }
    }
);


/*************************************
*会計中のオーダーをすべてフェッチ
**************************************/
import { fetchAllAccountingOrders } from "@/api/frontApi";
const accountingData = ref("");
const send_fetchAllAccountingOrders = async () => {
    try {
        const res = await fetchAllAccountingOrders({ deskId: props.deskId });
        const code = res.data.code;
        if (code === 1) {
            accountingData.value = res.data.data
            console.log(accountingData.value);
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

const setPayMethodByNum = (Num) => {
    if (Num == '0') {
        return '現金'
    } else {
        return 'クレジットカード'
    }
}


/*************************************
*会計確認完了
**************************************/
import { accountingConfirmationCompleted } from "@/api/frontApi";
const seed_accountingConfirmationCompleted = async () => {
    try {
        const res = await accountingConfirmationCompleted(accountingData.value);
        const code = res.data.code;
        if (code === 1) {
            emit('close');
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("失敗しました。もう一度お試しください。");
    }
}

</script>

<style lang="less" scoped>
@import '@/assets/css/accountingConfirmationDialog.less';
</style>
