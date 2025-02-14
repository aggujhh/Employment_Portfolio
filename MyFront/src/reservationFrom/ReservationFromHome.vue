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
    <ReservationFromStep1 :isVisible="isVisible.step1" @close="closeModal" @next="nextStep2"
        :isCompleted="isCompleted.step1" />
    <ReservationFromStep2 :isVisible="isVisible.step2" @prev="nextStep1" @next="nextStep3" :isCompleted="isCompleted.step2"/>
    <ReservationFromStep3 :isVisible="isVisible.step3" :req="req" @prev="nextStep2" @send="nextCompleted" />
    <ReservationCompletedDialog :isVisible="isVisible.completed" @close="closeModal" />
</template>

<script setup>
import { reactive } from "vue"
import ReservationFromStep1 from "./ReservationFromStep1.vue";
import ReservationFromStep2 from "./ReservationFromStep2.vue";
import ReservationFromStep3 from "./ReservationFromStep3.vue";
import ReservationCompletedDialog from "./ReservationCompletedDialog.vue";

const req = reactive({
    date: "",
    timeRange: "",
    name: "",
    katakana: "",
    content: "",
    people: "",
    tel: "",
    mail: "",
    specialNote: "",
})

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
const nextStep1 = () => {
    isVisible.step1 = true;
    isVisible.step2 = false;
    isVisible.step3 = false;
    isVisible.completed = false;
}

const nextStep2 = (result = {
    selectedDate: req.date,
    timeRange: req.timeRange
}) => {
    isVisible.step1 = false;
    isVisible.step2 = true;
    isVisible.step3 = false;
    isVisible.completed = false;
    req.date = result.selectedDate
    req.timeRange = result.timeRange
    isCompleted.step1 = false
}

const nextStep3 = (result = {
    name: req.name,
    katakana: req.katakana,
    content: req.content,
    people: req.people,
    tel: req.tel,
    mail: req.mail,
    specialNote: req.specialNote,
}) => {
    isVisible.step1 = false;
    isVisible.step2 = false;
    isVisible.step3 = true;
    isVisible.completed = false;
    req.name = result.name
    req.katakana = result.katakana
    req.content = result.content
    req.people = result.people
    req.tel = result.tel
    req.mail = result.mail
    req.specialNote = result.specialNote
    isCompleted.step2 = false
}


const nextCompleted = async () => {
    const result = await seed_addReservation();
    if (result) {
        isVisible.step1 = false;
        isVisible.step2 = false;
        isVisible.step3 = true;
        isVisible.completed = true;
        isCompleted.step1 = true
        isCompleted.step2 = true
    }
}

const isCompleted = reactive({
    step1: false,
    step2: false,
})

const openStep1 = () => {
    isVisible.step1 = true;
};

//予約情報をサーバに送信する
import { addReservation } from "@/api/reservationApi";
const seed_addReservation = async () => {
    try {
        req.date = new Date(req.date).toISOString().split("T")[0];
        req.people = Number(req.people)
        const res = await addReservation(req);
        console.log(res.data.msg);
        if (res.data.code !== 0) {
            console.log(res.data.data);
            return true
        } else {
            alert("送信失敗しました:" + res.data.msg)
            return false
        }
    } catch (err) {
        alert("リクエストエラー:", err)
        return false;
    }
}



</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>