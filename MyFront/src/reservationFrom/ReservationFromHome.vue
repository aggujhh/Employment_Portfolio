<template>
    <section id='reservationFromHome'>
        <div class="content">
            <h3>ご予約はこちらから</h3>
            <p>当サイトでは、簡単にご予約いただけます。</p>
            <p>「予約する」のボタンをクリックし、必要事項を入力して送信してください。</p>
            <p>お客様のご希望に合わせてスムーズに対応いたします。</p>
        </div>
        <button @click="openStep1">予約する</button>
    </section>
    <ReservationFromStep1 :isVisible="isVisible.step1" @close="closeModal" />
    <ReservationFromStep2 />
    <ReservationFromStep3 />
    <ReservationCompletedDialog />
</template>

<script setup>
import { reactive } from "vue"
import ReservationFromStep1 from "./ReservationFromStep1.vue";
import ReservationFromStep2 from "./ReservationFromStep1.vue";
import ReservationFromStep3 from "./ReservationFromStep1.vue";
import ReservationCompletedDialog from "./ReservationCompletedDialog.vue";
const isVisible = reactive({
    step1: false,
    step2: false,
    step3: false,
    completed: false
})
// モーダルを閉じる関数
const closeModal = () => {
    isVisible.step1 = false;
    isVisible.step2 = false;
    isVisible.step3 = false;
    isVisible.completed = false;
};

const openStep1 = () => {
    isVisible.step1 = true;
};


</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>