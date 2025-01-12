<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id='qrCode'>
        <ul>
            <li v-for="(item, index) in qrCodeArray" :key="index">
                <div class="qrCode">
                    <img :src="`public\\qrCodeImages\\${ item.deskId }\\qr_${ item.fileName }.png`">
                </div>
                <p>テーブル番号: {{ item.deskId }}</p>
            </li>
        </ul>
    </section>
</template>

<script setup>
import { ref, onMounted } from "vue"

import { fetchAllQrCode } from '@/api/qrCodeApi'
const qrCodeArray = ref([])
const seed_fetchAllQrCode = async () => {
    try {
        const res = await fetchAllQrCode();
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            console.log(res.data.data);
            
            qrCodeArray.value = res.data.data
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}

// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
    seed_fetchAllQrCode()
});
</script>

<style lang="less" scoped>
@import '@/assets/css/qrCode.less';
</style>
