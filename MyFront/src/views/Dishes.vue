<template>
    <section id="dishes">
        <ul>
            <!-- 料理リストをループ表示 -->
            <li class="dish" :class="{ flipped: isFlipped[index] }" v-for="(item, index) in res" :key="index">
                <div class="front">
                    <!-- 料理画像 -->
                    <div class="img" :style="`background-image: url(${image_pathes[index]})`">
                    </div>
                    <label>
                        <!-- 画像更新ボタン -->
                        <input type="file" @change="(event) => onImageChange(event, item)" />
                        <button type="button" class="changeImg">画像更新</button>
                    </label>
                    <div class="info">
                        <!-- 料理情報表示 -->
                        <h3 class="title" :title="item.name">{{ item.name }}</h3>
                        <p class="price">
                            <span>値段:</span> <span>¥{{ item.price }}(税抜金額)</span>
                        </p>
                        <p class="salesStatus"><span>販売状況:</span>{{ toState(item.state) }}</p>
                        <p class="description">
                            <span>紹介:</span> {{ item.description }}
                        </p>
                        <!-- 削除・修正ボタン -->
                        <button @click="() => { openDeleteDialog(item) }">削除する</button>
                        <button @click="flip(index)">修正する</button>
                    </div>
                </div>
                <div class="back">
                    <!-- 編集フォーム -->
                    <ul>
                        <li>
                            <label>
                                <p>料理名:</p>
                                <input type="text" v-model="dish[index].name" />
                            </label>
                        </li>
                        <li>
                            <label>
                                <p>値段:</p>
                                <input type="text" v-model="dish[index].price" /><span>円</span>
                            </label>
                        </li>
                        <li>
                            <!-- 販売状況を選択 -->
                            <p>販売状況:</p>
                            <div>
                                <label><input type="radio" :name="'state-' + index" value="0"
                                        v-model="dish[index].state" />販売中</label>
                                <label><input type="radio" :name="'state-' + index" value="1"
                                        v-model="dish[index].state" />完売</label>
                                <label><input type="radio" :name="'state-' + index" value="2"
                                        v-model="dish[index].state" />一時停止中</label>
                            </div>
                        </li>
                        <li>
                            <!-- 紹介文を編集 -->
                            料理の紹介<br /><textarea v-model="dish[index].description"></textarea>
                        </li>
                    </ul>
                    <!-- 編集確認・キャンセルボタン -->
                    <button @click="updateInfo(index)">確認</button>
                    <button @click="cancel(index)">戻る</button>
                </div>
            </li>
            <!-- 新規追加ボタン -->
            <li class="add" @click="() => { isVisible.add = true }">
                <svg version="1.1" viewBox="0 0 24 24" height="30px" width="30px">
                    <path class="cls-1" fill="#3b3632"
                        d="M12,0C5.4,0,0,5.4,0,12s5.4,12,12,12,12-5.4,12-12S18.6,0,12,0ZM19,13h-6v6h-2v-6h-6v-2h6v-6h2v6h6v2Z" />
                    <path class="cls-2" fill="#fbb034"
                        d="M12,0C5.4,0,0,5.4,0,12s5.4,12,12,12,12-5.4,12-12S18.6,0,12,0ZM12,22c-5.5,0-10-4.5-10-10S6.5,2,12,2s10,4.5,10,10-4.5,10-10,10Z" />
                    <polygon class="cls-3" fill="#fbb034"
                        points="19 11 19 13 13 13 13 19 11 19 11 13 5 13 5 11 11 11 11 5 13 5 13 11 19 11" />
                </svg>
            </li>
        </ul>
        <!-- 新規追加モーダル -->
        <AddDish :isVisible="isVisible.add" :dishCategoryId="req.dishCategoryId" @close="closeModal"
            @refresh="reloadPage" />
        <!-- 削除ダイアログ -->
        <DeleteDialog :isVisible="isVisible.delete" :name="dishName" :deleteMethod="deleteMethod" @close="closeModal" />
    </section>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { fetDishByCategoryId, updateDishImage, updateDishInfo, deleteDish } from "@/api/dishApi";
import { useRoute } from "vue-router";
import AddDish from "@/components/AddDish.vue";
import cloneDeep from 'lodash/cloneDeep';


// ルート情報
const route = useRoute();

// リクエストのデータ
const req = reactive({
    dishCategoryId: Number(route.params.id), // カテゴリーID
});

// データ格納用のリアクティブ変数
const res = ref([]); // 料理データ
const image_pathes = ref([]); // 画像パス
const dish = ref([]); // 編集用の料理データ
const isFlipped = ref([]); // フリップ状態
let TempDishArray = []

// モーダルの表示・非表示管理
const isVisible = reactive({
    add: false,
    delete: false,
});

// モーダルを閉じる関数
const closeModal = () => {
    isVisible.add = false;
    isVisible.delete = false;
};

// フリップ状態を切り替える
const flip = (index) => {
    isFlipped.value[index] = !isFlipped.value[index];
};

// キャンセル時の処理
const cancel = (index) => {
    flip(index); // フリップを元に戻す
    // 逐项替换，避免直接赋值整个数组
    dish.value.forEach((_, i) => {
        dish.value[i] = { ...TempDishArray[i] };
    });
    // dish.value[index].price = 123;
};

