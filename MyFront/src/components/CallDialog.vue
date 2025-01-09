<template>
    <section id='call' v-if="isVisible">
        <div class="dialog">
            <button class="close_btn" @click="close">×</button>
            <h3>テーブルA1のお客様から呼び出しがございます。</h3>
            <button class="check_btn" @click="send_checkedCall">確 認</button>
        </div>
    </section>
</template>

<script setup>
import { } from "vue"

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'deskId'])
const emit = defineEmits(['close']);
const close = () => {
    emit('close');
}

import { checkedCall } from "@/api/frontApi";
const send_checkedCall = async () => {
  try {
    const res = await checkedCall({ id: props.deskId });
    const code = res.data.code; // ステータスコードを取得
    if (code === 1) {
      emit('close');
    } else {
      console.log(res.data.msg);
    }
  } catch (error) {
    // エラー処理
    console.error("リクエストエラー:", error);
  }
}

</script>

<style lang="less" scoped>
@import '@/assets/css/callDialog.less';
</style>
