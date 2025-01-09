<template>
  <HeaderView></HeaderView>
  <NavView></NavView>
  <section id="data_analysis">
    <ul class="nav">
      <li @click="redirectPage('/data_analysis/3day')" :class="{ active: isActive('/data_analysis/3day') }">
        <p>三日間</p>
      </li>
      <li @click="redirectPage('/data_analysis/5day')" :class="{ active: isActive('/data_analysis/5day') }">
        <p>五日間</p>
      </li>
      <li @click="redirectPage('/data_analysis/1week')" :class="{ active: isActive('/data_analysis/1week') }">
        <p>今週</p>
      </li>
      <li @click="redirectPage('/data_analysis/1mooth')" :class="{ active: isActive('/data_analysis/1mooth') }">
        <p>今月</p>
      </li>
      <li @click="redirectPage('/data_analysis/1year')" :class="{ active: isActive('/data_analysis/1year') }">
        <p>今年</p>
      </li>
    </ul>
    <DataAnalysisInfo />
  </section>
</template>

<script setup>
import { ref } from "vue"
import { useRouter, useRoute } from "vue-router";
import DataAnalysisInfo from "@/components/DataAnalysisInfo.vue";

// ルーターとルートの取得
const router = useRouter();
const route = useRoute();

const redirectPage = (path) => {
  router.push(path);
};

// 現在のルートがアクティブか確認する関数
const isActive = (path) => {
  return route.path === path;
};
</script>

<style lang="less" scoped>
@import '@/assets/css/dataAnalysis.less';
</style>