// 状態を表す文字列を取得
const toState = (state) => {
    const index = Number(state);
    const stateMap = {
        0: "販売中", // 販売中
        1: "完売", // 一時停止中
        2: "一時停止中"
    };
    return stateMap[index];
};

// API から料理データを取得
const fetchDishes = async (req) => {
    try {
        const response = await fetDishByCategoryId(req);
        res.value = response.data.data; // データを保存
        setImgPath(res.value); // 画像パスを設定
        setIsFlipped(res.value); // フリップ状態を初期化
        setDishArray(res.value); // 編集用データを設定
        console.log("dish: ", res.value);
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("料理の取得に失敗しました。");
    }
};

// 画像パスを設定
const setImgPath = (res) => {
    image_pathes.value = res.map(
        (item) => `/dish-img/${item.dishCategoryId}/${item.image}`
    );
};

// フリップ状態を初期化
const setIsFlipped = (res) => {
    isFlipped.value = res.map(() => false); // 全ての状態を false に設定
};

// 編集用の料理データを設定
const setDishArray = (res) => {
    dish.value = res.map((item) => {
        return {
            name: item.name,
            price: item.price,
            state: item.state,
            description: item.description,
        };
    });
    TempDishArray = cloneDeep(dish.value);
    console.log("TempDishArray", TempDishArray);
    console.log("dish.value", dish.value);
};



// ルートIDの変更を監視し、データを再取得
watch(
    () => route.params.id,
    (newValue, oldValue) => {
        console.log(newValue, oldValue);
        req.dishCategoryId = Number(newValue);
        fetchDishes(req); // データを再取得
    },
    { immediate: true }
);

// コンポーネントマウント時にデータを取得
onMounted(() => {
    fetchDishes(req); // データ取得メソッドを呼び出す
});


const defaultReqImage = {
    id: "",
    dishCategoryId: "",
    name: "",
    image: "",
};

const reqImage = reactive({ ...defaultReqImage })

// **画像を変更する関数**
const onImageChange = (event, item) => {
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
            reqImage.image = e.target.result;
            reqImage.name = item.image;
            reqImage.dishCategoryId = item.dishCategoryId;
            reqImage.id = item.id;
            updateImage();
        };
        reader.readAsDataURL(file);
    }
};

// API から料理データを取得
const updateImage = async () => {
    try {
        const response = await updateDishImage(reqImage);
        const code = response.data.code;
        if (code === 1) {
            alert(`画像チェンジを成功しました。`);
            reloadPage(reqImage.dishCategoryId)
        } else {
            alert("画像チェンジを失敗しました。");
            console.log(response.data.msg);
        }
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("画像チェンジを失敗しました。");
    }
};



// ページをリロードする関数
const reloadPage = (value) => {
    console.log("ページを再読み込み中...");
    fetchDishes({ dishCategoryId: value }); // データを再取得
};


const defaultReqInfo = {
    id: "",
    name: "",
    state: "",
    price: 0,
    description: ""
};

const reqInfo = reactive({ ...defaultReqImage })

// API から料理データを取得
const updateInfo = async (index) => {
    reqInfo.id = res.value[index].id
    reqInfo.name = dish.value[index].name
    reqInfo.state = dish.value[index].state
    reqInfo.price = dish.value[index].price
    reqInfo.description = dish.value[index].description
    if (reqInfo.name === "" || reqInfo.price === "") {
        alert('料理名または値段は空にできません、入力してください。');
        return;
    }

    if (reqInfo.name.length > 30 || reqInfo.price.length > 7 || reqInfo.description.length > 70) {
        alert("制限文字数内で入力してください。料理名30文字以内、値段7桁以内、説明70文字以内で。")
        return;
    }
    const numberRegex = /^[0-9]+$/;
    if (!numberRegex.test(reqInfo.price)) {
        alert('値段は数字のみ入力してください。');
        return;
    }

    const forbiddenRegex = /['"`;\\/\-\-#()=<>]/g;
    if (forbiddenRegex.test(reqInfo.name) || forbiddenRegex.test(reqInfo.description)) {
        alert('不適合な記号が入力されました: \' " ` ; \\ / -- # ( ) = < >');
        return;
    }
    try {
        const response = await updateDishInfo(reqInfo);
        const code = response.data.code;
        if (code === 1) {
            alert(`料理修正を成功しました。`);
            reloadPage(res.value[index].dishCategoryId)
        } else {
            alert("料理修正を失敗しました。");
            console.log(response.data.msg);
        }
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("料理修正を失敗しました。");
    }
};

// 削除ダイアログを開く関数
const dishName = ref()
const dishCategoryId = ref()
const deleteReq = reactive({
    id: "",
    image: "",
    dishCategoryId: ""
})

const openDeleteDialog = (item) => {
    isVisible.delete = true;
    dishName.value = item.name;
    deleteReq.id = item.id;
    deleteReq.image = item.image;
    deleteReq.dishCategoryId = item.dishCategoryId
};

// 料理を削除メソッド
const deleteMethod = async () => {
    try {
        // API を呼び出して料理を削除
        const res = await deleteDish(deleteReq);
        console.log(res);
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            alert(`料理「${dishName.value}」が削除されました。`);
            closeModal();
            reloadPage(deleteReq.dishCategoryId)
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("削除失敗しました。もう一度お試しください。");
    }
};

</script>

<style lang="less" scoped>
@import '@/assets/css/dishes.less';
</style>
