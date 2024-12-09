<template>
    <section id="nav">
        <div class="head">
            <div class="nav_btn" :class="{ active: isActive }" @click="toggleActive()">
                <div class="hamburger"></div>
            </div>
        </div>
        <nav :class="{ active: isActive }">
            <ul>
                <li @click="navigateTo('/front_screen')" :class="{ nav_active: isNavActive('/front_screen') }">
                    フロント画面
                </li>
                <li @click="navigateTo('/kitchen_screen')" :class="{ nav_active: isNavActive('/kitchen_screen') }">
                    厨房画面
                </li>
                <li @click="navigateTo('/menu')" :class="{ nav_active: isNavActive('/menu') }">
                    メニュー管理
                </li>
                <li @click="navigateTo('/reservation')" :class="{ nav_active: isNavActive('/reservation') }">
                    予約管理
                </li>
                <li @click="navigateTo('/shift')" :class="{ nav_active: isNavActive('/shift') }">
                    シフト管理
                </li>
                <li @click="navigateTo('/data_analysis')" :class="{ nav_active: isNavActive('/data_analysis') }">
                    データ分析
                </li>
                <li @click="navigateTo('/staff')" :class="{ nav_active: isNavActive('/staff') }">
                    社員管理
                </li>
                <li @click="navigateTo('/permission')" :class="{ nav_active: isNavActive('/permission') }">
                    権限管理
                </li>
            </ul>
        </nav>
    </section>
</template>

<script setup>
import { ref } from "vue"
import { useRouter, useRoute } from "vue-router";
const router = useRouter();
const route = useRoute();

// 导航方法
const navigateTo = (path) => {
    router.push(path);
};

// 判断当前路由是否激活
const isNavActive = (path) => {
    return route.path === path;
};

const isActive = ref(false);

const toggleActive = () => {
    isActive.value = !isActive.value;
};
</script>

<style lang="less" scoped>
@import '@/assets/css/nav.less';
</style>
