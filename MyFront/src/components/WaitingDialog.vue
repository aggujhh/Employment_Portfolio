<template>
    <section id='WaitingDialog' v-if="isVisible">
        <div class="dialog">
            <h3>
                <span>店</span>
                <span>員</span>
                <span>が</span>
                <span>す</span>
                <span>ぐ</span>
                <span>に</span>
                <span>対</span>
                <span>応</span>
                <span>し</span>
                <span>ま</span>
                <span>す</span>
                <span>の</span>
                <span>で</span>
                <span>、</span>
                <span>し</span>
                <span>ば</span>
                <span>ら</span>
                <span>く</span>
                <span>お</span>
                <span>待</span>
                <span>ち</span>
                <span>く</span>
                <span>だ</span>
                <span>さ</span>
                <span>い</span>
                <span>。</span>
            </h3>
        </div>
    </section>
</template>

<script setup>
import { ref } from "vue"

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible'])
// const emit = defineEmits(['close']);
// const close = () => {
//     emit('close');
// }
</script>

<style lang="less" scoped>
@import '@/assets/css/waitingDialog.less';
</style>
