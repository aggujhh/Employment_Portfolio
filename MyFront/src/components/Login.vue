<template>
    <section id="login">
        <h2>username:{{ state.name }}</h2>
        <h2>password:{{ password }}</h2>
        <button @click="changeName">changeName</button>
        <button @click="changePs">changePs</button>
        <ul v-for="g in games" v-bind:key="g.id">{{ g.name }}</ul>
    </section>
</template>

<script lang="ts" setup name="Login">
import { ref, reactive } from 'vue';
import { type UserInter } from '@/types';

// データ
let username = ref("陳太郎")
let password = ref("123456")
let user: UserInter = {
    id: "陳太郎",
    password: "123456"
}

const state = reactive({
    count: 0,
    name: 'Vue'
});

//メソッド
function changeName() {
    state.name = "風太郎"
}
function changePs() {
    password.value = "654321"
}

let games = [
    { id: '001', name: "王者荣耀" },
    { id: '002', name: "元神" },
    { id: '003', name: "接地求生" },
]
</script>

<style lang="less"></style>