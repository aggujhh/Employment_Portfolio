<template>
    <header>
        <div class="logo" @click="() => { router.push('/home') }">
            <img src="@\assets\images\logo.svg" />
        </div>
        <div class="title">
            <p>{{ title }}</p>
        </div>
        <div class="admin">
            <img src="@\assets\images\icon\admin.svg">
            <p :title="name.value">ADMIN : {{ name }}</p>
            <svg class="logout" height="20" width="20" viewBox="0 0 29.14 35" fill="#fff" @click="logout()">
                <path class="cls-1"
                    d="M25,7.5v-3.5c0-.55-.45-1-1-1H4c-.55,0-1,.45-1,1v27c0,.55.45,1,1,1h20c.55,0,1-.45,1-1v-4.5c0-.83.67-1.5,1.5-1.5s1.5.67,1.5,1.5v4.5c0,2.21-1.79,4-4,4H4c-2.21,0-4-1.79-4-4V4C0,1.79,1.79,0,4,0h20c2.21,0,4,1.79,4,4v3.5c0,.18-.03.34-.09.5-.2.58-.76,1-1.41,1s-1.21-.42-1.41-1c-.06-.16-.09-.32-.09-.5Z" />
                <path class="cls-2"
                    d="M7.6,17.92c-.56,0-1.01-.46-1.01-1.01s.45-1.01,1.01-1.01h17.59s-.09-.08-.09-.08l-4.57-4.57c-.39-.39-.39-1.02,0-1.41.39-.39,1.02-.39,1.41,0l3.16,3.16,3,2.99.21.21c.39.39.39,1.03,0,1.42l-.21.21-3,2.99-3.16,3.16c-.39.39-1.02.39-1.41,0-.39-.39-.39-1.02,0-1.41l4.57-4.57.09-.09" />
            </svg>
        </div>
    </header>
</template>

<script setup>
import { ref, watch, computed } from "vue"
import router from '@/router';
import { useUserStore } from '@/stores/userStore';
import { getCurrentUserId } from "@/utils/session";


const title = ref(window.location.pathname.split('/')[1].replace(/_/g, ' ').toUpperCase());
const name = ref("")

const updateTitle = () => {
    title.value = window.location.pathname.split('/')[1].replace(/_/g, ' ').toUpperCase()
};

const currentUserId = getCurrentUserId()
if (currentUserId) {
    const userStore = useUserStore();
    name.value = computed(() => userStore.users[currentUserId]?.name || '');
}


// 监听路由变化
watch(
    () => router.path,
    () => {
        updateTitle();
    }
);

const logout = () => {
    useUserStore().removeUser(currentUserId)
    localStorage.removeItem("session");
    router.push("/login");
}



</script>

<style lang="less" scoped>
@import '@/assets/css/header.less';
</style>