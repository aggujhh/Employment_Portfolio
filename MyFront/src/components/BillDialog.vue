<template>
    <section id='bill_dialog' v-if="isVisible">
        <div class="dialog">
            <h3>お支払い</h3>
            <ul>
                <li>
                    <p>商品小計</p>
                    <p>¥{{ formatTotalPrice }}</p>
                </li>
                <li>
                    <p>外税</p>
                    <p>¥0</p>
                </li>
                <li>
                    <p>合計</p>
                    <p>¥{{ formatTotalPrice }}</p>
                </li>
                <li>
                    <p>(10%標準対象)</p>
                    <p>¥{{ formatTotalPrice }}</p>
                </li>
                <li>
                    <p>(内消費税)</p>
                    <p>{{ tax }}</p>
                </li>
                <li>
                    <p>お支払い金額</p>
                    <p>¥{{ formatTotalPrice }}</p>
                </li>
            </ul>
            <div class="pay-method">
                <p>お支払方法</p>
                <label><input type="radio" value="0" name="pay-method" v-model="payMethod">現金</label>
                <label><input type="radio" value="1" name="pay-method" v-model="payMethod">クレジットカード</label>
            </div>
            <div class="btn">
                <button @click="close">戻る</button>
                <button @click="next">お支払いへ進む</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref, computed } from "vue"

const payMethod = ref('0')

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'totalPrice'])
const emit = defineEmits(['close', 'next']);
const close = () => {
    emit('close');
}

const next = () => {
    emit('next', payMethod.value);
}
const formatTotalPrice = computed(() => {
    return props.totalPrice.toLocaleString();
});

const tax = computed(() => {
    const value = Math.floor(props.totalPrice * 0.1)
    return value.toLocaleString();
});

</script>

<style lang="less" scoped>
@import '@/assets/css/bill_dialog.less';
</style>
