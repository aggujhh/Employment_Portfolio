<template>
    <section id='thankYouBusiness' v-if="isVisible">
        <div class="dialog">
            <h3>ご来店、ありがとうございました。</h3>
            <div class="img2"></div>
            <div class="img1"></div>            
        </div>
    </section>
</template>

<script setup>
import { } from "vue"
/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'deskId'])

</script>

<style lang="less" scoped>
@import '@/assets/css/thankYouBusiness.less';
</style>
