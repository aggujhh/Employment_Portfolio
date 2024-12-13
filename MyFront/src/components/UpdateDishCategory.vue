<template>
    <section id="update_dish_category" v-if="isVisible">
        <div class="dialog">
            <h3>メニューの種類を修正</h3>
            <div>
                <span>種類名 :</span>
                <input type="text" v-model="data.name" @input="countInput" ref="inputField" />
                <p class="text-conut" ref="textCountDisplay">{{ textCount }}/20文字</p>
            </div>
            <div>
                <button @click="agree()">確認</button>
                <button class="close-btn" @click="close()">戻る</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref, reactive, watch,computed } from "vue";
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
    if (textCount.value > 20) {
        alert("種類名は20文字以内に抑えてください。")
        return;
    }
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

// 动态计算 data.name 的长度
const textCount = computed(() => data.name.length);
const inputField = ref(null);
const textCountDisplay = ref(null);
const countInput = () => {
    if (textCount.value > 20) {
        inputField.value.style.setProperty("border", "1.5px solid red", "important");
        inputField.value.style.backgroundColor = "#ffe6e6";
        textCountDisplay.value.style.color = "red";
    } else {
        inputField.value.style.setProperty("border", "1.5px solid #fbb034", "important");
        inputField.value.style.backgroundColor = "#fff";
        textCountDisplay.value.style.color = "#818181";
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/css/update_dish_category.less';
</style>
