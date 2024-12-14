<template>
    <section id='add_dish' v-if="isVisible">
        <div class="dialog">
            <h3>料理を追加する?</h3>
            <ul>
                <li>
                    <label>
                        <p>料理名</p>
                        <input type="text" v-model="req.name" ref="nameInput"
                            @input="updateCount('name', 30, nameInput, nameDisplay)" />
                    </label>
                    <p class="text-conut" ref="nameDisplay">{{ textCount.name }}/30文字</p>
                </li>
                <li>
                    <label>
                        <p>値段</p>
                        <input type="text" v-model="req.price" ref="priceInput"
                            @input="updateCount('price', 7, priceInput, priceDisplay)" />
                        <span>円</span>
                    </label>
                    <p class="text-conut" ref="priceDisplay">{{ textCount.price }}/7文字</p>
                </li>
                <li>
                    <p>販売状況</p>
                    <div class="radio">
                        <label><input type="radio" name="status" value="0" v-model="req.state" />販売中
                        </label>
                        <label><input type="radio" name="status" value="1" v-model="req.state" />完売
                        </label>
                        <label><input type="radio" name="status" value="2" v-model="req.state" />一時停止中
                        </label>
                    </div>
                </li>
                <li>
                    <p>画像</p>
                    <input type="file" @change="onImageChange" id="upImg" />
                    <label for="upImg">
                        <div class="dishImg" :style="`background-image: url(${req.image})`"></div>
                        <div>UPLORD</div>
                    </label>
                </li>
                <li>
                    <p>料理の紹介</p>
                    <textarea v-model="req.description" ref="descriptionInput"
                        @input="updateCount('description', 70, descriptionInput, descriptionDisplay)"></textarea>
                    <p class="text-conut" ref="descriptionDisplay">{{ textCount.description }}/70文字</p>
                </li>
            </ul>
            <div>
                <button @click="agree()">確認</button>
                <button class="close-btn" @click="close()">戻る</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { ref, reactive, watch } from "vue"
import { addDish } from "@/api/dishApi";

const props = defineProps(['isVisible', 'dishCategoryId'])
const emit = defineEmits(['close','refresh']);

const defaultReq = {
    dishCategoryId: props.dishCategoryId,
    name: "",
    image: "",
    price: "",
    state: "0",
    description: "",
};
const req = reactive({ ...defaultReq })

watch(
    () => props.dishCategoryId,
    (newValue) => {
        req.dishCategoryId = newValue || "";
    }, { immediate: true }
);

const onImageChange = (event) => {
    const file = event.target.files[0];
    // **画像形式の確認**
    const imageTypes = ['image/jpeg', 'image/jpg', 'image/png'];
    if (!imageTypes.includes(file.type)) {
        alert("無効な画像形式です。使用可能な形式: JPG, JPEG, PNG");
        return;
    } 

    // **画像サイズの確認**
    const maxSize = 2 * 1024 * 1024; // 制限 2MB
    if (file.size > maxSize) {
        alert("画像サイズが2MBを超えています。より小さい画像をアップロードしてください。");
        return;
    }

    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            req.image = e.target.result;
        };
        reader.readAsDataURL(file);
    }
}



const close = () => {
    Object.assign(textCount, defaultTextCount);
    Object.assign(req, defaultReq);
    emit('close');
}

const defaultTextCount = {
    name: 0,
    price: 0,
    description: 0,
};
const textCount = reactive({ ...defaultTextCount });

const nameInput = ref(null);
const nameDisplay = ref(null);
const priceInput = ref(null);
const priceDisplay = ref(null);
const descriptionInput = ref(null);
const descriptionDisplay = ref(null);

const updateCount = (field, maxLength, inputRef, displayRef) => {
    const value = req[field];
    const count = value.length;

    // 判断并设置样式
    if (count > maxLength) {
        inputRef.style.setProperty("border", "1.5px solid red", "important");
        inputRef.style.backgroundColor = "#ffe6e6";
        displayRef.style.color = "red";
    } else {
        inputRef.style.setProperty("border", "1.5px solid #fbb034", "important");
        inputRef.style.backgroundColor = "#fff";
        displayRef.style.color = "#818181";
    }
    textCount[field] = count;
};

const agree = async () => {
    if (req.name === "" || req.price === "") {
        alert('料理名または値段は空にできません、入力してください。');
        return;
    }

    if (textCount.name > 30 || textCount.price > 7 || textCount.description > 70) {
        alert("制限文字数内で入力してください。")
        return;
    }
    const numberRegex = /^[0-9]+$/;
    if (!numberRegex.test(req.price)) {
        alert('値段は数字のみ入力してください。');
        return;
    }

    const forbiddenRegex = /['"`;\\/\-\-#()=<>]/g;
    if (forbiddenRegex.test(req.name) || forbiddenRegex.test(req.description)) {
        alert('不適合な記号が入力されました: \' " ` ; \\ / -- # ( ) = < >');
        return;
    }
    try {
        // API を呼び出してログイン処理を行う
        const res = await addDish(req); // API 呼び出し
        console.log(res);
        // レスポンスデータを取得
        const code = res.data.code; // ステータスコードを取得
        // 成功した場合
        if (code == 1) {
            // ホームページにリダイレクト
            alert("新しいメニューの種類が追加を成功しました。");
            emit('refresh',req.dishCategoryId); // 親コンポーネントに通知してページをリロードする
            close();   // 親コンポーネントに通知してモーダルを閉じる
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
@import '@/assets/css/add_dish.less';
</style>
