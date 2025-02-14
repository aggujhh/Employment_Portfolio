<template>
  <section id='reservationCompleted' v-if="isVisible">
    <div class="dialog">
      <div class="img"><img src="@/assets/images/icon/check_mark.svg"></div>
      <p>予約が確認しました</p>
      <button @click="close">完了</button>
    </div>
  </section>
</template>

<script setup>
import { } from "vue"
const props = defineProps(['isVisible'])
const emit = defineEmits(['close']);

const close = () => {
    emit('close');
}

</script>

<style lang="less" scoped>
@import '@/assets/css/reservationCompletedDialog.less';
</style>
