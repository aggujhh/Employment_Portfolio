<template>
    <section id="update_dish_category" v-if="isVisible">
        <div class="dialog">
            <h3>メニューの種類を修正</h3>
            <div><span>種類名 :</span><input type="text" v-model="data.name" /></div>
            <div>
                <button @click="agree()">確認</button>
                <button class="close-btn" @click="close()">戻る</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { reactive, watch } from "vue";
import { updateDishCategory } from "@/api/dishCategoryApi";

const props = defineProps(['isVisible', 'id', 'name'])
const emit = defineEmits(['close', 'refresh']);
console.log("props", props.id, props.name);

const data = reactive({
    id: "",// カテゴリーid
    name: "",// カテゴリー名
});

watch(
    () => [props.id, props.name],
    ([newId, newName]) => {
        data.id = newId;
        data.name = newName;
    }, { immediate: true }
);

const close = () => {
    data.name = props.name
    emit('close');
}

const agree = async () => {
    try {
        // API を呼び出してログイン処理を行う
        const res = await updateDishCategory(data); // API 呼び出し
        console.log(res);

        // レスポンスデータを取得
        const code = res.data.code; // ステータスコードを取得
        // 成功した場合
        if (code == 1) {
            // ホームページにリダイレクト
            alert("新しいメニューの種類が追加を成功しました。");
            data.name = null
            emit('refresh', props.id); // 親コンポーネントに通知してページをリロードする
            emit('close');   // 親コンポーネントに通知してモーダルを閉じる
        } else {
            // エラーメッセージを表示
            alert("追加失敗しました。もう一度お試しください。");
            console.log(res.data.msg);
        }
    } catch (error) {
        // リクエスト中にエラーが発生した場合の処理
        console.error("リクエストエラー:", error);
        alert("追加失敗しました。もう一度お試しください。");
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/css/update_dish_category.less';
</style>
