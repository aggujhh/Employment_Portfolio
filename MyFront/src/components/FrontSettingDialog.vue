<template>
    <section id='frontSetting' v-if="isVisible">
        <div class="dialog">
            <button class="close_btn" @click="close">×</button>
            <h3>設定</h3>
            <ul>
                <li>
                    <p>すべてのデスクを最初の状態に戻る</p>
                    <button class="reset_btn" @click="api_resetAllTables">reset</button>
                </li>
            </ul>
        </div>
    </section>
</template>

<script setup>
import { } from "vue"

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible'])
const emit = defineEmits(['close']);
const close = () => {
    emit('close');
}


import { resetAllTables } from "@/api/deskApi";
const api_resetAllTables = async () => {
    try {
        const res = await resetAllTables();
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            // api_fetchAllTables();
        } else {
            alert(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("リセット失敗しました。もう一度お試しください。");
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/css/frontSettingDialog.less';
</style>
