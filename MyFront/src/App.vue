<template>
    <RouterView></RouterView>
    <!-- <nav>
        <RouterLink to="/home" active-class="active">home</RouterLink>
        <RouterLink to="/news" active-class="active">news</RouterLink>
        <RouterLink to="/about" active-class="active">about</RouterLink>
    </nav>
    <div class="content">
        
    </div> -->
    <!-- <Login></Login> -->
</template>

<script lang="ts" setup name="App">
// import Login from './components/Login.vue';
import { RouterView,RouterLink } from 'vue-router';
</script>

<style lang="less">
@import "@/assets/css/style.less";
</style>