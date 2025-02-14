<template>
    <section id='hearder'>
        <div class="step-container" :class="step">
            <div class="step step1">
                <div class="circle">1</div>
                <span>日時の選択</span>
            </div>
            <div class="line line1"></div>
            <div class="step step2">
                <div class="circle">2</div>
                <span>情報入力</span>
            </div>
            <div class="line line2"></div>
            <div class="step step3">
                <div class="circle">3</div>
                <span>内容確認</span>
            </div>
        </div>
    </section>
</template>

<script setup>
import { watch, ref } from "vue"
const props = defineProps(['step'])

const step = ref('step1')
watch(
    () => props.step, // propsのisVisibleを監視
    (newVal) => {
        console.log(newVal);
        step.value = newVal
    }, { immediate: true }
);
</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>
