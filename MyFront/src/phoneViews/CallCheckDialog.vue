<template>
  <section id='callCheck' v-if="isVisible">
    <div class="dialog">
      <h3>{{ title }}</h3>
      <div class="btn">
        <button @click="send">確認</button>
        <button @click="close">戻る</button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, watch } from "vue"
import { useRoute } from "vue-router";
const route = useRoute();

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'flag'])
const emit = defineEmits(['close', 'calling']);
const close = () => {
  emit('close');
}

const title = ref("")
watch(
  () => props.flag,
  (newValue) => {
    if (newValue === '0') {
      title.value = "スタッフを呼び出します。よろしいですか？"
    } else {
      title.value = "呼び出しを取り消しますか？"
    }
  }
);


/*************************************
*サーバに送信
**************************************/
import { callForStaff } from "@/api/orderApi";
const send_callForStaff = async () => {
  try {
    const res = await callForStaff({ id: route.params.desk_id });
    const code = res.data.code; // ステータスコードを取得
    if (code === 1) {
      emit('close');
      emit('calling');
    } else {
      console.log(res.data.msg);
    }
  } catch (error) {
    // エラー処理
    console.error("リクエストエラー:", error);
  }
}

import { cancelCall } from "@/api/orderApi";
const send_cancelCall = async () => {
  try {
    const res = await cancelCall({ id: route.params.desk_id });
    const code = res.data.code; // ステータスコードを取得
    if (code === 1) {
      emit('close');
      emit('calling');
    } else {
      console.log(res.data.msg);
    }
  } catch (error) {
    // エラー処理
    console.error("リクエストエラー:", error);
  }
}

const send = () => {
  if (props.flag === '0') {
    send_callForStaff();
  } else {
    send_cancelCall();
  }
}

</script>

<style lang="less" scoped>
@import '@/assets/css/callCheckDialog.less';
</style>
