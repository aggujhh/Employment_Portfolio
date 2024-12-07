<template>
    <section id="login">
        <div class="logo">
            <img src="@\assets\images\logo.svg" />
        </div>
        <div class="login_main">
            <div class="title">
                <p> WELCOME TO</p>
                <p>MY SYSTEM</p>
            </div>
            <div class="input-box">
                <input type="text" v-model="data.id" placeholder="ユーザー名" />
                <input type="password" v-model="data.password" placeholder="パスワード" />
                <div class="btn" @click="send()">
                    <input type="button" />
                    <p>ログイン</p>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup>
import { reactive, watch } from "vue"
import { login } from "@/api/empApi";

const data = reactive({
    id: '',
    password: ''
});

watch(data, (newValue) => {
    console.log('data对象发生了变化：', newValue);
})

const send = async () => {
    if (!data.id.trim() || !data.password.trim()) {
        alert("ユーザー名とパスワードを入力してください。");
        return;
    }

    try {
        const res = await login(data); // 调用 API
        console.log(res);
        
        // const code = res.data.code;
        // const dataStr = res.data.data; // 这里得到的是一个 JSON 字符串
        // const data = JSON.parse(dataStr); // 将字符串解析为对象

        // // 更新 Vuex 状态
        // store.commit("setName", data.name);
        // store.commit("setJob", data.jobTitle);
        // store.commit("setUsername", username.value);

        // // 存储 token 到 localStorage
        // localStorage.setItem("token", data.token);

        // if (code == 1) {
        //     // 跳转到 /home
        //     router.push("/home");
        // } else {
        //     alert("パスワードまたはアカウントが正しくありません");
        //     console.log(res.data.msg);
        // }
    } catch (error) {
        console.error("请求出错:", error);
        alert("ログインに失敗しました。もう一度お試しください。");
    }
};


</script>

<style lang="less" scoped>
@import '@/assets/css/login.less';
</style>
