<template>
    <section id='step1' v-if="isVisible">
        <ReservationHearder/>
        <button @click="close">戻る</button>
    </section>
</template>

<script setup>
import { } from "vue"
import ReservationHearder from "./ReservationHearder.vue";

const props = defineProps(['isVisible'])
const emit = defineEmits(['close']);
const close = () => {
    emit('close');
}



</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>
