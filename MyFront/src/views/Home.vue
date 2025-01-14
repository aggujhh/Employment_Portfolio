<template>
  <HeaderView></HeaderView>
  <section id="home">
    <ul>
      <li @click="() => { router.push('/front_screen') }">
        <p>フロント画面</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/kitchen_screen') }">
        <p>厨房画面</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/menu') }">
        <p>メニュー管理</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/qrCode') }">
        <p>QRコード管理</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/data_analysis') }">
        <p>データ分析</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/reservation') }">
        <p>予約管理</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/staff') }">
        <p>社員管理</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/shift') }">
        <p>シフト管理</p>
        <div class="img"></div>
      </li>
      <li @click="() => { router.push('/permission') }">
        <p>権限管理</p>
        <div class="img"></div>
      </li>
    </ul>
  </section>
</template>

<script setup>
import { } from "vue"
import router from '@/router';
</script>

<style lang="less" scoped>
@import '@/assets/css/home.less';
</style>
