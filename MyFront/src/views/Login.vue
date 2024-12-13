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
                <input ref="input1" type="text" v-model="data.id" @keydown.down="downInput()" @keydown.enter="downInput()" placeholder="ユーザー名" />
                <input ref="input2" type="password" v-model="data.password" @keydown.enter="send()" @keydown.up="upInput()"
                    placeholder="パスワード" />
                <div class="btn" @click="send()">
                    <input type="button" />
                    <p>ログイン</p>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref,reactive } from "vue";
import router from '@/router';
import { login } from "@/api/empApi";
import session from "@/utils/session";
import { useUserStore } from '@/stores/userStore';

const input1 = ref(null);
const input2 = ref(null);

const downInput = () => {
    input2.value.focus()
}

const upInput = () => {
    input1.value.focus()
}

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

     // SQLインジェクションを引き起こす可能性のある特殊記号を禁止する正規表現を定義
    const forbiddenRegex = /['"`;\\/\-\-#()=<>]/g;
    if (forbiddenRegex.test(data.id)||forbiddenRegex.test(data.password)) {
        alert('不適合な記号が入力されました: \' " ` ; \\ / -- # ( ) = < >');
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
