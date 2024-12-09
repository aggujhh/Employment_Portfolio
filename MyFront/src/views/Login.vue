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
import { reactive } from "vue";
import router from '@/router';
import { login } from "@/api/empApi";
import session from "@/utils/session";
import { useUserStore } from '@/stores/userStore';

// ログインフォームデータを reactive で作成
const data = reactive({
    id: '',        // ユーザー ID
    password: ''   // パスワード
});

/**
 * ログイン処理を送信
 */
const send = async () => {
    // ユーザー名またはパスワードが入力されていない場合のエラーチェック
    if (!data.id.trim() || !data.password.trim()) {
        alert("ユーザー名とパスワードを入力してください。"); // ユーザー入力を促す
        return;
    }

    try {
        // API を呼び出してログイン処理を行う
        const res = await login(data); // API 呼び出し
        console.log(res);

        // レスポンスデータを取得
        const code = res.data.code; // ステータスコードを取得
        const res_data = JSON.parse(res.data.data); // レスポンスの JSON データを解析

        // Pinia ストアのインスタンスを取得
        const userStore = useUserStore();

        // ユーザーデータをストアに保存または更新
        userStore.upsertUser({
            id: res_data.id,      // ユーザー ID
            name: res_data.name,  // ユーザー名
            type: res_data.type,  // ユーザータイプ
            job: res_data.job     // ジョブタイトル
        });

        // トークンを localStorage に保存
        session.setSession(res_data.id, res_data.token, 60 * 60 * 1000); // トークンの有効期限を 15 分間に設定

        // ログインが成功した場合
        if (code == 1) {
            // ホームページにリダイレクト
            router.push("/home");
        } else {
            // エラーメッセージを表示
            alert("パスワードまたはアカウントが正しくありません");
            console.log(res.data.msg);
        }
    } catch (error) {
        // リクエスト中にエラーが発生した場合の処理
        console.error("リクエストエラー:", error);
        alert("ログインに失敗しました。もう一度お試しください。");
    }
};
</script>


<style lang="less" scoped>
@import '@/assets/css/login.less';
</style>
