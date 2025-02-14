<template>
  <section id='step3' v-if="isVisible">
    <ReservationHearder :step="'step3'" />
    <main>
      <ul>
        <li>
          <p>担当者名: </p> 
          <p>{{ req.name }}</p>
        </li>
        <li>
          <p>担当者名(カタカナ): </p>
          <p>{{ req.katakana }}</p> 
        </li>
        <li>
          <p>予約日: </p>
          <p>{{ req.date }}</p>
        </li>
        <li>
          <p>予約時間帯: </p>
          <p>{{ req.timeRange }}</p>
        </li>
        <li>
          <p>予約内容: </p>
          <p>{{ req.content }}</p>
        </li>
        <li>
          <p>人数: </p>
          <p>{{ req.people }}</p>
        </li>
        <li>
          <p>連絡先: </p>
          <p>{{ req.tel }}</p>
        </li>
        <li>
          <p>メールアドレス: </p>
          <p>{{ req.mail }}</p>
        </li>
        <li>
          <p>特記事項: </p>
          <p>{{ req.specialNote }}</p>
        </li>
      </ul>
      <div class="btn">
        <button @click="prev">戻る</button>
        <button @click="send">送信</button>
      </div>
    </main>
  </section>
</template>
<script setup>
import { watch } from "vue"
import ReservationHearder from "./ReservationHearder.vue";

const props = defineProps(['isVisible', 'req'])
const emit = defineEmits(['prev', 'send']);

watch(
  () => props.req, // propsのisVisibleを監視
  (newVal) => {
  }, { deep: true, immediate: true }
);

// 点击「戻る」按钮
const prev = () => {
  emit("prev");
};

// 点击「次へ」按钮
const send = () => {
  emit('send');
};
</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>
