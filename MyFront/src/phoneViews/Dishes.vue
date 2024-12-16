<template>
    <section id="dish">
        <ul>
            <li v-for="(dish, i) in res" :key="i">
                <!-- <div :class="statusTextClass(dish.status)">売り切れ</div> -->
                <!-- <div class="dishCard" :class="statusClass1(dish.status)"> -->
                <div class="dishImg" :style="`background-image: url(${image_pathes[i]})`"></div>
                <div class="dishInfo">
                    <p class="title" :title="dish.name">{{ dish.name }}</p>
                    <p class="description" :title="dish.description">{{ dish.description }}</P>
                    <p class="price"><span>&yen;</span>{{ dish.price }}</p>
                    <div class="count">
                        <div class="minus" @click="minus(dish.id)">
                            <p>－</p>
                        </div>
                        <span>{{ store.dishes[countParam.deskId][dish.id].count || 0 }}</span>
                        <div class="add" @click="plus(dish.id)">
                            <p>＋</p>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </section>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from "vue"
import { useRoute } from "vue-router";
import { fetchDishByCategoryId } from "@/api/orderApi";
const emit = defineEmits(['sumOrderAndPrice']);
import { useDishStore } from "@/stores/dishStore";

// ルート情報
const route = useRoute();

// リクエストのデータ
const req = reactive({
    dishCategoryId: Number(route.params.id), // カテゴリーID
});
const res = ref([]); // 料理データ
const image_pathes = ref([]); // 画像パス
const store = useDishStore();
const countParam = reactive({
    deskId: route.params.desk_id,
})

// API から料理データを取得
const fetchDishes = async (req) => {
    try {
        const response = await fetchDishByCategoryId(req);
        console.log("dish: ", response);
        res.value = response.data; // データを保存
        console.log("dish: ", res.value);
        setImgPath(res.value); // 画像パスを設定
        setDishStore()


    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("料理の取得に失敗しました。");
    }
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

// 画像パスを設定
const setImgPath = (res) => {
    image_pathes.value = res.map(
        (item) => `/order-img/${item.dishCategoryId}/${item.image}`
    );
};

const setDishStore = () => {
    res.value.forEach(
        (item) => store.setDish(route.params.desk_id, item.id, item.name, item.price)
    )
}

const plus = (dishId) => {
    if (store.getDishCount(countParam.deskId, dishId) < 10) {
        store.plusDishCount(countParam.deskId, dishId)
        emit('sumOrderAndPrice')
    }
}

const minus = (dishId) => {
    if (store.getDishCount(countParam.deskId, dishId) > 0) {
        store.minusDishCount(countParam.deskId, dishId)
        emit('sumOrderAndPrice')
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/css/phone_dishes.less';
</style>
