<template>
    <section id="dishes">
        <ul>
            <li class="dish" :class="{ flipped: isFlipped[index] }" v-for="(item, index) in res" :key="index">
                <div class="front">
                    <div class="img" :style="`background-image: url(${image_pathes[index]})`"></div>
                    <button class="changeImg">画像更新</button>
                    <div class="info">
                        <h3 class="title">{{ item.name }}</h3>
                        <p class="price">
                            <span>値段:</span> <span>¥{{ item.price }}(税抜金額)</span>
                        </p>
                        <p class="salesStatus"><span>販売状況:</span>{{ toState(item.state) }}</p>
                        <p class="description">
                            <span>紹介:</span> {{ item.description }}
                        </p>
                        <button @click="flip(index)">修正する</button>
                        <button>削除する</button>
                    </div>
                </div>
                <div class="back">
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
                            <p>販売状況:</p>
                            <div>
                                <label><input type="radio" :name="'state-' + index" value="0"
                                        v-model="dish[index].state" />販売中</label>
                                <label><input type="radio" :name="'state-' + index" value="1"
                                        v-model="dish[index].state" />一時停止中</label>
                            </div>
                        </li>
                        <li>
                            料理の紹介<br /><textarea v-model="dish[index].description"></textarea>
                        </li>
                    </ul>
                    <button>確認</button>
                    <button @click="cancel(index)">戻る</button>
                </div>
            </li>
            <li class="add" @click="() => { isVisible.add = true }">
                <svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 24 24" height="30px" width="30px">
                    <path class="cls-1" fill="#3b3632"
                        d="M12,0C5.4,0,0,5.4,0,12s5.4,12,12,12,12-5.4,12-12S18.6,0,12,0ZM19,13h-6v6h-2v-6h-6v-2h6v-6h2v6h6v2Z" />
                    <path class="cls-2" fill="#fbb034"
                        d="M12,0C5.4,0,0,5.4,0,12s5.4,12,12,12,12-5.4,12-12S18.6,0,12,0ZM12,22c-5.5,0-10-4.5-10-10S6.5,2,12,2s10,4.5,10,10-4.5,10-10,10Z" />
                    <polygon class="cls-3" fill="#fbb034"
                        points="19 11 19 13 13 13 13 19 11 19 11 13 5 13 5 11 11 11 11 5 13 5 13 11 19 11" />
                </svg>
            </li>
        </ul>
        <AddDish :isVisible="isVisible.add" @close="closeModal" />
    </section>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { fetDishByCategoryId } from "@/api/dishApi";
import { useRoute } from "vue-router";
import AddDish from "@/components/AddDish.vue";

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

// モーダルの表示・非表示管理
const isVisible = reactive({
    add: false,
    delete: false,
    update: false,
});

// モーダルを閉じる関数
const closeModal = () => {
    isVisible.add = false;
    isVisible.delete = false;
    isVisible.update = false;
};

// フリップ状態を切り替える
const flip = (index) => {
    isFlipped.value[index] = !isFlipped.value[index];
};

// キャンセル時の処理
const cancel = (index) => {
    flip(index); // フリップを元に戻す
};

// 状態を表す文字列を取得
const toState = (state) => {
    const index = Number(state);
    const stateMap = {
        0: "販売中", // 販売中
        1: "一時停止中", // 一時停止中
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
</script>

<style lang="less" scoped>
@import '@/assets/css/dishes.less';
</style>
